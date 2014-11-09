#include <stdio.h>
#include <stdlib.h>

int main(int argc, char** argv) {
	if(argc!=3) {
		fprintf(stderr, "Usage: %s sleep_time repeat_factor\n", argv[0]);
		return 1;
	}

	int repeatFactor = atoi(argv[2]);
	int sleepTime = atoi(argv[1]);

	int i;
	for(i = 0; i < repeatFactor; i++)
	{
		sleep(sleepTime);
		fprintf(stderr, "Current iteration times: %i, Process ID: %i, Parent Process ID: %i\n",
				i, getpid(), getppid());
	}

	return 0;
}
