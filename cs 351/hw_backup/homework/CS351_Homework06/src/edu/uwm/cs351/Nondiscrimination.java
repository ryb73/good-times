package edu.uwm.cs351;

import java.util.Comparator;

/**
 * A task priority mechanism that does not discriminate on the basis
 * of importance or deadline or size.  Every task is
 * highest priority.
 */
public class Nondiscrimination implements Comparator<Task> {

	public int compare(Task o1, Task o2) {
		return 0; // all tasks are of equal importance
	}

	@Override
	public String toString() {
		return "Nondiscrimination";
	}
	
	private static Comparator<Task> instance = new Nondiscrimination();
	public static Comparator<Task> getInstance() { return instance; }
}
