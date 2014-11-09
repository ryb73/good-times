// This is an assignment for students to complete after reading Chapter 3 of
// "Data Structures and Other Objects Using Java" by Michael Main.

package edu.uwm.cs351;

/******************************************************************************
 * This class is a homework assignment;
 * A DiskSeq is a collection of Disks.
 * The sequence can have a special "current element," which is specified and 
 * accessed through four methods that are not available in the sequence class 
 * (start, getCurrent, advance and hasCurrent).
 *
 * @note
 *   (1) The capacity of one a sequence can change after it's created, but
 *   the maximum capacity is limited by the amount of free memory on the 
 *   machine. The constructor, insert, insertAll, clone, 
 *   and concatenation methods will result in an
 *   OutOfMemoryError when free memory is exhausted.
 *   <p>
 *   (2) A sequence's capacity cannot exceed the maximum integer 2,147,483,647
 *   (Integer.MAX_VALUE). Any attempt to create a larger capacity
 *   results in a failure due to an arithmetic overflow. 
 *
 * @see
 *   <A HREF="../../../../edu/colorado/collections/DoubleArraySeq.java">
 *   Java Source Code for a related class by Michael Main
 *   </A>
 *
 ******************************************************************************/
public class DiskSeq implements Cloneable
{
	// Invariant of the DiskSeq class:
	//   1. The number of elements in the sequences is in the instance variable 
	//      manyItems.
	//   2. For an empty sequence (with no elements), we do not care what is 
	//      stored in any of data; for a non-empty sequence, the elements of the
	//      sequence are stored in data[0] through data[manyItems-1], and we
	//      don't care what's in the rest of data.
	//   3. If there is a current element, then it lies in data[currentIndex];
	//      if there is no current element, then currentIndex equals manyItems. 
	private Disk[ ] data;
	private int manyItems;
	private int currentIndex; 

	private static int INITIAL_CAPACITY = 0;

	/**
	 * Initialize an empty sequence with an initial capacity of INITIAL_CAPACITY.
	 * The {@link #insert(Disk)} method works
	 * efficiently (without needing more memory) until this capacity is reached.
	 * @param - none
	 * @postcondition
	 *   This sequence is empty and has an initial capacity of INITIAL_CAPACITY
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for initial array.
	 **/   
	public DiskSeq( )
	{
		this(INITIAL_CAPACITY);
	}


	/**
	 * Initialize an empty sequence with a specified initial capacity.
	 * The {@link #insert(Disk)} method works
	 * efficiently (without needing more memory) until this capacity is reached.
	 * @param initialCapacity
	 *   the initial capacity of this sequence
	 * @precondition
	 *   initialCapacity is non-negative.
	 * @postcondition
	 *   This sequence is empty and has the given initial capacity.
	 * @exception IllegalArgumentException
	 *   Indicates that initialCapacity is negative.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for an array with this many elements.
	 *   new Disk[initialCapacity].
	 **/   
	public DiskSeq(int initialCapacity)
	{
		if(initialCapacity < 0)
			throw new IllegalArgumentException("DiskSeq cannot have negative capacity: " + initialCapacity);

		data = new Disk[initialCapacity];
		manyItems = 0;
		currentIndex = manyItems;
	}


	/**
	 * Add a new element to this sequence, before the current element (if any). 
	 * If the new element would take this sequence beyond its current capacity,
	 * then the capacity is increased before adding the new element.
	 * @param element
	 *   the new element that is being added
	 * @postcondition
	 *   A new copy of the element has been added to this sequence. If there was
	 *   a current element, then the new element is placed before the current
	 *   element. If there was no current element, then the new element is placed
	 *   at the end of the sequence. In all cases, the new element becomes the
	 *   new current element of this sequence. 
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for increasing the sequence's capacity.
	 **/
	public void insert(Disk element)
	{
		ensureCapacity(manyItems + 1);

		System.arraycopy(data, currentIndex, data, currentIndex + 1, manyItems - currentIndex);
		data[currentIndex] = element;
		manyItems++;
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
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory to increase the size of this sequence.
	 **/
	public void insertAll(DiskSeq addend)
	{
		if(addend.size() < 1) return;

		ensureCapacity(manyItems + addend.size());

		System.arraycopy(data, currentIndex, data, currentIndex + addend.size(), manyItems - currentIndex);
		System.arraycopy(addend.data, 0, data, currentIndex, addend.size());
		manyItems += addend.size();
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
		if(!hasCurrent())
			throw new IllegalStateException("No current element - must start traversal using DiskSeq.start()");
		++currentIndex;
	}


	/**
	 * Generate a copy of this sequence.
	 * @param - none
	 * @return
	 *   The return value is a copy of this sequence. Subsequent changes to the
	 *   copy will not affect the original, nor vice versa.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for creating the clone.
	 **/ 
	@Override
	public DiskSeq clone( )
	{  // Clone a DiskSeq object.
		DiskSeq answer;

		try
		{
			answer = (DiskSeq) super.clone( );
		}
		catch (CloneNotSupportedException e)
		{  // This exception should not occur. But if it does, it would probably
			// indicate a programming error that made super.clone unavailable.
			// The most common error would be forgetting the "Implements Cloneable"
			// clause at the start of this class.
			throw new RuntimeException
			("This class does not implement Cloneable");
		}

		answer.data = data.clone( );

		return answer;
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
	 *   elements of s2 (with no current element).
	 * @exception NullPointerException.
	 *   Indicates that one of the arguments is null.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for the new sequence.
	 **/   
	public static DiskSeq catenation(DiskSeq s1, DiskSeq s2)
	{
		if(s1 == null || s2 == null)
			throw new NullPointerException("Cannot catenate null sequence");

		DiskSeq res = new DiskSeq(s1.size() + s2.size());
		res.insertAll(s1);
		res.insertAll(s2);
		return res;
	}


	/**
	 * Change the current capacity of this sequence as needed so that
	 * the capacity is at least as big as the parameter.
	 * This code must work correctly and efficiently if the minimum
	 * capacity is (1) smaller or equal to the current capacity (do nothing)
	 * (2) at most double the current capacity (double the capacity)
	 * or (3) more than double the current capacity (new capacity is the
	 * minimum passed).
	 * @param minimumCapacity
	 *   the new capacity for this sequence
	 * @postcondition
	 *   This sequence's capacity has been changed to at least minimumCapacity.
	 *   If the capacity was already at or greater than minimumCapacity,
	 *   then the capacity is left unchanged.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for: new array of minimumCapacity elements.
	 **/
	private void ensureCapacity(int minimumCapacity)
	{
		if(minimumCapacity >= data.length) {
			Disk[] newSequence = new Disk[Math.max(minimumCapacity, data.length * 2)];
			System.arraycopy(data, 0, newSequence, 0, data.length);
			data = newSequence;
		}
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
		if(!hasCurrent())
			throw new IllegalStateException("Can't get current element - must use DiskReq.start()");
		return data[currentIndex];
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
		return currentIndex < manyItems;
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
		if(!hasCurrent())
			throw new IllegalStateException("No current element - must call DiskReq.start()");
		System.arraycopy(data, currentIndex + 1, data, currentIndex, manyItems - currentIndex - 1);
		--manyItems;
	}


	/**
	 * Determine the number of elements in this sequence.
	 * @param - none
	 * @return
	 *   the number of elements in this sequence
	 **/ 
	public int size( )
	{
		return manyItems;
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
		currentIndex = 0;
	}
}