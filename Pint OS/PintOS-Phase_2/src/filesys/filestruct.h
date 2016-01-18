#include <list.h>
#include <stdio.h>
#include "../threads/synch.h"

struct thrd_file_struct
{
	int file_descrip;													 /* Descriptor if this file */
	struct file * file_opened;                 /* Pointer to this file */
	struct list_elem file_elem;                /* List element for this file in
																								the opened_files list */
};
