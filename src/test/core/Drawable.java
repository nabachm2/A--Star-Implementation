package test.core;
import java.awt.Graphics;

/**
 * Basic interface for drawing/acting test object
 * 
 * @author Nicholas
 *
 */
public interface Drawable {

	/**
	 * Draws the object to the given graphics
	 * 
	 * @param g
	 */
	void draw(Graphics g);
	
	/**
	 * Acts the object, using the amount of timePassed
	 * as a reference
	 * 
	 * @param timePassed
	 */
	void act(float timePassed);
	
}
