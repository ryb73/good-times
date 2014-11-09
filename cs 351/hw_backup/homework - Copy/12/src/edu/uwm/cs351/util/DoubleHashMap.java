package edu.uwm.cs351.util;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class DoubleHashMap<K, V> extends AbstractMap<K, V> {

	private static final int INITIAL_CAPACITY = 7;
	private static final double CROWDED = 0.75;

	@SuppressWarnings("unchecked")
	private static <K,V> MyEntry<K,V>[] makeArray(int l) {
		return new MyEntry[l];
	}

	/// DESIGN
	// The table's size and suze - 2 must be prime.
	// If the table has null at some position, this means there was never 
	// an entry here since the last rehashing.
	// If an entry is removed, we leave the entry object in place
	// (so that successive finds know that the entry had been occupied)
	// We rehash the table if the number of entry objects / size > CROWDED.
	// 

	private MyEntry<K,V>[] table = makeArray(INITIAL_CAPACITY);
	private volatile Set<Entry<K,V>> entrySet;
	private int used = 0; // the number of non-null entries in the table
	private int numEntries = 0; // the number of entries with non-null keys in table


	/// Invariant checks:

	private boolean _report(String message) {
		System.err.println("Invariant error: " + message);
		return false;
	}

	private boolean _wellFormed() {
		// All the issues in the DESIGN
		if(!Primes.isPrime(table.length) || !Primes.isPrime(table.length - 2))
			return _report("table size or size - 2 is not prime");

		if((double)used / table.length > CROWDED)
			return _report("table is crowded, hasn't been rehashed");

		// That every entry with a non-null key can be found.
		// (To test this, you must NOT call a public member function.)
		for(int i = 0; i < table.length; ++i) {
			if(table[i] != null && table[i].key != null) {
				K key = table[i].key;

				boolean found = false;
				for(int j = 0; !found; ++j) {
					int hash = hash(key, j);
					Entry<K,V> e = table[hash];
					if(e == null) return _report("encountered null entry when trying to find non-null key");
					if(e.getKey() != null && e.getKey().equals(key)) found = true;
				}
			}
		}

		int uCount = 0;
		int nCount = 0;
		for(int i = 0; i < table.length; ++i) {
			if(table[i] != null) {
				++uCount;
				if(table[i].key != null) ++nCount;
			}
		}

		if(uCount != used) return _report("number of used cells is inaccurate");
		if(nCount != numEntries) return _report("numEntries is inaccurate");

		return true;
	}

	public DoubleHashMap() {}

	public static boolean debugString = false;

	@Override
	public void clear() {
		assert _wellFormed() : "invariant false before clear()";

		for(int i = 0; i < table.length; ++i) {
			table[i] = null;
		}

		used = 0;
		numEntries = 0;

		assert _wellFormed() : "invariant false after clear()";
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(Object key) {
		assert _wellFormed() : "invariant broken at beginning of get(" + key + ")";
	
		try {
			int loc = findKey((K)key, false);
			return (loc < 0) ? null : table[loc].value;
		} catch(ClassCastException e) {
			return null;
		}
	}

	@Override
	public V put(K key, V value) {
		assert _wellFormed() : "invariant false before put(" + key + "," + value + ")";
	
		if(key == null) throw new IllegalArgumentException("key and value must not be null");
	
		int loc = findKey(key, true);
		V res = table[loc].setValue(value);
	
		if((double)used / table.length > CROWDED)
			rehash(2 * numEntries);
	
		assert _wellFormed() : "invariant false after put(" + key + "," + value + ")";
		return res;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V remove(Object key) {
		assert _wellFormed() : "invariant false before remove(" + key + ")";
	
		try {
			int loc = findKey((K)key, false);
			if(loc < 0) return null;

			V res = table[loc].value;
			table[loc] = new MyEntry<K,V>(null, null);
			--numEntries;
			return res;
		} catch(ClassCastException e) {
			return null;
		} finally {
			assert _wellFormed() : "invariant false after remove(" + key + ")";
		}
	}

	@Override
	public int size() {
		return numEntries;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean containsKey(Object key) {
		assert _wellFormed() : "invariant false before containsKey(" + key + ")";

		try {
			int loc = findKey((K)key, false);
			return loc >= 0;
		} catch(ClassCastException e) {
			return false;
		} finally {
			assert _wellFormed() : "invariant false after containsKey(" + key + ")";
		}
	}

	private int hash(Object key, int i) {
		int h = Math.abs(key.hashCode());
		int hash = h % table.length;
		int hash2 = h % (table.length - 2) + 1;

		return (hash + (i * hash2)) % table.length;
	}

	private void rehash(int size) {
		size = Math.max(Primes.nextTwinPrime(size), INITIAL_CAPACITY);
		MyEntry<K,V>[] oldTable = table;
		table = makeArray(size);
		numEntries = 0;
		used = 0;

		for(int i = 0; i < oldTable.length; ++i) {
			if(oldTable[i] != null && oldTable[i].getValue() != null) {
				put(oldTable[i].key, oldTable[i].value);
			}
		}
	}

	private int findKey(K key, boolean createNew) {
		if(key == null) return -1;

		for(int i = 0; ; ++i) {
			int hash = hash(key, i);
			Entry<K,V> e = table[hash];
			if(createNew && (e == null || e.getKey() == null)) {
				table[hash] = new MyEntry<K,V>(key, null);
				++used;
				++numEntries;
				return hash;
			}
			if(e == null)
				return -1;
			if(e.getKey() != null && e.getKey().equals(key))
				return hash;
		}
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		assert _wellFormed() : "invariant broken at beginning of entrySet";
		if (entrySet == null) {
			entrySet = new EntrySet();
		}
		return entrySet;
	}

	@Override
	public String toString() {
		if (!debugString) return super.toString();
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		boolean first = true;
		for (Entry<K,V> e : table) {
			if (first) first = false; else sb.append(',');
			if (e != null) {
				if (e.getKey() == null) {
					sb.append("X");
				} else {
					sb.append(e);
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}

	private class EntrySet extends AbstractSet<Entry<K,V>> {
		@Override
		public Iterator<Entry<K, V>> iterator() {
			return new MyIterator();
		}

		@Override
		public int size() {
			return DoubleHashMap.this.size();
		}

		@Override
		public void clear() {
			DoubleHashMap.this.clear();
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean contains(Object o) {
			assert _wellFormed() : "Invariant broken at start of EntrySet.contains";

			if(o == null) return false;

			try {
				Entry<K,V> castedEntry = (Entry<K,V>)o;
				int loc = findKey(castedEntry.getKey(), false);
				if(loc < 0) return false;
				return (table[loc].value == null) ?
						castedEntry.getValue() == null : table[loc].value.equals(castedEntry.getValue());
			} catch(ClassCastException e) {
				return false;
			}
		}

		@Override
		public boolean remove(Object o) {
			assert _wellFormed() : "Invariant broken at start of EntrySet.remove";

			if(!contains(o)) return false;
			Entry<?,?> e = (Entry<?,?>)o;
			DoubleHashMap.this.remove(e.getKey());

			assert _wellFormed() : "Invariant broken at start of EntrySet.remove";
			return true;
		}
	}

	private class MyIterator implements Iterator<Entry<K,V>> {
		int currentIndex = -1;
		int nextIndex;

		private boolean _wellFormed() {
			return true;
		}

		MyIterator() {
			nextIndex = -1;
			for(int i = 0; i < table.length; ++i) {
				if(table[i] != null && table[i].value != null) {
					nextIndex = i;
					break;
				}
			}
		}

		public boolean hasNext() {
			assert _wellFormed() : "invariant broken before hasNext()";
			return nextIndex >= 0;
		}

		public Entry<K, V> next() {
			if (!hasNext()) throw new NoSuchElementException("at end of map");
			assert _wellFormed() : "invariant broken at start of next()";

			currentIndex = nextIndex;
			nextIndex = -1;
			for(int i = currentIndex+1; i < table.length; ++i) {
				if(table[i] != null && table[i].value != null) {
					nextIndex = i;
					break;
				}
			}

			assert _wellFormed() : "invariant broken at end of next()";
			return table[currentIndex];
		}

		public void remove() {
			assert _wellFormed() : "invariant broken at start of iterator.remove()";

			if(currentIndex >= 0) {
				table[currentIndex] = new MyEntry<K,V>(null, null);
				--numEntries;
				currentIndex = -1;
			}

			assert _wellFormed() : "invariant broken at end of iterator.remove()";
		}
	}
}