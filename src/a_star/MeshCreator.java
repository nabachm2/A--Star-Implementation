package a_star;

import java.awt.Point;
import java.util.Random;

import math.Vector3f;

import test.core.TestController;
import tree.NavMesh;
import tree.PathTreeController;

public class MeshCreator {

	public static void createMeshes(boolean[][] mesh) {
		NavMesh[][] meshes = new NavMesh[mesh.length][mesh[0].length];
		long count = 0;
		int scale = 8;
		for (int i = 0;i < mesh.length;i ++) {
			for (int k = 0;k < mesh[i].length;k ++) {
				if (!mesh[i][k])
					continue;
					
				int cd = 1;
				boolean change = true;
				while (change) {
					cd ++;
					try {
						for (int x = i;x < i + cd;x ++)
							for (int y = k;y < k + cd;y ++)
								if (!mesh[x][y]) {
									cd --;
									change = false;
								}
					} catch (ArrayIndexOutOfBoundsException ex) { 
						cd --;
						change = false;
					}
				}
				
				count++;
				Mesh m = new Mesh(new Point(k * scale, i * scale));
				m.addPoint(new Point((k + cd) * scale, i * scale));
				m.addPoint(new Point((k + cd) * scale, (i + cd) * scale));
				m.addPoint(new Point(k * scale, (i + cd) * scale));
				TestController.getInstance().addDrawable(m);
				NavMesh nm = new NavMesh(new Vector3f((k + cd / 2.0f) * scale, 0, (i + cd / 2.0f) * scale), new Vector3f(cd * scale, 0, cd * scale));
				PathTreeController.getInstance().add(nm);

				
				for (int x = i;x < i + cd;x ++)
					for (int y = k;y < k + cd;y ++) {
						mesh[x][y] = false;
						meshes[x][y] = nm;
					}
				
				
			}
		}
		System.out.println(count);	
		
		for (int i = 0;i < mesh.length;i ++) {
			for (int k = 0;k < mesh[i].length;k ++) {
				if (meshes[i][k] != null) {
					if (i + 1 < mesh.length)
						meshes[i][k].setAdjacent(meshes[i + 1][k]);
					
					if (k + 1 < mesh[i].length)
						meshes[i][k].setAdjacent(meshes[i][k + 1]);
				}
			}
		}
		
		
//		for (int i = 0;i < mesh.length;i ++) {
//			for (int k = 0;k < mesh[i].length;k ++) {
//				if (mesh[i][k]) {
//					count ++;
//					Mesh m = new Mesh(new Point(k * scale, i * scale));
//					m.addPoint(new Point((k + 1) * scale, i * scale));
//					m.addPoint(new Point((k + 1) * scale, (i + 1) * scale));
//					m.addPoint(new Point(k * scale, (i + 1) * scale));
//					TestController.getInstance().addDrawable(m);
//					Vector3f[] points = { new Vector3f(k * scale, 0, i * scale),
//										  new Vector3f((k + 1) * scale, 0, i * scale),
//										  new Vector3f((k + 1) * scale, 0, (i + 1) * scale),
//										  new Vector3f(k * scale, 0, (i + 1) * scale)
//					};
//					PathTreeController.getInstance().add(new NavMesh(points));
//				}
//			}
//		}
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
	
	public static boolean[][] testMap2() {
		boolean map[][] = new boolean[100][200];
		for (int i = 0;i < map.length;i ++) 
			for (int k = 0;k < map[i].length;k ++) 
				map[i][k] = true;
		
		return map;
	}
	
	public static boolean[][] testMap3() {
		boolean map[][] = new boolean[100][200];
		for (int i = 0;i < map.length;i ++) 
			for (int k = 0;k < map[i].length;k ++) 
				map[i][k] = true;
		
		Random r = new Random(12403L);
		for (int i = 0;i < 3000;i ++) {
			int x = r.nextInt(100);
			int y = r.nextInt(200);
			map[x][y] = false;
		}
		
		return map;
	}
}
