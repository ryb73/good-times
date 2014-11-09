package edu.uwm.cs351.util;

import java.util.Comparator;


public class TreeDictionary<K,V> extends AbstractDictionary<K, V>  {

	private static class Node<K,V> extends Entry<K,V> {
		Node<K,V> left, right;
		Node(K k, V v) {super(k,v); }
	}
	
	private Node<K,V> root = null;
	private Comparator<K> comparator; // must not be null!
	// NB: we don't have a numEntries field:
	// This makes your code simpler (albeit inefficient)
	
	/**
	 * Report a problem and return false.  It prints a problem report and returns false.
	 * This method is used to report errors discovered in the invariant checker.
	 * @param x message indicating the problem
	 * @return false
	 */
	private static boolean _report(String x) {
		System.err.println("Problem: " + x);
		return false;
	}
	
	/**
	 * Report ordering problems with the subtree rooted at the node.
	 * The {@link Node#key}s in the subtree must all be within the bounds (low,high)
	 * and the BST property must be true within the subtree as well.
	 * If false is returned a problem has been reported. Otherwise,
	 * if no problems are found, true is returned.  This method is called
	 * by the invariant checker for the class.  It calls itself recursively
	 * for subtrees.  It never looks at the {@link Node#value} fields.
	 * @param n root of subtree to consider
	 * @param low low bound (exclusive) on keys in the subtree, or null if not bounded below
	 * @param high high bound (exclusive) on keys in the subtree, or null if not bounded above
	 * @return false if problems found and reported.  Otherwise, return true.
	 */
	private boolean _checkInRange(Node<K,V> n, K low, K high) {
		if(n == null) return true;

		if(!_checkBSTOrder(n))
			return _report("BST Nodes out of order");
		if((low != null && comparator.compare(n.key, low) <= 0) ||
				(high != null && comparator.compare(n.key, high) >= 0))
			return _report("BST Nodes out of range");

		if(!_checkInRange(n.left, low, high)) return false;
		if(!_checkInRange(n.right, low, high)) return false;

		return true;
	}

	private boolean _checkBSTOrder(Node<K, V> from) {
		if(from.left != null && comparator.compare(from.key, from.left.key) < 0) {
			return false;
		}

		if(from.right != null && comparator.compare(from.key, from.right.key) > 0) {
			return false;
		}

		return true;
	}

	/** Check the invariant, returning true if everything is OK.
	 * This code will have reported an error if it returns false.
	 */
	private boolean _wellFormed() {
		// 1. Ensure that the comparator is not null
		if(comparator == null) return _report("Comparator is null.");
		// 2. Check that all keys in the tree are ordered correctly.
		//      Use _checkInRange with no bounds
		return _checkInRange(root, null, null);
	}
	
	/**
	 * Create a tree dictionary with the given comparator to compare keys with
	 * @param comp comparator to use, must not be null
	 * @throws IllegalArgumentException if comparator is null.
	 */
	public TreeDictionary(Comparator<K> comp) {
		if(comp == null) throw new IllegalArgumentException("Comparator must not be null.");
		comparator = comp;

		assert _wellFormed() : "invariant failed in constructor";
	}
	
	/* (non-Javadoc)
	 * @see edu.uwm.cs351.util.Dictionary#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		assert _wellFormed() : "invariant false before isEmpty()";
		return root == null;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.cs351.util.Dictionary#get(K)
	 */
	@Override
	public V get(K key) {
		assert _wellFormed() : "invariant false before get(" + key + ")";

		if(key == null) return null;
		Node<K,V> resNode = _findChild(root, key, false); 
		return (resNode == null) ? null : resNode.value;
	}

	/**
	 * Recursively searches for the child of p (or p itself) with the specified key.
	 * 
	 * @param p	parent node
	 * @param key
	 * @param createNew true if a new node should be created in the event that the key is not found.
	 * @return Node with specified key
	 */
	private Node<K,V> _findChild(Node<K, V> p, K key, boolean createNew) {
		if(p == null) return null;
		if(p.key.equals(key)) return p;

		int cmp = comparator.compare(p.key, key);
		if(p.left != null && cmp > 0)
			return _findChild(p.left, key, createNew);
		if(p.right != null && cmp < 0)
			return _findChild(p.right, key, createNew);

		if(createNew) {
			Node<K,V> newNode = new Node<K,V>(key, null);
			if(cmp > 0) p.left = newNode;
			else p.right = newNode;
			return newNode;
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.cs351.util.Dictionary#clear()
	 */
	@Override
	public void clear() {
		assert _wellFormed() : "invariant false before clear()";
		root = null;
		assert _wellFormed() : "invariant false after clean()";		
	}
	
	/* (non-Javadoc)
	 * @see edu.uwm.cs351.util.Dictionary#put(K, V)
	 */
	public void put(K key, V value) {
		assert _wellFormed() : "invariant false before put(" + key + "," + value + ")";

		if(key == null) throw new IllegalArgumentException("key must not be null");
		if(root == null) {
			root = new Node<K,V>(key, value);
		} else {
			Node<K,V> putNode = _findChild(root, key, true);
			putNode.value = value;
		}

		assert _wellFormed() : "invariant false after put(" + key + "," + value + ")";
	}

	/* (non-Javadoc)
	 * @see edu.uwm.cs351.util.Dictionary#inorder(edu.uwm.cs351.util.TreeDictionary.EntryPredicate)
	 */
	public void keepIf(EntryPredicate<K,V> pred) {
		assert _wellFormed() : "invariant false before inorder(" + pred + ")";

		if(pred == null) throw new IllegalArgumentException("pred must not be null");
		if(root != null)
			_keepNodeIf(null, root, pred);

		assert _wellFormed() : "invariant false after inorder(" + pred + ")";
	}

	private void _keepNodeIf(Node<K,V> p, Node<K, V> n, EntryPredicate<K, V> pred) {
		if(n.left != null) {
			_keepNodeIf(n, n.left, pred);
		}

		while(!pred.keep(n)) {
			Node<K,V> next = _removeNode(p, n);
			if(next == null) {
				return;
			} else if(next != n) {
				_keepNodeIf(p, next, pred);
				return;
			}
		}

		if(n.right != null) {
			_keepNodeIf(n, n.right, pred);
		}
	}

	private Node<K, V> _removeNode(Node<K,V> p, Node<K, V> n) {
		if(!(p == null && n == root) && !(n == p.left || n == p.right)) throw new IllegalArgumentException("n is not a child of p");

		Node<K,V> res;
		if(n.left != null && n.right != null) {
			Node<K,V> successor;
			Node<K,V> successorParent = n;
			for(successor = n.right; successor.left != null; successor = successor.left) {
				successorParent = successor;
			}
			n.key = successor.key;
			n.value = successor.value;
			_removeNode(successorParent, successor); // TODO: fix
			res = n;
		} else if(n.left != null) {
			if(p == null) {
				root = n.left;
			} else if(n == p.left) {
				p.left = n.left;
			} else {
				p.right = n.left;
			}
			res = null;
		} else if(n.right != null) {
			if(p == null) root = n.right;
			else if(n == p.left) p.left = n.right;
			else p.right = n.right;
			res = n.right;
		} else {
			if(p == null) root = null;
			else if(n == p.left) p.left = null;
			else p.right = null;
			res = null;
		}

		return res;
	}
}