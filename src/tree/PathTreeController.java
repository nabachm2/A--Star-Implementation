package tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import math.Vector2f;
import math.Vector3f;

public class PathTreeController {

	private static PathTreeController controller;

	private PathTree topNode;

	private PathTreeController() {
		topNode = new PathTree(new Vector2f(0, 0), 16384);
	}

	public static PathTreeController getInstance() {
		if (controller == null)
			controller = new PathTreeController();

		return controller;
	}

	public void add(NavMesh mesh) {
		topNode.add(mesh);
	}

	public ArrayList<NavMesh> getMeshForPoint(Vector3f end) {
		ArrayList<NavMesh> results = new ArrayList<NavMesh>();
		topNode.getMeshForLocation(results, end);
		return results;
	}

	private void cleanUpPathingNodes(HashSet<NavMesh> o, HashSet<NavMesh> c) {
		for (NavMesh m : o)
			m.pathNode = null;

		for (NavMesh m : c)
			m.pathNode = null;
	}

	private float triarea2(Vector3f a, Vector3f b, Vector3f c) {
		float ax = b.x - a.x;
		float ay = b.z - a.z;
		float bx = c.x - a.x;
		float by = c.z - a.z;
		return bx * ay - ax * by;
	}
	
	public ArrayList<Vector3f> createPath(ArrayList<NavMesh> corridor, Vector3f start, Vector3f end) {
	
		//TODO handle start,end in same mesh
		ArrayList<Vector3f> path = new ArrayList<Vector3f>();
		Vector3f portalApex = new Vector3f(start);
		path.add(start);
		
		if (corridor == null || corridor.size() == 1) {
			path.add(end);
			return path;
		}
		
		Vector3f portalLeft = new Vector3f();
		Vector3f portalRight = new Vector3f();
		corridor.get(0).getContactPoints(corridor.get(1), portalLeft, portalRight);
		
		int apexIndex = 0, leftIndex = 0, rightIndex = 0;
		
		Vector3f temp1 = new Vector3f();
		Vector3f temp2 = new Vector3f();
		for (int i = 1;i < corridor.size();i ++)
		{
			if (i == corridor.size() - 1) {
				temp1.set(end);
				temp2.set(end);
			} else {
				corridor.get(i).getContactPoints(corridor.get(i + 1), temp1, temp2);
			}
		         // Update right vertex.
			 if (triarea2(portalApex, portalRight, temp2) <= 0.0f)
		     {
				 if (portalApex.equals(portalRight) || triarea2(portalApex, portalLeft, temp2) > 0.0f)
		         {
		              // Tighten the funnel.
		             portalRight.set(temp2);
		             rightIndex = i;
		         }
		         else
		         {
		                 // Right over left, insert left to path and restart scan from portal left point.
		             path.add(new Vector3f(portalLeft));
		                 // Make current left the new apex.
		             portalApex.set(portalLeft);
		             apexIndex = leftIndex;
		                 // Reset portal
		             portalLeft.set(portalApex);
		             portalRight.set(portalApex);
		             leftIndex = apexIndex;
		             rightIndex = apexIndex;
		                 // Restart scan
		             i = apexIndex;
		             continue;
		         }
		     }

		         // Update left vertex.
		     if (triarea2(portalApex, portalLeft, temp1) >= 0.0f)
		     {
		         if (portalApex.equals(portalLeft) || triarea2(portalApex, portalRight, temp1) < 0.0f)
		         {
		                 // Tighten the funnel.
		             portalLeft.set(temp1);
		             leftIndex = i;
		         }
		         else
		         {
		              // Left over right, insert right to path and restart scan from portal right point.
		             path.add(new Vector3f(portalRight));
		                 // Make current right the new apex.
		             portalApex.set(portalRight);
		             apexIndex = rightIndex;
		                 // Reset portal
		             portalLeft.set(portalApex);
		             portalRight.set(portalApex);
		             leftIndex = apexIndex;
		             rightIndex = apexIndex;
		                 // Restart scan
		             i = apexIndex;
		             continue;
		         }
		    }
		}
		path.add(end);
		return path;
	}

	public ArrayList<NavMesh> createCorridor(Vector3f start, Vector3f end) {
		ArrayList<NavMesh> results = new ArrayList<NavMesh>();
		NavMesh startMesh = topNode.getMeshForLocation(results, start).get(0);
		results.clear();
		NavMesh goalMesh = topNode.getMeshForLocation(results, end).get(0);
		
		HashSet<NavMesh> closedSet = new HashSet<NavMesh>();
		HashSet<NavMesh> openSet = new HashSet<NavMesh>();
		openSet.add(startMesh);

		startMesh.createPathingNode(end, null);

		while (!openSet.isEmpty()) {
			NavMesh current = null;
			for (NavMesh m : openSet)
				if (current == null || current.pathNode.getFScore() > m.pathNode.getFScore())
					current = m;

			openSet.remove(current);
			closedSet.add(current);
			if (current == goalMesh) {
				ArrayList<NavMesh> finalPath = new ArrayList<NavMesh>();
				goalMesh.pathNode.buildPath(finalPath);
				Collections.reverse(finalPath);
				cleanUpPathingNodes(openSet, closedSet);
				return finalPath;
			}

			for (NavMesh m : current.adjacent) {
				if (closedSet.contains(m))
					continue;
				if (!openSet.contains(m)) {
					openSet.add(m);
					m.createPathingNode(end, current);
				} else {
					float oldGScore = m.pathNode.gScore;
					float newGScore = PathNode.calculateGScore(m.pathNode, current.pathNode);
					if (oldGScore > newGScore) {
						m.pathNode.parent = current;
						m.pathNode.gScore = newGScore;
					}
				}
			}
		}

		cleanUpPathingNodes(openSet, closedSet);
		return null;
	}

}
