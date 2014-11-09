package edu.uwm.cs351;

import java.util.AbstractCollection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A list of words, often in order.
 */
public class WordList extends AbstractCollection<String> {

	private static class Node {
		String word;
		Node prev, next;
		Node(String w) {
			word = w;
			prev = next = this;
		}
		Node(String w, Node p, Node n) {
			word = w;
			prev = p;
			next = n;
		}
	}
	
	final Node dummy = new Node("<none>");
	int numWords = 0;
	
	private boolean _report(String message) {
		System.err.println("Invariant problem: " + message);
		return false;
	}
	
	private boolean _wellFormed() {
		Node p = dummy;
		int count = 0;
		for (Node n = dummy.next; n != dummy; n = n.next) {
			if (n == null) return _report("Next for Node(" + p.word + ") is null");
			if (n.prev != p) return _report("Prev for Node(" + n.word + ") is wrong.");
			p = n;
			++count;
		}
		if (count != numWords) return _report("Counted  " + count + " words, not " + numWords);
		return true;
	}
	
	public WordList() {
		assert _wellFormed() : "invariant broken after constructor";
	}

	@Override
	public void clear() {
		assert _wellFormed() : "invariant broken at start of clear()";
		dummy.next = dummy;
		dummy.prev = dummy;
		numWords = 0;
		assert _wellFormed() : "invariant broken at end of clear()";
	}

	@Override
	public int size() {
		assert _wellFormed() : "invariant broken at start of size()";
		return numWords;
	}
	
	@Override
	public Iterator<String> iterator() {
		assert _wellFormed() : "invariant broken at start of iterator()";
		return new MyIterator();
	}
	
	/* Add a word to the end of the list without attempting to sort the result.
	 * @see java.util.AbstractCollection#add(java.lang.Object)
	 */
	@Override
	public boolean add(String s) {
		assert _wellFormed() : "invariant broken at start of add(" + s + ")";
		addBefore(new Node(s, dummy.prev, dummy), dummy);
		assert _wellFormed() : "invariant broken at end of add(" + s + ")";
		return true;
	}
	
	/**
	 * Do insertion sort over the list, removing a later string that compares equal to
	 * an earlier string.
	 * The insertion sort must be smart, that is, linear when the list is already sorted
	 * or almost sorted.  Read the textbook p. 606,613
	 * @param comp string comparison to use
	 */
	public void sort(Comparator<String> comp) {
		assert _wellFormed() : "invariant broken at start of sort()";

		for(Node n = dummy.next.next; n != dummy; n = n.next) {
			boolean insertDone = false;
			for(Node ncmp = n.prev; !insertDone && ncmp != dummy; ncmp = ncmp.prev) {
				final int c = comp.compare(n.word, ncmp.word);
				if(c == 0) {
					removeLink(n);
					insertDone = true;
				} else if(c > 0) {
					if(ncmp != n.prev) {	// if n isn't already after ncmp
						removeLink(n);
						addAfter(n, ncmp);
					}
					insertDone = true;
				}
			}

			if(!insertDone) {
				removeLink(n);
				addAfter(n, dummy);
			}
		}

		assert _wellFormed() : "invariant broken at end of sort()";
	}

	private void addBefore(Node n, Node b) {
		assert _wellFormed() : "invariant broken at start of addBefore(" + n + ", " + b + ")";

		n.prev = b.prev;
		n.next = b;
		b.prev.next = n;
		b.prev = n;
		++numWords;

		assert _wellFormed() : "invariant broken at end of addBefore(" + n + ", " + b + ")";
	}

	private void addAfter(Node n, Node a) {
		assert _wellFormed() : "invariant broken at start of addAfter(" + n + ", " + a + ")";

		n.next = a.next;
		n.prev = a;
		a.next.prev = n;
		a.next = n;
		++numWords;

		assert _wellFormed() : "invariant broken at end of addAfter(" + n + ", " + a + ")";
	}

