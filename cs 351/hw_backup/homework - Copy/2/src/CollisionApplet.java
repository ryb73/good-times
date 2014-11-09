import java.awt.Color;
import java.awt.Graphics;

import edu.uwm.cs351.Disk;
import edu.uwm.cs351.Point;
import edu.uwm.cs351.Vector;
import edu.uwm.cs351.gfx.AnimationApplet;

public class CollisionApplet extends AnimationApplet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	public static void main(String[] args) {
		Disk d1 = new Disk(new Point(20,20), new Vector(1,0), 10, Color.BLUE);
		Disk d2 = new Disk(new Point(50,30), new Vector(-1,0), 10, Color.RED);

		for (int i = 0; i < 100; ++i) {
			System.out.println("D1: " + d1.getPosition() + "; " + d1.getVelocity());
			System.out.println("D2: " + d2.getPosition() + "; " + d2.getVelocity());
			d1.checkBounds(70, 50);
			d2.checkBounds(70,50);
			d1.checkCollision(d2);
			d1.move();
			d2.move();
		}
	}

	Disk d1, d2, d3;
	
	@Override
	public void initialize() {
		d1 = new Disk(new Point(20,20), new Vector(2,0), 10, Color.BLUE);
		d2 = new Disk(new Point(50,30), new Vector(-3,0), 10, Color.RED);
		d3 = new Disk(new Point(70,10), new Vector(0,2), 10, Color.GREEN);
	}

	@Override
	public void move() {
		int maxx = this.getWidth();
		int maxy = this.getHeight();
		d1.checkBounds(maxx,maxy);
		d2.checkBounds(maxx,maxy);
		d3.checkBounds(maxx, maxy);
		d1.checkCollision(d2);
		d1.checkCollision(d3);
		d2.checkCollision(d3);
		d1.move();
		d2.move();
		d3.move();
	}

	@Override
	public void paintContents(Graphics g) {
		d1.paint(g);
		d2.paint(g);
		d3.paint(g);
	}
}
