/**
 * 
 */
package edu.uwm.cs351;

import java.awt.Graphics;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Ryan
 *
 */
public class Group extends AbstractShape {
	private List<Shape> shapes;

	public Group() {
		shapes = new LinkedList<Shape>();
	}

	/* (non-Javadoc)
	 * @see edu.uwm.cs351.Shape#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(Graphics g) {
		for(Shape s : shapes) {
			s.draw(g);
		}
	}

	/* (non-Javadoc)
	 * @see edu.uwm.cs351.Shape#elementName()
	 */
	@Override
	public String elementName() {
		return "Group";
	}

	/* (non-Javadoc)
	 * @see edu.uwm.cs351.AbstractShape#addShape(edu.uwm.cs351.Shape)
	 */
	/**
	 * @throws NullPointerException if s is null
	 */
	@Override
	public boolean addShape(Shape s) {
		if(s == null) throw new NullPointerException("shape is null");
		return shapes.add(s);
	}

	/* (non-Javadoc)
	 * @see edu.uwm.cs351.AbstractShape#shapesDone()
	 */
	@Override
	public boolean shapesDone() {
		return true; //shapes.size() > 0;
	}

	/* (non-Javadoc)
	 * @see edu.uwm.cs351.AbstractShape#writeShapes(java.io.Writer)
	 */
	@Override
	protected void writeShapes(Writer w) throws IOException {
		super.writeShapes(w);
		for(Shape s : shapes) {
			s.toXML(w);
		}
	}
}