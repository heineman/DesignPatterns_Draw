package draw.controller.handler;

import java.awt.Cursor;
import java.awt.Point;
import java.util.Optional;

import draw.model.Element;
import draw.model.Model;
import draw.palette.Action;
import draw.view.DrawingPalette;

/**
 * React to anchor points on MouseMotion with changing cursor; thereafter, handle dragging behavior.
 * 
 * Chain of responsibility
 * 
 * 1. Motion only (handle cursor changes)
 * 2. Once Pressed
 *      2a. If in empty space, launches select capability
 *      2b. If on an anchor point, launches resize of selected group
 *      2c. If on an element, selects element (and even entire group that contains element
 */
public class ChangeCursorHandler extends Handler  {
	
	Cursor crossHair = new Cursor(Cursor.CROSSHAIR_CURSOR);
	Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	Cursor moveCursor = new Cursor(Cursor.MOVE_CURSOR);
	Cursor nw = new Cursor(Cursor.NW_RESIZE_CURSOR);
	Cursor ne = new Cursor(Cursor.NE_RESIZE_CURSOR);
	Cursor se = new Cursor(Cursor.SE_RESIZE_CURSOR);
	Cursor sw = new Cursor(Cursor.SW_RESIZE_CURSOR);
	
	public ChangeCursorHandler(Model model, DrawingPalette view) {
		super(model, view);
	}
	
	/** Available for anything... */
	@Override
	protected boolean interested(Action action) {
		return true;
	}
	
	@Override
	public boolean start(Point pt, int modifiers, Optional<Element> elt) {
		if (!elt.isPresent()) {
			view.drawingPanel().setCursor(crossHair);
		}
		
		return super.start(pt, modifiers, elt);
	}	
	
	@Override 
	public boolean move(Point pt, int modifiers, Optional<Element> element, int anchor) {
		if (!element.isPresent()) {
			view.drawingPanel().setCursor(defaultCursor);
		} else {
			if (anchor == Cursor.NW_RESIZE_CURSOR) {
				view.drawingPanel().setCursor(nw);
			} else if (anchor == Cursor.NE_RESIZE_CURSOR) {
				view.drawingPanel().setCursor(ne);
			} else if (anchor == Cursor.SE_RESIZE_CURSOR) {
				view.drawingPanel().setCursor(se);
			} else if (anchor == Cursor.SW_RESIZE_CURSOR) {
				view.drawingPanel().setCursor(sw);
			} 
		}
		
		return super.move(pt, modifiers, element, anchor);
	}
	
	@Override
	public boolean startAnchor(Point start, int modifiers, Element elt, int result) {
		view.drawingPanel().setCursor(crossHair);
		
		return super.startAnchor(start, modifiers, elt, result);
	}

	@Override
	public boolean completeAnchor(Point pt, int modifiers, Element elt) {
		view.drawingPanel().setCursor(defaultCursor);
		
		return super.completeAnchor(pt, modifiers, elt);
	}
}
