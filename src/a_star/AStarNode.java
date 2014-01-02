package a_star;

import java.util.LinkedList;

/**
 * This class provides the basic outline for implementing 
 * a graph structure, which can be used by the PathingAlgorithim.java
 * aStarPath() method. By extending this class an providing correct
 * functionality for the methods, this class will serve as a directed
 * graph capable of finding the shortest path between 2 nodes
 * 
 * @author Nicholas
 *
 */
public abstract class AStarNode implements Comparable<AStarNode> {

	//The parent, only used during path finding
	private AStarNode parent;
	
	//Total cost so far, only used during path finding
	private float gScore;
	
	//Heuristic (guess) cost to the final end
	private float hScore;
	
	@Override
	public abstract boolean equals(Object o);
	
	/**
	 * This method must be overridden so that nodes that are equal
	 * will return the same hashcode
	 */
	@Override
	public abstract int hashCode();
	
	/**
	 * Given the destination node, this method returns a "guess" value
	 * of the distance between this node and the end node. This can be
	 * the manhatten distance (s.x - e.x) + (s.y - e.y) + ... 
	 * or the as the bird flies distance, so long as it is consistent. Note
	 * that this doesn't actually improve pathing, but a better implementation makes 
	 * the algorithm run faster
	 * 
	 * @param end the final goal node
	 * @return the guess distance to an end node
	 */
	public abstract float calculateHScore(AStarNode end);
	
	/**
	 * Given another node, this calculates the cost to move
	 * from this node to the given node. This can be done with
	 * computing distances, or by factoring in values from terrain features.
	 * 
	 * @param node the destination node
	 * @return the cost 
	 */
	public abstract float calculateMoveCost(AStarNode node);
	
	/**
	 * Returns the nodes that can be traveled to from this node.
	 * Note: this is basically a directed graph.
	 * 
	 * @return A List of nodes that can be traveled to from this node
	 */
	public abstract Iterable<AStarNode> getAdjacentNodes();
	
	@Override
	public int compareTo(AStarNode node) {
		return (int) Math.signum(getFScore() - node.getFScore());
	}
	
	/**
	 * After the shortest path has been calculated, this
	 * method puts the nodes into a list by traversing through
	 * the parents
	 * 
	 * @param finalPath the list to add the path to.
	 */
	public void buildPath(LinkedList<AStarNode> finalPath) {
		finalPath.addFirst(this);
		if (parent == null) 
			return;
		
		parent.buildPath(finalPath);
	}
	
	/**
	 * Cleans up the path nodes after the pathing algorithm has
	 * been run. This is automatically called by PathingAlgorithm.aStarPath
	 */
	public void clean() {
		parent = null;
		hScore = 0;
		gScore = 0;
	}
	
	/**
	 * @return the fScore value (ie gScore + hScore)
	 */
	public float getFScore() {
		return gScore + hScore;
	}
	
	/**
	 * @return the gScore
	 */
	public float getGScore() {
		return gScore;
	}

	/**
	 * @param gScore the gScore to set
	 */
	public void setGScore(float gScore) {
		this.gScore = gScore;
	}

	/**
	 * @return the hScore
	 */
	public float getHScore() {
		return hScore;
	}

	/**
	 * @param hScore the hScore to set
	 */
	public void setHScore(float hScore) {
		this.hScore = hScore;
	}
	
	/**
	 * Sets the parent and updates the gScore;
	 * 
	 * @param parent the parent to set
	 */
	public void setParent(AStarNode parent) {
		this.parent = parent;
		this.gScore = calculateMoveCost(parent) + parent.getGScore();
	}	
	
}
