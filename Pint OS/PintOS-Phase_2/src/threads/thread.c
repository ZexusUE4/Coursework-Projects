#include "threads/thread.h"
#include <debug.h>
#include <stddef.h>
#include <random.h>
#include <stdio.h>
#include <string.h>
#include "threads/flags.h"
#include "threads/interrupt.h"
#include "threads/intr-stubs.h"
#include "threads/palloc.h"
#include "threads/switch.h"
#include "threads/synch.h"
#include "threads/vaddr.h"
#include "threads/arithmetic.h"
#ifdef USERPROG
#include "userprog/syscall.h"
#include "userprog/process.h"
#endif

#include "filesys/filestruct.h"



/* Random value for struct thread's `magic' member.
   Used to detect stack overflow.  See the big comment at the top
   of thread.h for details. */
#define THREAD_MAGIC 0xcd6abf4b
#define DBG(charArr) printf("DEBUG: %s\n",charArr);

/* List of processes in THREAD_READY state, that is, processes
   that are ready to run but not actually running. */
static struct list ready_list;

/* List of all processes.  Processes are added to this list
   when they are first scheduled and removed when they exit. */
static struct list all_list;

/* Idle thread. */
static struct thread *idle_thread;

/* Initial thread, the thread running init.c:main(). */
static struct thread *initial_thread;

/* Lock used by allocate_tid(). */
static struct lock tid_lock;

/* Stack frame for kernel_thread(). */
struct kernel_thread_frame
  {
    void *eip;                  /* Return address. */
    thread_func *function;      /* Function to call. */
    void *aux;                  /* Auxiliary data for function. */
  };

/* Statistics. */
static long long idle_ticks;    /* # of timer ticks spent idle. */
static long long kernel_ticks;  /* # of timer ticks in kernel threads. */
static long long user_ticks;    /* # of timer ticks in user programs. */

/* Scheduling. */
#define TIME_SLICE 4            /* # of timer ticks to give each thread. */
static unsigned thread_ticks;   /* # of timer ticks since last . */

/* If false (default), use round-robin scheduler.
   If true, use multi-level feedback queue scheduler.
   Controlled by kernel command-line option "-o mlfqs". */
bool thread_mlfqs;

static int load_avg;

static void kernel_thread (thread_func *, void *aux);

static void idle (void *aux UNUSED);
static struct thread *running_thread (void);
static struct thread *next_thread_to_run (void);
static void init_thread (struct thread *, const char *name, int priority);
static bool is_thread (struct thread *) UNUSED;
static void *alloc_frame (struct thread *, size_t size);
static void schedule (void);
void thread_schedule_tail (struct thread *prev);
static tid_t allocate_tid (void);

/* MA: Prototypes */
bool lock_priority_large (const struct list_elem *a,
                     const struct list_elem *b,
                     void *aux UNUSED);
void ready_list_priority_insert(struct thread* new_thread);
bool thread_priority_more(struct list_elem *it1,struct list_elem *it2
                          ,void *aux UNUSED);
bool is_mlfqs();
void thread_test_preemption (void);
void thread_add_lock (struct lock *lock);
void thread_remove_lock (struct lock *lock);

/* MA: END */

/* Initializes the threading system by transforming the code
   that's currently running into a thread.  This can't work in
   general and it is possible in this case only because loader.S
   was careful to put the bottom of the stack at a page boundary.

   Also initializes the run queue and the tid lock.

   After calling this function, be sure to initialize the page
   allocator before trying to create any threads with
   thread_create().

   It is not safe to call thread_current() until this function
   finishes. */
void
thread_init (void)
{
  ASSERT (intr_get_level () == INTR_OFF);

  lock_init (&tid_lock);
  list_init (&ready_list);
  list_init (&all_list);

  /* Set up a thread structure for the running thread. */
  initial_thread = running_thread ();
  init_thread (initial_thread, "main", PRI_DEFAULT);
	initial_thread->status = THREAD_RUNNING;
  initial_thread->tid = allocate_tid ();
	load_avg=0;
}

