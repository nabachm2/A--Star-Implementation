package tree;

import java.util.ArrayList;

import math.Vector3f;

public class PathNode {

	private NavMesh owner;
	private Vector3f center;
	
	public NavMesh parent;
	public float gScore;
	private float hScore;
	
	public PathNode(Vector3f center, Vector3f end, NavMesh owner, NavMesh parent) {
		this.center = center;
		this.parent = parent;
		this.owner = owner;
		
		if (parent != null) {
			hScore = end.distance(center);
			gScore = calculateGScore(this, parent.pathNode);
		}
	}
	
	public ArrayList<NavMesh> buildPath(ArrayList<NavMesh> mesh) {
		mesh.add(owner);
		if (parent == null)
			return mesh;
		
		return parent.pathNode.buildPath(mesh);
	}

	public static float calculateGScore(PathNode node, PathNode parent) {
		return parent.center.distance(node.center) + parent.gScore;
	}
	
	public float getFScore() {
		return gScore + hScore;
	}
	
}
