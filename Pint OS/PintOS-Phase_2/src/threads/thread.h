#ifndef THREADS_THREAD_H
#define THREADS_THREAD_H
#define max( a ,  b) ( (a) >= (b) ? (a) : (b))
#define min( a ,  b) ( (a) <= (b) ? (a) : (b))
#include <debug.h>
#include <list.h>
#include "threads/synch.h"
#include <stdint.h>

/* States in a thread's life cycle. */
enum thread_status
  {
    THREAD_RUNNING,     /* Running thread. */
    THREAD_READY,       /* Not running but ready to run. */
    THREAD_BLOCKED,     /* Waiting for an event to trigger. */
    THREAD_DYING        /* About to be destroyed. */
  };

/* Thread identifier type.
   You can redefine this to whatever type you like. */
//--------------------------------------------------
typedef int pid_t;                      /*defiining the type of pid_t*/
//-----------------------------------------------------
typedef int tid_t;
#define TID_ERROR ((tid_t) -1)          /* Error value for tid_t. */

/* Thread priorities. */
#define PRI_MIN 0                       /* Lowest priority. */
#define PRI_DEFAULT 31                  /* Default priority. */
#define PRI_MAX 63                      /* Highest priority. */

/* A kernel thread or user process.

   Each thread structure is stored in its own 4 kB page.  The
   thread structure itself sits at the very bottom of the page
   (at offset 0).  The rest of the page is reserved for the
   thread's kernel stack, which grows downward from the top of
   the page (at offset 4 kB).  Here's an illustration:

        4 kB +---------------------------------+
             |          kernel stack           |
             |                |                |
             |                |                |
             |                V                |
             |         grows downward          |
             |                                 |
             |                                 |
             |                                 |
             |                                 |
             |                                 |
             |                                 |
             |                                 |
             |                                 |
             +---------------------------------+
             |              magic              |
             |                :                |
             |                :                |
             |               name              |
             |              status             |
        0 kB +---------------------------------+

   The upshot of this is twofold:

      1. First, `struct thread' must not be allowed to grow too
         big.  If it does, then there will not be enough room for
         the kernel stack.  Our base `struct thread' is only a
         few bytes in size.  It probably should stay well under 1
         kB.

      2. Second, kernel stacks must not be allowed to grow too
         large.  If a stack overflows, it will corrupt the thread
         state.  Thus, kernel functions should not allocate large
         structures or arrays as non-static local variables.  Use
         dynamic allocation with malloc() or palloc_get_page()
         instead.

   The first symptom of either of these problems will probably be
   an assertion failure in thread_current(), which checks that
   the `magic' member of the running thread's `struct thread' is
   set to THREAD_MAGIC.  Stack overflow will normally change this
   value, triggering the assertion. */
/* The `elem' member has a dual purpose.  It can be an element in
   the run queue (thread.c), or it can be an element in a
   semaphore wait list (synch.c).  It can be used these two ways
   only because they are mutually exclusive: only a thread in the
   ready state is on the run queue, whereas only a thread in the
   blocked state is on a semaphore wait list. */

struct thread
  {
    /* Owned by thread.c. */
    tid_t tid;                          /* Thread identifier. */
    enum thread_status status;          /* Thread state. */
    char name[16];                      /* Name (for debugging purposes). */
    uint8_t *stack;                     /* Saved stack pointer. */
    int priority;                       /* Priority. */

    /* MA: Priority Donations: Additional members */
    int base_priority;                  /* Initial priority, set on init or when set */

    struct list acquired_locks;          /* Locks held by this thread. */
    struct lock *waiting_on;            /* Lock that is blocking this thread. */
    /* MA: END */

    struct list_elem allelem;           /* List element for all threads list. */
    int thread_nice ;										/* how nice is it to other threads */
    int recent_cpu  ;										/* the cpu time recieved by this thread*/
    /* Shared between thread.c and synch.c. */
    struct list_elem elem;              /* List element. */

    /* Owned by userprog/process.c. */
    uint32_t *pagedir;                  /* Page directory. */

    /* Owned by thread.c. */
    unsigned magic;                     /* Detects stack overflow. */
    int64_t ticks;                      /* Time for the thread to wait*/

  #ifdef USERPROG

		int file_index;                     /* Index of the file to be opened next
                                           incremented when a file is opened by
                                           this thread */

		struct list opened_files;           /* List of the opened_files by this
                                           thread */

    int return_status;                  /* Return status of the child thread */
    int child_return_status;            /* A list to save the children of this
                                           thread */
    struct list child_list;             /* List to have the childs of this
                                           process */
    pid_t waiting_on_process;           /* pid of the process that this thread
                                           waiting on */
    struct semaphore wsem;              /* Semaphore to wait on in the processes
                                           wait */
    struct semaphore sync_sema;         /* Semaphore to synchronize the
                                           communication between the child
                                           thread and the current thread */

    struct list_elem child_elem;        /* List element for child threads */
    struct thread  *parent;                        /* tid of parent thread */
    struct file *exe_file;              /* Pointer to the executable file of The
                                           current thread */
    #endif
  };



/* If false (default), use round-robin scheduler.
   If true, use multi-level feedback queue scheduler.
   Controlled by kernel command-line option "-o mlfqs". */
extern bool thread_mlfqs;


void thread_init (void);
void thread_start (void);

void thread_tick (void);
void thread_print_stats (void);

typedef void thread_func (void *aux);
tid_t thread_create (const char *name, int priority, thread_func *, void *);

void thread_block (void);
void thread_unblock (struct thread *);

struct thread *thread_current (void);
tid_t thread_tid (void);
const char *thread_name (void);

void thread_exit (void) NO_RETURN;
void thread_yield (void);

/* Performs some operation on thread t, given auxiliary data AUX. */
typedef void thread_action_func (struct thread *t, void *aux);
void thread_foreach (thread_action_func *, void *);

/*calculations for MLFQ*/
void calc_new_priority_for_all(void);
void calc_new_priority(struct thread *t, void *aux);

void calc_load_avg(void);

void calc_new_recent_cpu(struct thread *t, void *aux);
void calc_recent_cpu(void);

int thread_get_priority (void);
void thread_set_priority (int);

int thread_get_nice (void);
void thread_set_nice (int);
int thread_get_recent_cpu (void);
int thread_get_load_avg (void);
/*-------------phase 2 functions---------------*/
struct file* get_open_file(int fd);

bool thread_priority_more(struct list_elem *it1,struct list_elem *it2
                          ,void *aux UNUSED);
#endif /* threads/thread.h */