/* Starts preemptive thread scheduling by enabling interrupts.
   Also creates the idle thread. */
void
thread_start (void)
{
  /* Create the idle thread. */
  struct semaphore idle_started;
  sema_init (&idle_started, 0);
  thread_create ("idle", PRI_MIN, idle, &idle_started);

  /* Start preemptive thread scheduling. */
  intr_enable ();

  /* Wait for the idle thread to initialize idle_thread. */
  sema_down (&idle_started);
}

/* Called by the timer interrupt handler at each timer tick.
   Thus, this function runs in an external interrupt context. */
void
thread_tick (void)
{
  struct thread *t = thread_current ();
	t->recent_cpu++;
  /* Update statistics. */
  if (t == idle_thread)
    idle_ticks++;
#ifdef USERPROG
  else if (t->pagedir != NULL)
    user_ticks++;
#endif
  else
    kernel_ticks++;

  /* Enforce preemption. */
  if (++thread_ticks >= TIME_SLICE)
    intr_yield_on_return ();
}

/* Prints thread statistics. */
void
thread_print_stats (void)
{
  printf ("Thread: %lld idle ticks, %lld kernel ticks, %lld user ticks\n",
          idle_ticks, kernel_ticks, user_ticks);
}

/* Creates a new kernel thread named NAME with the given initial
   PRIORITY, which executes FUNCTION passing AUX as the argument,
   and adds it to the ready queue.  Returns the thread identifier
   for the new thread, or TID_ERROR if creation fails.

   If thread_start() has been called, then the new thread may be
   scheduled before thread_create() returns.  It could even exit
   before thread_create() returns.  Contrariwise, the original
   thread may run for any amount of time before the new thread is
   scheduled.  Use a semaphore or some other form of
   synchronization if you need to ensure ordering.

   The code provided sets the new thread's `priority' member to
   PRIORITY, but no actual priority scheduling is implemented.
   Priority scheduling is the goal of Problem 1-3. */
tid_t
thread_create (const char *name, int priority,
               thread_func *function, void *aux)
{
  struct thread *t;
  struct kernel_thread_frame *kf;
  struct switch_entry_frame *ef;
  struct switch_threads_frame *sf;
  tid_t tid;
  enum intr_level old_level;

  ASSERT (function != NULL);

  /* Allocate thread. */
  t = palloc_get_page (PAL_ZERO);
  if (t == NULL)
    return TID_ERROR;

  /* Initialize thread. */
  init_thread (t, name, priority);
  tid = t->tid = allocate_tid ();

  /* Prepare thread for first run by initializing its stack.
     Do this atomically so intermediate values for the 'stack'
     member cannot be observed. */
  old_level = intr_disable ();

  /* Stack frame for kernel_thread(). */
  kf = alloc_frame (t, sizeof *kf);
  kf->eip = NULL;
  kf->function = function;
  kf->aux = aux;

  /* Stack frame for switch_entry(). */
  ef = alloc_frame (t, sizeof *ef);
  ef->eip = (void (*) (void)) kernel_thread;

  /* Stack frame for switch_threads(). */
  sf = alloc_frame (t, sizeof *sf);
  sf->eip = switch_entry;
  sf->ebp = 0;

  intr_set_level (old_level);

  /* Add to run queue. */
  thread_unblock (t);

/* MA: Yielding the current thread to let the newly created thread to run if it
  it had a higher priority */
  thread_test_preemption();
/* MA: END */

  return tid;
}

/* Puts the current thread to sleep.  It will not be scheduled
   again until awoken by thread_unblock().

   This function must be called with interrupts turned off.  It
   is usually a better idea to use one of the synchronization
   primitives in synch.h. */
