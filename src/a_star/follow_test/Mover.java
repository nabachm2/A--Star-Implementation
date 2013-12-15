package a_star.follow_test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import math.Vector3f;

import test.core.Drawable;
import tree.NavMesh;
import tree.PathTreeController;

public class Mover implements Drawable {

	//private static final Random r = new Random(12403L);
	private static final Random r = new Random(1204L);
	
	private ArrayList<Vector3f> path;
	private int pathIndex;
	
	private Vector3f location;
	private Vector3f direction;
	
	public Mover(Vector3f start) {
		Random r = new Random(12403L);
		Vector3f end = new Vector3f(r.nextInt(190 * 6) + 2, 0, r.nextInt(90 * 6) + 2);
		ArrayList<NavMesh> c = PathTreeController.getInstance().createCorridor(start, end);
		path = PathTreeController.getInstance().createPath(c, start, end);
		
		location = new Vector3f(start);
		direction = path.get(1).subtract(path.get(0)).normalizeLocal();
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillOval((int) location.x - 3, (int) location.z - 3, 6, 6);
		
		g.setColor(Color.BLACK);
		g.fillOval((int) path.get(path.size() - 1).x - 6, (int) path.get(path.size() - 1).z - 6, 12, 12);
		
		Vector3f start = null;
		for (Vector3f p : path) {
			if (start != null)
				g.drawLine((int) start.x, (int) start.z, (int) p.x, (int) p.z);
			
			start = p;
		}
	}

	@Override
	public void act(float timePassed) {
		final float SPEED = 90;
		location.x += direction.x * timePassed * SPEED;
		location.y += direction.y * timePassed * SPEED;
		location.z += direction.z * timePassed * SPEED;
		
		if (location.distanceSquared(path.get(pathIndex + 1)) < 7) {
			location.set(path.get(pathIndex + 1));
			pathIndex ++;
			if (pathIndex == path.size() - 1) {
				pathIndex = 0;
				path.clear();
				do {
					try {
						System.out.println("FINDING MOVER PATH");
						Vector3f end = new Vector3f(r.nextInt(190 * 6) + 2, 0, r.nextInt(90 * 6) + 2);
						ArrayList<NavMesh> c = PathTreeController.getInstance().createCorridor(location, end);
						path = PathTreeController.getInstance().createPath(c, new Vector3f(location), end);
					} catch (IndexOutOfBoundsException ex) { }
				} while (path.size() == 0);
			}
			
			direction = path.get(pathIndex + 1).subtract(path.get(pathIndex)).normalizeLocal();
		}	
	}
	
	public Vector3f getLocation() {
		return location;
	}
}
