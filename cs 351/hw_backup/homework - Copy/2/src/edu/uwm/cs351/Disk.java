/**
 * CS 351 Homework 1
 * Ryan Biwer 
 */
package edu.uwm.cs351;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author Ryan Biwer
 *
 */
public class Disk {
	private Point _position;
	private Vector _velocity;
	private final int _radius;
	private final Color _color;

	/**
	 * @param _position
	 * @param velocity
	 * @param radius
	 * @param color
	 */
	public Disk(Point position, Vector velocity, int radius, Color color) {
		_position = position;
		_velocity = velocity;
		_radius = radius;
		_color = color;
	}

	/**
	 * @return position
	 */
	public Point getPosition() {
		return _position;
	}

	/**
	 * @return velocity
	 */
	public Vector getVelocity() {
		return _velocity;
	}

	/**
	 * Moves the disk by the magnitude of the current velocity.
	 */
	public void move() {
		_position = _velocity.move(_position);
	}

	/**
	 * Makes sure the ball is within (0, 0) and (x, y), and if the ball is outside or touching the bounds
	 * and moving outside, reverse the ball's velocity appropriately. 
	 * 
	 * @param x
	 * @param y
	 */
	public void checkBounds(int x, int y) {
		if(x > 2 * _radius && ((_position.x() <= _radius && _velocity.dx() < 0) ||
				(_position.x() >= x - _radius && _velocity.dx() > 0))) {
			_velocity = _velocity.reflectX();
		}

		if(y > 2 * _radius && ((_position.y() <= _radius && _velocity.dy() < 0) ||
				(_position.y() >= y - _radius && _velocity.dy() > 0))) {
			_velocity = _velocity.reflectY();
		}
	}

	/**
	 * Checks if the two disks have collided and if so, reflects the balls accordingly.
	 * 
	 * @param disk
	 */
	public void checkCollision(Disk d) {
		if(d._color.equals(Color.GREEN)) {
			/*System.out.println("_position:\t" + _position);
			System.out.println("d._position:\t" + d._position);
			System.out.println("_velocity:\t" + _velocity);
			System.out.println("d._velocity:\t" + d._velocity);
			System.out.println("_color:\t\t" + _color);
			System.out.println("d._color:\t\t" + d._color);
			System.out.println("distance:\t" + _position.distance(d._position));
			System.out.println("--------------------------------");*/
		}

		if(_position.distance(d._position) <= _radius + d._radius) { // disks are touching
			Vector normal = new Vector(_position, d._position).normalize();
			if(normal.dot(_velocity) - normal.dot(d._velocity) > 0) { // disks are moving together
				// disks are touching and moving together -- there's a collision
				Vector thisNormal = normal.scale(normal.dot(_velocity));
				Vector thisTangent = _velocity.add(thisNormal.scale(-1));
				Vector otherNormal = normal.scale(normal.dot(d._velocity));
				Vector otherTangent = d._velocity.add(otherNormal.scale(-1));

				/*System.out.println("_position:\t" + _position);
				System.out.println("d._position:\t" + d._position);
				System.out.println("_velocity:\t" + _velocity);
				System.out.println("d._velocity:\t" + d._velocity);
				System.out.println("normal:\t\t" + normal);
				System.out.println("thisNormal:\t" + thisNormal);
				System.out.println("thisTangent:\t" + thisTangent);
				System.out.println("otherNormal:\t" + otherNormal);
				System.out.println("otherTangent:\t" + otherTangent);
				System.out.println("--------------------------------");*/

				_velocity = thisTangent.add(otherNormal);
				d._velocity = otherTangent.add(thisNormal);
			}
		}
	}

	public void paint(Graphics g) {
		g.setColor(_color);
		g.fillOval((int)_position.x() - _radius, (int)_position.y() - _radius, _radius * 2, _radius * 2);
	}
}