void
thread_block (void)
{
  ASSERT (!intr_context ());
  ASSERT (intr_get_level () == INTR_OFF);

  struct thread * t = thread_current();

  t->status = THREAD_BLOCKED;

  schedule ();
}

/* Transitions a blocked thread T to the ready-to-run state.
   This is an error if T is not blocked.  (Use thread_yield() to
   make the running thread ready.)

   This function does not preempt the running thread.  This can
   be important: if the caller had disabled interrupts itself,
   it may expect that it can atomically unblock a thread and
   update other data. */
void
thread_unblock (struct thread *t)
{
  enum intr_level old_level;

  ASSERT (is_thread (t));

  old_level = intr_disable ();
  ASSERT (t->status == THREAD_BLOCKED);

/* MA: Insertion into the ready list */
  ASSERT(is_thread(t));
  ready_list_priority_insert(t);
/* MA: END */
  t->status = THREAD_READY;
  intr_set_level (old_level);
}

/* Returns the name of the running thread. */
const char *
thread_name (void)
{
  return thread_current ()->name;
}

/* Returns the running thread.
   This is running_thread() plus a couple of sanity checks.
   See the big comment at the top of thread.h for details. */
struct thread *
thread_current (void)
{
  struct thread *t = running_thread ();

  /* Make sure T is really a thread.
     If either of these assertions fire, then your thread may
     have overflowed its stack.  Each thread has less than 4 kB
     of stack, so a few big automatic arrays or moderate
     recursion can cause stack overflow. */
  ASSERT (is_thread (t));
  ASSERT (t->status == THREAD_RUNNING);

  return t;
}

/* Returns the running thread's tid. */
tid_t
thread_tid (void)
{
  return thread_current ()->tid;
}

/* Deschedules the current thread and destroys it.  Never
   returns to the caller. */
void
thread_exit (void)
{
  ASSERT (!intr_context ());

#ifdef USERPROG

/*--------------------for Phase 2-----------Hassan Ahmed------------------------*/
/*------------------------------------------------------------------------------*/
  /*remove opened files in this thread*/

	struct thread * temp_thread = thread_current();
	struct list_elem *e;
	for (e = list_begin (&(temp_thread->opened_files)); e != list_end (&(temp_thread->opened_files));)
	{
		struct thrd_file_struct *t = list_entry (e, struct thrd_file_struct, file_elem);
    #ifdef USERPROG
		  syscall_close(t->file_descrip);
      e = list_next(e);
      free(t);
    #endif
	}

/*------------------------------------------------------------------------------*/

  process_exit ();
#endif

  /* Remove thread from all threads list, set our status to dying,
     and schedule another process.  That process will destroy us
     when it calls thread_schedule_tail(). */
  intr_disable ();

  list_remove (&thread_current()->allelem);
  thread_current ()->status = THREAD_DYING;
  schedule ();
  NOT_REACHED ();
}

/* Yields the CPU.  The current thread is not put to sleep and
   may be scheduled again immediately at the scheduler's whim. */
void
thread_yield (void)
{
  struct thread *cur = thread_current ();
  enum intr_level old_level;

  ASSERT (!intr_context ());

  old_level = intr_disable ();
  if (cur != idle_thread){
     /* MA: Insertion into the ready list */
      ASSERT(is_thread(cur));
      ready_list_priority_insert(cur);
     /* MA: END */
  }
  cur->status = THREAD_READY;
  schedule ();
  intr_set_level (old_level);
}

/* Invoke function 'func' on all threads, passing along 'aux'.
   This function must be called with interrupts off. */
void
thread_foreach (thread_action_func *func, void *aux)
{
  struct list_elem *e;

  ASSERT (intr_get_level () == INTR_OFF);

  for (e = list_begin (&all_list); e != list_end (&all_list);
       e = list_next (e))
    {
      struct thread *t = list_entry (e, struct thread, allelem);
      func (t, aux);
    }
}

