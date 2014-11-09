package edu.uwm.cs351;

import java.util.Comparator;

public class SortedBag<E> {
	
	/** 
	 * A data class used for the linked structure for the linked list implementation of Bag.
	 * This class is simply the generic version of the class from Homework #4
	 */
	private static class Node<E> {
		E data;
		Node<E> next;

		public Node(E data, Node<E> next) {
			this.data = data;
			this.next = next;
		}
		
		public Node() {
			this.next = this;
		}
	}
	
	/*
	 * The private fields are similar to that in Homework #4, except,
	 * with a dummy node linked cyclicly, we have no need for "head"
	 * We also got rid of the redundant cursor (always precursor.next)
	 */
	private int numItems;
	private Node<E> tail;
	private Comparator<E> cmp;

	/**
	 * Used to report an error found when checking the invariant.
	 * By providing a string, this will help debugging the class if the invariant should fail.
	 * @param error string to print to report the exact error found
	 * @return false always
	 */
	private boolean report(String error) {
		System.out.println("Invariant error found: " + error);
		return false;
	}

	/**
	 * Check the invariant.
	 * Return false if any problem is found.  Returning the result
	 * of {@link #report(String)} will make it easier to debug invariant problems.
	 * @return whether invariant is currently true
	 */
	private boolean _wellFormed() {
		if (tail == null) return report("tail is null!");
		if (cmp == null) return report("cmp is null");
		
		// check that list is not cyclic, except that tail reaches itself
		{
			Node<E> fast = tail.next;
			for (Node<E> p = tail; fast != tail && fast != null && 
								fast.next != tail && fast.next != null; p = p.next) {
				if (p == fast) return report("list is cyclic in the wrong way");
				fast = fast.next.next;
			}
		}
		
		int count = 0;
		for (Node<E> p = tail.next; p != tail; p= p.next) {
			if (p.next == null) return report("Found null pointer in list");
			if (count > 0 && count < numItems) {
				if (cmp.compare(p.data,p.next.data) > 0) return report("Found unsorted data in list");
			}
			++count;
		}
		if (numItems != count) return report("num items should be " + count+ ", was " + numItems);
		
		// If no problems found, then return true:
		return true;
	}

	/**
	 * Create an empty Bag.
	 * @param - none
	 * @postcondition
	 *   This Bag is empty 
	 **/   
	public SortedBag(Comparator<E> c )
	{
		if (c == null) throw new IllegalArgumentException("comparator must not be null");
		numItems = 0;
		tail = new Node<E>();
		cmp = c;
		assert _wellFormed() : "invariant failed in constructor";
	}

	public void setComparator(Comparator<E> c) {
		assert _wellFormed() : "invariant false before setComparator";
		if (cmp == c) return; // whew!
		cmp = c;
		// now we have to resort the items:
		// in order to do an efficient insertion sort, we
		// need to add things from the beginning, but we
		// want to keep things fast if the list is already or mostly sorted
		Node<E> dummy = tail.next;
		Node<E> reversed = null;
		Node<E> tmp = null;
		for (Node<E> p = dummy.next; p != dummy; p=tmp) {
			tmp = p.next;
			p.next = reversed;
			reversed = p;
		}
		tail = dummy;
		tail.next = dummy;
		for (Node<E> p = reversed; p != null; p = tmp) {
			tmp = p.next;
			Node<E> lag = dummy;
			while (lag != tail && c.compare(p.data,lag.next.data) > 0) {
				lag = lag.next;
			}
			p.next = lag.next;
			lag.next = p;
			//insert code here
			if(lag == tail) tail = lag.next;
		}
		assert _wellFormed() : "invariant false before setComparator";		
	}
	
	/**
	 * Add a new element to this Bag at the first legal place.
	 * @param element
	 *   the new element that is being added
	 * @postcondition
	 *   A new copy of the element has been added to this Bag
	 *   in order,
	 **/
	public void add(E element)
	{
		assert _wellFormed() : "invariant wrong at start of add";
		Node<E> lag = tail.next;
		while (lag != tail && cmp.compare(lag.next.data,element) < 0 ) {
			lag = lag.next;
		}
		lag.next = new Node<E>(element,lag.next);
		if (lag == tail) tail = lag.next;
		assert _wellFormed() : "invariant wrong at end of addAfter";
	}


	/**
	 * Generate a copy of this Bag.
	 * @param - none
	 * @return
	 *   The return value is a copy of this Bag. Subsequent changes to the
	 *   copy will not affect the original, nor vice versa.
	 *   Whatever was current in the original object is now current in the clone.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for creating the clone.
	 **/ 
	@SuppressWarnings("unchecked")
	public SortedBag<E> clone( )
	{  	 
		assert _wellFormed() : "invariant wrong at start of clone()";

		SortedBag<E> result;

		try
		{
			result = (SortedBag<E>) super.clone( );
		}
		catch (CloneNotSupportedException e)
		{  
			// This exception should not occur. But if it does, it would probably
			// indicate a programming error that made super.clone unavailable.
			// The most common error would be forgetting the "Implements Cloneable"
			// clause at the start of this class.
			throw new RuntimeException
			("This class does not implement Cloneable");
		}

		result.tail = new Node<E>(tail.data,null);
		Node<E> from, to;
		for (from = tail.next, to = result.tail; from != tail; from = from.next) {
			to = to.next = new Node<E>(from.data,null);
		}
		to.next = result.tail;

		assert _wellFormed() : "invariant wrong at end of clone()";
		assert result._wellFormed() : "invariant wrong for result of clone()";
		return result;
	}

	/**
	 * Determine the number of elements in this Bag.
	 * @param - none
	 * @return
	 *   the number of elements in this Bag
	 **/ 
	public int size( )
	{
		assert _wellFormed() : "invariant wrong at start of size()";
		return numItems;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		boolean first = true;
		for (Node<E> p = tail.next.next; p != tail.next; p = p.next) {
			if (first) first = false;
			else sb.append(",");
			sb.append(p.data);
		}
		sb.append("}");
		return sb.toString();

	}
}
