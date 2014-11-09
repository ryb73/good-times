/**
 * 
 */
package edu.uwm.cs351;

import java.util.Comparator;

/**
 * @author Ryan
 *
 */
public class Smart implements Comparator<Task> {
	@Override
	public int compare(Task o1, Task o2) {
		// TODO Auto-generated method stub
		if(o1.getImportance() == Task.MAX_VALUE) return 1;
		double v1 = (100.0*o1.getImportance() / o1.getSize());
		double v2 = (100.0*o2.getImportance() / o2.getSize());
		double d1 = (o1.getDeadline() * 1.85);
		double d2 = (o2.getDeadline() * 1.85);
		return (int)((v1 - d1) - (v2 - d2));
	}

	@Override
	public String toString() {
		return "Smart";
	}

	private static Comparator<Task> instance = new Smart();
	public static Comparator<Task> getInstance() { return instance; }
}