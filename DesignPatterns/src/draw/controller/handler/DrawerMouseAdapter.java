package draw.controller.handler;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;

import draw.view.DrawingPanel;

/**
 * GUI Handlers are able to draw into the graphics after all elements have been redrawn. 
 * 
 * This DrawerMouseAdapter is able to both react to mouse events and then provide the ability
 * to perform special drawing into the available Graphics context.
 */
public class DrawerMouseAdapter extends MouseAdapter {
	
	/**
	 * Invoked from within {@link DrawingPanel} after all Element objects have been drawn.
	 * 
	 * @param g   Graphics object into which to draw
	 */
	public void paint(Graphics g) {
		// override as needed.
	}
}
