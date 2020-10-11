package draw.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPanel;

import draw.controller.handler.ActiveToolHandler;
import draw.controller.handler.DrawerMouseAdapter;
import draw.model.Element;
import draw.model.Model;

public class DrawingPanel extends JPanel {

	/** To keep eclipse happy. */
	private static final long serialVersionUID = 4804450442528721187L;
	
	Model model;
	final static int anchorSize = 4;
	
	DrawerMouseAdapter handler;
	
	public DrawingPanel(Model model) {
		super();
		this.model = model;
	}
	
	public void registerHandler(ActiveToolHandler handler) {
		this.handler = handler;
		addMouseListener(handler);
		addMouseMotionListener(handler);
	}
	
	/** 
	 * Draw Anchors for the given element (if selected). 
	 * 
	 * @param g    Graphics object
	 * @param elt  element whose anchors are drawn
	 */
	public void drawAnchor(Graphics g, Element elt) {
		if (elt.isSelected()) {
			Rectangle[] anchors = elt.getAnchors();
			
			for (int i = 0; i < anchors.length; i++) {
				g.setColor(Color.white);
				g.fillRect(anchors[i].x, anchors[i].y, anchors[i].width,anchors[i].height);
			}
			for (int i = 0; i < anchors.length; i++) {
				g.setColor(Color.black);
				g.drawRect(anchors[i].x, anchors[i].y, anchors[i].width,anchors[i].height);
			}
		}
	}
	
	/** 
	 * Paint all known components and 
	 */
	@Override
	public void paintComponent(Graphics g) { 
		super.paintComponent(g);
		
		if (model == null) { return; }
		
		for (Element elt : model) {
			elt.draw(g);
			
			// anchors for selected object(s) drawn on top of object.
			if (elt.isSelected()) { drawAnchor(g, elt); }
		}
		
		// pass on to controllers who might want to draw extra, should one exist
		if (handler != null) {
			handler.paint(g);
		}
	}
}
