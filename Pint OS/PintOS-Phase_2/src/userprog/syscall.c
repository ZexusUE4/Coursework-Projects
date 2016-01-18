#include "userprog/syscall.h"
#include <stdio.h>
#include <list.h>
#include <syscall-nr.h>

#include "devices/shutdown.h"

#include "userprog/pagedir.h"

#include "threads/interrupt.h"
#include "threads/thread.h"
#include "threads/synch.h"
#include "threads/vaddr.h"


#include "filesys/filesys.h"
#include "filesys/file.h"
#include "filesys/filestruct.h"

static void syscall_handler (struct intr_frame *);
static struct lock file_lock;


void
syscall_init (void)
{

  intr_register_int (0x30, 3, INTR_ON, syscall_handler, "syscall");
	lock_init (&file_lock);

}

static void
syscall_handler (struct intr_frame *f UNUSED)
{
  //--------------------------------------------------------------------------
  //-----------Ahmed SHawky.....email:ahmedshawkyanwar@gmail.com--------------
  //--------------------------------------------------------------------------
  uint32_t *pointer=f->esp;

  validate_addr(pointer);
  /*a switch used to detect the syscall number and call the required handler */
  switch (*pointer){
  	case SYS_HALT:{
      validate_addr(pointer+1);
	    halt();
	  	break;
    }
  	case SYS_EXIT:{

      validate_addr(pointer+1);
      int exit_status = *(pointer+1);
		  exit(exit_status);

  	 	break;
  	}

  	case SYS_EXEC:{

    validate_addr(pointer+1);
		f->eax=exec(*(pointer+1));

		break;
  	}
  	case SYS_WAIT:{

    validate_addr(pointer+1);
		f->eax=wait(*(pointer+1));     //found in process.c
 		 break;
  	}

    case SYS_CREATE:{
    validate_addr(pointer+1);
    validate_addr(pointer+2);
  		f->eax= syscall_create(*(pointer+1) , *(pointer+2) );
  		break;
	  }
	case SYS_REMOVE:{
    validate_addr(pointer+1);
		f->eax= syscall_remove(*(pointer+1) );
		break;
	}
	case SYS_OPEN:{
    validate_addr(pointer+1);
		f->eax= syscall_open(*(pointer+1) );
		break;
	}
	case SYS_TELL:{
    validate_addr(pointer+1);
		f->eax= syscall_tell (*(pointer+1) );
		break;
	}
	case SYS_SEEK:{
    validate_addr(pointer+1);
    validate_addr(pointer+2);
		syscall_seek (*(pointer+1), *(pointer+2) );
		break;
	}
	case SYS_WRITE:{
    validate_addr(pointer+1);
    validate_addr(pointer+2);
    validate_addr(pointer+3);

    int fd = *(pointer+1);
    const char * buf = *(pointer+2);
    unsigned size = *(pointer+3);

		f->eax= syscall_write (fd,buf,size);

		break;
	}
	case SYS_READ:{
    validate_addr(pointer+1);
    validate_addr(pointer+2);
    validate_addr(pointer+3);

		f->eax= syscall_read (*(pointer+1) , *(pointer+2), *(pointer+3) );
		break;
	}
	case SYS_FILESIZE:{
    validate_addr(pointer+1);
		f->eax= syscall_filesize (*(pointer+1));
		break;
	}
	case SYS_CLOSE:{
    validate_addr(pointer+1);
		syscall_close (*(pointer+1));
		break;
	}
	default :{
		printf ("system call!\n");
	}
  }

}
/*--------------------------------------Ahmed Shawky-----------------------------------*/
/*used to shutdown pintos*/
void
halt(){

	shutdown();
}
/*close the currently running process*/
void
exit(int status){

	thread_current()->return_status = status;

	printf("%s: exit(%d)\n",thread_name(),thread_current()->return_status);

	thread_exit();

}
/*execute a file by creating a new process*/
pid_t
exec(const char *cmd_line){

  validate_addr(cmd_line);

	return process_execute((cmd_line));  //found in process.c

}
/*make the current thread wait for a cirtain child*/
int
wait(pid_t pid){

	return process_wait(pid);

}



/*---------------------------------------Hassan Ahmed--------------------------------------*/

/* Creates a file with the specified size */
bool
syscall_create (const char *file, unsigned initial_size)
{
  validate_addr(file);

	lock_acquire(&file_lock);
	bool flag = filesys_create (file, initial_size) ;
	lock_release(&file_lock);
	return flag;
}

/* Removes a file if no process has taht file opened*/
bool
syscall_remove (const char *file)
{
  validate_addr(file);

	lock_acquire(&file_lock);
	bool flag = filesys_remove (file);
	lock_release(&file_lock);
	return flag;
}

/*opens a new file and returns a pointer to the file refered to
  it checks if the file exists or not and return -1*/
