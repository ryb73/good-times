// This is an assignment for students to complete after reading Chapter 5 of
// "Data Structures and Other Objects Using Java" by Michael Main.

package edu.uwm.cs351;

/******************************************************************************
 * This class is a homework assignment;
 * A Seq is a collection.
 * The sequence can have a special "current element," which is specified and 
 * accessed through four methods
 * (start, getCurrent, advance and hasCurrent).
 *
 ******************************************************************************/
public class Seq<E> implements Cloneable
{
	
	/** 
	 * A data class used for the linked structure for the linked list implementation of Sequence.
	 * This class is a doubly-linked generic version of the class from Homework #5
	 */
	private static class Node<E> {
		E _data;
		Node<E> _prev, _next;

		public Node(E data, Node<E> prev, Node<E> next) {
			this._data = data;
			this._prev = prev;
			this._next = next;
		}
	}
	
	/*
	 * We do everything from the dummy node that permits us to
	 * access the head and tail easily.  We also have a cursor pointer.
	 */
	private int _numItems;
	private Node<E> _dummy;
	private Node<E> _cursor;

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
		// Invariant:
		// The dummy is never null.
		// The list must not include a cycle, except from the tail to the dummy to the head.
		// The list includes no null prev or next pointers.
		// Every _next pointer has the opposite _prev pointer.
		// The node pointed at by cursor is in the list.
		// numItems is number of nodes in list, excluding the dummy node
		
		// Implementation:
		// Do multiple checks: each time returning false if a problem is found.
		
		// We do the first two for you
		
		if (_dummy == null) return report("dummy is null!");
		
		// check that list is not cyclic, except that tail reaches itself
		{
			Node<E> fast = _dummy._next;
			for (Node<E> p = _dummy; fast != _dummy && fast != null && 
								fast._next != _dummy && fast._next != null; p = p._next) {
				if (p == fast) return report("list is cyclic in the wrong way");
				fast = fast._next._next;
			}
		}
		// NB: For Homework #8, we intend to ask you to write the cycle check code yourself!
		
		// Implement remaining conditions (not as many as before).

		// The list includes no null prev or next pointers.
		if(_dummy._next == null) return report("list includes null next pointer");
		if(_dummy._prev == null) return report("list includes null prev pointer");
		for(Node<E> n = _dummy._next; n != _dummy; n = n._next) {
			if(n._next == null) return report("list includes null next pointer");
			if(n._prev == null) return report("list includes null prev pointer");
		}

		// Every _next pointer has the opposite _prev pointer.
		if(_dummy._next._prev != _dummy) return report("next and prev links don't match");
		for(Node<E> n = _dummy._next; n != _dummy; n = n._next) {
			if(n != n._next._prev) return report("next and prev links don't match");
		}

		// The node pointed at by cursor is in the list.
		boolean found = _cursor == _dummy;
		for(Node<E> n = _dummy._next; !found && n != _dummy; n = n._next) {
			if(n == _cursor) found = true;
		}
		if(!found) return report("cursor doesn't point to a node in the list");

		// numItems is number of nodes in list, excluding the dummy node
		int count = 0;
		for(Node<E> n = _dummy._next; n != _dummy; n = n._next) {
			++count;
		}
		if(count != _numItems) return report("numItems is inaccurate");

