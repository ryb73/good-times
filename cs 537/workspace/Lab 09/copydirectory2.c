/**
 * copydirectory2 -- multi-process
 */

#include <errno.h>
#include <fcntl.h>
#include <limits.h>
#include <string.h>
#include <dirent.h>
#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/select.h>
#include <sys/time.h>
#include <sys/wait.h>

#define BLKSIZE PIPE_BUF

void populate_file_list(int fd, char* from, char* to);
int readwrite(int fromfd, int tofd);
int copyfile(int fromfd, int tofd);
ssize_t r_read(int fd, void *buf, size_t size);
ssize_t r_write(int fd, void *buf, size_t size);
int isdirectory(char *path);
int getpermissions(char* path);
pid_t r_wait(int *stat_loc);
int r_close(int fildes);
int readline(int fd, char* line, int maxLen);

int main(int argc, char* argv[]) {
	if(argc != 3) {
		printf("Usage: %s from_dir to_dir\n", argv[0]);
		return 1;
	}

	// If the directory doesn't exist, create it
	if(access(argv[2], F_OK) != 0 && mkdir(argv[2], getpermissions(argv[1])) == -1) {
		char errorMsg[128];
		sprintf(errorMsg, "Unable to create directory '%s'.", argv[2]);
		perror(errorMsg);
		return 2;
	}

	int fd[2];
	if(pipe(fd) == -1) {
		perror("Unable to create pipe between processes.");
		return 3;
	}

	// Used to control access to main pipe
	int tickets[2];
	if(pipe(tickets) == -1) {
		perror("Unable to create pipe between processes.");
		return 4;
	}

	pid_t child_pid;
	int i;
	for(i = 0; i < 5; ++i) {
		if((child_pid = fork()) == 0)
			break;
	}

	if(child_pid > 0) {
		// Parent
		r_close(fd[0]);
		r_close(tickets[0]);

		// Allow children to begin reading
		r_write(tickets[1], "t", 1);

		//printf("Parent process %ld populating list of files to copy.\n", (long)getpid());
		populate_file_list(fd[1], argv[1], argv[2]);
		r_close(fd[1]);

		//printf("Finished populating list.\n");

		while(r_wait(0) >= 0) // wait for children
			;
	} else if(child_pid == 0) {
		// Children
		r_close(fd[1]);

		char fromFile[256];
		char toFile[256];

		int complete = 0;
		while(!complete) {
			char ticket;
			r_read(tickets[0], &ticket, 1);
			//printf("child %ld obtained ticket\n", (long)getpid());

			if(readline(fd[0], fromFile, 256) > 0) {
				//printf("Child %ld read: '%s'\n", (long)getpid(), fromFile);

				readline(fd[0], toFile, 256);
				//printf("Child %ld read: '%s'\n\n", (long)getpid(), toFile);

				int inFile = open(fromFile, O_RDONLY);
				if(inFile == -1) {
					char errorMsg[128];
					sprintf(errorMsg, "Unable to open file '%s' for reading.", fromFile);
					perror(errorMsg);
					continue;
				}

				int outFile = open(toFile, O_WRONLY | O_CREAT | O_TRUNC, getpermissions(fromFile));
				if(inFile == -1) {
					char errorMsg[128];
					sprintf(errorMsg, "Unable to open file '%s' for writing.", toFile);
					perror(errorMsg);
					continue;
				}

				copyfile(inFile, outFile);
				close(inFile);
				close(outFile);
			} else {
				complete = 1;
			}

			// Return ticket
			//printf("child %ld returning ticket\n", (long)getpid());
			r_write(tickets[1], "t", 1);
		}
	}

	//printf("Process %ld exiting.\n", (long)getpid());
	//copydir(argv[1], argv[2]);

	return 0;
}

void populate_file_list(int fd, char* from, char* to) {
	struct dirent *direntp;
	DIR *dirp;

	if ((dirp = opendir(from)) == NULL) {
		char error[255];
		sprintf(error, "Failed to open directory '%s'", from);
		perror(error);
		return;
	}

	char newFrom[255];
	char newTo[255];
	while ((direntp = readdir(dirp)) != NULL) {
		strcpy(newFrom, from);
		strcat(newFrom, "/");
		strcat(newFrom, direntp->d_name);

		strcpy(newTo, to);
		strcat(newTo, "/");
		strcat(newTo, direntp->d_name);

		if(strcmp(direntp->d_name, ".") == 0 || strcmp(direntp->d_name, "..") == 0) {
			// do nothing
		} else if(isdirectory(newFrom)) {
			if(access(newTo, F_OK) != 0 && mkdir(newTo, getpermissions(newFrom)) == -1) {
				char errorMsg[128];
				sprintf(errorMsg, "Unable to create directory '%s'.", newTo);
				perror(errorMsg);
				continue;
			}

			populate_file_list(fd, newFrom, newTo);
		} else {
			r_write(fd, newFrom, strlen(newFrom));
			r_write(fd, "\n", 1);
			r_write(fd, newTo, strlen(newTo));
			r_write(fd, "\n", 1);
			//printf("Parent wrote \"%s::%s\" to the queue.\n", newFrom, newTo);
		}
	}

	while ((closedir(dirp) == -1) && (errno == EINTR))
		;
}

int readwrite(int fromfd, int tofd) {
   char buf[BLKSIZE];
   int bytesread;

   if ((bytesread = r_read(fromfd, buf, BLKSIZE)) < 0)
      return -1;
   if (bytesread == 0)
      return 0;
   if (r_write(tofd, buf, bytesread) < 0)
      return -1;
   return bytesread;
}

int copyfile(int fromfd, int tofd) {
   int bytesread;
   int totalbytes = 0;

   while ((bytesread = readwrite(fromfd, tofd)) > 0)
      totalbytes += bytesread;
   return totalbytes;
}

ssize_t r_read(int fd, void *buf, size_t size) {
   ssize_t retval;
   while (retval = read(fd, buf, size), retval == -1 && errno == EINTR) ;
   return retval;
}

ssize_t r_write(int fd, void *buf, size_t size) {
   char *bufp;
   size_t bytestowrite;
   ssize_t byteswritten;
   size_t totalbytes;

   for (bufp = buf, bytestowrite = size, totalbytes = 0;
        bytestowrite > 0;
        bufp += byteswritten, bytestowrite -= byteswritten) {
      byteswritten = write(fd, bufp, bytestowrite);
      if ((byteswritten) == -1 && (errno != EINTR))
         return -1;
      if (byteswritten == -1)
         byteswritten = 0;
      totalbytes += byteswritten;
   }
   return totalbytes;
}

int isdirectory(char *path) {
   struct stat statbuf;

   if (stat(path, &statbuf) == -1)
      return 0;
   else
      return S_ISDIR(statbuf.st_mode);
}

int getpermissions(char* path) {
	struct stat info;
	if(stat(path, &info) == -1)
		return 0;

	return info.st_mode & 0777;
}

pid_t r_wait(int *stat_loc) {
   pid_t retval;
   while (((retval = wait(stat_loc)) == -1) && (errno == EINTR)) ;
   return retval;
}

int r_close(int fildes) {
   int retval;
   while (retval = close(fildes), retval == -1 && errno == EINTR) ;
   return retval;
}

int readline(int fd, char* line, int maxLen) {
	int numRead = 0;
	while(numRead < maxLen && read(fd, line, 1) > 0) {
		if(line[0] == '\n') {
			line[0] = 0;
			return numRead;
		}

		++numRead;
		++line;
	}

	return numRead;
}
