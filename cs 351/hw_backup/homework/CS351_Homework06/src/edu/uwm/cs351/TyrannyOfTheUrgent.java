package edu.uwm.cs351;

import java.util.Comparator;

public class TyrannyOfTheUrgent implements Comparator<Task> {

	public int compare(Task o1, Task o2) {
		// Task 1 is higher priority (return > 0) if
		// it's deadline is sooner.  On ties, choose the more important
		int res = o2.getDeadline() - o1.getDeadline();
		if(res == 0) res = o1.getImportance() - o2.getImportance();
		return res;
	}
	
	@Override
	public String toString() {
		return "Tyranny of the urgent";
	}
	
	private static Comparator<Task> instance = new TyrannyOfTheUrgent();
	public static Comparator<Task> getInstance() { return instance; }

}