/*---------------------- Phase 2 functions-----------------by Hassan Ahmed--------------*/
struct file*
get_open_file(int fd)
{
	struct thread * temp_thread = thread_current();
	struct list_elem *e;
	for (e = list_begin (&(temp_thread->opened_files)); e != list_end (&(temp_thread->opened_files));
       e = list_next (e))
	{
		struct thrd_file_struct *t = list_entry (e, struct thrd_file_struct, file_elem);
		if( fd == t->file_descrip)
		{
			return t->file_opened;
		}
	}
  return NULL;
}

void
remove_file(int fd)
{
  struct thread * temp_thread = thread_current();
	struct list_elem *e;
	for (e = list_begin (&(temp_thread->opened_files)); e != list_end (&(temp_thread->opened_files));
       e = list_next (e))
	{
		struct thrd_file_struct *t = list_entry (e, struct thrd_file_struct, file_elem);
		if( fd == t->file_descrip)
		{
			 list_remove (&t->file_elem);
		}
	}
}

/*--------------------------------------------------------------------------------------*/



/*Calculate new priority.*/
void
calc_new_priority_for_all()
{
	thread_foreach(&calc_new_priority,NULL);
	list_sort(&ready_list, &thread_priority_more,NULL);
}

void
calc_new_priority(struct thread *t_thread , void *vd UNUSED)
{
	if(t_thread == idle_thread)
		return;
	int pri_temp = PRI_MAX - cnvrt_to_int( div_x_n(t_thread->recent_cpu , 4) ) - t_thread->thread_nice*2 ;
	t_thread->priority = max(PRI_MIN , min(PRI_MAX , pri_temp) ) ;
	return;
}

/*calculate new recent_cpu*/
void
calc_recent_cpu()
{
	thread_foreach(&calc_new_recent_cpu,NULL);
}

void
calc_new_recent_cpu(struct thread *t_thread , void *vd UNUSED)
{
	if (t_thread == idle_thread)
		return;
	t_thread->recent_cpu = add_x_n( mul( div (2*load_avg , add_x_n(2*load_avg , 1)
                              ) , t_thread->recent_cpu) ,t_thread->thread_nice);
}

/*calcualte new load_avg*/

void calc_load_avg()
{
	int ready_threads;
	if(thread_current()!=idle_thread)
		ready_threads= list_size (&ready_list)  +1;
	else
		ready_threads= list_size (&ready_list) ;
	load_avg = add_x_n( mul_x_n(load_avg , 59) , ready_threads) / 60;
}

//load_avg = (59/60)*load_avg + (1/60)*ready_threads
/* Sets the current thread's priority to NEW_PRIORITY. */
void
thread_set_priority (int new_priority)
{

  if(!thread_mlfqs){
    enum intr_level old_level = intr_disable ();
    struct thread *t = thread_current ();
    int old_priority = t->priority;

    /* Always update base priority. */
    t->base_priority = new_priority;

    /* Only update priority and test preemption if new priority
       is smaller and current priority is not donated by another
       thread. */
    if (new_priority < old_priority && list_empty (&t->acquired_locks))
      {
        t->priority = new_priority;
        thread_test_preemption ();
      }

    intr_set_level (old_level);
}
	else
	{
    struct thread * cur = thread_current();
		new_priority = max (PRI_MIN , min (PRI_MAX , new_priority) ) ;
		cur->priority = new_priority;
	}
}

/* Returns the current thread's priority. */
int
thread_get_priority (void)
{
  return thread_current()->priority;
}

/* Sets the current thread's nice value to NICE. */
void
thread_set_nice (int nice )
{
	thread_current ()->thread_nice = nice;
	calc_new_recent_cpu(thread_current() , NULL);
	calc_new_priority(thread_current() , NULL);
}

/* Returns the current thread's nice value. */
int
thread_get_nice (void)
{
  return thread_current ()->thread_nice;
}

