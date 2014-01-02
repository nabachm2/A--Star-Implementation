package a_star;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * This class implements the A star pathing algorithm,
 * see the following links for more details
 * http://www.gamedev.net/page/resources/_/technical/artificial-intelligence/a-pathfinding-for-beginners-r2003
 * http://en.wikipedia.org/wiki/A*_search_algorithm
 * 
 * @author Nicholas
 *
 */
public class PathingAlgorithms {

	/**
	 * Scrubs any pathing data that might have been changed during the 
	 * algorithms last run through
	 * 
	 * @param closedSet
	 * @param openSet
	 */
	private static void cleanupPathingNodes(Iterable<AStarNode> closedSet, 
			Iterable<AStarNode> openSet) {
		for (AStarNode n : closedSet)
			n.clean();
			
		for (AStarNode n : openSet)
			n.clean();
	}
	
	/**
	 * This implements the A* pathing algorithm to find the shortest
	 * path between 2 nodes. If no path exists, this method returns null.
	 * 
	 * The general overview for the algorithm is as follows:
	 * there is an open set of nodes that still are available for processing, 
	 * and a closed set for nodes that have been processed and discarded. 
	 * Then the algorithm uses the following steps
	 * 
	 * 1, Pop off the node with lowest f score from openset (current), add to closedset
	 * 2. If current == end, terminate
	 * 3. Else iterate through current's neighbors and if the are not in
	 *    the closed or open set add to openset, set current as parent
	 *    if already in openset, if current to neighbor is closer, set parent of neighbor to current
	 * 4. Repeat!
	 *  
	 * Currently the implementation uses a hashset for the closedset (optimal)
	 * and a hashset for the open set (not optimal). Ideally the open set
	 * is a priority queue, however, the queue also needs to be able to 
	 * handle value updates when the value is already in the queue, which 
	 * no data structure provided by the Java standard framework provides.
	 * In c++ Boosts::multi_array would be a good fit...
	 * 
	 * @param start the start node
	 * @param end the end node
	 * @return a LinkedList of nodes with the final path
	 */
	public static LinkedList<AStarNode> aStarPath(AStarNode start, AStarNode end) {
		if (start == null || end == null)
			return null;
		
		HashSet<AStarNode> closedSet = new HashSet<AStarNode>();
		HashSet<AStarNode> openSet = new HashSet<AStarNode>();
		openSet.add(start); //add start to openset
		start.setHScore(start.calculateHScore(end));

		while (!openSet.isEmpty()) { //while there are still nodes to check
			AStarNode current = null;
			for (AStarNode m : openSet) //find the node with the lowest fscore
				if (current == null || current.getFScore() > m.getFScore())
					current = m;
			
			openSet.remove(current); //remove the node from open set
			closedSet.add(current); //add to close set
			if (current.equals(end)) { //we have found our path!
				LinkedList<AStarNode> finalPath = new LinkedList<AStarNode>();
				end.buildPath(finalPath); //build the path using the parents
				cleanupPathingNodes(closedSet, openSet); //cleanup all the values
				return finalPath;
			}

			for (AStarNode m : current.getAdjacentNodes()) { //iterate through neighbors 
				if (closedSet.contains(m))
					continue;
				if (!openSet.contains(m)) { //node isn't in openset, add it
					m.setHScore(m.calculateHScore(end));
					openSet.add(m);
					m.setParent(current);
				} else { //node is already in openset, see if this is better path
					float oldGScore = m.getGScore();
					float newGScore = m.calculateMoveCost(current) + current.getGScore();
					if (oldGScore > newGScore) //found better path! update parent
						m.setParent(current);
				}
			}
		}
		cleanupPathingNodes(closedSet, openSet); //no path found :(
		return null; 
	}
	
}
