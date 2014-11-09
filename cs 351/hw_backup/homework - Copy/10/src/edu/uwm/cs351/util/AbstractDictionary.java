package edu.uwm.cs351.util;

/**
 * A partial implementation of {@link Dictionary}.
 * The extending class must only implement {@link #put} and {@link #keepIf}
 * to be fully functional.  An implementation may want to override 
 * {@link #size}, {@link #get} and {@link #clear} for better efficiency.
 * @param <K> key type
 * @param <V> value type
 */
public abstract class AbstractDictionary<K, V> implements Dictionary<K, V> {
	public boolean isEmpty() {
		return size() == 0;
	}

	public void clear() {
		keepIf(new EntryPredicate<K, V>() {
			@Override
			public boolean keep(Entry<K, V> entry) {
				return false;
			}
		});
	}

	public V get(final K key) {
		if(key == null) return null; //throw new NullPointerException("null key");

		final Cell<V> res = new Cell<V>(null);
		keepIf(new EntryPredicate<K, V>() {
			@Override
			public boolean keep(Entry<K, V> entry) {
				if(key.equals(entry.key))
					res.set(entry.value);
				return true;
			}
		});

		return res.get();
	}

	/* (non-Javadoc)
	 * @see edu.uwm.cs351.util.Dictionary#size()
	 * NB: This implementation is inefficient.  It is here to show
	 * how {@link #keepIf} can be used to iterate through a dictionary.
	 */
	public int size()
	{
		// local variables used in an anonymous nested class must be final
		final Cell<Integer> count = new Cell<Integer>(0);
		keepIf(new EntryPredicate<K,V>() {
			// this is a new anonymous nested class that implements the
			// EntryPredicate interface
			public boolean keep(Entry<K,V> e) {
				// a final variable cannot be updated, so that's why "count"
				// cannot be an "int".  Instead we make it a cell, whose
				// value can be updated here:
				count.set(count.get()+1);
				return true;
			}
		});
		return count.get();
	}
}