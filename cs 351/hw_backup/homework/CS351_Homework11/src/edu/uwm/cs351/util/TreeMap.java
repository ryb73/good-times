package edu.uwm.cs351.util;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;

public class TreeMap<K,V>  extends AbstractMap<K,V> {

	// Here is the data structure to use.

	private static class Node<K,V> extends AbstractEntry<K,V> {
		Node<K,V> left, right;
		Node(K k, V v) {
			super(k,v);
			left = right = null;
		}

	}

	private Comparator<K> comparator;
	private Node<K,V> root;
	private int numItems = 0;


	/// Invariant checks:

	private boolean _report(String message) {
		System.err.println("Invariant error: " + message);
		return false;
	}

	/**
	 * Return true if data in this subtree are never null and are correctly sorted
	 * and are all in the range between the lower and upper (exclusive).
	 * If either bound is null, then that means that there is no limit at this side.
	 * @param node root of subtree to examine
	 * @param lower value that all nodes must be greater than.  If null, then
	 * there is no lower bound.
	 * @param upper value that all nodes must be greater than. If null,
	 * then there is no upper bound.
	 * @return true if the subtree is fine.  If false is returned, a problem
	 * should be reported.
	 */
	private boolean _checkInRange(Node<K,V> node, K lower, K upper) {
		if (node == null) return true;
		if (lower != null && comparator.compare(lower, node.key) >= 0) {
			return _report("Failed: " + lower + " < " + node.key);
		}
		if (upper != null && comparator.compare(node.key, upper) >= 0) {
			return _report("Failed: " + node.key + " < " + upper);
		}
		return _checkInRange(node.left,lower,node.key) && _checkInRange(node.right,node.key,upper);
	}

	/**
	 * Return the number of nodes in the subtree rooted at n (including n).
	 * This operation counts nodes; it does not use {@link #numItems}.
	 * @param n reference to subtree in which to count nodes.
	 * @return number of nodes in subtree.
	 */
	private int _count(Node<K,V> n) {
		if(n == null) return 0;
		return _count(n.left) + _count(n.right) + 1;
	}

	/**
	 * Check the invariant, printing a message if not satisfied.
	 * @return whether invariant is correct
	 */
	private boolean _wellFormed() {
		// 1. check that comparator is not null
		if(comparator == null) return _report("Comparator is null");
		// 2. check that all nodes are in range
		if(!_checkInRange(root, null, null)) return false;
		// 3. check that number of items matches number of nodes
		if(_count(root) != numItems) return _report("numItems is inaccurate");
		return true;
	}

	/// constructor

	public TreeMap(Comparator<K> c) {
		if (c == null) throw new IllegalArgumentException("comparator must not be null");
		comparator = c;
		assert _wellFormed() : "invariant broken after construction";
	}

