package bounding;

import math.Vector3f;


public interface BoundingVolume {

	boolean containedInVolume(BoundingVolume bounds);
	Vector3f getLocation();
	
}
