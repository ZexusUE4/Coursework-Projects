#ifndef USERPROG_SYSCALL_H
#define USERPROG_SYSCALL_H
#include <list.h>


void syscall_init (void);
bool syscall_create (const char *file, unsigned initial_size);
bool syscall_remove (const char *file);
int  syscall_open (const char *file);
int  syscall_filesize (int fd);
int  syscall_read (int fd, void *buffer, unsigned size);
int  syscall_write (int fd, const void *buffer, unsigned size);
void syscall_seek (int fd, unsigned position);
void syscall_close (int fd);
unsigned syscall_tell (int fd);
#endif /* userprog/syscall.h */


