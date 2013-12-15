package a_star;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JOptionPane;

import a_star.follow_test.Follower;
import a_star.follow_test.Mover;
import bounding.BoundingRect;

import math.Vector3f;

import test.core.Drawable;
import test.core.PointCluster;
import test.core.TestController;
import test.core.TestManager;
import test.core.TesterFrame;
import tree.NavMesh;
import tree.PathTreeController;

public class UIManager implements TestManager {

	private Mesh currentMesh;
	private Point currentPoint;
	
	private Point start;
	private Point end;
	
	public UIManager() {
		TestController.getInstance().setTestManager(this);
		MeshCreator.createMeshes(MeshCreator.testMap3());
//		Mover m = new Mover(new Vector3f(4, 0, 4));
//		TestController.getInstance().addDrawable(m);
//		
//		Random r = new Random(2300L);
//		for (int i = 0;i < 20;i ++) {
//			Vector3f l = new Vector3f(r.nextInt(1200), 0, r.nextInt(500));
//			if (!PathTreeController.getInstance().getMeshForPoint(l).isEmpty())
//				TestController.getInstance().addDrawable(new Follower(l, m));
//		}
//		
		new TesterFrame(6 * 200, 6 * 110);
	}
	
	private Point getPoint(int x, int y) {
		for (Drawable d : TestController.getInstance().getDrawables())
			if (d instanceof Mesh) {
				Mesh m = (Mesh) d;
				for (Point p : m.getPoints())
					if (p.distanceSq(x, y) < 10) {
						return p;
					}
			}
		return null;
	}

	@Override
	public void handleMouseClick(int x, int y, boolean left) {
		System.out.println(x + " " + y);
		if (left) {
			Point p = getPoint(x, y);
			if (p == null)
				p = new Point(x, y);
			
			if (currentMesh == null) {
				currentMesh = new Mesh(p);
				TestController.getInstance().addDrawable(currentMesh);
			} else {
				currentMesh.addPoint(p);
			}
		} else  {
			if (start == null) {
				start = new Point(x, y);
			} else {
				end  = new Point(x, y);
			
				
				long startTime = System.nanoTime();
				System.out.println("A:" + PathTreeController.getInstance().getMeshForPoint(new Vector3f(end.x, 0, end.y)).get(0).adjacent.size());
				ArrayList<NavMesh> corridor = PathTreeController.getInstance().createCorridor(new Vector3f(start.x, 0, start.y), new Vector3f(end.x, 0, end.y));
				ArrayList<Vector3f>path = PathTreeController.getInstance().createPath(corridor, new Vector3f(start.x, 0, start.y), new Vector3f(end.x, 0, end.y));
				System.out.println("TOTAL TIME:" + (System.nanoTime() - startTime) / 1000000.0);
				
				ArrayList<Vector3f> contacts = new ArrayList<Vector3f>();
				for (int i = 0;i < corridor.size() - 1;i ++) {
					Vector3f c1 = new Vector3f();
					Vector3f c2 = new Vector3f();
					corridor.get(i).getContactPoints(corridor.get(i + 1), c1, c2);
					contacts.add(c1);
					contacts.add(c2);
				}
				
				TestController.getInstance().addDrawable(new PointCluster(contacts, false));
				TestController.getInstance().addDrawable(new PointCluster(path, true));
				
				
				
				for (NavMesh nm : corridor)
					for (Drawable d : TestController.getInstance().getDrawables())
						if (d instanceof Mesh) {
							Mesh m = (Mesh) d;
								
							Point center = new Point((int) nm.getCenter().x, (int) nm.getCenter().z);
							if (m.getPolygon().contains(center)) {
								m.setSelected();
								break;
							}
						}
			}
		}
	}

	@Override
	public void handleMouseDown(int x, int y, boolean left) { 
		System.out.println(x + " " + y);
		System.out.println(left);
		if (left)
			currentPoint = getPoint(x, y);
	}
	
	@Override
	public void handleMouseUp() { 
		currentPoint = null;
	}

	@Override
	public void handleMouseDrag(int x, int y) {
		if (currentPoint == null)
			return;
		
		currentPoint.setLocation(x, y);
	}
	
	@Override
	public void handleKeyPress(int keyCode) {
		if (keyCode == KeyEvent.VK_SPACE)
			currentMesh = null;
	}
	
	public static void main(String[] args) {
		new UIManager();
	}
	
}
