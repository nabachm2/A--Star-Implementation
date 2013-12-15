package tree;
import java.util.ArrayList;

import math.Vector3f;


public class NavMesh {

	private static int counter = 0;
	
	private Vector3f center;
	private Vector3f dimensions;//Consider dividing by 2 and using that
	public  ArrayList<NavMesh> adjacent;
	
	public PathNode pathNode;
	
	public NavMesh(Vector3f c, Vector3f d) {
		center = c;
		dimensions = d;
		adjacent = new ArrayList<NavMesh>();
	}
	
	public void setAdjacent(NavMesh mesh2) {
		if (this == mesh2 || mesh2 == null)
			return;
		
		if (adjacent.contains(mesh2))
			return;
		
		adjacent.add(mesh2);
		mesh2.adjacent.add(this);
	}
	
	public void getContactPoints(NavMesh mesh2, Vector3f c1, Vector3f c2) {	
		if (center.z - dimensions.z / 2 >= mesh2.center.z - mesh2.dimensions.z / 2) {
			c1.x = Math.min(center.x + dimensions.x / 2, mesh2.center.x + mesh2.dimensions.x / 2);
			c2.x = Math.max(center.x - dimensions.x / 2, mesh2.center.x - mesh2.dimensions.x / 2);
		} else {
			c2.x = Math.min(center.x + dimensions.x / 2, mesh2.center.x + mesh2.dimensions.x / 2);
			c1.x = Math.max(center.x - dimensions.x / 2, mesh2.center.x - mesh2.dimensions.x / 2);
		}
			
		c1.y = Math.min(center.y + dimensions.y / 2, mesh2.center.y + mesh2.dimensions.y / 2);
		c2.y = Math.max(center.y - dimensions.y / 2, mesh2.center.y - mesh2.dimensions.y / 2);
		
		if (center.x - dimensions.x / 2 < mesh2.center.x - mesh2.dimensions.x / 2) {
			c1.z = Math.min(center.z + dimensions.z / 2, mesh2.center.z + mesh2.dimensions.z / 2);
			c2.z = Math.max(center.z - dimensions.z / 2, mesh2.center.z - mesh2.dimensions.z / 2);
		} else {
			c2.z = Math.min(center.z + dimensions.z / 2, mesh2.center.z + mesh2.dimensions.z / 2);
			c1.z = Math.max(center.z - dimensions.z / 2, mesh2.center.z - mesh2.dimensions.z / 2);
		}
		
		float radius = 2;
		if (c1.x == c2.x) {
			radius = (c1.z > c2.z) ? radius : -radius;
			c1.z -= radius;
			c2.z += radius;
		} else {
			radius = (c1.x > c2.x) ? radius : -radius;
			c1.x -= radius;
			c2.x += radius;
		}
	}
	
	public void createPathingNode(Vector3f end, NavMesh parent) {
		pathNode = new PathNode(center, end, this, parent);
	}
	
	public boolean canContain(Vector3f p) {//TODO get max and min from points directly
		return p.x >= center.x - dimensions.x / 2 - .1f && p.x <= center.x + dimensions.x / 2 + .1f &&
			   p.z >= center.z - dimensions.z / 2 - .1f && p.z <= center.z + dimensions.z / 2 + .1f;
	}
	
	public void generateEdges(ArrayList<Edge> edges) {
		int cz = 1;
		if (dimensions.x == 0)
			cz = -1;
		
		Vector3f p1 = new Vector3f(center.x - dimensions.x / 2, center.y - dimensions.y / 2, center.z - dimensions.z / 2);
		Vector3f p2 = new Vector3f(center.x + dimensions.x / 2, center.y - dimensions.y / 2, center.z - cz * dimensions.z / 2);
		Vector3f p3 = new Vector3f(center.x + dimensions.x / 2, center.y + dimensions.y / 2, center.z + dimensions.z / 2);
		Vector3f p4 = new Vector3f(center.x - dimensions.x / 2, center.y + dimensions.y / 2, center.z + cz * dimensions.z / 2);
		
		edges.add(new Edge(p1, p2, this));
		edges.add(new Edge(p2, p3, this));
		edges.add(new Edge(p3, p4, this));
		edges.add(new Edge(p4, p1, this));
	}
	
	public Vector3f getCenter() {
		return center;
	}
	
	public Vector3f getDimensions() {
		return dimensions;
	}
	
}
