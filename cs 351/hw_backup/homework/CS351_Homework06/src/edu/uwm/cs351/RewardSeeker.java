package edu.uwm.cs351;

import java.util.Comparator;

/**
 * Choose tasks based on which has the biggest reward,
 * that is, the most important.
 */
public class RewardSeeker implements Comparator<Task> {

	public int compare(Task o1, Task o2) {
		// Choose most important task first
		// Ignore other attributes
		return o1.getImportance() - o2.getImportance();
	}

	@Override
	public String toString() {
		return "Reward Seeker";
	}
	
	private static Comparator<Task> instance = new RewardSeeker();
	public static Comparator<Task> getInstance() { return instance; }
}
