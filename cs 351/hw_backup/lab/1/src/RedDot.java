import java.awt.Color;
import java.awt.Graphics;

import edu.uwm.cs351.gfx.AnimationApplet;


public class RedDot extends AnimationApplet {

	private static final long serialVersionUID = 1L;
	int x = 350;
	int y = 175;
	int diameter = 100;
	Color color = Color.red;

	@Override
	public void initialize() {
		// nothing here
	}

	@Override
	public void move() {
		// nothing here
	}

	@Override
	public void paintContents(Graphics g) {
		//set color to use for background
		g.setColor(Color.WHITE);
		//draw filled rectangle to be used as background
		g.fillRect(0, 0, getWidth(), getHeight());
		//set color to use for circle
		g.setColor(color);
		//draw filled circle
		g.fillOval(x, y, diameter, diameter);
	}
}