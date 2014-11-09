package edu.uwm.cs351;

import java.awt.Graphics;
import java.io.IOException;
import java.io.Writer;

/**
 * @author Ryan Biwer
 */
public class Circle extends NormalShape {
	private static final double MIN_RADIUS = 0;
	private double radius = 0;

	/* (non-Javadoc)
	 * @see edu.uwm.cs351.Shape#elementName()
	 */
	@Override
	public String elementName() {
		return "Circle";
	}

	/* (non-Javadoc)
	 * @see edu.uwm.cs351.NormalShape#drawFill(java.awt.Graphics)
	 */
	@Override
	protected void drawFill(Graphics g) {
		super.drawFill(g);
		g.fillOval((int)(center.x() - radius), (int)(center.y() - radius), (int)(radius * 2), (int)(radius * 2));
	}

	/* (non-Javadoc)
	 * @see edu.uwm.cs351.NormalShape#drawOutline(java.awt.Graphics)
	 */
	@Override
	protected void drawOutline(Graphics g) {
		super.drawOutline(g);
		g.drawOval((int)(center.x() - radius), (int)(center.y() - radius), (int)(radius * 2), (int)(radius * 2));
	}

	/* (non-Javadoc)
	 * @see edu.uwm.cs351.NormalShape#addAttribute(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addAttribute(String name, String value) {
		if(name.equals("radius")) {
			try {
				radius = Double.parseDouble(value);
			} catch(NumberFormatException nfe) {
				return false;
			}
			return radius > MIN_RADIUS;
		}
		return super.addAttribute(name, value);
	}

	/* (non-Javadoc)
	 * @see edu.uwm.cs351.NormalShape#attributesDone()
	 */
	@Override
	public boolean attributesDone() {
		return (radius > MIN_RADIUS) && super.attributesDone();
	}

	/* (non-Javadoc)
	 * @see edu.uwm.cs351.NormalShape#writeAttributes(java.io.Writer)
	 */
	@Override
	protected void writeAttributes(Writer w) throws IOException {
		super.writeAttributes(w);
		w.write(" radius='" + radius + "'");
	}
}