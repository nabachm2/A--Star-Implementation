package test.core;


import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * Main thread for the game. Controls the general loop (act, render, clear, repeat) as
 * well as interprets user keystrokes. 
 * @author Nicholas
 *
 */
public class TesterFrame extends JFrame {

	private TesterCanvas canvas;
	
	public TesterFrame(int w, int h) {
		setVisible(true);
		setSize(w, h);
		setLayout(null);
		canvas = new TesterCanvas(w, h);
		add(canvas).setBounds(0, 0, w, h);
		canvas.init();
	}
	
	private class TesterCanvas extends Canvas implements Runnable {
	
		private Dimension dimensions;
		
		private BufferStrategy strategy;
	    private BufferedImage buffer;
	    private Graphics2D backgroundGraphics;
	    private Graphics2D graphics;
		
		/**
		 * Constructor for GameEngine which set the dimensions and calls init()
		 * @param width
		 * 		width (in pixels) of the game
		 * @param height
		 * 		height (in pixels) of the game
		 * @param r
		 * 		Reference to the GameRender which is attached to the main UI
		 */
		public TesterCanvas(int width, int height) {
			super(GraphicsEnvironment.getLocalGraphicsEnvironment()
	                        .getDefaultScreenDevice()
	                        .getDefaultConfiguration());		
			dimensions = new Dimension(width, height);
		}
		
		private Graphics2D getBuffer() {
			if (graphics == null) {
				try {
					graphics = (Graphics2D) strategy.getDrawGraphics();
				} catch (IllegalStateException e) {
					return null;
				}
			}
			return graphics;
		}
	
		private boolean updateScreen() {
			graphics.dispose();
			graphics = null;
			try {
				strategy.show();
				Toolkit.getDefaultToolkit().sync();
				return (!strategy.contentsLost());
	
			} catch (NullPointerException e) {
				return true;
	
			} catch (IllegalStateException e) {
				return true;
			}
		}
		
		/**
		 * Inits core components of the game
		 */
		public void init() {
			GraphicsConfiguration config = GraphicsEnvironment.getLocalGraphicsEnvironment()
											.getDefaultScreenDevice()
											.getDefaultConfiguration();
			buffer = config.createCompatibleImage(dimensions.width, dimensions.height, Transparency.TRANSLUCENT);
			
			createBufferStrategy(2);
	        do {
	        	strategy = getBufferStrategy();
	        } while (strategy == null);
			
			new Thread(this).start();
			this.addMouseListener(TestController.getInstance());
			this.addKeyListener(TestController.getInstance());
			this.addMouseMotionListener(TestController.getInstance());
		}
		
		@Override
		public void run() {
			backgroundGraphics = (Graphics2D) buffer.getGraphics();
			long startTime = System.currentTimeMillis();
			while (true) {//TODO change...
				
				long endTime = System.currentTimeMillis();
				float timePassed = (endTime - startTime) / 1000.0f;
				TestController.getInstance().actAll(timePassed);
	
				do {
					Graphics2D bg = getBuffer();
					
					backgroundGraphics.setColor(Color.WHITE);
					backgroundGraphics.fillRect(0, 0, dimensions.width, dimensions.height);
					TestController.getInstance().drawAll(backgroundGraphics);
				
					bg.drawImage(buffer, 0, 0, null);
					
					bg.dispose();
				} while (!updateScreen());
				
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) { }
				
				startTime = endTime; 
			}
		}
	
	}
	
}
