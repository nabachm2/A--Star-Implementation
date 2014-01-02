package a_star.follow_test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Random;

import test.core.Drawable;
import test.core.TestPanel;
import a_star.AStarNode;
import a_star.PathingAlgorithms;
import a_star.example.AStarMesh;

/**
 * This class was created to test the path finding of moving object.
 * A Mover (if it doesn't have a destination) will calculate a path
 * to a random location and move to it. Movers will be "chased" by Followers
 * if they are close enough
 * 
 * @author Nicholas
 *
 */
public class Mover implements Drawable {
	
	//a seeded random generator
	private final static Random random = new Random(1024L);
	
	//a reference to the test window
	private TestPanel mainWindow;
	
	//the mover's current path
	private LinkedList<AStarNode> path;
	//the mover's current path spot
	private int pathIndex;
	//the time between moves
	private float moveTimer;
	
	//the mover's current location
	private Point2D location;
	
	/**
	 * Creates a mover at the given location
	 * 
	 * @param mainWindow a reference to the test window
	 * @param start the starting location
	 */
	public Mover(TestPanel mainWindow, Point2D start) {
		location = new Point2D.Double(start.getX(), start.getY());
		this.mainWindow = mainWindow;
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.fillOval((int) location.getX() - 5, (int) location.getY() - 5, 10, 10);
	}

	/**
	 * Clears the current path that the move is following,
	 * it will recalculate a path on the next call to act()
	 * 
	 */
	public void clearPath() { 
		moveTimer = 0;
		pathIndex = 0;
		path = null;
	}
	
	@Override
	public void act(float timePassed) {	
		if (path != null && path.size() > 1) { //if we have a path...
			final float SPEED = 12;
			moveTimer += timePassed * SPEED; //update the moveTimer
			
			AStarMesh m1 = (AStarMesh) path.get(pathIndex);
			AStarMesh m2 = (AStarMesh) path.get(pathIndex + 1);
			
			//calculate the move direction
			double dirx = -m1.getCenter().getX() + m2.getCenter().getX();
			double diry = -m1.getCenter().getY() + m2.getCenter().getY();
			
			double x = m1.getCenter().getX() + dirx * moveTimer;
			double y = m1.getCenter().getY() + diry * moveTimer;
			location.setLocation(x, y); //update the location by parameterizing the location
			//where t is moveTimer
			
			if (moveTimer > 1) { //we have reached the next node of our path
				moveTimer = 0; 
				location.setLocation(m2.getCenter());
				pathIndex ++;
				if (pathIndex == path.size() - 1) //we finished our path
					clearPath();
			}
			
		} else { //we need to recalculate a new path
			clearPath();
			AStarMesh start = mainWindow.getMeshForPoint(location);
			
			int width = mainWindow.getWidth();
			int height = mainWindow.getHeight(); //calculate random location
			Point2D ep = new Point2D.Double(random.nextInt(width), random.nextInt(height));
			AStarMesh end = mainWindow.getMeshForPoint(ep);
			if (end == null) return;
			
			path = PathingAlgorithms.aStarPath(start, end); //create the path
		}
	}
	
	public Point2D getLocation() {
		return location;
	}
}
