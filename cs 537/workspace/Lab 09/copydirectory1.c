/**
 * copydirectory1 -- single process, single thread
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

void copydir(char* from, char* to);
int readwrite(int fromfd, int tofd);
int copyfile(int fromfd, int tofd);
ssize_t r_read(int fd, void *buf, size_t size);
ssize_t r_write(int fd, void *buf, size_t size);
int isdirectory(char *path);
int getpermissions(char* path);

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

	copydir(argv[1], argv[2]);

	return 0;
}

void copydir(char* from, char* to) {
	struct dirent *direntp;
	DIR *dirp;

	if ((dirp = opendir(from)) == NULL) {
		char error[255];
		sprintf(error, "Failed to open directory '%s'", from);
		perror(error);
		return;
	}

	while ((direntp = readdir(dirp)) != NULL) {
		char newFrom[255];
		strcpy(newFrom, from);
		strcat(newFrom, "/");
		strcat(newFrom, direntp->d_name);

		char newTo[255];
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

			copydir(newFrom, newTo);
		} else {
			int inFile = open(newFrom, O_RDONLY);
			if(inFile == -1) {
				char errorMsg[128];
				sprintf(errorMsg, "Unable to open file '%s' for reading.", newFrom);
				perror(errorMsg);
				continue;
			}

			int outFile = open(newTo, O_WRONLY | O_CREAT | O_TRUNC, getpermissions(newFrom));
			if(inFile == -1) {
				char errorMsg[128];
				sprintf(errorMsg, "Unable to open file '%s' for writing.", newTo);
				perror(errorMsg);
				continue;
			}

			copyfile(inFile, outFile);
			close(inFile);
			close(outFile);
		}
	}

	while ((closedir(dirp) == -1) && (errno == EINTR)) ;
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