//returns the idle thread.
struct thread *
get_idle_thread(){
  return idle_thread;
}

/* Returns 100 times the system load average. */
int
thread_get_load_avg (void)
{
  return cnvrt_rounding(load_avg*100);
}

/* Returns 100 times the current thread's recent_cpu value. */
int
thread_get_recent_cpu (void)
{
  return cnvrt_rounding((thread_current()->recent_cpu)*100);
}

/* Idle thread.  Executes when no other thread is ready to run.

   The idle thread is initially put on the ready list by
   thread_start().  It will be scheduled once initially, at which
   point it initializes idle_thread, "up"s the semaphore passed
   to it to enable thread_start() to continue, and immediately
   blocks.  After that, the idle thread never appears in the
   ready list.  It is returned by next_thread_to_run() as a
   special case when the ready list is empty. */
static void
idle (void *idle_started_ UNUSED)
{
  struct semaphore *idle_started = idle_started_;
  idle_thread = thread_current ();
  sema_up (idle_started);

  for (;;)
    {
      /* Let someone else run. */
      intr_disable ();
      thread_block ();

      /* Re-enable interrupts and wait for the next one.

         The `sti' instruction disables interrupts until the
         completion of the next instruction, so these two
         instructions are executed atomically.  This atomicity is
         important; otherwise, an interrupt could be handled
         between re-enabling interrupts and waiting for the next
         one to occur, wasting as much as one clock tick worth of
         time.

         See [IA32-v2a] "HLT", [IA32-v2b] "STI", and [IA32-v3a]
         7.11.1 "HLT Instruction". */
      asm volatile ("sti; hlt" : : : "memory");
    }
}

/* Function used as the basis for a kernel thread. */
static void
kernel_thread (thread_func *function, void *aux)
{
  ASSERT (function != NULL);

  intr_enable ();       /* The scheduler runs with interrupts off. */
  function (aux);       /* Execute the thread function. */
  thread_exit ();       /* If function() returns, kill the thread. */
}

/* Returns the running thread. */
struct thread *
running_thread (void)
{
  uint32_t *esp;

  /* Copy the CPU's stack pointer into `esp', and then round that
     down to the start of a page.  Because `struct thread' is
     always at the beginning of a page and the stack pointer is
     somewhere in the middle, this locates the curent thread. */
  asm ("mov %%esp, %0" : "=g" (esp));
  return pg_round_down (esp);
}

/* Returns true if T appears to point to a valid thread. */
static bool
is_thread (struct thread *t)
{
  return t != NULL && t->magic == THREAD_MAGIC;
}

/* Does basic initialization of T as a blocked thread named
   NAME. */
static void
init_thread (struct thread *t, const char *name, int priority)
{
  ASSERT (t != NULL);
  ASSERT (PRI_MIN <= priority && priority <= PRI_MAX);
  ASSERT (name != NULL);

  memset (t, 0, sizeof *t);
  t->status = THREAD_BLOCKED;
  strlcpy (t->name, name, sizeof t->name);
  t->stack = (uint8_t *) t + PGSIZE;
  t->priority = priority;

  /* MA: Initializing base priority, waiting_on and priority_donations list */
  t->base_priority = priority;
  t->waiting_on = NULL;
  list_init(&t->acquired_locks);
  /* MA: END */

	if(t==initial_thread)
		t->ticks=0;
	if(thread_mlfqs)
	{
		if(t == initial_thread)
			t->recent_cpu = 0;
		else
			t->recent_cpu = thread_get_recent_cpu();
		t->thread_nice = 0;
	}
  t->file_index = 2;
  t->magic = THREAD_MAGIC;
  list_push_back (&all_list, &t->allelem);
//--------------------------------------AS initalize thread for user prog
#ifdef USERPROG
  //[X]init the new added data structure
  list_init(&t->child_list);
  list_init(&t->opened_files);
  sema_init (&(t->wsem),0);
  sema_init(&t->sync_sema,0);

  if(is_thread(running_thread ())){
    list_push_back(&(running_thread ()->child_list),&(t->child_elem));
    t->parent = running_thread();
  }
  else{
    t->parent = NULL;
  }

#endif
//-----------------------------------------
}

