A--Star-Implementation
======================

This repo presents a functional, adaptable implementation of the A* Algorithm for java. Commonly this is used for path finding and graph traversal, but it has a few other uses. A good overview can be found here:
http://en.wikipedia.org/wiki/A*_search_algorithm

A more detailed explanation of the classes can be found in the doxygen, however relevant code can be found in
PathingAlgorithm.java and AStarNode.java.

To use the A* Algorithm implemented, a graph node (or any class) needs to extend AStarNode and implement the required methods. Then a simple call to PathingAlgorithm.aStarPath(start, end) will return the shortest path in optimal time.

Further to show example usage, I have included an implementation of AStarNode using meshes that are connected, much like what is used in 3D environment. The example jar includes sprites that move to random location, and are "chased" by followers, using the best path (found with A*).
