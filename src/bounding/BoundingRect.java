package bounding;

import math.Vector3f;

public class BoundingRect implements BoundingVolume {

	public Vector3f location;
	public Vector3f dimensions;
	
	public BoundingRect(Vector3f loc, Vector3f dim) {
		location = loc;
		dimensions = dim.divideLocal(2.0f);
	}
	
	public static boolean rectContainsPoint(Vector3f rectLoc, Vector3f rectDim, Vector3f loc) {
		return (loc.x >= rectLoc.x - rectDim.x && loc.x <= rectLoc.x + rectDim.x &&
				loc.y >= rectLoc.y - rectDim.y && loc.y <= rectLoc.y + rectDim.y &&
				loc.z >= rectLoc.z - rectDim.z && loc.z <= rectLoc.z + rectDim.z);
	}
	
	//Assumes that the dim is already divided by 2
	public static boolean rectContainedInRect(Vector3f r1Loc, Vector3f r1Dim, Vector3f r2Loc, Vector3f r2Dim) {		
		return !(r1Loc.x + r1Dim.x < r2Loc.x - r2Dim.x || r2Loc.x + r2Dim.x < r1Loc.x - r1Dim.x ||
				 r1Loc.y + r1Dim.y < r2Loc.y - r2Dim.y || r2Loc.y + r2Dim.y < r1Loc.y - r1Dim.y ||
				 r1Loc.z + r1Dim.z < r2Loc.z - r2Dim.z || r2Loc.z + r2Dim.z < r1Loc.z - r1Dim.z);
	}
	
	//Assumes dim is already divided by 2
	public boolean containedInRect(Vector3f loc, Vector3f dim) {
		return BoundingRect.rectContainedInRect(location, dimensions, loc, dim);
	}
	
	public boolean containsPoint(Vector3f loc) {
		return BoundingRect.rectContainsPoint(location, dimensions, loc);
	}

	@Override
	public boolean containedInVolume(BoundingVolume bounds) {
		if (bounds instanceof BoundingRect) {
			BoundingRect other = (BoundingRect) bounds;
			return containedInRect(other.location, other.dimensions);
		} 
		return false;
	}

	@Override
	public Vector3f getLocation() {
		return location;
	}
	
	public Vector3f getDimensions() {
		return dimensions;
	}
	
}
