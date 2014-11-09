// This is an assignment for students to complete after reading Chapter 4 of
// "Data Structures and Other Objects Using Java" by Michael Main.

package edu.uwm.cs351;

/******************************************************************************
 * This class is a homework assignment;
 * A DiskSeq is a collection of Disks.
 * The sequence can have a special "current element," which is specified and 
 * accessed through four methods
 * (start, getCurrent, advance and hasCurrent).
 *
 ******************************************************************************/
public class DiskSeq implements Cloneable
{
	// Declare the private static Node class.
	// It should have a constructor but no methods.
	// The fields of Node should have "default" access (neither public, nor private)
	private static class Node {
		Disk data;
		Node next;
		public Node(Disk d, Node n) {
			data = d;
			next = n;
		}
	}

	
	// Declare the private fields of DiskSeq
	// (see textbook, page 226 -- five are recommended, 
	// but you should only define three.  See Homework.)
	private int manyNodes;
	private Node head;
	private Node precursor;


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
		// manyNodes is number of nodes in list
		// precursor is either null or points to a node in the list.
		
		// Implementation:
		// Do multiple checks: each time returning false if a problem is found.
		
		// We do the first one for you:
		// check that list is not cyclic
		if (head != null) {
			// This check uses an interesting property described by Guy Steele (CLtL 1987)
			Node fast = head.next;
			for (Node p = head; fast != null && fast.next != null; p = p.next) {
				if (p == fast) return report("list is cyclic!");
				fast = fast.next.next;
			}
		}
		
		// Implement remaining conditions.
		// manyNodes is number of nodes in list
		if(head == null && manyNodes != 0) {
			return report("manyNodes is wrong (should be 0)");
		} else if(head != null) {
			int c = 0;
			for(Node n = head; n != null; n = n.next) {
				c++;
			}
			if(c != manyNodes) return report("manyNodes is wrong (should be " + c + ")");
		}

		// precursor is either null or points to a node in the list.
		if(precursor != null) {
			boolean found = false;
			for(Node n = head; n != null; n = n.next) {
				if(n == precursor) {
					found = true;
					break;
				}
			}

			if(!found) {
				return report("precursor is not null and doesn't point to a node in the list");
			}
		}

		// If no problems found, then return true:
		return true;
	}


	/**
	 * Create an empty sequence.
	 * @param - none
	 * @postcondition
	 *   This sequence is empty 
	 **/   
	public DiskSeq( )
	{
		manyNodes = 0;
		head = null;
		precursor = null;
		assert _wellFormed() : "invariant failed in constructor";
	}

	/**
	 * Add a new element to this sequence, before the current element. 
	 * @param element
	 *   the new element that is being added
	 * @postcondition
	 *   A new copy of the element has been added to this sequence. If there was
	 *   a current element, then the new element is placed before the current
	 *   element. If there was no current element, then the new element is placed
	 *   at the end of the sequence. In all cases, the new element becomes the
	 *   new current element of this sequence. 
	 **/
	public void insert(Disk element)
	{
		assert _wellFormed() : "invariant wrong at start of insert";

		if(precursor == null) {
			Node newNode = new Node(element, head);
			head = newNode;
		} else {
			Node newNode = new Node(element, precursor.next);
			precursor.next = newNode;
		}
		++manyNodes;

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
	public void insertAll(DiskSeq addend)
	{
		assert _wellFormed() : "invariant wrong at start of addAll";
		assert addend._wellFormed() : "invariant of parameter wrong at start of addAll";

		if(addend.head != null) {
			DiskSeq clone = addend.clone();
			if(head != null) {
				Node n = clone.head;
				while(n.next != null) {
					n = n.next;
				}
				if(precursor == null) {
					n.next = head;
					head = clone.head;
				} else {
					n.next = precursor.next;
					precursor.next = clone.head;
				}
			} else {
				head = clone.head;
			}

			manyNodes += addend.manyNodes;
		}

		assert _wellFormed() : "invariant wrong at end of insertAll";
		assert addend._wellFormed() : "invariant of parameter wrong at end of insertAll";
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

		if(!hasCurrent())
			throw new IllegalStateException("No current element: can't advance sequence");

		if(precursor == null) {
			precursor = head;
		} else {
			precursor = precursor.next;
		}

		assert _wellFormed() : "invariant wrong at end of advance()";
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
	public DiskSeq clone( )
	{  	 
		assert _wellFormed() : "invariant wrong at start of clone()";

		DiskSeq result;

		try
		{
			result = (DiskSeq) super.clone( );
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

		// Implemented by student.
		// Now do the hard work of cloning the list.
		// See pp. 193-197, 228
		// Setting precursor correctly is tricky.
		if(head != null) {
			result.head = new Node(head.data, null);
			result.precursor = (precursor == head) ? result.head : null;
			for(Node n = head.next, cursor = result.head; n != null; n = n.next, cursor = cursor.next) {
				cursor.next = new Node(n.data, null);
				if(precursor == n)
					result.precursor = cursor.next;
			}

			/*System.out.println("\nmanyNodes = " + manyNodes);
			System.out.println("r.manyNodes = " + result.manyNodes);
			System.out.println("precursor = " + ((precursor == null) ? null : precursor.data));
			System.out.println("r.precursor = " + ((result.precursor == null) ? null : result.precursor.data));
			for(Node n = head, n2 = result.head; n != null; n = n.next, n2 = n2.next) {
				System.out.println(n.data + ", " + n2.data);
			}
			System.out.println();*/
		}

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
	public static DiskSeq catenation(DiskSeq s1, DiskSeq s2)
	{
		DiskSeq res = s1.clone();
		while (res.hasCurrent()) res.advance();
		res.insertAll(s2);
		while (res.hasCurrent()) res.advance();
		return res;
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
	public Disk getCurrent( )
	{
		assert _wellFormed() : "invariant wrong at start of getCurrent()";

		if(!hasCurrent())
			throw new IllegalStateException("No current element: must use DiskSeq.start()");
		if(precursor == null) return head.data;
		return precursor.next.data;

		// This method shouldn't modify any fields, hence no assertion at end
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
		assert _wellFormed() : "invariant wrong at start of getCurrent()";

		return ((precursor == null && manyNodes > 0) || (precursor != null && precursor.next != null));

		// This method shouldn't modify any fields, hence no assertion at end
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

		if(!hasCurrent())
			throw new IllegalStateException("No current element: must call DiskSeq.start()");

		if(precursor == null) {
			head = head.next;
		} else {
			precursor.next = precursor.next.next;
		}
		--manyNodes;

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

		return manyNodes;

		// This method shouldn't modify any fields, hence no assertion at end
	}


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
		precursor = null;
		assert _wellFormed() : "invariant wrong at end of start()";
	}

}

