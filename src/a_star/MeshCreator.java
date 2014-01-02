package a_star;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import test.core.TestPanel;

/**
 * This class provides methods to create a grid of meshes to 
 * test the pathfinding algorithms. These grids will already set
 * up the graph, by setting adjacent meshes.
 * 
 * @author Nicholas
 *
 */
public class MeshCreator {

	/**
	 * Given a grid of booleans, this method builds the connected graph
	 * where for any grid[x][y] == true, a square mesh will be created.
	 * 
	 * @param grid
	 * @return The grid of AStarMesh connected as a graph structure.
	 */
	public static AStarMesh[][] createMeshes(boolean[][] grid) {
		AStarMesh[][] meshes = new AStarMesh[grid.length][grid[0].length];
		
		//create the meshes
		final int scale = TestPanel.SCALE;
		for (int i = 0;i < grid.length;i ++) {
			for (int k = 0;k < grid[i].length;k ++) {
				if (grid[i][k]) {
					ArrayList<Point> points = new ArrayList<Point>();
					points.add(new Point(i * scale, k * scale));
					points.add(new Point((i + 1) * scale, k * scale));
					points.add(new Point((i + 1) * scale, (k + 1) * scale));
					points.add(new Point(i * scale, (k + 1) * scale));
					meshes[i][k] = new AStarMesh(points);
				}
			}
		}
		
		//connect adjacents
		for (int i = 0;i < grid.length - 1;i ++) {
			for (int k = 0;k < grid[0].length - 1;k ++) {
				if (meshes[i][k] != null) {
					if (grid[i + 1][k]) {
						meshes[i][k].addAdjacent(meshes[i + 1][k]);
						meshes[i + 1][k].addAdjacent(meshes[i][k]);
					}
					
					if (grid[i][k + 1]) {
						meshes[i][k].addAdjacent(meshes[i][k + 1]);
						meshes[i][k + 1].addAdjacent(meshes[i][k]);
					}
				}
			}
		}
		return meshes;
	}
	
	public static boolean[][] testMap1() {
		return new boolean[][] { 
				{false, true , true , true , true , false},
				{false, true , false, false, true , false},
				{false, true , true , true , true , false},
				{false, true , false, false, true , false},
				{false, true , true , true , true , false},
			};
	}
	
	/**
	 * Creates a 200, 100 grid with no holes
	 * 
	 * @return matrix of boolean values
	 */
	public static boolean[][] testMap2() {
		boolean map[][] = new boolean[200][100];
		for (int i = 0;i < map.length;i ++) 
			for (int k = 0;k < map[i].length;k ++) 
				map[i][k] = true;
		
		return map;
	}
	
	/**
	 * Creates a 200, 100 grid with random holes
	 * 
	 * @return matrix of boolean values
	 */
	public static boolean[][] testMap3() {
		boolean map[][] = new boolean[200][100];
		for (int i = 0;i < map.length;i ++) 
			for (int k = 0;k < map[i].length;k ++) 
				map[i][k] = true;
		
		Random r = new Random(12403L);
		for (int i = 0;i < 3000;i ++) {
			int x = r.nextInt(map.length);
			int y = r.nextInt(map[0].length);
			map[x][y] = false;
		}
		
		return map;
	}
}