/* Allocates a SIZE-byte frame at the top of thread T's stack and
   returns a pointer to the frame's base. */
static void *
alloc_frame (struct thread *t, size_t size)
{
  /* Stack data is always allocated in word-size units. */
  ASSERT (is_thread (t));
  ASSERT (size % sizeof (uint32_t) == 0);

  t->stack -= size;
  return t->stack;
}

/* Chooses and returns the next thread to be scheduled.  Should
   return a thread from the run queue, unless the run queue is
   empty.  (If the running thread can continue running, then it
   will be in the run queue.)  If the run queue is empty, return
   idle_thread. */
static struct thread *
next_thread_to_run (void)
{
  if (list_empty (&ready_list))
    return idle_thread;
  else
    return list_entry (list_pop_front (&ready_list), struct thread, elem);
}

/* Completes a thread switch by activating the new thread's page
   tables, and, if the previous thread is dying, destroying it.

   At this function's invocation, we just switched from thread
   PREV, the new thread is already running, and interrupts are
   still disabled.  This function is normally invoked by
   thread_schedule() as its final action before returning, but
   the first time a thread is scheduled it is called by
   switch_entry() (see switch.S).

   It's not safe to call printf() until the thread switch is
   complete.  In practice that means that printf()s should be
   added at the end of the function.

   After this function and its caller retfurns, the thread switch
   is complete. */
void
thread_schedule_tail (struct thread *prev)
{
  struct thread *cur = running_thread ();

  ASSERT (intr_get_level () == INTR_OFF);

  /* Mark us as running. */
  cur->status = THREAD_RUNNING;

  /* Start new time slice. */
  thread_ticks = 0;

#ifdef USERPROG
  /* Activate the new address space. */
  process_activate ();
#endif

  /* If the thread we switched from is dying, destroy its struct
     thread.  This must happen late so that thread_exit() doesn't
     pull out the rug under itself.  (We don't free
     initial_thread because its memory was not obtained via
     palloc().) */
  if (prev != NULL && prev->status == THREAD_DYING && prev != initial_thread)
    {
      ASSERT (prev != cur);
      palloc_free_page (prev);
    }
}

/* Schedules a new process.  At entry, interrupts must be off and
   the running process's state must have been changed from
   running to some other state.  This function finds another
   thread to run and switches to it.

   It's not safe to call printf() until thread_schedule_tail()
   has completed. */
static void
schedule (void)
{
  struct thread *cur = running_thread ();
  struct thread *next = next_thread_to_run ();
  struct thread *prev = NULL;

  ASSERT (intr_get_level () == INTR_OFF);
  ASSERT (cur->status != THREAD_RUNNING);
  ASSERT (is_thread (next));

  if (cur != next)
    prev = switch_threads (cur, next);
  thread_schedule_tail (prev);
}


/* Returns a tid to use for a new thread. */
static tid_t
allocate_tid (void)
{
  static tid_t next_tid = 1;
  tid_t tid;

  lock_acquire (&tid_lock);
  tid = next_tid++;
  lock_release (&tid_lock);

  return tid;
}

/* Offset of `stack' member within `struct thread'.
   Used by switch.S, which can't figure it out on its own. */
uint32_t thread_stack_ofs = offsetof (struct thread, stack);

/*------------------------------------------------------------------------
--------------------------- M.Aladdin Begin --------------------------------
---------------------------------------------m.aladdin.gn@gmail.com-------*/

/* ready_list insertion function based on the scheduling mode */
void
ready_list_priority_insert(struct thread* new_thread){
	list_insert_ordered(&ready_list,&new_thread->elem,
    	thread_priority_more,(void*) 0);
}

