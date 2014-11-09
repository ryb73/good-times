/**
 * CS 351 Homework 1
 * Ryan Biwer
 */
package edu.uwm.cs351;

/**
 * @author Ryan Biwer
 *
 */
public class Point {
	private final double _x;
	private final double _y;

	public Point(double x, double y) {
		_x = x;
		_y = y;
	}

	/**
	 * @return x
	 */
	public double x() {
		return _x;
	}

	/**
	 * @return
	 */
	public double y() {
		return _y;
	}

	@Override
	public String toString() {
		return "(" + _x + "," + _y + ")";
	}

	/**
	 * Computes the distance between two points.
	 * 
	 * @param point
	 * @return distance
	 */
	public double distance(Point p) {
		return Math.sqrt(Math.pow(_x - p._x, 2) + Math.pow(_y - p._y, 2));
	}
}