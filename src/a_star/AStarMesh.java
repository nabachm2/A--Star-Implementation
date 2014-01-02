package a_star;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * An example of a mesh based graph, which implements AStarNode,
 * and all the abstract methods. This class is more of an example
 * of how to implement AStarNode.java for practical purposes
 * 
 * 
 * @author Nicholas
 *
 */
public class AStarMesh extends AStarNode {

	//A list of all the adjacent nodes
	private ArrayList<AStarNode> adjacents;
	
	//The points that define the bounds of the mesh
	private ArrayList<Point> points;
	
	//The center of the mesh
	private Point center;
	
	//Used for drawing the mesh to the screen, for test purposes
	private Polygon drawMesh;
	private boolean selected;
	
	/**
	 * Creates a mesh from the given points/vertices.
	 * 
	 * @param points An array of vertices
	 */
	public AStarMesh(ArrayList<Point> points) {
		adjacents = new ArrayList<AStarNode>();
		this.points = points;
		recalculateCenter();
		
		drawMesh = new Polygon();
		for (Point pt : points)
			drawMesh.addPoint(pt.x, pt.y);
	}
	
	/**
	 * Recalculates the center point, using the average
	 * of all the vertices
	 * 
	 */
	private void recalculateCenter() {
		int xc = 0, yc = 0;
		for (Point p : points) {
			xc += p.x;
			yc += p.y;
		}
			
		center = new Point(xc / points.size(), yc / points.size());
	}
	
	/**
	 * Add the point to the list of vertices
	 * 
	 * @param point
	 */
	public void addPoint(Point point) {
		points.add(point);
		drawMesh.addPoint(point.x, point.y);
		recalculateCenter();
	}
	
	public void draw(Graphics g) {
		g.setColor(selected ? Color.YELLOW : Color.WHITE);		
		g.fillPolygon(drawMesh);
		g.setColor(Color.BLACK);
		g.drawPolygon(drawMesh);
	}
	
	public void addAdjacent(AStarMesh mesh) {
		if (adjacents.contains(mesh))
			return;
		
		adjacents.add(mesh);
	}
	
	@Override
	public boolean equals(Object o) {
		return ((AStarMesh) o).center.equals(center);
	}

	@Override
	public int hashCode() {
		return center.hashCode();
	}
	
	@Override
	public float calculateMoveCost(AStarNode node) {
		if (!(node instanceof AStarMesh)) {
			System.out.println("INVALID VALUE: CALCULATE DISTANCE SQUARED");
			return -1;
		}
		
		//just uses the distance between centers
		return (float) center.distance(((AStarMesh) node).center);
	}

	@Override
	public ArrayList<AStarNode> getAdjacentNodes() {
		return adjacents;
	}

	@Override
	public float calculateHScore(AStarNode end) {
		return calculateMoveCost(end); //just uses the as-the-bird flies distance
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public Point2D getCenter() {
		return center;
	}

}