	private void removeLink(Node n) {
		assert _wellFormed() : "invariant broken at start of removeLink(" + n + ")";

		n.prev.next = n.next;
		n.next.prev = n.prev;
		--numWords;

		assert _wellFormed() : "invariant broken at end of removeLink(" + n + ")";
	}
	
	/** Merge in the new words given into this list, sorting both lists
	 * in the process.  Furthermore all existing words in the new word list
	 * are removed.  So afterwards, the newWords lists will include ONLY
	 * the words that were added to this list.
	 * <p>
	 * The algorithm must be linear in the size of both lists, that is,
	 * go through each list just once (after the initial sort).
	 * @param comp string comparison to use
	 * @param newWords list of words to add, reduced to those actually added.
	 */
	public void merge(Comparator<String> comp, WordList newWords) {
		assert _wellFormed() : "invariant broken at start of merge()";

		sort(comp);
		newWords.sort(comp);
		/*System.out.println(this);//TODO
		System.out.println(newWords);*/

		if(newWords.isEmpty()) return;

		MyIterator it = (MyIterator)iterator();
		MyIterator newIt = (MyIterator)newWords.iterator();
		String newWord = newIt.next();
		boolean wordsDone = false;
		while(!wordsDone && it.hasNext()) {
			String cur = it.next();

			boolean nextWord = false; 
			while(!nextWord) {
				final int c = comp.compare(cur, newWord);
				//System.out.println(cur + ", " + newWord + ", " + c);//TODO
				if(c >= 0) {
					if(c == 0)
						newIt.remove();
					else
						it.insert(newWord);

					if(newIt.hasNext()) {
						newWord = newIt.next();
					} else {
						newWord = null;
						wordsDone = nextWord = true;
					}
				} else {
					nextWord = true;
				}
			}
		}

		while(newWord != null) {
			add(newWord);
			if(newIt.hasNext())
				newWord = newIt.next();
			else
				newWord = null;
		}

		/*System.out.println(this);//TODO
		System.out.println(newWords);
		System.out.println("-------------------");*/

		assert _wellFormed() : "invariant broken at end of merge()";	
	}
	
	// TODO: the remaining methods
	
	private class MyIterator implements Iterator<String> {
		private Node current;
		private boolean removeOK = false;
		
		private boolean _wellFormed() {
			if (!WordList.this._wellFormed()) return false;
			if (current == null) return _report("current is null");
			if (current == dummy) {
				if (removeOK) return _report("shouldn't be allowed to remove dummy");
				return true;
			}
			for (Node p = dummy.next; p != dummy; p = p.next) {
				if (p == current) return true;
			}
			return _report("couldn't find cursor in list");
		}
		
		public MyIterator() {
			current = dummy;
			assert _wellFormed() : "invariant broken at end of constructor";
		}
		
		public boolean hasNext() {
			assert _wellFormed() : "invariant broken at start of hasNext()";
			return current.next != dummy;
		}
		
		public String next() {
			if (!hasNext()) throw new NoSuchElementException("at end of list");
			current = current.next;
			removeOK = true;
			assert _wellFormed() : "invariant broken at end of next()";
			return current.word;
		}
		
		public void remove() {
			assert _wellFormed() : "invariant broken at start of remove()";
			if (!removeOK) throw new IllegalStateException("Cannot remove now");
			current = current.prev;
			removeLink(current.next);
			removeOK = false;
			assert _wellFormed() : "invariant broken at end of remove()";
		}
		
		// this method is potentially useful in merge:
		/**
		 * Insert the given word before the current one.
		 * @param word word to insert
		 */
		public void insert(String word) {
			assert _wellFormed() : "invariant broken at start of insert()";
			if (!removeOK) throw new IllegalStateException("No current now");
			addBefore(new Node(word), current);
			assert _wellFormed() : "invariant broken at end of insert()";
		}
	}
}