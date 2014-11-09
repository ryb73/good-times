package edu.uwm.cs351;

import java.util.Comparator;

/**
 * Prefer high value tasks, that is tasks
 * that have the biggest bang (importance) for the buck (size).
 * FOr tasks with equal value, prefer the one with the earlier deadline.
 */
public class ValueHunter implements Comparator<Task> {

	public int compare(Task o1, Task o2) {
		// choose the task with higher value
		// (highest value per size)
		// On ties, choose the more urgent task
		int res;
		double value1 = ((double)o1.getImportance() / o1.getSize());
		double value2 = ((double)o2.getImportance() / o2.getSize());
		if(value1 == value2) {
			res = o2.getDeadline() - o1.getDeadline();
		} else if(value1 > value2) {
			res = 1;
		} else {
			res = -1;
		}

		return res;
	}

	@Override
	public String toString() {
		return "Value Hunter";
	}
	
	private static Comparator<Task> instance = new ValueHunter();
	public static Comparator<Task> getInstance() { return instance; }
}
