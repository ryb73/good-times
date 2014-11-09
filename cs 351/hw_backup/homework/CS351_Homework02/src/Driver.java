import java.awt.Color;

import edu.uwm.cs351.Disk;
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
		  
		} catch (Exception e) {
			++failed;
			e.printStackTrace();
		}
		System.out.println("Passed " + passed + " tests.");
		System.out.println("Failed " + failed + " test" + (failed == 1? "." : "s."));
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
		}
	}
	
	private static void test(String x, String expected, String name) {
		if (x.equals(expected)) ++passed;
		else {
			++failed;
			System.out.println("\n!!! Failed test: " + name + ".  Expected \"" + expected + "\", but got \"" + x + "\"\n");
		}
	}
}
