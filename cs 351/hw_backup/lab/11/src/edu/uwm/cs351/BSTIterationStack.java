package edu.uwm.cs351;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;

import edu.uwm.cs351.BSTExample.Node;

public class BSTIterationStack extends Vector<Node> {

	private static final long serialVersionUID = 1L;
	
	private Stack<Node> pending;
	private ArrayList<Integer> inOrder;
	
	// (1) pending is not null
	// (2) inOrder is not null
	// (3) each node is a right descendant of the left child of the node below it in the stack (if any)
	private boolean _wellFormed() {
		if(pending == null) {
			return _report("pending is null");
		}
		if(inOrder == null) {
			return _report("inOrder is null");
		}
		Node below = null;
		//iteration below starts at bottom of stack
		for (Node n : pending) {
			if (below != null && !_rightAncestor(below.left,n)) {
				return false;
			}
			below = n;
		}
		return true;
	}
	
	// This predicate is needed to implement the invariant checker:
	// It returns true if b is the same as a or a right descendant
	// (following only "right" links).   If it is not, it should return false
	// after printing an error message
	private boolean _rightAncestor(Node a, Node b) {
		Node p = a;
		//ADD CODE BELOW
		for(; p != null; p = p.right)
			if(p == b) return true;
			return _report("node b is not a right descendant of node a");
	}
	
	private boolean _report(String message) {
		System.err.println("Invariant error: " + message);
		return false;
	}
	
	public BSTIterationStack() {
		pending = new Stack<Node>();
		inOrder = new ArrayList<Integer>();
		assert _wellFormed() : "invariant broken after constructor";
	}
	
	public Node push(Node n) {
		assert _wellFormed() : "invariant broken before push";
		Node r = pending.push(n);
		assert _wellFormed() : "invariant broken after push";
		return r;
	}
	
	public Node pop() {
		assert _wellFormed() : "invariant broken before pop";
		Node n = pending.pop();
		inOrder.add(n.data);
		assert _wellFormed() : "invariant broken after pop";
		return n;
	}
	
	public int print() {
		System.out.print("In order traversal values: ");
		for(int i=0; i<inOrder.size(); ++i) {
			System.out.print(inOrder.get(i));
			System.out.print((i < inOrder.size()-1)?", ":"\n");
		}
		return inOrder.size();
	}
	
}
