import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import edu.uwm.cs351.Disk;
import edu.uwm.cs351.DiskSeq;
import edu.uwm.cs351.Point;
import edu.uwm.cs351.Vector;
import edu.uwm.cs351.gfx.AnimationApplet;

public class CollisionApplet extends AnimationApplet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;

	
	Color[] colors = new Color[] {
			Color.BLACK,
			Color.BLUE,
			Color.GREEN,
			Color.CYAN,
			Color.RED,
			Color.MAGENTA,
			Color.YELLOW,
			Color.LIGHT_GRAY
	};
	private static final int MAXSPEED = 5;
	private static final int MINRADIUS = 5;
	private static final int MAXRADIUS = 15;
	
	// We have three sequences of disks -- all in the same order
	// because internal iterators clash between:
	// (1) moving disks
	// (2) testing for collisions with other disks
	// (3) painting disks
	DiskSeq moveSeq;
	DiskSeq collisionSeq;
	DiskSeq paintSeq;
	
	@Override
	public void initialize() {
		moveSeq = new DiskSeq();
		int n = 3;
		String nArg = getParameter("n");
		int maxx = getWidth();
		int maxy = getHeight();
		Random r = new Random();
		
		if (nArg != null) {
			try {
				n = Integer.parseInt(nArg);
			} catch (NumberFormatException e) {
				System.err.println("badly formatted 'n' parameter: " + nArg);
			} 
		}

		for (int i=0; i < n; ++i) {
			Disk d = new Disk(new Point(r.nextInt(maxx),r.nextInt(maxy)),
					new Vector(randSpeed(r), randSpeed(r)),
					r.nextInt(MAXRADIUS-MINRADIUS) + MINRADIUS,
					colors[r.nextInt(colors.length)]);
			moveSeq.insert(d);
		}
		collisionSeq = moveSeq.clone();
		paintSeq = moveSeq.clone();
	}

	private int randSpeed(Random r) {
		return r.nextInt(MAXSPEED) - r.nextInt(MAXSPEED);
	}

	@Override
	public void move() {
		// check all disks in bounds
		int w = getWidth();
		int h = getHeight();
		for(collisionSeq.start(); collisionSeq.hasCurrent(); collisionSeq.advance()) {
			collisionSeq.getCurrent().checkBounds(w, h);
		}

		// then check collisions between all disks
		int loops = moveSeq.size() - 1;
		for(int i = 0; i < loops; ++i) {
			moveSeq.start();
			collisionSeq.start();
			for(int a = 0; a < i; ++a) {
				moveSeq.advance();
				collisionSeq.advance();
			}

			Disk d = moveSeq.getCurrent();
			for(collisionSeq.advance(); collisionSeq.hasCurrent(); collisionSeq.advance()) {
				d.checkCollision(collisionSeq.getCurrent());
			}
		}

		// then move all disks
		for(moveSeq.start(); moveSeq.hasCurrent(); moveSeq.advance()) {
			moveSeq.getCurrent().move();
		}
	}

	@Override
	public void paintContents(Graphics g) {
		// paint all the disks
		for(paintSeq.start(); paintSeq.hasCurrent(); paintSeq.advance()) {
			paintSeq.getCurrent().paint(g);
		}
	}
}
