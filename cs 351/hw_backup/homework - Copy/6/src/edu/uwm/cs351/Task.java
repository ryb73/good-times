package edu.uwm.cs351;

import java.util.Comparator;

public class Task {

	final String _name;
	
	public static final int MAX_VALUE = 1000;
	
	// the following integers should be in the range [0,MAX_VALUE]
	private final int _importance; // units of good for accomplishing
	private final int _deadline; // units of time after beginning that it must be completed
	private final int _size; // how many units of time to complete
	
	private Task _prev, _next;
	
	public static class ParameterException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public ParameterException(String s) { 
			super("Parameter " +s + " exceeded limits of [0," + MAX_VALUE + "]"); 
		}
	}
	
	private static void checkParameter(String name, int p) {
		if (p < 0 || p > MAX_VALUE) throw new ParameterException(name);
	}
	
	public Task(String name, int importance, int deadline, int size) {
		checkParameter("importance",importance);	
		checkParameter("importance",importance);	
		_name = name;
		_importance = importance;
		_deadline = deadline;
		_size = size;
	}	
	
	public String getName() {
		return _name;
	}
	
	public int getImportance() {
		return _importance;
	}
	
	public int getDeadline() {
		return _deadline;
	}
	
	public int getSize() {
		return _size;
	}
	
	public Task getPrevious() {
		return _prev;
	}
	
	public Task getNext() {
		return _next;
	}
	
	// "@Override" means we want to change the normal
	// behavior of this method, inherited from Object.
	// "@Override" is optional in Java, but recommended
	@Override
	public String toString() {
		return "Task(" + _name + "," + _importance + "," + _deadline + "," + _size + ")";
	}
	
	/**
	 * Add another task into this list by priority order.
	 * If the other task is higher priority, place it somewhere before this task.
	 * Otherwise place it somewhere after this task.
	 * It may be necessary to move multiple times forward
	 * or multiple times backward (but not both!) but in neither case can it
	 * use loops.  It must use recursion (to other nodes) instead.
	 * @param other task to add
	 * @param priority task priority of tasks in the list.
	 * @throws IllegalArgumentException if trying to add a duplicate task to the list.
	 */
	public void addInPriority(Task other, Comparator<Task> priority) {
		if(other == this)
			throw new IllegalArgumentException("Cannot add the same task twice.");

		if(priority.compare(this, other) >= 0) {
			if(_next == null || (priority.compare(_next, other) < 0)) {
				addAfter(other);
			} else {
				_next.addInPriority(other, priority);
			}
		} else {
			if(_prev == null || priority.compare(_prev, other) > 0) {
				addBefore(other);
			} else {
				_prev.addInPriority(other, priority);
			}
		}
	}

	/**
	 * @param other task to add
	 * @throws NullPointerException if other is null
	 */
	private void addBefore(Task other) {
		if(this._prev != null)
			this._prev._next = other;
		other._prev = this._prev;
		other._next = this;
		_prev = other;
	}

	/**
	 * @param other task to add
	 * @throws NullPointerException if other is null
	 */
	private void addAfter(Task other) {
		if(this._next != null)
			this._next._prev = other;
		other._next = this._next;
		other._prev = this;
		_next = other;
	}
	
	/**
	 * Remove this item from its list.
	 * This task will be disconnected from any other task.
	 */
	public void remove() {
		if(this._prev != null)
			this._prev._next = this._next;
		if(this._next != null)
			this._next._prev = this._prev;

		this._next = null;
		this._prev = null;
	}
}
