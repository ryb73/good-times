#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>

int main(int argc,	char *argv[ ]) {
	pid_t childpid;				/* indicates process should spawn another	 */
	int error;					/* return value from dup2 call				 */
	int fd[2];					/* file descriptors returned by pipe 		 */
	int i;						/* number of this process (starting with 1)	 */
	int nprocs;					/* total number of processes in ring 		 */
	char* recstr, *sentstr, *temp;
	char* substring1, *substring2;
	int num1 = 0;
	int num2 = 0;
	int output = 0;
	int maxchars = 32000;
	int status;
	pid_t orig_pid;

	temp = (char*) malloc(maxchars);
	recstr = (char*) malloc(maxchars);
	sentstr = (char*) malloc(maxchars);
	substring1 = (char*) malloc(maxchars);
	substring2 = (char*) malloc(maxchars);
	

	/* check command line for a valid number of processes to generate */
	if ( (argc != 2) || ((nprocs = atoi (argv[1])) <= 0) ) {
		fprintf (stderr, "Usage: %s nprocs\n", argv[0]);
		return 1; 
	}  
	if (pipe (fd) == -1) {		/* connect std input to std output via a pipe */
	  perror("Failed to create starting pipe");
	  return 1;
	}
	if ((dup2(fd[0], STDIN_FILENO) == -1) ||
		(dup2(fd[1], STDOUT_FILENO) == -1)) {
	  perror("Failed to connect pipe");
	  return 1;
	}
	if ((close(fd[0]) == -1) || (close(fd[1]) == -1)) {
	  perror("Failed to close extra descriptors");
	  return 1; 
	}		
	
	for (i = 1; i < nprocs;	i++) {			/* create the remaining processes */
		if (pipe (fd) == -1) {
		 fprintf(stderr, "[%ld]:failed to create pipe %d: %s\n",
				(long)getpid(), i, strerror(errno));
		 return 1; 
		}
		
		if ((childpid = fork()) == -1) {
			fprintf(stderr, "[%ld]:failed to create child %d: %s\n",
				 (long)getpid(), i, strerror(errno));
		 return 1; 
		} 

		if (childpid > 0) 			  /* for parent process, reassign stdout */
		  error = dup2(fd[1], STDOUT_FILENO);
		else								/* for child process, reassign stdin */
		  error = dup2(fd[0], STDIN_FILENO);
		
		if (error == -1) {
			fprintf(stderr, "[%ld]:failed to dup pipes for iteration %d: %s\n",
				 (long)getpid(), i, strerror(errno));
			return 1; 
		}
	  
		if ((close(fd[0]) == -1) || (close(fd[1]) == -1)) {
		 fprintf(stderr, "[%ld]:failed to close extra descriptors %d: %s\n",
				(long)getpid(), i, strerror(errno));
		 return 1; 
		}

		if (childpid)
		{			
			// end parent process
			break;
		}

		orig_pid = getppid();			

		//read from stdin (should be stdout of parent)
		strcpy(recstr, "");
		status = read(STDIN_FILENO, recstr, maxchars);
		strcpy(temp, recstr);

		//split that string into its 2 integers
		strcpy(substring1, strtok(temp," ")); 
		strcpy(substring2, strtok(NULL," "));
		num1 = atoi(substring1);
		num2 = atoi(substring2);
	 }

	if (i==1)
	{
		// if this is the original parent, put "1 1" into stdout
		strcpy(sentstr, "1 1");
		fflush(stdout);
		fprintf(stdout, "%s", sentstr);
		
		// read stdin to see whether last process reported back yet 
		fflush(stdout);
		status = read(STDIN_FILENO, recstr, maxchars);
		fprintf(stderr, "Original Parent read %s\n", recstr);
		strcpy(substring1, strtok(recstr," "));
		strcpy(substring2, strtok(NULL," "));
		fprintf(stderr, "Fib(%d) = %s\n", nprocs + 1, substring2);
	} else {
		//print out the 2nd integer, then the sum of both integers
		sprintf(sentstr, "%d %d", num2, num1 + num2);
		fprintf(stdout, "%s", sentstr);
		fprintf(stderr, "Process %d with ID %ld and parent id %ld received %s and sent %s.\n",
		i, (long)getpid(), (long)orig_pid, recstr, sentstr);
	}

	free(recstr);
	free(sentstr);
	free(temp);
	free(substring1);
	free(substring2);
	return 0; 
}	  
