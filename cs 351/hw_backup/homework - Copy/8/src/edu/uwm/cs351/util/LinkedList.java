// This is an assignment for students to complete after reading Chapter 4 of
// "Data Structures and Other Objects Using Java" by Michael Main.

package edu.uwm.cs351.util;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/******************************************************************************
 * This class is a homework assignment;
 * A LinkedList is a collection of objects.
 *
 ******************************************************************************/
public class LinkedList<E>extends AbstractCollection<E>
{
	// Declare the private static Node class.
	// It should have a constructor but no methods.
	// The fields of Node should have "default" access (neither public, nor private)

	private static class Node<E> {
		E _data;
		Node<E> _next;

		public Node(E data, Node<E> next) {
			this._data = data;
			this._next = next;
		}
	}

	// We keep a head, a tail and a count of the number of items
	private int manyItems;
	private Node<E> head;
	private Node<E> tail;


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
	 * Check the invariant.  For information on what a class invariant is,
	 * please read page 123 in the textbook.
	 * Return false if any problem is found.  Returning the result
	 * of {@link #report(String)} will make it easier to debug invariant problems.
	 * @return whether invariant is currently true
	 */
	private boolean _wellFormed() {
		// Invariant:
		// list must not include a cycle.
		// manyItems is number of nodes in list
		// tail points to the last node in the list (if any)
		
		// Implementation:
		// Do multiple checks: each time returning false if a problem is found.
		
		// We do the first one for you:
		// check that list is not cyclic
		if (head != null) {
			// This check uses an interesting property described by Guy Steele (CLtL 1987)
			Node<E> fast = head._next;
			for (Node<E> p = head; fast != null && fast._next != null; p = p._next) {
				if (p == fast) return report("list is cyclic!");
				fast = fast._next._next;
			}
		}
		
		// Implement remaining conditions.

		// manyItems is number of nodes in list
		int count = 0;
		for(Node<E> n = head; n != null; n = n._next) {
			++count;
		}
		if(count != manyItems) return report("manyItems is inaccurate!");

		// tail points to the last node in the list (if any)
		Node<E> p = head;
		if(p != null) {
			for(; p._next != null; p = p._next) { }
		}
		if(p != tail) return report("tail does not point to the last node!");

		// If no problems found, then return true:
		return true;
	}


	/**
	 * Create an empty sequence.
	 * @param - none
	 * @postcondition
	 *   This sequence is empty 
	 **/   
	public LinkedList( )
	{
		head = null;
		tail = null;
		manyItems = 0;
		assert _wellFormed() : "invariant failed in constructor";
	}

	/**
	 * Remove all elements in this collection.
	 **/
	@Override
	public void clear( )
	{
		assert _wellFormed() : "invariant wrong at start of clear()";

		head = null;
		tail = null;
		manyItems = 0;

		assert _wellFormed() : "invariant wrong at end of clear()";
	}
	
	/**
	 * Determine the number of elements in this list.
	 * @param - none
	 * @return
	 *   the number of elements in this list
	 **/ 
	@Override
	public int size( )
	{
		assert _wellFormed() : "invariant wrong at start of size()";
		return manyItems;
		// This method shouldn't modify any fields, hence no assertion at end
	}

	/**
	 * Add a new element to this list at the end.
	 * @param element
	 *   the new element that is being added
	 * @return true
	 **/
	@Override
	public boolean add(E element)
	{
		assert _wellFormed() : "invariant wrong at start of add";

		Node<E> newNode = new Node<E>(element, null);
		if(isEmpty()) {
			head = tail = newNode;
		} else {
			tail._next = newNode;
			tail = newNode;
		}
		++manyItems;

		assert _wellFormed() : "invariant wrong at end of add";
		return true;
	}

	@Override
	public Iterator<E> iterator() {
		return new MyIterator();
	}

	private class MyIterator implements Iterator<E> {
		// Define whatever fields you wish
		// BUT: you need to check ALL redundancies in the _wellFormed predicate
		// NB: You can use head and tail

		private Node<E> precursor;
		private Node<E> cursor;

		private boolean _wellFormed() {
			// first check invariant of main class:
			if (!LinkedList.this._wellFormed()) return false; // problem already reported

			if((precursor != null && precursor != cursor && precursor._next != cursor) ||
					(precursor == null && cursor != null && cursor != head))
				return report("precursor is not consistent with cursor");

			// report any errors found
			return true;
		}

		// Implement every method needed for an iterator

		// Make sure you check the invariant at the start of every method
		// and at the end of every method that changes anything.

		MyIterator() {
			precursor = null;
			cursor = null;
			assert _wellFormed() : "invariant wrong at end of MyIterator()";
		}

		@Override
		public boolean hasNext() {
			assert _wellFormed() : "invariant wrong at beginning of hasNext()";

			if(isEmpty()) return false;
			return cursor == null || cursor._next != null;
		}

		/**
		 * @throws NoSuchElementException if there are no more elements.
		 */
		@Override
		public E next() throws NoSuchElementException {
			assert _wellFormed() : "invariant wrong at beginning of next()";

			if(!hasNext()) {
				throw new NoSuchElementException("No next element.");
			}
			if(!started()) {
				cursor = head;
			} else {
				if(precursor != cursor) {
					if(precursor == null) {
						precursor = head;
					} else {
						precursor = precursor._next;
					}
				}
				cursor = cursor._next;
			}

			assert _wellFormed() : "invariant wrong at end of next()";
			return cursor._data;
		}

		private boolean started() {
			assert _wellFormed() : "invariant wrong at beginning of started()";
			return precursor != null || cursor != null;
		}

		@Override
		public void remove() {
			assert _wellFormed() : "invariant wrong at beginning of remove()";

			if(precursor == cursor)
				throw new IllegalStateException("remove is not allowed at this time");

			if(tail == cursor)
				tail = precursor;
			if(precursor == null) {
				head = cursor._next;
			} else {
				precursor._next = cursor._next;
			}
			cursor = precursor;
			--manyItems;

			assert _wellFormed() : "invariant wrong at end of remove()";
		}
	}
}