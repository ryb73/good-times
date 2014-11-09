package edu.uwm.cs351;

import java.awt.Graphics;
import java.io.IOException;
import java.io.Writer;

/**
 * @author Ryan Biwer
 */
public class Rectangle extends NormalShape {
	private int width = 0;
	private int height = 0;

	/* (non-Javadoc)
	 * @see edu.uwm.cs351.Shape#elementName()
	 */
	@Override
	public String elementName() {
		return "Rectangle";
	}

	/* (non-Javadoc)
	 * @see edu.uwm.cs351.NormalShape#addAttribute(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addAttribute(String name, String value) {
		if(name.equals("width")) {
			try {
				width = Integer.parseInt(value);
			} catch(NumberFormatException nfe) {
				return false;
			}
			return width > 0;
		} else if(name.equals("height")) {
			try {
				height = Integer.parseInt(value);
			} catch(NumberFormatException nfe) {
				return false;
			}
			return height > 0;
		}
		return super.addAttribute(name, value);
	}

	/* (non-Javadoc)
	 * @see edu.uwm.cs351.NormalShape#attributesDone()
	 */
	@Override
	public boolean attributesDone() {
		return (width > 0 && height > 0) && super.attributesDone();
	}

	/* (non-Javadoc)
	 * @see edu.uwm.cs351.NormalShape#drawFill(java.awt.Graphics)
	 */
	@Override
	protected void drawFill(Graphics g) {
		super.drawFill(g);
		g.fillRect((int)(center.x() - width/2), (int)(center.y() - height/2), width, height);
	}

	/* (non-Javadoc)
	 * @see edu.uwm.cs351.NormalShape#drawOutline(java.awt.Graphics)
	 */
	@Override
	protected void drawOutline(Graphics g) {
		super.drawOutline(g);
		g.drawRect((int)(center.x() - width/2), (int)(center.y() - height/2), width, height);
	}

	/* (non-Javadoc)
	 * @see edu.uwm.cs351.NormalShape#writeAttributes(java.io.Writer)
	 */
	@Override
	protected void writeAttributes(Writer w) throws IOException {
		super.writeAttributes(w);
		w.write(" width='" + width + "' height='" + height + "'");
	}
}