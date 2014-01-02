package test.core;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import a_star.AStarMesh;
import a_star.MeshCreator;

/**
 * Sets up a basic window with a clickable mesh to find the shortest
 * path, and a bunch of movers and followers. Puts the view
 * inside a scroll panel, since it will be large.
 * 
 * @author Nicholas
 *
 */
public class Main {
	
	public static void main(String[] args) {
		//create our graph
		AStarMesh[][] meshGrid = MeshCreator.createMeshes(MeshCreator.testMap3());
		
		int width = meshGrid.length * TestPanel.SCALE;
		int height = meshGrid[0].length * TestPanel.SCALE;
		
		//create our panel
		TestPanel panel = new TestPanel(meshGrid);
		panel.setPreferredSize(new Dimension(width, height));
		
		//put it in a scroll pane
		JScrollPane scrollPane = new JScrollPane(panel);
		
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		JFrame frame = new JFrame();
		frame.setSize(400, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(scrollPane);
		frame.setVisible(true);
	}
	
}
