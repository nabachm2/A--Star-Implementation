package tree;
import bounding.BoundingRect;
import math.Vector3f;


public class Edge implements Comparable<Edge> {

	private static final float DIM_ERROR = 1f;
	
	public Vector3f slope;
	public BoundingRect bounds;
	
	private NavMesh parent;
	
	public Edge(Vector3f start, Vector3f end, NavMesh parent) {
		float xd = end.x - start.x;
		float yd = end.y - start.y;
		float zd = end.z - start.z;
		Vector3f cLoc = new Vector3f(start.x + xd / 2, start.y + yd / 2, start.z + zd / 2);
		Vector3f dim1 = new Vector3f(Math.max(DIM_ERROR, Math.abs(xd) - DIM_ERROR), Math.abs(yd) + DIM_ERROR, Math.max(DIM_ERROR, Math.abs(zd) - DIM_ERROR));
		bounds = new BoundingRect(cLoc, dim1);
		
		slope = end.subtract(start).normalizeLocal();
		slope.x = Math.abs(slope.x);
		slope.y = Math.abs(slope.y);
		slope.z = Math.abs(slope.z);
		
		this.parent = parent;
	}

	@Override
	public int compareTo(Edge edge) {
		int slopeComp = slope.compareTo(edge.slope);
		if (slopeComp != 0)
			return slopeComp;
		
		if (Math.abs(slope.x) > 0.0f)
			return (int) (bounds.location.z - edge.bounds.location.z); 
		
		if (Math.abs(slope.z) > 0.0f)
			return (int) (bounds.location.x - edge.bounds.location.x); 
		
		return 0;
	}

	public NavMesh getParent() {
		return parent;
	}
	
	
}
