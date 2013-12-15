package test.core;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;



public class TestController implements MouseListener, MouseMotionListener, KeyListener {

	private static TestController controller;
		
	private ArrayList<Drawable> components;
	private ArrayList<Drawable> additions;
	
	private TestManager testManager;
	
	private TestController() {
		components = new ArrayList<Drawable>();
		additions = new ArrayList<Drawable>();
	}
	
	public static TestController getInstance() {
		if (controller == null)
			controller = new TestController();
		
		return controller;
	}
	
	public void addDrawable(Drawable d) {
		additions.add(d);
	}
	
	public void actAll(float timePassed) {
		for (Drawable d : components)
			d.act(timePassed);
		
		components.addAll(additions);
		additions.clear();
	}
	
	public void drawAll(Graphics2D g) {
		for (Drawable d : components)
			d.draw(g);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (testManager != null)
			testManager.handleMouseClick(e.getX(), e.getY(), e.getButton() == MouseEvent.BUTTON1);		
	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) {
		if (testManager != null)
			testManager.handleMouseDown(e.getX(), e.getY(), e.getButton() == MouseEvent.BUTTON1);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (testManager != null)
			testManager.handleMouseUp();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) { 
		if (testManager != null)
			testManager.handleMouseDrag(e.getX(), e.getY());
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (testManager != null)
			testManager.handleKeyPress(e.getKeyCode());
	}
	
	@Override
	public void mouseMoved(MouseEvent e) { }
	
	@Override
	public void keyReleased(KeyEvent e) { }

	@Override
	public void keyTyped(KeyEvent arg0) { }
	
	public void setTestManager(TestManager manager) {
		testManager = manager;
	}

	public ArrayList<Drawable> getDrawables() {
		return components;
	}
	
}
