package test.core;

public interface TestManager {

	void handleMouseClick(int x, int y, boolean left);
	void handleMouseDown(int x, int y, boolean left);
	void handleMouseUp();
	void handleMouseDrag(int x, int y);
	void handleKeyPress(int keyCode);
	
}