/* Priority Scheduling: Comparison function that tells whether the priority of
   t1 is more than t2 or not */
bool
thread_priority_more(struct list_elem *it1,struct list_elem *it2
                          ,void *aux UNUSED){

  struct thread *t1 = list_entry(it1,struct thread,elem);
  struct thread *t2 = list_entry(it2,struct thread,elem);

  return t1->priority > t2->priority;
}

/* To check if not in MLFQS mode */
bool
is_mlfqs(){
  return thread_mlfqs;
}
/* Sorting the given thread list with descending priority */
void
sort_thread_list (struct list *l)
{
    if (list_empty (l))
        return;
    list_sort (l, thread_priority_more, NULL);
}

/* Tests whether the current thread should yield to a higher priority thread or
not */
void
thread_test_preemption (void)
{
  enum intr_level old_level = intr_disable ();
  if (!list_empty (&ready_list) && thread_current ()->priority <
      list_entry (list_front (&ready_list), struct thread, elem)->priority){
      thread_yield ();
    }
  intr_set_level (old_level);
}

/* Adds a held lock to current thread. */
void
thread_add_lock (struct lock *lock)
{
  enum intr_level old_level = intr_disable ();
  list_insert_ordered (&thread_current ()->acquired_locks, &lock->elem,
                       lock_priority_large, NULL);

  /* Update priority and test preemption if lock's priority
     is larger than current priority. */
  if (lock->max_priority > thread_current ()->priority)
    {
      thread_current ()->priority = lock->max_priority;
      thread_test_preemption ();
    }
  intr_set_level (old_level);
}
/* Remove a held lock from current thread. */
void
thread_remove_lock (struct lock *lock)
{
  enum intr_level old_level = intr_disable ();
  /* Remove lock from list and update priority. */
  list_remove (&lock->elem);
  thread_update_priority (thread_current ());
  intr_set_level (old_level);
}

/* Donate current thread's priority to another thread. */
void
thread_donate_priority (struct thread *t)
{
  enum intr_level old_level = intr_disable ();
  thread_update_priority (t);
  /* If thread is in ready list, reorder it. */
  if (t->status == THREAD_READY)
    {
      sort_thread_list(&ready_list);
      thread_test_preemption();
    }
  intr_set_level (old_level);
}

/* Update thread's priority. This function only update
   priority and do not preempt. */
void
thread_update_priority (struct thread *t)
{
  enum intr_level old_level = intr_disable ();
  int max_priority = t->base_priority;
  int lock_priority;

  /* Get locks' max priority. */
  if (!list_empty (&t->acquired_locks))
    {
      list_sort (&t->acquired_locks, lock_priority_large, NULL);
      lock_priority = list_entry (list_front (&t->acquired_locks),
                                  struct lock, elem)->max_priority;
      if (lock_priority > max_priority)
        max_priority = lock_priority;
    }

  t->priority = max_priority;
  intr_set_level (old_level);
}

/*------------------------------------------------------------------------
--------------------------- M.Aladdin End --------------------------------
---------------------------------------------m.aladdin.gn@gmail.com-------*/


/*------------------------------------------------------------------------
----------------------------Ahmed Shawky----------------------------------
--------------------------------------------------------------------------*/

/*return a child whose tid is given asa parameter*/
struct thread*
get_thread_by_tid (tid_t tid)
{
  struct list_elem *elem;
  struct thread *ret;
  enum intr_level old_level;
  old_level=intr_disable();
  ret = NULL;
  for (elem = list_begin (&(thread_current()->child_list));elem != list_end (&(thread_current()->child_list));
   elem = list_next (elem))
    {
      ret = list_entry (elem, struct thread, child_elem);
      ASSERT (is_thread (ret));
      if (ret->tid == tid)
      {
        intr_set_level (old_level);
        return ret;
      }
    }
  intr_set_level (old_level);
  return NULL;
}