int
syscall_open (const char *file)
{
  /*Vailidate the pointer file to check if it's inisde userspace*/
  validate_addr(file);

  /*enters critical section*/
  lock_acquire(&file_lock);

  /*try to open file*/
  struct file * file_temp = filesys_open (file);

  /*checks if the file was opened*/
  if(file_temp==NULL)
  {
    lock_release(&file_lock);
    return -1;
  }
  else
	{
    /* Creates a new struct for the newly opened file */
    struct thread * temp_thread = thread_current();
		struct thrd_file_struct *opened_file = (struct thrd_file_struct *) malloc(sizeof *opened_file);
		opened_file->file_descrip = temp_thread->file_index;
		opened_file->file_opened = file_temp;

    /* Adds the file struct to the thread opened_files list */
		list_push_back( &(temp_thread->opened_files) , &(opened_file->file_elem));
    int temp_indx = thread_current()->file_index;
    thread_current()->file_index++;

    /*exits the critical section*/
    lock_release(&file_lock);
		return temp_indx;
	}
}

/*returns the size of the file refered to by fd*/
int
syscall_filesize (int fd)
{
	struct file* temp = get_open_file(fd);
	if(temp==NULL)
		return 0;
	else
		return file_length(temp);
}

/*Read from file refered to by fd*/
int
syscall_read (int fd, void *buffer, unsigned size)
{
  /*Vailidate the pointer buffer to check if it's inisde userspace*/
  validate_addr(buffer);
  validate_addr(buffer+size-1);

  /*checks if the fd refers to write in console if true exit*/
	if(fd==1 )
		return -1;

  /*lock the following part to prevent writing to the same file by different
    processes so that data won't be lost*/
	lock_acquire(&file_lock);

  /*checks if fd writes in console if fd =1*/
  if(fd==0)
	{
    int i = 0;
    for( i = 0 ; i < size ; i++)
    {
      char * p = (char *)((int)buffer + i);
      *p = input_getc();
    }
		lock_release(&file_lock);
	}

  /*get the file refered to by fd if the returned file is null then the file
    wasn't opened by this process*/
	struct file* temp = get_open_file(fd);
	if(temp==NULL)
	{
		lock_release(&file_lock);
		return -1;
	}

  /*read from the opened file*/
	int ret_val = file_read(temp , buffer , size);
	lock_release(&file_lock);
	return ret_val;
}

/* Writes into the file chosen by fd*/
int
syscall_write (int fd, const void *buffer, unsigned size)
{
  /*Vailidate the pointer buffer to check if it's inisde userspace*/
  validate_addr(buffer);
  validate_addr(buffer+size-1);
  /*checks if the fd refers to read in console if true exit*/
  if(fd==0 )
		exit(-1);
  /*lock the following part to prevent writing to the same file by different
    processes so that data won't be lost*/
	lock_acquire(&file_lock);

  /*checks if fd writes in console if fd =1*/
	if(fd==1)
	{
		putbuf (buffer, size);
		lock_release(&file_lock);
  	return size;
  }
  /*get the file refered to by fd if the returned file is null then the file
    isn't opened by this process*/
	struct file* temp = get_open_file(fd);
	if(temp==NULL)
	{
		lock_release(&file_lock);
		exit(-1);
	}
  /*writes in the opened file*/
	int ret_val = file_write(temp , buffer , size);
  /*exit critical section*/
	lock_release(&file_lock);

	return ret_val;
}

/* Moves the file pointer position to the given position */
void
syscall_seek (int fd, unsigned position)
{
	struct file* temp = get_open_file(fd);
	if(temp==NULL)
		return ;
	lock_acquire(&file_lock);
	file_seek( temp , position);
	lock_release(&file_lock);
}

/* Returns the poistion of the file pointer in the file with the given file
   descripitive */
unsigned
syscall_tell (int fd)
{
	struct file* temp = get_open_file(fd);
	if(temp==NULL)
		return 0;
	lock_acquire(&file_lock);
	int ret_val = file_tell( temp );
	lock_release(&file_lock);
		return ret_val;
}

/*Closes the opened file refered to by fd*/
void
syscall_close (int fd)
{
	struct file* temp = get_open_file(fd);
	if(temp==NULL)
		return ;
	lock_acquire(&file_lock);
	file_close( temp );
	remove_file(fd);
	lock_release(&file_lock);
}
/*---------------------------------------Hassan Ahmed------------------------*/

/*------------------------------------------------------------------------
--------------------------- M.Aladdin Begin --------------------------------
---------------------------------------------m.aladdin.gn@gmail.com-------*/

/* Validates the recieved address if it's mapped and in the user Virtual address
   space */
void validate_addr(uint32_t address){
    if(!is_user_vaddr(address) || address == NULL || pagedirect_mapped(thread_current()->pagedir,address) == false){
      exit(-1);
    }
}

/*------------------------------------------------------------------------
--------------------------- M.Aladdin End --------------------------------
---------------------------------------------m.aladdin.gn@gmail.com-------*/
