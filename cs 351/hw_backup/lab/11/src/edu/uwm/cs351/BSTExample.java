package edu.uwm.cs351;

public class BSTExample {

	public static class Node {
		int data;
		Node left,right;
		Node(int d) {
			data = d;
		}
	}
	
	private Node root;
	private Node a = new Node(1);
	private Node b = new Node(2);
	private Node c = new Node(3);
	private Node d = new Node(4);
	private Node e = new Node(5);
	private Node f = new Node(6);
	private Node g = new Node(7);
	private Node h = new Node(8);
	private Node i = new Node(9);
	private Node j = new Node(10);
	private Node k = new Node(11);
	private Node l = new Node(12);
	private Node m = new Node(13);
	private BSTIterationStack pending;
	
	public BSTExample() {
		pending = new BSTIterationStack();
		buildBST();
	}
	
	public void buildBST() {
		//build tree
		root = j;
		root.left = b;
		root.right = l;
		b.left = a;
		b.right = f;
		l.left = k;
		l.right = m;
		f.left = e;
		f.right = i;
		e.left = c;
		i.left = g;
		c.right = d;
		g.right = h;
	}
	
	public boolean printInOrderTraversal() {
		pending.push(j);
		pending.push(b);
		pending.push(a);
		//PUSH AND POP FROM PENDING APPROPRIATELY
		System.out.println(pending.pop().data);
		System.out.println(pending.pop().data);
		pending.push(f);
		pending.push(e);
		pending.push(c);
		System.out.println(pending.pop().data);
		pending.push(d);
		System.out.println(pending.pop().data);
		System.out.println(pending.pop().data);
		System.out.println(pending.pop().data);
		pending.push(i);
		pending.push(g);
		System.out.println(pending.pop().data);
		pending.push(h);
		System.out.println(pending.pop().data);
		System.out.println(pending.pop().data);
		System.out.println(pending.pop().data);
		pending.push(l);
		pending.push(k);
		System.out.println(pending.pop().data);
		System.out.println(pending.pop().data);
		pending.push(m);
		System.out.println(pending.pop().data);
		return pending.print() == 13;
	}
}
