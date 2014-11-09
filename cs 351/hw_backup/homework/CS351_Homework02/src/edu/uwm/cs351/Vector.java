/**
 * CS 351 Homework 1
 * Ryan Biwer
 */
package edu.uwm.cs351;

/**
 * @author Ryan Biwer
 *
 */
public class Vector {
	private final double _dx;
	private final double _dy;

	public Vector() {
		this(0, 0);
	}

	public Vector(double dx, double dy) {
		_dx = dx;
		_dy = dy;
	}

	public Vector(Point from, Point to) {
		this(to.x() - from.x(), to.y() - from.y());
	}

	/**
	 * @return dx
	 */
	public double dx() {
		return _dx;
	}

	/**
	 * @return dy
	 */
	public double dy() {
		return _dy;
	}

	/**
	 * @return new Vector with reflected x value
	 */
	public Vector reflectX() {
		return new Vector(-_dx, _dy);
	}

	/**
	 * @return new Vector with reflected y value
	 */
	public Vector reflectY() {
		return new Vector(_dx, -_dy);
	}

	/**
	 * Moves the specified point by the magnitude of the vector and returns a new point.
	 * 
	 * @param point
	 * @return moved point
	 */
	public Point move(Point p) {
		return new Point(p.x() + _dx, p.y() + _dy);
	}

	/**
	 * @param vector
	 * @return sum of the two Vectors
	 */
	public Vector add(Vector v) {
		return new Vector(_dx + v._dx, _dy + v._dy);
	}

	/**
	 * @param d
	 * @return new vector scaled by a factor of d
	 */
	public Vector scale(double d) {
		return new Vector(_dx * d, _dy * d);
	}

	/**
	 * @param vector
	 * @return the dot product of the two Vectors
	 */
	public double dot(Vector v) {
		return _dx * v._dx + _dy * v._dy;
	}

	/**
	 * @return magnitude
	 */
	public double magnitude() {
		return Math.sqrt(dot(this));
	}

	/**
	 * @return new Vector with same direction and a magnitude of 1
	 */
	public Vector normalize() {
		return scale(1 / magnitude());
	}

	@Override
	public String toString() {
		return "<" + _dx + "," + _dy + ">";
	}
}