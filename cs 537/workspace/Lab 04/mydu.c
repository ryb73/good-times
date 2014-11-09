#include <stdio.h>
#include <dirent.h>
#include <stdbool.h>
#include <unistd.h>
#include <errno.h>
#include <sys/stat.h>
#include <sys/types.h>

bool include_files = false;
bool follow_links = false;

int depth_first_report(char* dir_name);
int size_file(char* path, int fsize);
int size_fileb(char* path, int fsize, bool print_files);
bool isdirectory(char *path);
int conditional_stat(const char* path, struct stat* buf);

int main(int argc, char* argv[]) {
	int num_files = 0;
	int i;
	for (i = 1; i < argc; ++i) {
		if (strcmp(argv[i], "-a") == 0) {
			include_files = true;
		} else if (strcmp(argv[i], "-L") == 0) {
			follow_links = true;
		} else if (isdirectory(argv[i])) {
			depth_first_report(argv[i]);
			++num_files;
		} else {
			size_fileb(argv[i], 0, true);
			++num_files;
		}
	}

	if (num_files == 0)
		depth_first_report(".");

	return 0;
}

/**
 * Recursively goes through each file and directory, adds up the total size, and prints
 * it out.
 */
int depth_first_report(char* dir_name) {
	struct dirent *direntp;
	DIR *dirp;

	if ((dirp = opendir(dir_name)) == NULL) {
		perror("Failed to open directory");
		return 0;
	}

	int dir_size = 0;

	while ((direntp = readdir(dirp)) != NULL) {
		char new_path[255];
		sprintf(new_path, "%s/%s", dir_name, direntp->d_name);

		if (strcmp(direntp->d_name, ".") == 0 || strcmp(direntp->d_name, "..") == 0) {
			continue;
		} else if (isdirectory(direntp->d_name)) {
			dir_size += depth_first_report(new_path);
		} else {
			dir_size += size_file(new_path, 0);
		}
	}

	dir_size = size_file(dir_name, dir_size);

	while ((closedir(dirp) == -1) && (errno == EINTR))
		;

	return dir_size;
}

/**
 * Determines the size of the given file/directory, adds it to the start_size,
 * and, depending on the type of the file and whether the "-a" flag was used,
 * prints it out.
 *
 * Returns the size of the file + start_size.
 */
int size_file(char* path, int fsize) {
	return size_fileb(path, fsize, include_files);
}

int size_fileb(char* path, int fsize, bool print_files) {
	struct stat statbuf;

	if (conditional_stat(path, &statbuf) == -1) {
		perror("Unable to get size of file");
		return 0;
	}

	fsize += (int) statbuf.st_size;

	if (print_files || isdirectory(path)) {
		printf("%d\t%s\n", fsize, path);
	}

	return fsize;
}

bool isdirectory(char *path) {
	struct stat statbuf;

	if (conditional_stat(path, &statbuf) == -1)
		return false;
	else
		return S_ISDIR(statbuf.st_mode);
}

/**
 * Acts as either stat or lstat, depending on whether the "-L" flag was used.
 */
int conditional_stat(const char* path, struct stat* buf) {
	if (follow_links)
		return stat(path, buf);
	else
		return lstat(path, buf);
}
