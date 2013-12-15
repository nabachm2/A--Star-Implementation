package a_star;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;

import test.core.Drawable;

public class Mesh implements Drawable {

	private ArrayList<Point> points;
	private Polygon meshDrawing;
	private boolean selected;
	private Point center;
	
	private Mesh parent;
	private ArrayList<Mesh> borders;
	private double gScore;
	private double hScore;
	private double fScore;
	
	public Mesh(Point point) {
		borders = new ArrayList<Mesh>();
		points = new ArrayList<Point>();
		points.add(point);			
		meshDrawing = new Polygon();
		meshDrawing.addPoint(point.x, point.y);
		recalculateCenter();
	}
	
	private void recalculateCenter() {
		int xc = 0;
		for (int x : meshDrawing.xpoints)
			xc += x;
		
		int yc = 0;
		for (int y : meshDrawing.ypoints)
			yc += y;
		center = new Point(xc / points.size(), yc / points.size());
	}
	
	public void addPoint(Point point) {
		points.add(point);
		meshDrawing.addPoint(point.x, point.y);
		recalculateCenter();
	}
	
	public LinkedList<Mesh> buildPath(LinkedList<Mesh> mesh) {
		mesh.addFirst(this);
		if (parent == null)
			return mesh;
		
		return parent.buildPath(mesh);
	}
	
	@Override
	public void act(float timePassed) {
		int[] x = meshDrawing.xpoints;
		int[] y = meshDrawing.ypoints;
		for (int i = 0;i < points.size();i ++) {
			x[i] = points.get(i).x;
			y[i] = points.get(i).y;
			
		}
		recalculateCenter();
	}

	@Override
	public void draw(Graphics2D g) {
		if (selected)
			g.setColor(Color.YELLOW);
		else
			g.setColor(Color.CYAN);
		g.fillPolygon(meshDrawing);
		g.setColor(Color.BLACK);
		g.drawPolygon(meshDrawing);
	}
	
	public ArrayList<Point> getPoints() {
		return points;
	}

	public Polygon getPolygon() {
		return meshDrawing;
	}
	
	public void addAdjacent(Mesh m2) {
		borders.add(m2);
	}
	
	public void setParent(Mesh p) {
		parent = p;
	}
	
	public Point getCenter() {
		return center;
	}
	
	public double getFScore() {
		return fScore;
	}

	public void setGScore(double gScore) {
		this.gScore = gScore;
		fScore = gScore + hScore;
	}
	
	public double getGScore() {
		return gScore;
	}
	
	public void setHScore(Point end) {
		hScore = center.distance(end);
		fScore = gScore + hScore;
	}

	public ArrayList<Mesh> getAdjacentMeshes() {
		return borders;
	}

	public void setSelected() {
		selected = true;
	}

}
