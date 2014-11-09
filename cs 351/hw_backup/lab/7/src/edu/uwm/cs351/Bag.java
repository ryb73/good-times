package edu.uwm.cs351;
/**
 * Bag implementation
 * using singly-linked acyclic list with one dummy node
 * @@author boyland, rrhardt
 *
 */
public class Bag<T> {

		private static class Node<T> {
			T data;
			Node<T> next;
			Node(T d, Node<T> n) {
				data = d;
				next = n;
			}
		}
		
		private final Node<T> dummy;
		private int manyItems;
		
		public Bag() {
			dummy = new Node<T>(null, null);
			manyItems = 0;
		}

		public void add(T x) {
			dummy.next = new Node<T>(x,dummy.next);
			++manyItems;
		}
		
		public boolean remove(T x) {
			boolean removed = false;
			for (Node<T> lag = dummy; !removed && lag.next != null; lag = lag.next) {
				if(x.equals(lag.next.data)) {
					lag.next = lag.next.next;
					--manyItems;
					removed = true;
				}
			}
			return removed;
		}
		
		public int size() {
			return manyItems;
		}
		
		public int countOccurrences(T target) {
			int answer = 0;
			for(Node<T> n = dummy; n != null; n = n.next) {
				if(target.equals(n.data))
					++answer;
			}
			return answer;
		}
}
