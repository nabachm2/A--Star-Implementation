package a_star.follow_test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import math.Vector3f;

import test.core.Drawable;
import tree.NavMesh;
import tree.PathTreeController;

public class Follower implements Drawable {

	private final static float UPDATE_TIME = .75f;
	private final static float SEARCH_RADIUS = 100 * 100;
	private final static float FOLLOW_RADIUS = 300 * 300;
	
	private static final Random s = new Random(6204L);
	
	private Mover mover;
	private boolean follower;
	
	private ArrayList<Vector3f> path;
	private int pathIndex;
	
	private Vector3f originalLocation;
	private Vector3f location;
	private Vector3f direction;
	
	private float updateCounter;
	private Random r;
	
	public Follower(Vector3f start, Mover m) {
		r = new Random(s.nextLong());
		originalLocation = new Vector3f(start);
		location = start;
		mover = m;
		do {
			try {
				Vector3f end = new Vector3f(r.nextInt(190 * 6) + 2, 0, r.nextInt(90 * 6) + 2);
				ArrayList<NavMesh> c = PathTreeController.getInstance().createCorridor(location, end);
				path = PathTreeController.getInstance().createPath(c, new Vector3f(location), end);
			} catch (IndexOutOfBoundsException ex) { ex.printStackTrace();}
		} while (path == null || path.size() == 0);
		
		location = new Vector3f(start);
		direction = path.get(1).subtract(path.get(0)).normalizeLocal();
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.ORANGE);
		g.fillOval((int) location.x - 2, (int) location.z - 2, 4, 4);
	}

	@Override
	public void act(float timePassed) {
		final float SPEED = 60;
		location.x += direction.x * timePassed * SPEED;
		location.y += direction.y * timePassed * SPEED;
		location.z += direction.z * timePassed * SPEED;
		
		if (!follower && location.distanceSquared(mover.getLocation()) < SEARCH_RADIUS) {
			follower = true;
			originalLocation.set(location);
		}
		if (location.distanceSquared(originalLocation) > FOLLOW_RADIUS)
			follower = false;
		
		updateCounter += timePassed;
		if (follower && updateCounter > UPDATE_TIME) {
			updateCounter = 0;
			pathIndex = 0;
			path.clear();
			do {
				try {
					Vector3f end = new Vector3f(mover.getLocation());
					ArrayList<NavMesh> c = PathTreeController.getInstance().createCorridor(location, end);
					path = PathTreeController.getInstance().createPath(c, new Vector3f(location), end);
				} catch (IndexOutOfBoundsException ex) { ex.printStackTrace();}
			} while (path.size() == 0);
	
			direction = path.get(pathIndex + 1).subtract(path.get(pathIndex)).normalizeLocal();
		}
			
		
		if (location.distanceSquared(path.get(pathIndex + 1)) < 4) {
			location.set(path.get(pathIndex + 1));
			pathIndex ++;
			if (pathIndex == path.size() - 1) {			
				pathIndex = 0;
				path.clear();
				do {
					try {
						Vector3f end;
						if (follower) {
							end = new Vector3f(mover.getLocation());
						} else {
							end = new Vector3f(r.nextInt(190 * 6) + 2, 0, r.nextInt(90 * 6) + 2);
						}
						
						ArrayList<NavMesh> c = PathTreeController.getInstance().createCorridor(location, end);
						path = PathTreeController.getInstance().createPath(c, new Vector3f(location), end);
					} catch (IndexOutOfBoundsException ex) { }
				} while (path.size() == 0);
			}
			direction = path.get(pathIndex + 1).subtract(path.get(pathIndex)).normalizeLocal();
		}	
	}

}
