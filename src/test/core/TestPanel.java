package test.core;


import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;

import a_star.AStarMesh;
import a_star.AStarNode;
import a_star.PathingAlgorithms;
import a_star.follow_test.Follower;
import a_star.follow_test.Mover;


/**
 * Main thread for the test view. Controls the general loop (act, render, clear, repeat) as
 * well as interprets user mouse clicks. 
 * @author Nicholas
 *
 */
public class TestPanel extends JPanel implements MouseListener, Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6628952032993632318L;

	//The scale for the grid space
	public static final int SCALE = 12;
	
	//A reference to the graph structure
	private AStarMesh[][] meshGrid;
	//A prerendered image of the meshes, to increase speed
	private BufferedImage meshImage;
	
	//A list of all our drawables (ie followers and movers)
	private CopyOnWriteArrayList<Drawable> drawables;
	
	//The selected mesh
	private AStarMesh selectedMesh;
	
	/**
	 * Creates a JPanel with the given graph
	 * structure
	 * 
	 * @param meshGrid
	 */
	public TestPanel(AStarMesh[][] meshGrid) {
		this.meshGrid = meshGrid;
		drawables = new CopyOnWriteArrayList<Drawable>();
		createMeshImage();
		
		addMouseListener(this);
		
		//set up at most 10 movers
		ArrayList<Mover> movers = new ArrayList<Mover>();
		for (int i = 0;i < 10;i ++) {
			int x = (int) (Math.random() * meshGrid.length * SCALE);
			int y = (int) (Math.random() * meshGrid[0].length * SCALE);
			if (this.getMeshForPoint(new Point(x, y)) == null) continue; 
			
			Mover m = new Mover(this, new Point(x, y));
			movers.add(m);
			drawables.add(m);
		}
		
		//set up at most 30 followers
		for (int i = 0;i < 30;i ++) {
			int x = (int) (Math.random() * meshGrid.length * SCALE);
			int y = (int) (Math.random() * meshGrid[0].length * SCALE);
			if (this.getMeshForPoint(new Point(x, y)) == null) continue; 
			
			drawables.add(new Follower(this, new Point(x, y), movers));
		}
		
		//start the game thread
		new Thread(this).start();
	}
	
	/**
	 * Recreates the prerendered mesh image, to reflect
	 * the final path found, or clear the path
	 */
	private void createMeshImage() {
		int width = meshGrid.length * SCALE;
		int height = meshGrid[0].length * SCALE;
		
		meshImage = new BufferedImage(width, height,
		                    BufferedImage.TYPE_INT_ARGB);
		
		Graphics g = meshImage.createGraphics();
		for (AStarMesh[] row : meshGrid) {
			for (AStarMesh mesh : row) {
				if (mesh == null)
					continue;
				
				mesh.draw(g);
			}
		}
	}
	
	/**
	 * Given a mouse click point, this method finds the 
	 * mesh that the click point falls in. Returns
	 * null if out of bounds, or no mesh exists
	 * 
	 * @param point the point within the jpanel
	 * @return AStarMesh for that is at that point
	 */
	public AStarMesh getMeshForPoint(Point2D point) { 
		int x = (int) point.getX() / SCALE;
		int y = (int) point.getY() / SCALE;
		if (x < 0 || y < 0 || x >= meshGrid.length || y >= meshGrid[0].length)
			return null;
		
		return meshGrid[x][y];
	}
	
	@Override
	public void paintComponent(Graphics g) {
		//clear back
		g.fillRect(0, 0, meshImage.getWidth(), meshImage.getHeight()); 
		//draw our back image
		g.drawImage(meshImage, 0, 0, meshImage.getWidth(), meshImage.getHeight(), null);
		
		//draw all drawables (ie movers/followers)
		for (Drawable draw : drawables)
			draw.draw(g);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) { }

	@Override
	public void mouseEntered(MouseEvent arg0) { }

	@Override
	public void mouseExited(MouseEvent arg0) { }

	@Override
	public void mousePressed(MouseEvent arg0) {
		AStarMesh sm = getMeshForPoint(arg0.getPoint());
		if (sm == null) return;
		
		if (selectedMesh == null) { //we haven't selected a mesh yet...
			selectedMesh = sm;
			selectedMesh.setSelected(true);
		} else { //we have a selected a mesh
			for (AStarMesh[] row : meshGrid) { //clear all spots
				for (AStarMesh mesh : row) {
					if (mesh == null)
						continue;
					
					mesh.setSelected(false);
				}
			}
			
			LinkedList<AStarNode> path = PathingAlgorithms.aStarPath(selectedMesh, sm);
			for (AStarNode node : path)
				((AStarMesh) node).setSelected(true); //color all the meshes in our final path
				
			selectedMesh = null;
		}
		createMeshImage();
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) { }

	@Override
	public void run() {
		float lastRenderTime = 0.00001f;
		while (true) {
			long startTime = System.nanoTime();
			for (Drawable d : drawables) //act all drawables
				d.act(lastRenderTime);
			
			repaint(); //redraw
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long endTime = System.nanoTime();	
			lastRenderTime = (float) ((endTime - startTime) / 1.0E9); //calculate the total time
		}
	}
	
	@Override
	public int getWidth() { 
		return SCALE * meshGrid.length;
	}
	
	@Override
	public int getHeight() { 
		return SCALE * meshGrid[0].length;
	}
	
}
