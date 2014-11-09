package edu.uwm.cs351;

import java.util.Comparator;

public class TreeSet<E> {
	
	private static class BSTNode<E> {
		E key;
		BSTNode<E> left, right;

		public BSTNode(E key) {
			this.key = key;
			this.left = null;
			this.right = null;
		}
	}
	
	private BSTNode<E> root;
	private Comparator<E> comparator;
	private final boolean debug;
	
	public TreeSet(Comparator<E> comparator, boolean debug) {
		root = null;
		this.comparator = comparator;
		this.debug = debug;
	}

	public void add(E key) {
		if(debug) System.out.println("add - starting at root");
		root = doAdd(root,key);
		if(debug) System.out.println("done.\n");
	}

	private BSTNode<E> doAdd(BSTNode<E> r, E key) {
		if (r == null) {
			if(debug) System.out.println("add - place here");
			return new BSTNode<E>(key);
		} 
		else if (comparator.compare(key,r.key) == 0) {
			if(debug) System.out.println("add - previously placed here");
			// ignore
		} 
		else if (comparator.compare(key,r.key) < 0) {
			if(debug) System.out.println("add - seek left");
			r.left = doAdd(r.left,key);
		} 
		else {
			if(debug) System.out.println("add - seek right");
			r.right = doAdd(r.right,key);
		}
		return r;
	}

	public void print() {
		if(debug) System.out.println("print - inorder left-to-right traversal");
		doPrint(root);
		if(debug) System.out.println("done.\n");
	}

	private void doPrint(BSTNode<E> r) {
		if (r == null) return;
		doPrint(r.left);
		System.out.println(r.key);
		doPrint(r.right);
	}
}
