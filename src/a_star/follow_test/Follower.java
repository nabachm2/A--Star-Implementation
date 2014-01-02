package a_star.follow_test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;

import test.core.Drawable;
import test.core.TestPanel;
import a_star.AStarNode;
import a_star.PathingAlgorithms;
import a_star.example.AStarMesh;


/**
 * This class was created to test the path finding of moving object.
 * A Follower looks for the closest Mover within a given SEARCH_RADIUS
 * and if it finds one, it will follow it by continually updating its path
 * 
 * @author Nicholas
 *
 */
public class Follower implements Drawable {

	//the time between path recalculations
	private final static float UPDATE_TIME = .75f;
	//the max search radius
	private final static float SEARCH_RADIUS = 100 * 100;
	//the max follow radius
	private final static float FOLLOW_RADIUS = 300 * 300;
	
	//reference to the test window
	private TestPanel mainWindow;
	
	//reference to all the movers
	private ArrayList<Mover> movers;
	//the current mover being followed
	private Mover currentMover;
	
	//the followers current path
	private LinkedList<AStarNode> path;
	private int pathIndex;
	private float moveTimer;
	
	private Point2D originalLocation;
	private Point2D location;
	
	private float updateCounter;
	
	/**
	 * Creates a follower at the given start position
	 * 
	 * @param mainWindow reference to the test window
	 * @param start the start location
	 * @param movers a list of all movers
	 */
	public Follower(TestPanel mainWindow, Point2D start, ArrayList<Mover> movers) {
		originalLocation = new Point2D.Double(start.getX(), start.getY());
		this.movers = movers;
		
		location = new Point2D.Double(start.getX(), start.getY());
		this.mainWindow = mainWindow;
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillOval((int) location.getX() - 4, (int) location.getY() - 4, 8, 8);
	}
	
	public void clearPath() {
		pathIndex = 0;
		path = null;
		moveTimer = 0;
	}

	@Override
	public void act(float timePassed) {
		if (currentMover == null) { //we are not following anything...
			for (Mover m : movers)
				if (m.getLocation().distanceSq(location) < SEARCH_RADIUS) //found a mover to follow!
					currentMover = m;
	
			originalLocation.setLocation(location);
		}
		if (location.distanceSq(originalLocation) > FOLLOW_RADIUS) { //the mover is too far away to follow
			currentMover = null;
			clearPath();
		}
		
		updateCounter += timePassed;
		if (currentMover != null && updateCounter > UPDATE_TIME) { //its time to recalculate the path
			updateCounter = 0;
			
			clearPath();
			AStarMesh start = mainWindow.getMeshForPoint(location);
			AStarMesh end = mainWindow.getMeshForPoint(currentMover.getLocation());
			
			path = PathingAlgorithms.aStarPath(start, end);
		}
			
		if (path != null && path.size() > 1) { //we have a path to follow
			final float SPEED = 9;
			moveTimer += timePassed * SPEED;
			
			AStarMesh m1 = (AStarMesh) path.get(pathIndex);
			AStarMesh m2 = (AStarMesh) path.get(pathIndex + 1);
			
			//calculate the direction
			double dirx = -m1.getCenter().getX() + m2.getCenter().getX();
			double diry = -m1.getCenter().getY() + m2.getCenter().getY();
			
			//calculate the paramartized location
			double x = m1.getCenter().getX() + dirx * moveTimer;
			double y = m1.getCenter().getY() + diry * moveTimer;
			location.setLocation(x, y);
			
			if (moveTimer > 1) { //we reached the next node in our path
				moveTimer = 0;
				location.setLocation(m2.getCenter());
				pathIndex ++;
				if (pathIndex == path.size() - 1) 
					clearPath();
			}
			
		}	
	}

}
