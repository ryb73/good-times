package edu.uwm.cs351;

public class Collector {
	
	public enum ThingType {
		NOTHING(-1), COIN(0), GEM(1), KEY(2), RING(3), STONE(4);
		
		private int value;
		
		private ThingType(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}	
	}
	
	private static class CollectorNode
	{
		ThingType thing;
		int count;
		CollectorNode next;
		
		public CollectorNode (ThingType thing, int count, CollectorNode next) {
			this.thing = thing;
			this.count = count;
			this.next = next;
		}
	}
	
	private CollectorNode head;
	
	public Collector() {
		head = null;
	}

	public Boolean hasNoThings() { 
		return head == null; 
	}

	public int howManyThings() {   
		int count = 0;
		for (CollectorNode cur = head; cur != null; cur=cur.next) {
			++count;
		}
		return count;
	}

	public int howManyOf(ThingType count_thing) {  
		for (CollectorNode cur = head; cur != null; cur=cur.next) {
			if (cur.thing == count_thing) return cur.count;
		}
		return 0;
	}

	public void grab(ThingType new_thing) {  
		// Being lazy is good. Never write two functions that are almost
		// the same if one can be written to use the other, with
		// change in function or in big-Oh efficiency.
		grabSome(new_thing,1);
	}

	public void grabSome(ThingType new_thing, int count) {  
		CollectorNode pre = null, cur = null;

		if (head==null || head.thing.getValue() > new_thing.getValue()) {
			head = new CollectorNode(new_thing,count,head); 
		}
		else if (head.thing == new_thing) {
			head.count += count;
		}
		else 
		{    
			pre = head; 
			cur = head.next;

			while (cur != null && (new_thing.getValue() > cur.thing.getValue()))
			{  
				pre = cur; 
				cur = cur.next;
			}
			//! Bug: doesn't check for increasing count!
			if(cur.thing == new_thing) {
				cur.count += count;
				return;
			}
			
			
			// create the new node, hitching the current after it
			CollectorNode n = new CollectorNode(new_thing,count,cur);
			// insert by hitching it after previous
			pre.next = n;
		}
	}

	public void leave(ThingType lv_thing) {   
		CollectorNode pre = head, cur;
		if (head == null) {
			throw new IllegalStateException("No things to leave!");
		}
		else if (head.thing == lv_thing) {   
			if (head.count > 1) { 
				head.count--;
			}
			else {       
				head = head.next; 
			}
		} 
		else {   
			cur = head.next;
			while (cur != null && (lv_thing.getValue() > cur.thing.getValue())) {  
				pre = cur; 
				cur = cur.next;
			}
			if (cur != null && cur.thing == lv_thing) {  
				if (cur.count > 1) {
					cur.count--;
				}
				else {    
					pre.next = cur.next;
				}
			} 
			else {
				throw new IllegalStateException("No matching thing to leave!");
			}
		}
	}

	public void leaveAll(ThingType lv_thing) {  
		CollectorNode pre = head, cur;
		if (head == null) {
			throw new IllegalStateException("No things to leave!");
		}
		else if (head.thing == lv_thing) {    
			head = head.next;
		} 
		else {   
			cur = head.next;
			while (cur != null && (lv_thing.getValue() > cur.thing.getValue())) {   
				pre = cur; 
				cur = cur.next;
			}
			if (cur != null && cur.thing == lv_thing) {   
				pre.next = cur.next;
			} 
			else {
				throw new IllegalStateException("No matching thing to leave!");
			}
		}
	}

	public void leaveEverything() {  
		// while we have anything, drop all of what the first thing is.
		while (!hasNoThings()) {
			leaveAll(whatIs(0));
		}
	}

	public ThingType whatIs(int pos) {  
		if (pos < 0) {
			throw new IllegalStateException("list what_is position negative");
		}
		CollectorNode cur = head;
		for (int i = 0; i < pos; i++) {
			if (cur == null) {
				throw new IllegalStateException("list what_is position too large");
			}
			else {
				cur = cur.next;
			}
		}
		return cur.thing;
	}

	public int whereIs(ThingType loc_thing) {  
		int i = 0;
		CollectorNode cur = head;
		while (cur != null && loc_thing != cur.thing) {    
			cur = cur.next; 
			i++;
		}
		if (cur == null) {
			throw new IllegalStateException("Thing not found");
		}
		else return i;
	}
}