	@SuppressWarnings("unchecked")
	private K asKey(Object x) {
		if (root == null || x == null) return null;
		try {
			comparator.compare(root.key,(K)x);
			comparator.compare((K)x,root.key);
			return (K)x;
		} catch (ClassCastException ex) {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractMap#clear()
	 */
	@Override
	public void clear() {
		assert _wellFormed() : "invariant false before clear()";
		root = null;
		numItems = 0;
		assert _wellFormed() : "invariant false before clear()";
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractMap#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object key) {
		assert _wellFormed() : "invariant false before containsKey(" + key + ")";

		K castedKey = asKey(key);
		if(castedKey == null) return false;

		return _findChild(root, castedKey, false) != null;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractMap#get(java.lang.Object)
	 */
	@Override
	public V get(Object key) {
		assert _wellFormed() : "invariant false before get(" + key + ")";

		K castedKey = asKey(key);
		if(castedKey == null) return null;
		Node<K,V> resNode = _findChild(root, castedKey, false); 
		return (resNode == null) ? null : resNode.value;
	}

	/**
	 * Recursively searches for the child of p (or p itself) with the specified key.
	 * 
	 * @param p				parent node
	 * @param key			key to search for
	 * @param createNew		true if a new node should be created in the event that the key is not found.
	 * @return Node with specified key
	 */
	private Node<K,V> _findChild(Node<K, V> p, K key, boolean createNew) {
		if(p == null) return null;

		int cmp = comparator.compare(p.key, key);
		if(cmp == 0)
			return p;
		if(p.left != null && cmp > 0)
			return _findChild(p.left, key, createNew);
		if(p.right != null && cmp < 0)
			return _findChild(p.right, key, createNew);

		if(createNew) {
			Node<K,V> newNode = new Node<K,V>(key, null);
			if(cmp > 0) p.left = newNode;
			else p.right = newNode;
			++numItems;
			return newNode;
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractMap#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public V put(K key, V value) {
		assert _wellFormed() : "invariant false before put(" + key + "," + value + ")";

		if(key == null) throw new IllegalArgumentException("key must not be null");

		V res = null;
		if(root == null) {
			root = new Node<K,V>(key, value);
			++numItems;
		} else {
			Node<K,V> putNode = _findChild(root, key, true);
			res = putNode.value;
			putNode.value = value;
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractMap#remove(java.lang.Object)
	 */
	@Override
	public V remove(Object key) {
		assert _wellFormed() : "invariant false before remove(" + key + ")";

		K castedKey = asKey(key);
		if(castedKey == null) return null;

		try {
			Node<K,V> p = findParent(castedKey);

			V res;
			if(p == null) {
				res = root.value;
				root = doRemove(root);
			} else if(p.left != null && comparator.compare(p.left.key, castedKey) == 0) {
				res = p.left.value;
				p.left = doRemove(p.left);
			} else {
				res = p.right.value;
				p.right = doRemove(p.right);
			}

			--numItems;
			return res;
		} catch(NoSuchElementException e) {
			return null;
		}
	}

	/**
	 * Searches the tree for the parent of the node with the specified key.
	 *  
	 * @param key key of child to search for
	 * @return parent of the node with specified key
	 * @throws NoSuchElementException if there's not element with the specified key
	 */
	private Node<K, V> findParent(K key) {
		Node<K,V> p = null;
		Node<K,V> n = root;
		while(n != null) {
			int c = comparator.compare(n.key, key);
			if(c == 0) {
				return p;
			} else if(c < 0) {
				p = n;
				n = n.right;
			} else {
				p = n;
				n = n.left;
			}
		}

		throw new NoSuchElementException("key not contained in tree");
	}

	/**
	 * Removes the specified node from the tree.
	 * 
	 * @param n node to remove
	 * @return the node that should take the place of the node that was removed
	 */
	private Node<K, V> doRemove(Node<K, V> n) {
		if(n.left != null && n.right != null) {
			Node<K,V> predecessor;
			Node<K,V> predecessorParent = n;
			for(predecessor = n.left; predecessor.right != null; predecessor = predecessor.right) {
				predecessorParent = predecessor;
			}

			n.key = predecessor.key;
			n.value = predecessor.value;
			if(predecessorParent == n)
				n.left = predecessor.left;
			else
				predecessorParent.right = predecessor.left;
			return n;
		} else if(n.left != null) {
			return n.left; 
		} else if(n.right != null) {
			return n.right;
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractMap#size()
	 */
	@Override
	public int size() {
		assert _wellFormed() : "invariant false before size()";
		return numItems;
	}

	private volatile Set<Entry<K,V>> entrySet;

	@Override
	public Set<Entry<K, V>> entrySet() {
		assert _wellFormed() : "invariant broken at beginning of entrySet";
		if (entrySet == null) {
			entrySet = new EntrySet();
		}
		return entrySet;
	}

	/**
	 * The "backing" set for this map.
	 * In other words, this set doesn't have its own data structure:
	 * it uses the data structure of the map.
	 */
	private class EntrySet extends AbstractSet<Entry<K,V>> {
		@Override
		public int size() {
			return TreeMap.this.size();
		}

		@Override
		public Iterator<Entry<K, V>> iterator() {
			return new MyIterator();
		}


		@SuppressWarnings("unchecked")
		@Override
		public boolean contains(Object o) {
			assert _wellFormed() : "Invariant broken at start of EntrySet.contains";

			if(!(o instanceof Entry)) return false;

			Entry e = (Entry)o;
			Iterator<Entry<K,V>> it = iterator();
			while(it.hasNext()) {
				Entry<K,V> next = it.next();
				if(next.getKey().equals(e.getKey())) {
					Object v1 = next.getValue();
					Object v2 = e.getValue();
					return v1 == v2 || (v1 != null && v1.equals(v2));
				}
			}
			return false;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean remove(Object x) {
			assert _wellFormed() : "Invariant broken at start of EntrySet.remove";

			if(!contains(x)) return false;
			Entry e = (Entry)x; // we can do this because anything contained in this set must be
								// an Entry
			TreeMap.this.remove(e.getKey());

			assert _wellFormed() : "Invariant broken at start of EntrySet.remove";
			return true;
		}

		@Override
		public void clear() {
			TreeMap.this.clear();
		}
	}

	/**
	 * Iterator over the map.
	 */
	private class MyIterator implements Iterator<Entry<K,V>> {

		private Stack<Node<K,V>> waiting = new Stack<Node<K,V>>();
		private Node<K,V> current = null;

		// This predicate is needed to implement the invariant checker:
		// It returns true if b is the same as a or a right descendant
		// (following only "right" links).   If it is not, it should return false
		// after printing an error message
		private boolean _rightAncestor(Node<K,V> a, Node<K,V> b) {
			if(a == null || b == null) return false;
			if(a == b) return true;
			return a != null && _rightAncestor(a.right, b);
		}

		private boolean _wellFormed() {
			// (1) calls the outer _wellFormed() (DONE)
			if (!TreeMap.this._wellFormed()) return false;

			// (2) checks that every node in the stack is not null and is in the tree somewhere.
			for(int i = 0; i < waiting.size(); ++i) {
				Node<K,V> n = waiting.get(i);
				if(n == null) return _report("stack contains null element");
				if(_findChild(root, n.key, false) != n) return _report("stack contains elements not in tree");

				if(i == 0) {
					// (4) The first (deepest) node (if any) in the stack must be a right descendant
					// of the root
					if(!_rightAncestor(root, waiting.get(0)))
						return _report("first node not a descendant of the root node");
				} else {
					// (3) each node is a right descendant of the left child of the previous (deeper) node
					// in the stack (if any)
					if(!_rightAncestor(waiting.get(i - 1).left, waiting.get(i)))
						return _report("node " + i + " not a descendant of the left child of node " + (i-1));
				}
			}

			// (5) the current node (if not null) must be in the tree
			// (6) the current node (if not null) must be less than any in the stack
			if(current != null) {
				if(_findChild(root, current.key, false) != current)
					return _report("current node is not in tree");
				if(!waiting.isEmpty() && comparator.compare(current.key, waiting.peek().key) >= 0)
					return _report("current node is not less than every node in the stack");
			}

			return true;
		}

		MyIterator() {
			pushLeftNodes(root);

			assert _wellFormed() : "invariant broken after iterator constructor";
		}

		/**
		 * Pushes all lefts node to the stack.
		 * @param parent
		 */
		private void pushLeftNodes(Node<K,V> parent) {
			while(parent != null) {
				waiting.push(parent);
				parent = parent.left;
			}
		}

		public boolean hasNext() {
			assert _wellFormed() : "invariant broken before hasNext()";
			return !waiting.isEmpty();
		}

		public Entry<K, V> next() {
			if (!hasNext()) throw new NoSuchElementException("at end of map");
			assert _wellFormed() : "invariant broken at start of next()";

			current = waiting.pop();
			pushLeftNodes(current.right);

			assert _wellFormed() : "invariant broken at end of next()";
			return current;
		}

		public void remove() {
			assert _wellFormed() : "invariant broken at start of iterator.remove()";

			if(current != null) {
				TreeMap.this.remove(current.key);
				current = null;
			}

			assert _wellFormed() : "invariant broken at end of iterator.remove()";
		}
	}
}