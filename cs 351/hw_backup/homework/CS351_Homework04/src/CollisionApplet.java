import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.Random;

import edu.uwm.cs351.Disk;
import edu.uwm.cs351.Point;
import edu.uwm.cs351.Vector;
import edu.uwm.cs351.gfx.AnimationApplet;
import edu.uwm.cs351.util.ArrayList;
import edu.uwm.cs351.util.List;

public class CollisionApplet extends AnimationApplet implements MouseListener {
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
	List<Disk> moveList;

	/**
	 * When the user clicks within [range] of a disk, it will be removed.
	 */
	private int range;
	
	@Override
	public void initialize() {
		moveList = new ArrayList<Disk>();
		int n = 3;
		String nArg = getParameter("n");
		int maxx = getWidth();
		int maxy = getHeight();
		Random r = new Random();
		range = 10;
		String rArg = getParameter("range");
		
		if (nArg != null) {
			try {
				n = Integer.parseInt(nArg);
			} catch (NumberFormatException e) {
				System.err.println("badly formatted 'n' parameter: " + nArg);
			} 
		}

		if(rArg != null) {
			try {
				range = Integer.parseInt(rArg);
			} catch(NumberFormatException e) {
				System.err.println("badly formatted 'range' parameter: " + rArg);
			}
		}

		for (int i=0; i < n; ++i) {
			Disk d = new Disk(new Point(r.nextInt(maxx),r.nextInt(maxy)),
					new Vector(randSpeed(r), randSpeed(r)),
					r.nextInt(MAXRADIUS-MINRADIUS) + MINRADIUS,
					colors[r.nextInt(colors.length)]);
			moveList.add(d);
		}

		addMouseListener(this);
	}

	private int randSpeed(Random r) {
		return r.nextInt(MAXSPEED) - r.nextInt(MAXSPEED);
	}

	@Override
	public void move() {
		//#(
		int maxx = this.getWidth();
		int maxy = this.getHeight();
		/*for (moveSeq.start(); moveSeq.hasCurrent(); moveSeq.advance()) {
			moveSeq.getCurrent().checkBounds(maxx, maxy);
		}*/
		for(Disk d : moveList) {
			d.checkBounds(maxx, maxy);
		}
		
		/*DiskSeq copy = collisionSeq;
		for (moveSeq.start(); moveSeq.hasCurrent(); moveSeq.advance()) {
			Disk d1 = moveSeq.getCurrent();
			for (copy.start(); copy.hasCurrent(); copy.advance()) {
				copy.getCurrent().checkCollision(d1);
			}
		}*/
		for(Disk d : moveList) {
			for(Disk d1 : moveList) {
				d1.checkCollision(d);
			}
		}
		
		/*for (moveSeq.start(); moveSeq.hasCurrent(); moveSeq.advance()) {
			moveSeq.getCurrent().move();
		}*/
		for(Disk d : moveList) {
			d.move();
		}
		// #)
		// check all disks in bounds
		// then check collisions between all disks
		// then move all disks
	}

	@Override
	public void paintContents(Graphics g) {
		// #(
		/*DiskSeq seq = paintSeq;
		for (seq.start(); seq.hasCurrent(); seq.advance()) {
			seq.getCurrent().paint(g);
		}*/
		for(Disk d : moveList) {
			d.paint(g);
		}
		// #)
		// paint all the disks
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int cx = e.getX();
		int cy = e.getY();
		Point p = new Point(cx, cy);

		boolean inRange = false;
		Iterator<Disk> diskit = moveList.iterator();
		while(diskit.hasNext()) {
			Disk d = diskit.next();
			if(d.getPosition().distance(p) <= range) {
				diskit.remove();
				inRange = true;
			}
		}

		if(!inRange) {
			Random r = new Random();
			Disk d = new Disk(p, new Vector(randSpeed(r), randSpeed(r)),
					r.nextInt(MAXRADIUS-MINRADIUS) + MINRADIUS,
					colors[r.nextInt(colors.length)]);
			moveList.add(d);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) { }

	@Override
	public void mouseReleased(MouseEvent e) { }
}
