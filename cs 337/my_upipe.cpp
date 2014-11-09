#include <iostream>
#include <unistd.h>
#include <sys/types.h>

using namespace std;

int main(int argc, char* argv[]) {
	if(argc != 3) {
		cout << "Usage: " << argv[0] << " cmd1 cmd2" << endl;
		return 1;
	}

	int fd[2];
	if(pipe(fd) == -1) {
		perror("Error creating pipe.");
		return 2;
	}

	pid_t fork_id = fork();
	if(fork_id == -1) {
		perror("Unable to create child process.");
		return 3;
	} else if(fork_id == 0) {
		// parent process
		if(dup2(fd[0], STDIN_FILENO) == -1) {
			perror("Error duplicating file descriptor for reading.");
			return 4;
		} else if(close(fd[0]) == -1 || close(fd[1]) == -1) {
			perror("Error closing pipe file descriptors.");
			return 5;
		} else {
			system(argv[2]);
		}
	} else {
		// parent process
		if(dup2(fd[1], STDOUT_FILENO) == -1) {
			perror("Error duplicating file descriptor for writing.");
			return 6;
		} else if(close(fd[0]) == -1 || close(fd[1]) == -1) {
			perror("Error closing pipe file descriptors.");
			return 7;
		} else {
			system(argv[1]);
		}
	}

	return 0;
}
