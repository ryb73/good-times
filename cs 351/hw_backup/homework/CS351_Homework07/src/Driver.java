import java.util.Arrays;

import edu.uwm.cs351.Seq;


public class Driver {

	private static int passed;
	private static int failed;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
		  testSeq("p0","p1","p2","p3","p4","p5");
		  
		} catch (Exception e) {
			++failed;
			e.printStackTrace();
		}
		System.out.println("Passed " + passed + " tests.");
		System.out.println("Failed " + failed + " test" + (failed == 1? "." : "s."));
	}

	
	@SuppressWarnings("unchecked")
	private static <E> void testSeq(E p0, E p1, E p2, E p3, E p4, E p5)
	{
		/// First some initial test to make sure we can build and traverse sequences.
		System.out.println("Initial tests");
		
		Seq<E> ps = new Seq<E>();
		testCursor(ps,0,"empty");

		if (failed != 0) {
			System.out.println("ADT too broken (1): skipping remaining tests");
			return;
		}

		ps.insert(p1);
		testCursor(ps,0,"{*p1}",p1);

		if (failed != 0) {
			System.out.println("ADT too broken (2): skipping remaining tests");
			return;
		}

		ps.start();
		ps.insert(p2);
		testCursor(ps,0,"{*p2,p1}",p2,p1);
		
		if (failed != 0) {
			System.out.println("ADT too broken (3): skipping remaining tests");
			return;
		}

		ps.start();
		ps.insert(p3);
		testCursor(ps,0,"{*p3,p2,p1}",p3,p2,p1);
		
		if (failed != 0) {
			System.out.println("ADT too broken (4): skipping remaining tests");
			return;
		}

		
		//// Main tests
		
		// After this point, we can assume addBefore correctly works
		// in a newly created sequence when adding at the beginning of the sequence.
		// We will construct sequences with this limitation for use in all remaining tests.


		/// Check addBefore in other situations:
		System.out.println("Testing insert in other situations");
		
		// at end of single element sequence
		ps = new Seq<E>();
		ps.insert(p1);
		ps.advance(); 
		ps.insert(p3);
		testCursor(ps,1,"{p1,*p3}",p1,p3);
		
		// in between two elements in a sequence
		ps = new Seq<E>();
		ps.insert(p1);
		ps.insert(p2);
		ps.advance();
		ps.insert(p3);
		testCursor(ps,1,"{p2,*p3,p1}",p2,p3,p1);
		
		// at end of two element sequence
		ps = new Seq<E>();
		ps.insert(p1);
		ps.insert(p2);
		ps.advance();
		ps.advance();
		ps.insert(p3);
		testCursor(ps,2,"{p2,p1,*p3}",p2,p1,p3);
		
		
		// now all possible places in a three element sequence
		
		// at start:
		ps = new Seq<E>();
		ps.insert(p3);
		ps.insert(p2);
		ps.insert(p1);
		ps.insert(p4);
		testCursor(ps,0,"{*p4,p1,p2,p3}",p4,p1,p2,p3);
	
		// after first
		ps = new Seq<E>();
		ps.insert(p3);
		ps.insert(p2);
		ps.insert(p1);
		ps.advance();
		ps.insert(p4);
		testCursor(ps,1,"{p1,*p4,p2,p3}",p1,p4,p2,p3);
	
		// after second
		ps = new Seq<E>();
		ps.insert(p3);
		ps.insert(p2);
		ps.insert(p1);
		ps.advance();
		ps.advance();
		ps.insert(p4);
		testCursor(ps,2,"{p1,p2,*p4,p3}",p1,p2,p4,p3);
	
		// after third
		ps = new Seq<E>();
		ps.insert(p3);
		ps.insert(p2);
		ps.insert(p1);
		ps.advance();
		ps.advance();
		ps.advance();
		ps.insert(p4);
		testCursor(ps,3,"{p1,p2,p3,*p4}",p1,p2,p3,p4);
	
		
		/// testing multiple inserts (to check for crashes or memory problems:
		
		System.out.println("Testing large number of inserts");

		ps = new Seq<E>();
		for (int i=0; i < 10000; ++i) {
			E d;
			switch (i % 5) {
			case 1: d = p1; break;
			case 2: d = p2; break;
			case 3: d = p3; break;
			case 4: d = p4; break;
			default: d = p5; break;
			}
			ps.insert(d);
			if (d == p3) ps.advance();
		}
	
	
		
		/// Checking removeCurrent
		System.out.println("Testing removeCurrent()");
		
		// removing only element
		ps = new Seq<E>();
		ps.insert(p1);
		ps.removeCurrent();
		testCursor(ps,0,"{}");
		
		// removing first of two elements
		ps = new Seq<E>();
		ps.insert(p1);
		ps.insert(p2);
		ps.removeCurrent();
		testCursor(ps,0,"{*p1}",p1);
		
		// removing second of two elements
		ps = new Seq<E>();
		ps.insert(p1);
		ps.insert(p2);
		ps.advance();
		ps.removeCurrent();
		testCursor(ps,1,"{p2}",p2);
		
		// removing first of three elements
		ps = new Seq<E>();
		ps.insert(p1);
		ps.insert(p2);
		ps.insert(p3);
		ps.removeCurrent();
		testCursor(ps,0,"{*p2,p1}",p2,p1);
		
		// removing second of three elements
		ps = new Seq<E>();
		ps.insert(p1);
		ps.insert(p2);
		ps.insert(p3);
		ps.advance();
		ps.removeCurrent();
		testCursor(ps,1,"{p3,*p1}",p3,p1);
		
		// removing third of three elements
		ps = new Seq<E>();
		ps.insert(p1);
		ps.insert(p2);
		ps.insert(p3);
		ps.advance();
		ps.advance();
		ps.removeCurrent();
		testCursor(ps,2,"{p3,p2}",p3,p2);
		
		// removing first of four elements
		ps = new Seq<E>();
		ps.insert(p1);
		ps.insert(p2);
		ps.insert(p3);
		ps.insert(p4);
		ps.removeCurrent();
		testCursor(ps,0,"{*p3,p2,p1}",p3,p2,p1);
		
		// removing second of four elements
		ps = new Seq<E>();
		ps.insert(p1);
		ps.insert(p2);
		ps.insert(p3);
		ps.insert(p4);
		ps.advance();
		ps.removeCurrent();
		testCursor(ps,1,"{p4,*p2,p1}",p4,p2,p1);
		
		// removing third of four elements
		ps = new Seq<E>();
		ps.insert(p1);
		ps.insert(p2);
		ps.insert(p3);
		ps.insert(p4);
		ps.advance();
		ps.advance();
		ps.removeCurrent();
		testCursor(ps,2,"{p4,p3,*p1}",p4,p3,p1);
		
		// removing fourth of four elements
		ps = new Seq<E>();
		ps.insert(p1);
		ps.insert(p2);
		ps.insert(p3);
		ps.insert(p4);
		ps.advance();
		ps.advance();
		ps.advance();
		ps.removeCurrent();
		testCursor(ps,3,"{p4,p3,p2}",p4,p3,p2);
		
		
		
		/// Checking clone
		System.out.println("Testing clone()");
		
		ps = new Seq<E>();
		testClone(ps,0,"{}");
		
		// cloning only element
		ps = new Seq<E>();
		ps.insert(p1);
		testClone(ps,0,"{*p1}",p1);
		
		// cloning first of two elements
		ps = new Seq<E>();
		ps.insert(p1);
		ps.insert(p2);
		testClone(ps,0,"{*p2,p1}",p2,p1);
		
		// cloning second of two elements
		ps = new Seq<E>();
		ps.insert(p1);
		ps.insert(p2);
		ps.advance();
		testClone(ps,1,"{p2,*p1}",p2,p1);
		
		// clone with two elements, no current
		ps = new Seq<E>();
		ps.insert(p1);
		ps.insert(p2);
		ps.advance();
		ps.advance();
		testClone(ps,2,"{p2,p1}",p2,p1);
		
		// cloning first of three elements
		ps = new Seq<E>();
		ps.insert(p1);
		ps.insert(p2);
		ps.insert(p3);
		testClone(ps,0,"{*p3,p2,p1}",p3,p2,p1);
		
		// cloning second of three elements
		ps = new Seq<E>();
		ps.insert(p1);
		ps.insert(p2);
		ps.insert(p3);
		ps.advance();
		testClone(ps,1,"{p3,*p2,p1}",p3,p2,p1);
		
		// cloning third of three elements
		ps = new Seq<E>();
		ps.insert(p1);
		ps.insert(p2);
		ps.insert(p3);
		ps.advance();
		ps.advance();
		testClone(ps,2,"{p3,p2,*p1}",p3,p2,p1);
		
		// cloning with three elements, no current
		ps = new Seq<E>();
		ps.insert(p1);
		ps.insert(p2);
		ps.insert(p3);
		ps.advance();
		ps.advance();
		ps.advance();
		testClone(ps,3,"{p3,p2,p1}",p3,p2,p1);

		
		
		//// Final Tests
		
		// There's no point doing these final tests if errors happened earlier.
		if (failed != 0) {
			System.out.println("Errors found, skipping insertAll and exception tests");
			return;
		}
		
		
		/// Checking addAll
		System.out.println("Testing insertAll()");

		for (int n1 = 0; n1 <= 3; ++n1) {
			for (int n2 = 0; n2 <= 3; ++n2) {
				Seq<E> ps1 = new Seq<E>();
				Seq<E> ps2 = new Seq<E>();
				if (n1 > 0) ps1.insert(p1);
				if (n1 > 1) ps1.insert(p2);
				if (n1 > 2) ps1.insert(p3);
				if (n2 > 0) ps2.insert(p5);
				if (n2 > 1) ps2.insert(p4);
				if (n2 > 2) ps2.insert(p3);				
				System.out.print(" " + n1 + n2);
				testAddAll(ps1,ps2,p0);
			}
		}
		System.out.println();
		
		if (failed != 0) {
			System.out.println("Errors found, skipping remaining tests");
			return;
		}
		
		
		/// Checking catenation
		System.out.println("Testing catenation");

		for (int n1 = 0; n1 <= 3; ++n1) {
			for (int n2 = 0; n2 <= 3; ++n2) {
				Seq<E> ps1 = new Seq<E>();
				Seq<E> ps2 = new Seq<E>();
				if (n1 > 0) ps1.insert(p1);
				if (n1 > 1) ps1.insert(p2);
				if (n1 > 2) ps1.insert(p3);
				if (n2 > 0) ps2.insert(p5);
				if (n2 > 1) ps2.insert(p4);
				if (n2 > 2) ps2.insert(p3);				
				System.out.print(" " + n1 + n2);
				testCatenation(ps1,ps2,p0);
			}
		}
		System.out.println();
		

		/// Checking errors
		System.out.println("Testing exceptions");
		
		ps = new Seq<E>();
		try {
			ps.getCurrent();
			test(false,"getCurrent() succeeded on empty list");
		} catch (IllegalStateException ex) {
			// OK
		} catch (RuntimeException ex) {
			test(false,"threw wrong exception: " + ex);
		}
		testCursor(ps,0,"{}'");
		
		ps = new Seq<E>();
		try {
			ps.advance();
			test(false,"advance() succeeded on empty list");
		} catch (IllegalStateException ex) {
			// OK
		} catch (RuntimeException ex) {
			test(false,"threw wrong exception: " + ex);
		}
		testCursor(ps,0,"{}'");
		
		ps = new Seq<E>();
		try {
			ps.removeCurrent();
			test(false,"removeCurrent() succeeded on empty list");
		} catch (IllegalStateException ex) {
			// OK
		} catch (RuntimeException ex) {
			test(false,"threw wrong exception: " + ex);
		}
		testCursor(ps,0,"{}'");
		
		
		// with one element:
		
		ps.insert(p1);
		ps.advance();
		try {
			ps.getCurrent();
			test(false,"getCurrent() succeeded at end of one element list");
		} catch (IllegalStateException ex) {
			// OK
		} catch (RuntimeException ex) {
			test(false,"threw wrong exception: " + ex);
		}
		testCursor(ps,1,"{p1}'",p1);
		
		ps = new Seq<E>();
		ps.insert(p1);
		ps.advance();
		try {
			ps.advance();
			test(false,"advance() succeeded at end of one element list");
		} catch (IllegalStateException ex) {
			// OK
		} catch (RuntimeException ex) {
			test(false,"threw wrong exception: " + ex);
		}
		testCursor(ps,1,"{p1}'",p1);
		
		ps = new Seq<E>();
		ps.insert(p1);
		ps.advance();
		try {
			ps.removeCurrent();
			test(false,"removeCurrent() succeeded at end of one element list");
		} catch (IllegalStateException ex) {
			// OK
		} catch (RuntimeException ex) {
			test(false,"threw wrong exception: " + ex);
		}
		testCursor(ps,1,"{p1}'",p1);

		
		// with two elements:
		
		ps = new Seq<E>();
		ps.insert(p1);
		ps.insert(p2);
		ps.advance();
		ps.advance();
		try {
			ps.getCurrent();
			test(false,"getCurrent() succeeded at end of two element list");
		} catch (IllegalStateException ex) {
			// OK
		} catch (RuntimeException ex) {
			test(false,"threw wrong exception: " + ex);
		}
		testCursor(ps,2,"{p2,p1}'",p2,p1);
		
		ps = new Seq<E>();
		ps.insert(p1);
		ps.insert(p2);
		ps.advance();
		ps.advance();
		try {
			ps.advance();
			test(false,"advance() succeeded at end of two element list");
		} catch (IllegalStateException ex) {
			// OK
		} catch (RuntimeException ex) {
			test(false,"threw wrong exception: " + ex);
		}
		testCursor(ps,2,"{p2,p1}'",p2,p1);
		
		ps = new Seq<E>();
		ps.insert(p1);
		ps.insert(p2);
		ps.advance();
		ps.advance();
		try {
			ps.removeCurrent();
			test(false,"removeCurrent() succeeded at end of two element list");
		} catch (IllegalStateException ex) {
			// OK
		} catch (RuntimeException ex) {
			test(false,"threw wrong exception: " + ex);
		}
		testCursor(ps,2,"{p2,p1}'",p2,p1);
	
		
		// There's no point doing the self tests if errors happened earlier.
		if (failed != 0) {
			System.out.println("Errors found, skipping insertAll self tests");
			return;
		}
		System.out.println("Testing insertAll with self.");
		
		ps = new Seq<E>();
		ps.insertAll(ps);
		testCursor(ps,0,"{}");
		
		ps = new Seq<E>();
		ps.insert(p1);
		ps.insertAll(ps);
		testCursor(ps,0,"{*p1,p1}",p1,p1);
		
		ps = new Seq<E>();
		ps.insert(p1);
		ps.advance();
		ps.insertAll(ps);
		testCursor(ps,1,"{p1,*p1}",p1,p1);
		
		ps = new Seq<E>();
		ps.insert(p2);
		ps.insert(p1);
		ps.insertAll(ps);
		testCursor(ps,0,"{*p1,p2,p1,p2}",p1,p2,p1,p2);
		
		ps = new Seq<E>();
		ps.insert(p2);
		ps.insert(p1);
		ps.advance();
		ps.insertAll(ps);
		testCursor(ps,1,"{p1,*p1,p2,p2}",p1,p1,p2,p2);
		
		ps = new Seq<E>();
		ps.insert(p2);
		ps.insert(p1);
		ps.advance();
		ps.advance();
		ps.insertAll(ps);
		testCursor(ps,2,"{p1,p2,*p1,p2}",p1,p2,p1,p2);
	
		ps = new Seq<E>();
		ps.insert(p3);
		ps.insert(p2);
		ps.insert(p1);
		ps.insertAll(ps);
		testCursor(ps,0,"{*p1,p2,p3,p1,p2,p3}",p1,p2,p3,p1,p2,p3);
		
		ps = new Seq<E>();
		ps.insert(p3);
		ps.insert(p2);
		ps.insert(p1);
		ps.advance();
		ps.insertAll(ps);
		testCursor(ps,1,"{p1,*p1,p2,p3,p2,p3}",p1,p1,p2,p3,p2,p3);
		
		ps = new Seq<E>();
		ps.insert(p3);
		ps.insert(p2);
		ps.insert(p1);
		ps.advance();
		ps.advance();
		ps.insertAll(ps);
		testCursor(ps,2,"{p1,p2,*p1,p2,p3,p3}",p1,p2,p1,p2,p3,p3);

		ps = new Seq<E>();
		ps.insert(p3);
		ps.insert(p2);
		ps.insert(p1);
		ps.advance();
		ps.advance();
		ps.advance();
		ps.insertAll(ps);
		testCursor(ps,3,"{p1,p2,p3,*p1,p2,p3}",p1,p2,p3,p1,p2,p3);
	
	}
	
	private static <E> void testClone(Seq<E> ps, int index, String name, E... parts) {
		Seq<E> c = ps.clone();
		int oldFailed = failed;
		testCursor(c,index,"cloned " + name,parts);
		
		if (index != parts.length) {
			if (failed == oldFailed) {
				c.start();
				for (int i=0; i < index; ++i) c.advance();
				c.removeCurrent();
				testCursor(ps,index,name,parts); // testing that original unmodified
			} else {
				System.out.println("Errors found in clone(), not testing aliasing.");
			}
		}
	}
	
	private static <E> void testAddAll(Seq<E> ps1, Seq<E> ps2, E p0) {
		for (ps1.start(); ps1.hasCurrent(); ps1.advance()) {
			for (ps2.start(); ps2.hasCurrent(); ps2.advance()) {
				testAddAllHelp(ps1,ps2,p0);
			}
			testAddAllHelp(ps1,ps2,p0);
		}
		for (ps2.start(); ps2.hasCurrent(); ps2.advance()) {
			testAddAllHelp(ps1,ps2,p0);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static <E> void testAddAllHelp(Seq<E> ps1, Seq<E> ps2, E p0) {
		Seq<E> c1 = ps1.clone();
		Seq<E> c2 = ps2.clone();
		int index = c1.size();
		while (c1.hasCurrent()) {
			--index;
			c1.advance();
		}
		int cur2 = c2.size();
		while (c2.hasCurrent()) {
			--cur2;
			c2.advance();
		}
		E[] second = (E[])new Object[c2.size()];
		E[] combined = (E[])new Object[c1.size() + c2.size()];
		int i=0;
		int cur1=index;
		for (c1.start(); c1.hasCurrent() && cur1 > 0; c1.advance(), --cur1) combined[i++] = c1.getCurrent();
		for (c2.start(); c2.hasCurrent(); c2.advance()) combined[i++] = c2.getCurrent();
		for (; c1.hasCurrent(); c1.advance()) combined[i++] = c1.getCurrent();
		i = 0;
		for (c2.start(); c2.hasCurrent(); c2.advance()) second[i++] = c2.getCurrent();
		c1 = ps1.clone();
		c2 = ps2.clone();
		c1.insertAll(c2);
		
		testCursor(c1,index,Arrays.toString(combined),combined);
		// remove some elements and append to end and check that c2 unaffected:
		while (c1.hasCurrent()) {
			c1.removeCurrent();
			if (c1.hasCurrent()) c1.advance();
		}
		c1.insert(p0);
		// If you get an error here: you have aliasing problems:
		testCursor(c2,cur2,"aliased " + second.toString(),second);
	}

	@SuppressWarnings("unchecked")
	private static <E> void testCatenation(Seq<E> ps1, Seq<E> ps2, E p0) {
		Seq<E> c1 = ps1.clone();
		Seq<E> c2 = ps2.clone();
		int total = c1.size() + c2.size();
		E[] all1 = (E[]) new Object[c1.size()];
		E[] all2 = (E[]) new Object[c2.size()];
		E[] combined = (E[])new Object[total];
		int cur1 = getCurrentIndex(c1);
		int cur2 = getCurrentIndex(c2);
		
		int i=0, j = 0;
		for (c1.start(); c1.hasCurrent(); c1.advance()) combined[i++] = all1[j++] = c1.getCurrent();
		j = 0;
		for (c2.start(); c2.hasCurrent(); c2.advance()) combined[i++] = all2[j++] = c2.getCurrent();
		
		c1 = ps1.clone();
		c2 = ps2.clone();
		Seq<E> result = Seq.catenation(c1,c2);
		
		testCursor(result,total,"combined[" + total + "]",combined);
		testCursor(c1,cur1,"ps1",all1);
		testCursor(c2,cur2,"ps2",all2);
		
		// remove some elements and append to end and check that c2 unaffected:
		while (c1.hasCurrent()) {
			c1.removeCurrent();
			if (c1.hasCurrent()) c1.advance();
		}
		c1.insert(p0);
		while (c2.hasCurrent()) {
			c2.removeCurrent();
			if (c2.hasCurrent()) c2.advance();
		}
		c2.insert(p0);
		
		// If you get an error here: you have aliasing problems:
		testCursor(result,total,"combined[" + total + "]'",combined);
	}
	
	private static <E> int getCurrentIndex(Seq<E> s) {
		int index = s.size();
		while (s.hasCurrent()) {
			s.advance();
			--index;
		}
		return index;
	}

	private static <E> void testCursor(Seq<E> ds, int index, String name, E... parts)
	{
		// System.out.println("test(" + name + ", " + index + "," + "Disk["  + parts.length + "])");
		E curr = null;
		if (index < parts.length) curr = parts[index];
		if (ds.hasCurrent()) {
			if (curr == null) {
				test(false,name + " should not have current");
			} else {
				test(ds.getCurrent(),curr,name + ".current");
			}
		} else {
			test(curr == null,name + " should have current");
		}
		test(ds.size(),parts.length,name + ".size()");
		int i=index;
		while (ds.hasCurrent() && i < parts.length) {
			test(ds.getCurrent(),parts[i],name + "[" + i + "]");
			++i;
			ds.advance();
		}
		test(!ds.hasCurrent(),name + " too long");
		test(!(i < parts.length),name + " too short");
		ds.start();
		i=0;
		while (ds.hasCurrent() && i < index) {
			test(ds.getCurrent(),parts[i],name + "[" + i + "]");
			++i;
			ds.advance();
		}
		test(ds.hasCurrent() || index == parts.length, name + " too short");
	}

	private static <E> void test(E x, E expected, String name) {
		if (x == expected) ++passed;
		else {
			++failed;
			System.out.println("\n!!! Failed test: " + name + ".  Expected " + expected + ", but got " + x + "\n");
		}
	}

	private static void test(boolean b, String problem) {
		if (b) {
			++passed;
		} else {
			++failed;
			System.out.println("\n!!! Failed test: " + problem);
		}
	}
}
