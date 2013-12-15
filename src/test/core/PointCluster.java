package test.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import math.Vector3f;

public class PointCluster implements Drawable {

	private ArrayList<Vector3f> points;
	private boolean connected;
	
	public PointCluster(ArrayList<Vector3f> p, boolean c) {
		points = p;
		connected = c;
	}
	
	@Override
	public void draw(Graphics2D g) {
		for (int i = 0;i < points.size() - 1;i ++) {
			if (i % 2 == 0)
				g.setColor(Color.GRAY);
			else 
				g.setColor(Color.BLUE);
			g.fillOval((int) (points.get(i).x - 2), (int) (points.get(i).z - 2), 4, 4);
			
			if (connected)
				g.drawLine((int) points.get(i).x, (int) points.get(i).z, (int) points.get(i + 1).x, (int) points.get(i + 1).z);
		}
	}

	@Override
	public void act(float timePassed) {
		// TODO Auto-generated method stub
		
	}

}