import java.awt.Color;
import java.util.Arrays;

import edu.uwm.cs351.Disk;
import edu.uwm.cs351.DiskSeq;
import edu.uwm.cs351.Point;
import edu.uwm.cs351.Vector;


public class Driver {

	private static int passed;
	private static int failed;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
		  testDisk();
		  
		  Disk p1 = new Disk(new Point(0,0),new Vector(),1,Color.BLACK);
		  Disk p2 = new Disk(new Point(0,0),new Vector(),2,Color.BLUE);
		  Disk p3 = new Disk(new Point(0,0),new Vector(),3,Color.RED);
		  Disk p4 = new Disk(new Point(0,0),new Vector(),4,Color.GREEN);
		  Disk p5 = new Disk(new Point(0,0),new Vector(),5,Color.YELLOW);
		  testDiskSeq(p1,p2,p3,p4,p5);
		  
		} catch (Exception e) {
			++failed;
			e.printStackTrace();
		}
		System.out.println("Passed " + passed + " tests.");
		System.out.println("Failed " + failed + " test" + (failed == 1? "." : "s."));
	}

	private static void testDisk() {
		  Point p1 = new Point(2.0,3.0);
		  test(p1.x(),2.0,"p1.x()");
		  test(p1.y(),3.0,"p1.y()");

		  Point p2 = new Point(-4.0,11.0);
		  test(p1.x(),2.0,"p1.x() again");
		  test(p2.x(),-4.0,"p2.x()");
		  test(p2.y(),11.0,"p2.y()");
		  
		  test(p1.toString(),"(2.0,3.0)","p1.toString()");
		  
		  Vector v0 = new Vector();
		  test(v0.dx(),0.0,"v0.dx()");
		  test(v0.dy(),0.0,"v0.dy()");
		  
		  Vector v1 = new Vector(-3.0,0.0);
		  test(v1.dx(),-3.0,"v1.dx()");
		  test(v1.dy(),0.0,"v1.dy()");
		  
		  Vector v2 = new Vector(p1,p2);
		  test(v2.dx(),-6.0,"v2.dx()");
		  test(v2.dy(),8.0,"v2.dy()");
		  
		  test(v0.move(p1),p1,"v0.move(p1)");
		  test(v1.move(p1),new Point(-1,3),"v1.move(p1)");
		  test(p1.x(),2.0,"p1.x() for the third time");
		  test(v2.move(p1),p2,"v2.move(p1)");
		  
		  test(v0.add(v1),v1,"v0.add(v1)");
		  test(v1.add(v2), new Vector(-9,8),"v1.add(v2)");
		  
		  test(v0.scale(Math.PI),v0,"v0.scale(anything)");
		  test(v1.scale(1.33333333),new Vector(-4,0),"v1.scale(1 1/3)");
		  test(v2.scale(0.1), new Vector(-0.6,0.8),"v2.scale(0.1)");
		  
		  test(v0.magnitude(),0,"|v0|");
		  test(v1.magnitude(),3,"|v1|");
		  test(v2.magnitude(),10,"|v2|");
		  
		  // Don't test:   test(v0.normalize().toString(),"(NaN,NaN)","v0.normalize()");
		  test(v1.normalize(),new Vector(-1,0),"v1.normalize()");
		  test(v2.normalize(),new Vector(-0.6,0.8),"v2.normalize()");
		  
		  test(v0.toString(),"<0.0,0.0>","v0.toString()");
		  test(v1.add(v1).toString(),"<-6.0,0.0>","v1.add(v1).toString()");
		  test(v1.toString(),"<-3.0,0.0>","v1.toString()");
		  test(v2.toString(),"<-6.0,8.0>","v2.toString()");
		  
		  test(v1.dot(v2),18.0,"v1.dot(v2)");
		  test(v2.dot(v2),100.0,"v2.dot(v2)");
		  test(v2.dot(new Vector(1,1)),2,"v2.dot(<1,1>)");
		  
		  test(v1.reflectX(),new Vector(3,0),"v1.reflectX()");
		  test(v2.reflectX(),new Vector(6,8),"v2.reflectX()");
		  test(v2.reflectY(),new Vector(-6,-8),"v2.reflectY()");
		  
		  Disk o1 = new Disk(p1,v1,1,Color.BLUE);
		  test(o1.getPosition(),p1,"o1.getPosition()");
		  test(o1.getVelocity(),v1,"o1.getVelocity()");
		  
		  // test checkBounds with only X coordinates
		  o1.checkBounds(1,10);
		  test(o1.getVelocity(),v1,"o1.getVelocity() after narrow bounds (1)");
		  o1.checkBounds(2,10);
		  test(o1.getVelocity(),v1,"o1.getVelocity() after narrow/irrelevant bounds (2)");
		  o1.checkBounds(3, 10);
		  test(o1.getVelocity(),v1,"o1.getVelocity() after irrelevant bounds (3)");
		  
		  o1.move();
		  test(o1.getPosition(), new Point(-1.0,3.0),"o1.move(); o1.getPosition()");
		  
		  o1.checkBounds(1, 1);
		  test(o1.getVelocity(),v1,"o1.getVelocity() after narrow bounds (4)");
		  o1.checkBounds(10, 10);
		  test(o1.getVelocity(),v1.reflectX(),"after 1st bounce, o1.getVelocity()");
		  o1.checkBounds(10, 10);
		  test(o1.getVelocity(),v1.reflectX(),"after irrelevant bounds (5), o1.getVelocity()");
		  
		  o1.move();
		  test(o1.getPosition(), new Point(2,3), "after 2nd move, o1.getPosition()");
		  o1.move();
		  test(o1.getPosition(), new Point(5,3), "after 3rd move, o1.getPosition()");
		  o1.checkBounds(10, 5);
		  test(o1.getVelocity(),v1.reflectX(),"after irrelevant bounds (6), o1.getVelocity()");
		  
		  o1.checkBounds(6,6);
		  test(o1.getVelocity(),v1,"after 2nd bounce, o1.getVelocity()");
		  o1.checkBounds(6,6);
		  test(o1.getVelocity(),v1,"after irrelevant bounds (7), o1.getVelocity()");
		  
		  // test check collision with only X coordinates
		  Disk o11 = new Disk(new Point(2,3),new Vector(0,1),2,Color.GREEN);
		  o1.checkCollision(o11);
		  test(o1.getVelocity(), v0, "after collision, o1.getVelocity()");
		  test(o11.getVelocity(), new Vector(-3,1), "after collision, o11.getVelocity()");
		  o1.checkCollision(o11);
		  test(o1.getVelocity(), v0, "after non-collision, o1.getVelocity()");
		  test(o11.getVelocity(), new Vector(-3,1), "after non-collision, o11.getVelocity()");
		  
		  o11.checkBounds(10,10);
		  test(o11.getVelocity(), new Vector(3,1), "after bounce, o11.getVelocity()");
		  
		  o11.checkCollision(o1);
		  test(o1.getVelocity(),v1.reflectX(),"after 2nd collision, o1.getVelocity()");
		  test(o11.getVelocity(),new Vector(0,1),"after 2nd collision, o11.getVelocity()");

		  // test bounds with X and Y issues, and non-collisions (due to distance):
		  Disk o2 = new Disk(p2,v2,2,Color.YELLOW);
		  o2.checkCollision(o1);
		  test(o2.getVelocity(),v2,"after non-collision with o1, o2.getVelocity()");
		  
		  o2.checkBounds(4,4);
		  test(o2.getVelocity(),v2,"after narrow bounds (8), o2.getVelocity()");
		  o2.checkBounds(10,10);
		  test(o2.getVelocity(),v2.scale(-1),"after double bounce, o2.getVelocity()");
		  o2.move();
		  test(o2.getPosition(),new Point(2,3),"after move(), o2.getPosition()");
		  
		  // an artificially constructed situation with rational coordinates.
		  Disk o3 = new Disk(new Point(10,16),new Vector(-3,5),3,Color.PINK);
		  Disk o4 = new Disk(new Point(7,20),new Vector(5,2),2,Color.cyan);
		  
		  o3.checkCollision(o4);
		  test(o3.getVelocity(),new Vector(1.32,-0.76), "after collision, o3.getVelocity()");
		  test(o4.getVelocity(),new Vector(.68,7.76), "after collision, o4.getVelocity()");
		  
		  o3.checkCollision(o4);
		  test(o3.getVelocity(),new Vector(1.32,-0.76), "after non-collision, o3.getVelocity()");
		  test(o4.getVelocity(),new Vector(.68,7.76), "after non-collision, o4.getVelocity()");
	}
	
	private static void testDiskSeq(Disk p1, Disk p2, Disk p3, Disk p4, Disk p5)
	{
		/// First some initial test to make sure we can build and traverse sequences.
		System.out.println("Initial tests");
		
		DiskSeq ps = new DiskSeq();
		test(ps,0,"empty");

		if (failed != 0) {
			System.out.println("ADT too broken (1): skipping remaining tests");
			return;
		}

		ps.insert(p1);
		test(ps,0,"{*p1}",p1);

		if (failed != 0) {
			System.out.println("ADT too broken (2): skipping remaining tests");
			return;
		}

		ps.start();
		ps.insert(p2);
		test(ps,0,"{*p2,p1}",p2,p1);
		
		if (failed != 0) {
			System.out.println("ADT too broken (3): skipping remaining tests");
			return;
		}

		ps.start();
		ps.insert(p3);
		test(ps,0,"{*p3,p2,p1}",p3,p2,p1);
		
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
		ps = new DiskSeq();
		ps.insert(p1);
		ps.advance(); 
		ps.insert(p3);
		test(ps,1,"{p1,*p3}",p1,p3);
		
		// in between two elements in a sequence
		ps = new DiskSeq();
		ps.insert(p1);
		ps.insert(p2);
		ps.advance();
		ps.insert(p3);
		test(ps,1,"{p2,*p3,p1}",p2,p3,p1);
		
		// at end of two element sequence
		ps = new DiskSeq();
		ps.insert(p1);
		ps.insert(p2);
		ps.advance();
		ps.advance();
		ps.insert(p3);
		test(ps,2,"{p2,p1,*p3}",p2,p1,p3);
		
		
		
		
		/// Checking removeCurrent
		System.out.println("Testing removeCurrent()");
		
		// removing only element
		ps = new DiskSeq();
		ps.insert(p1);
		ps.removeCurrent();
		test(ps,0,"{}");
		
		// removing first of two elements
		ps = new DiskSeq();
		ps.insert(p1);
		ps.insert(p2);
		ps.removeCurrent();
		test(ps,0,"{*p1}",p1);
		
		// removing second of two elements
		ps = new DiskSeq();
		ps.insert(p1);
		ps.insert(p2);
		ps.advance();
		ps.removeCurrent();
		test(ps,1,"{p2}",p2);
		
		// removing first of three elements
		ps = new DiskSeq();
		ps.insert(p1);
		ps.insert(p2);
		ps.insert(p3);
		ps.removeCurrent();
		test(ps,0,"{*p2,p1}",p2,p1);
		
		// removing second of three elements
		ps = new DiskSeq();
		ps.insert(p1);
		ps.insert(p2);
		ps.insert(p3);
		ps.advance();
		ps.removeCurrent();
		test(ps,1,"{p3,*p1}",p3,p1);
		
		// removing third of three elements
		ps = new DiskSeq();
		ps.insert(p1);
		ps.insert(p2);
		ps.insert(p3);
		ps.advance();
		ps.advance();
		ps.removeCurrent();
		test(ps,2,"{p3,p2}",p3,p2);
		
		// removing first of four elements
		ps = new DiskSeq();
		ps.insert(p1);
		ps.insert(p2);
		ps.insert(p3);
		ps.insert(p4);
		ps.removeCurrent();
		test(ps,0,"{*p3,p2,p1}",p3,p2,p1);
		
		// removing second of four elements
		ps = new DiskSeq();
		ps.insert(p1);
		ps.insert(p2);
		ps.insert(p3);
		ps.insert(p4);
		ps.advance();
		ps.removeCurrent();
		test(ps,1,"{p4,*p2,p1}",p4,p2,p1);
		
		// removing third of four elements
		ps = new DiskSeq();
		ps.insert(p1);
		ps.insert(p2);
		ps.insert(p3);
		ps.insert(p4);
		ps.advance();
		ps.advance();
		ps.removeCurrent();
		test(ps,2,"{p4,p3,*p1}",p4,p3,p1);
		
		// removing fourth of four elements
		ps = new DiskSeq();
		ps.insert(p1);
		ps.insert(p2);
		ps.insert(p3);
		ps.insert(p4);
		ps.advance();
		ps.advance();
		ps.advance();
		ps.removeCurrent();
		test(ps,3,"{p4,p3,p2}",p4,p3,p2);
		
		
		
		/// Checking clone
		System.out.println("Testing clone()");
		
		ps = new DiskSeq();
		testClone(ps,0,"{}");
		
		// cloning only element
		ps = new DiskSeq();
		ps.insert(p1);
		testClone(ps,0,"{*p1}",p1);
		
		// cloning first of two elements
		ps = new DiskSeq();
		ps.insert(p1);
		ps.insert(p2);
		testClone(ps,0,"{*p2,p1}",p2,p1);
		
		// cloning second of two elements
		ps = new DiskSeq();
		ps.insert(p1);
		ps.insert(p2);
		ps.advance();
		testClone(ps,1,"{p2,*p1}",p2,p1);
		
		// clone with two elements, no current
		ps = new DiskSeq();
		ps.insert(p1);
		ps.insert(p2);
		ps.advance();
		ps.advance();
		testClone(ps,2,"{p2,p1}",p2,p1);
		
		// cloning first of three elements
		ps = new DiskSeq();
		ps.insert(p1);
		ps.insert(p2);
		ps.insert(p3);
		testClone(ps,0,"{*p3,p2,p1}",p3,p2,p1);
		
		// cloning second of three elements
		ps = new DiskSeq();
		ps.insert(p1);
		ps.insert(p2);
		ps.insert(p3);
		ps.advance();
		testClone(ps,1,"{p3,*p2,p1}",p3,p2,p1);
		
		// cloning third of three elements
		ps = new DiskSeq();
		ps.insert(p1);
		ps.insert(p2);
		ps.insert(p3);
		ps.advance();
		ps.advance();
		testClone(ps,2,"{p3,p2,*p1}",p3,p2,p1);
		
		// cloning with three elements, no current
		ps = new DiskSeq();
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
				DiskSeq ps1 = new DiskSeq();
				DiskSeq ps2 = new DiskSeq();
				if (n1 > 0) ps1.insert(p1);
				if (n1 > 1) ps1.insert(p2);
				if (n1 > 2) ps1.insert(p3);
				if (n2 > 0) ps2.insert(p5);
				if (n2 > 1) ps2.insert(p4);
				if (n2 > 2) ps2.insert(p3);				
				System.out.print(" " + n1 + n2);
				testAddAll(ps1,ps2);
			}
		}
		System.out.println();
		

		/// Checking errors
		System.out.println("Testing exceptions");
		
		ps = new DiskSeq();
		try {
			ps.getCurrent();
			test(false,"getCurrent() succeeded on empty list");
		} catch (IllegalStateException ex) {
			// OK
		} catch (RuntimeException ex) {
			test(false,"threw wrong exception: " + ex);
		}
		test(ps,0,"{}'");
		
		ps = new DiskSeq();
		try {
			ps.advance();
			test(false,"advance() succeeded on empty list");
		} catch (IllegalStateException ex) {
			// OK
		} catch (RuntimeException ex) {
			test(false,"threw wrong exception: " + ex);
		}
		test(ps,0,"{}'");
		
		ps = new DiskSeq();
		try {
			ps.removeCurrent();
			test(false,"removeCurrent() succeeded on empty list");
		} catch (IllegalStateException ex) {
			// OK
		} catch (RuntimeException ex) {
			test(false,"threw wrong exception: " + ex);
		}
		test(ps,0,"{}'");
		
		
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
		test(ps,1,"{p1}'",p1);
		
		ps = new DiskSeq();
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
		test(ps,1,"{p1}'",p1);
		
		ps = new DiskSeq();
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
		test(ps,1,"{p1}'",p1);

		
		// with two elements:
		
		ps = new DiskSeq();
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
		test(ps,2,"{p2,p1}'",p2,p1);
		
		ps = new DiskSeq();
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
		test(ps,2,"{p2,p1}'",p2,p1);
		
		ps = new DiskSeq();
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
		test(ps,2,"{p2,p1}'",p2,p1);
	
		
		// There's no point doing the self tests if errors happened earlier.
		if (failed != 0) {
			System.out.println("Errors found, skipping insertAll self tests");
			return;
		}
		System.out.println("Testing insertAll with self.");
		
		ps = new DiskSeq();
		ps.insertAll(ps);
		test(ps,0,"{}");
		
		ps = new DiskSeq();
		ps.insert(p1);
		ps.insertAll(ps);
		test(ps,0,"{*p1,p1}",p1,p1);
		
		ps = new DiskSeq();
		ps.insert(p1);
		ps.advance();
		ps.insertAll(ps);
		test(ps,1,"{p1,*p1}",p1,p1);
		
		ps = new DiskSeq();
		ps.insert(p2);
		ps.insert(p1);
		ps.insertAll(ps);
		test(ps,0,"{*p1,p2,p1,p2}",p1,p2,p1,p2);
		
		ps = new DiskSeq();
		ps.insert(p2);
		ps.insert(p1);
		ps.advance();
		ps.insertAll(ps);
		test(ps,1,"{p1,*p1,p2,p2}",p1,p1,p2,p2);
		
		ps = new DiskSeq();
		ps.insert(p2);
		ps.insert(p1);
		ps.advance();
		ps.advance();
		ps.insertAll(ps);
		test(ps,2,"{p1,p2,*p1,p2}",p1,p2,p1,p2);
	
		ps = new DiskSeq();
		ps.insert(p3);
		ps.insert(p2);
		ps.insert(p1);
		ps.insertAll(ps);
		test(ps,0,"{*p1,p2,p3,p1,p2,p3}",p1,p2,p3,p1,p2,p3);
		
		ps = new DiskSeq();
		ps.insert(p3);
		ps.insert(p2);
		ps.insert(p1);
		ps.advance();
		ps.insertAll(ps);
		test(ps,1,"{p1,*p1,p2,p3,p2,p3}",p1,p1,p2,p3,p2,p3);
		
		ps = new DiskSeq();
		ps.insert(p3);
		ps.insert(p2);
		ps.insert(p1);
		ps.advance();
		ps.advance();
		ps.insertAll(ps);
		test(ps,2,"{p1,p2,*p1,p2,p3,p3}",p1,p2,p1,p2,p3,p3);

		ps = new DiskSeq();
		ps.insert(p3);
		ps.insert(p2);
		ps.insert(p1);
		ps.advance();
		ps.advance();
		ps.advance();
		ps.insertAll(ps);
		test(ps,3,"{p1,p2,p3,*p1,p2,p3}",p1,p2,p3,p1,p2,p3);
	
	}
	
	private static void testClone(DiskSeq ps, int index, String name, Disk... parts) {
		DiskSeq c = ps.clone();
		int oldFailed = failed;
		test(c,index,"cloned " + name,parts);
		
		if (index != parts.length) {
			if (failed == oldFailed) {
				c.start();
				for (int i=0; i < index; ++i) c.advance();
				c.removeCurrent();
				test(ps,index,name,parts); // testing that original unmodified
			} else {
				System.out.println("Errors found in clone(), not testing aliasing.");
			}
		}
	}
	
	private static void testAddAll(DiskSeq ps1, DiskSeq ps2) {
		for (ps1.start(); ps1.hasCurrent(); ps1.advance()) {
			for (ps2.start(); ps2.hasCurrent(); ps2.advance()) {
				testAddAllHelp(ps1,ps2);
			}
			testAddAllHelp(ps1,ps2);
		}
		for (ps2.start(); ps2.hasCurrent(); ps2.advance()) {
			testAddAllHelp(ps1,ps2);
		}
	}
	
	private static void testAddAllHelp(DiskSeq ps1, DiskSeq ps2) {
		DiskSeq c1 = ps1.clone();
		DiskSeq c2 = ps2.clone();
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
		Disk[] second = new Disk[c2.size()];
		Disk[] combined = new Disk[c1.size() + c2.size()];
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
		
		test(c1,index,Arrays.toString(combined),combined);
		// remove some elements and append to end and check that c2 unaffected:
		while (c1.hasCurrent()) {
			c1.removeCurrent();
			if (c1.hasCurrent()) c1.advance();
		}
		c1.insert(new Disk(new Point(0,0),new Vector(),10,Color.WHITE));
		// If you get an error here: you have aliasing problems:
		test(c2,cur2,"aliased " + second.toString(),second);
	}

	private static void test(DiskSeq ds, int index, String name, Disk... parts)
	{
		// System.out.println("test(" + name + ", " + index + "," + "Disk["  + parts.length + "])");
		Disk curr = null;
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

	private static void test(Disk x, Disk expected, String name) {
		if (x == expected) ++passed;
		else {
			++failed;
			System.out.println("\n!!! Failed test: " + name + ".  Expected " + expected + ", but got " + x + "\n");
			//debug
			new Exception().printStackTrace();
		}
	}

	private static void test(Vector v, Vector expected, String name) {
		test(v.dx(),expected.dx(),name+".x()");
		test(v.dy(),expected.dy(),name+".y()");
	}

	private static void test(Point p, Point expected, String name) {
		test(p.x(),expected.x(),name+".x()");
		test(p.y(),expected.y(),name+".y()");
	}
	
	private static double FUDGE = 0.0001;
	
	private static void test(double x, double expected, String name) {
		if (Math.abs(x-expected) < FUDGE) ++passed;
		else {
			++failed;
			System.out.println("\n!!! Failed test: " + name + ".  Expected " + expected + ", but got " + x + "\n");
			//debug
			new Exception().printStackTrace();
		}
	}
	
	private static void test(String x, String expected, String name) {
		if (x.equals(expected)) ++passed;
		else {
			++failed;
			System.out.println("\n!!! Failed test: " + name + ".  Expected \"" + expected + "\", but got \"" + x + "\"\n");
			//debug
			new Exception().printStackTrace();
		}
	}

	private static void test(boolean b, String problem) {
		if (b) {
			++passed;
		} else {
			++failed;
			System.out.println("\n!!! Failed test: " + problem);
			//debug
			new Exception().printStackTrace();
		}
	}
}
