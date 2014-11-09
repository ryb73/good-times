#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <limits.h>
#include <errno.h>

int makeargv(const char *s, const char *delimiters, char ***argvp);
pid_t r_wait(int *stat_loc);

int main(int argc, char** argv) {
	if (argc != 2) {
		fprintf(stderr, "Usage: %s n\n", argv[0]);
		return 1;
	}

	char* commandline;
	int pr_count, pr_limit;
	pr_limit = atoi(argv[1]);
	pr_count = 0;
	while (fgets(commandline, MAX_CANON, stdin) != NULL) {
		if(pr_count == pr_limit) {
			fprintf(stderr, "Reached the limit, pr_count=%i, pr_limit=%i \n",
					pr_count, pr_limit);
			wait(NULL);
			pr_count--;
		}

		fprintf(stderr, "The command line is : %s", commandline);
		fprintf(stderr, "Current pr_count = %i, pr_limit = %i, pid = %i \n",
				pr_count, pr_limit, (long)getpid());
		pr_count++;
		pid_t childpid = fork();
		if(childpid == -1) {
			fprintf(stderr, "Failed to fork process.\n");
			return 0;
		} else if(childpid == 0) {
			char** tokens;
			makeargv(commandline, " ", &tokens);
			execvp(tokens[0], tokens);
			return 0;
		}
	}

	while(r_wait(NULL) > 0)	// wait for all children
		;

	return 0;
}

int makeargv(const char *s, const char *delimiters, char ***argvp) {
   int error;
   int i;
   int numtokens;
   const char *snew;
   char *t;

   if ((s == NULL) || (delimiters == NULL) || (argvp == NULL)) {
      errno = EINVAL;
      return -1;
   }
   *argvp = NULL;
   snew = s + strspn(s, delimiters);         /* snew is real start of string */
   if ((t = malloc(strlen(snew) + 1)) == NULL)
      return -1;
   strcpy(t, snew);
   numtokens = 0;
   if (strtok(t, delimiters) != NULL)     /* count the number of tokens in s */
      for (numtokens = 1; strtok(NULL, delimiters) != NULL; numtokens++) ;

                             /* create argument array for ptrs to the tokens */
   if ((*argvp = malloc((numtokens + 1)*sizeof(char *))) == NULL) {
      error = errno;
      free(t);
      errno = error;
      return -1;
   }
                        /* insert pointers to tokens into the argument array */
   if (numtokens == 0)
      free(t);
   else {
      strcpy(t, snew);
      **argvp = strtok(t, delimiters);
      for (i = 1; i < numtokens; i++)
          *((*argvp) + i) = strtok(NULL, delimiters);
    }
    *((*argvp) + numtokens) = NULL;             /* put in final NULL pointer */
    return numtokens;
}

pid_t r_wait(int *stat_loc) {
   pid_t retval;
   while (((retval = wait(stat_loc)) == -1) && (errno == EINTR)) ;
   return retval;
}
