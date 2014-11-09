package edu.uwm.cs351;

import java.util.Comparator;

public class TaskList {

	private final Comparator<Task> _priority;
	private final Task _marker = new Task("dummy",Task.MAX_VALUE,0,0);

	public TaskList(Comparator<Task> priority) {
		_priority = priority;
	}
	
	public void add(Task t) {
		// We assume the marker is always highest priority
		// or at least not lower than any other task.
		// That way, the marker is always at the beginning.
		// If some other task has higher priority,
		// the priority comparator is perverse.
		if (_priority.compare(_marker, t) < 0) {
			System.out.println(_priority + "perversely prefers " + t);
		}
		_marker.addInPriority(t, _priority);
	}
	
	/**
	 * Perform all possible tasks in the list that can
	 * be completed before the time limit or the task's
	 * own deadline.  Every time a task is completed,
	 * the time advances by the size of the task, but
	 * no work can be done after the time limit has passed.
	 * Tasks should be performed in priority order, except
	 * that no task should even be attempted unless there will
	 * be time to complete it before the time limit and
	 * before its deadline.  The latter tasks should be discarded.
	 * At the end, all tasks should be completed or discarded:
	 * the task list should be empty.
	 * @param timeLimit how much time may elapse while performing tasks
	 * @return sum of the importance of each task completed on time.
	 */
	public int performAll(int timeLimit) {
		/*System.out.println("My tasks: (" + timeLimit + " time limit)");
		for (Task t = _marker.getNext(); t != null; t = t.getNext()) {
			System.out.println("  " + t);
		}*/

		int elapsed = 0;
		int totalImportance = 0;

		for(Task t = _marker.getNext(); t != null && timeLimit > 0; ) {
			int s = t.getSize();
			if(s <= timeLimit - elapsed && elapsed + t.getSize() <= t.getDeadline()) {
				totalImportance += t.getImportance();
				elapsed += t.getSize();
			}

			Task p = t;
			t = t.getNext();
			p.remove();
		}

		return totalImportance;
	}
}