		// If no problems found, then return true:
		return true;
	}


	/**
	 * Create an empty sequence.
	 * @param - none
	 * @postcondition
	 *   This sequence is empty 
	 **/   
	public Seq( )
	{
		_numItems = 0;
		_dummy = new Node<E>(null, null, null);
		_dummy._next = _dummy;
		_dummy._prev = _dummy;
		_cursor = _dummy;

		assert _wellFormed() : "invariant failed in constructor";
	}


	/// Iterator Implementation:
	
	/**
	 * Set the current element at the front of this sequence.
	 * @param - none
	 * @postcondition
	 *   The front element of this sequence is now the current element (but 
	 *   if this sequence has no elements at all, then there is no current 
	 *   element).
	 **/ 
	public void start( )
	{
		assert _wellFormed() : "invariant wrong at start of start()";

		_cursor = _dummy._next;

		assert _wellFormed() : "invariant wrong at end of start()";
	}

	
	/**
	 * Accessor method to determine whether this sequence has a specified 
	 * current element that can be retrieved with the 
	 * getCurrent method. 
	 * @param - none
	 * @return
	 *   true (there is a current element) or false (there is no current element at the moment)
	 **/
	public boolean hasCurrent( )
	{
		assert _wellFormed() : "invariant wrong at start of hasCurrent()";

		return _cursor != _dummy;

		// This method shouldn't modify any fields, hence no assertion at end
	}

	/**
	 * Accessor method to get the current element of this sequence. 
	 * @param - none
	 * @precondition
	 *   hasCurrent() returns true.
	 * @return
	 *   the current element of this sequence
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   getCurrent may not be called.
	 **/
	public E getCurrent( )
	{
		assert _wellFormed() : "invariant wrong at start of getCurrent()";

		if(!hasCurrent()) throw new IllegalStateException("Must call start() before iterating through list");
		return _cursor._data;

		// This method shouldn't modify any fields, hence no assertion at end
	}

	/**
	 * Move forward, so that the current element is now the next element in
	 * this sequence.
	 * @param - none
	 * @precondition
	 *   hasCurrent() returns true. 
	 * @postcondition
	 *   If the current element was already the end element of this sequence 
	 *   (with nothing after it), then there is no longer any current element. 
	 *   Otherwise, the new element is the element immediately after the 
	 *   original current element.
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   advance may not be called.
	 **/
	public void advance( )
	{
		assert _wellFormed() : "invariant wrong at start of advance()";

		if(!hasCurrent()) throw new IllegalStateException("Must call start() before iterating through list");
		_cursor = _cursor._next;

		assert _wellFormed() : "invariant wrong at end of advance()";
	}

	/**
	 * Add a new element to this sequence, before the current element. 
	 * @param element
	 *   the new element that is being added
	 * @postcondition
	 *   The element has been added to this sequence. If there was
	 *   a current element, then the new element is placed before the current
	 *   element. If there was no current element, then the new element is placed
	 *   at the end of the sequence. In all cases, the new element becomes the
	 *   new current element of this sequence. 
	 **/
	public void insert(E element)
	{
		assert _wellFormed() : "invariant wrong at start of insert";

		Node<E> newNode = new Node<E>(element, _cursor._prev, _cursor);
		_cursor._prev._next = newNode;
		_cursor._prev = newNode;
		_cursor = newNode;
		++_numItems;

		assert _wellFormed() : "invariant wrong at end of insert";
	}

	/**
	 * Place the contents of another sequence (which may be the
	 * same sequence as this!) into this sequence.
	 * @param addend
	 *   a sequence whose contents will be placed into this sequence
	 * @precondition
	 *   The parameter, addend, is not null. 
	 * @postcondition
	 *   The elements from addend have been placed into
	 *   this sequence. The current element of this sequence is now
	 *   the first element inserted (if any).  If the added sequence
	 *   is empty, this sequence and the current element (if any) are
	 *   unchanged.
	 * @exception NullPointerException
	 *   Indicates that addend is null. 
	 **/
	public void insertAll(Seq<E> addend)
	{
		assert _wellFormed() : "invariant wrong at start of insertAll";
		assert addend._wellFormed() : "invariant of parameter wrong at start of insertAll";

		Seq<E> addendClone = addend.clone();
		for(Node<E> n = addendClone._dummy._prev; n != addendClone._dummy; n = n._prev) {
			insert(n._data);
		}

		assert _wellFormed() : "invariant wrong at end of insertAll";
		assert addend._wellFormed() : "invariant of parameter wrong at end of insertAll";
	}   


	/**
	 * Generate a copy of this sequence.
	 * @param - none
	 * @return
	 *   The return value is a copy of this sequence. Subsequent changes to the
	 *   copy will not affect the original, nor vice versa.
	 *   Whatever was current in the original object is now current in the clone.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for creating the clone.
	 **/ 
	@Override
	@SuppressWarnings("unchecked")
	public Seq<E> clone( )
	{  	 
		assert _wellFormed() : "invariant wrong at start of clone()";

		Seq<E> result;

		try
		{
			result = (Seq<E>) super.clone( );
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

		result._dummy = new Node<E>(null, null, null);
		if(_cursor == _dummy) result._cursor = result._dummy;

		Node<E> newNodes = result._dummy;
		for(Node<E> n = _dummy._next; n != _dummy; n = n._next) {
			newNodes._next = new Node<E>(n._data, newNodes, null);
			if(n == _cursor)
				result._cursor = newNodes._next;

			newNodes = newNodes._next;
		}
		newNodes._next = result._dummy;
		result._dummy._prev = newNodes;

		assert _wellFormed() : "invariant wrong at end of clone()";
		assert result._wellFormed() : "invariant wrong for result of clone()";
		return result;
	}


	/**
	 * Create a new sequence that contains all the elements from one sequence
	 * followed by another.
	 * @param s1
	 *   the first of two sequences
	 * @param s2
	 *   the second of two sequences
	 * @precondition
	 *   Neither s1 nor s2 is null.
	 * @return
	 *   a new sequence that has the elements of s1 followed by the
	 *   elements of s2 (with no current element)
	 * @exception NullPointerException.
	 *   Indicates that one of the arguments is null.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for the new sequence.
	 **/ 
	public static <E> Seq<E> catenation(Seq<E> s1, Seq<E> s2)
	{
		Seq<E> res = s1.clone();
		res._cursor = res._dummy;
		res.insertAll(s2);
		res._cursor = res._dummy;
		return res;
	}

	/**
	 * Remove the current element from this sequence.
	 * @param - none
	 * @precondition
	 *   hasCurrent() returns true.
	 * @postcondition
	 *   The current element has been removed from this sequence, and the 
	 *   following element (if there is one) is now the new current element. 
	 *   If there was no following element, then there is now no current 
	 *   element.
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   removeCurrent may not be called. 
	 **/
	public void removeCurrent( )
	{
		assert _wellFormed() : "invariant wrong at start of removeCurrent()";

		if(!hasCurrent()) throw new IllegalStateException("Must call start() to initiate iteration");

		_cursor._prev._next = _cursor._next;
		_cursor._next._prev = _cursor._prev;
		Node<E> c = _cursor._next;
		_cursor._next = null;
		_cursor._prev = null;
		_cursor = c;
		--_numItems;

		assert _wellFormed() : "invariant wrong at end of removeCurrent()";
	}


	/**
	 * Determine the number of elements in this sequence.
	 * @param - none
	 * @return
	 *   the number of elements in this sequence
	 **/ 
	public int size( )
	{
		assert _wellFormed() : "invariant wrong at start of size()";

		return _numItems;

		// This method shouldn't modify any fields, hence no assertion at end
	}
}