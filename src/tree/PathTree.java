package tree;
import java.util.ArrayList;

import math.Vector2f;
import math.Vector3f;


public class PathTree {

	private final static int MAX_CAPACITY = 120;//MAX < POWER OF 2 - 1
//	private final static int MAX_CAPACITY = 10000;//MAX < POWER OF 2 - 1
	
	private Vector2f location;
	private float dimensions;
	
	private ArrayList<NavMesh> children;
	private PathTree[] childNodes;
	
	public PathTree(Vector2f loc, float dim) {
		location = loc;
		dimensions = dim;
		children = new ArrayList<NavMesh>(4);
	}
	
	private void initChildNodes() {
		childNodes = new PathTree[4];
		float dP = dimensions / 2.0f;
		childNodes[0] = new PathTree(new Vector2f(location.x - dP / 2, location.y - dP / 2), dP);
		childNodes[1] = new PathTree(new Vector2f(location.x + dP / 2, location.y - dP / 2), dP);
		childNodes[2] = new PathTree(new Vector2f(location.x - dP / 2, location.y + dP / 2), dP);
		childNodes[3] = new PathTree(new Vector2f(location.x + dP / 2, location.y + dP / 2), dP);
		
		for (NavMesh mesh : children)
			add(mesh);
		
		children = null;
	}
	
	public void add(NavMesh mesh) {
		if (childNodes == null) {
			children.add(mesh);
			if (children.size() > MAX_CAPACITY)
				initChildNodes();
		} else  {
			for (PathTree pt : childNodes) 
				if (pt.canContain(mesh))
					pt.add(mesh);
		}
		
	}
	
	public ArrayList<NavMesh>  getMeshForLocation(ArrayList<NavMesh> results, Vector3f loc) {
		if (childNodes == null) {
			for (NavMesh mesh : children) 
				if (mesh.canContain(loc))
					results.add(mesh);
		} else  {
			for (PathTree pt : childNodes) {
				if (pt.canContain(loc)) {
					pt.getMeshForLocation(results, loc);
					return results;
				}
			}
		}
		return results;
	}
	
	public void collectAllEdges(ArrayList<Edge> edges) {
		if (childNodes == null) {
			for (NavMesh mesh : children) 
				mesh.generateEdges(edges);
		} else  {
			for (PathTree pt : childNodes) 
				pt.collectAllEdges(edges);
		}
	}
	
	public boolean canContain(Vector3f loc) {
		float dp = (dimensions + .1f) / 2.0f ;
		return loc.x >= location.x - dp && loc.x <= location.x + dp && 
			   loc.z >= location.y - dp && loc.z <= location.y + dp;
	}
	
	public boolean canContain(NavMesh mesh) {
		Vector3f c = mesh.getCenter();
		Vector3f d = mesh.getDimensions();
		float dp = dimensions / 2.0f;
		return !(c.x + d.x / 2 < location.x - dp || location.x + dp < c.x - d.x / 2 ||
				 c.z + d.z / 2 < location.y - dp || location.y + dp < c.z - d.z / 2);
	}
	
}
