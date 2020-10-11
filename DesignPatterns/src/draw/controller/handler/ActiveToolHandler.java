package draw.controller.handler;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Optional;

import draw.model.Element;
import draw.model.Model;
import draw.view.DrawingPalette;
import draw.controller.IActionInterface;

/**
 * Adapts mouse events into actions, corresponding to {@link IActionInterface}, based on the active tool.
 * 
 * This class provides the concrete implementation of an Adapter, that converts incoming events from one
 * interface (MouseListener, MouseMotionListener) into an outgoing sequence of different events on 
 * another interface (IActionInterface).
 * 
 * Adapters can store transient state as they perform their work. This adapter keeps tract of:
 * 
 * <ol>
 * <li>start - Where the initial mouse press event occurred for drag events.
 * <li>anchorType - If initial press was in an anchor (4/5/6/7) or not (0).
 * <li>elt - The affected element if the press event occurred over one.
 * </ol>
 * 
 * The events are handled by the given {@link IActionInterface} handler.
 * 
 * <img src="doc-files/adapter.png" alt="Adapter Pattern">
 */
public class ActiveToolHandler extends DrawerMouseAdapter {

	Model model;
	DrawingPalette view;

	/** Where initiation of drag starts. */
	Optional<Point> start = Optional.empty();

	/** Type of anchor drag, NW (6) NE (7) SW (4) SE (5). */
	int anchorType = 0;

	/** Potential element that is affected by mouse events. */
	Optional<Element> elt = Optional.empty();

	/** Adapt mouse events into IActionInterface events processed by this handler. */
	IActionInterface handler;

	public ActiveToolHandler(Model model, DrawingPalette view, IActionInterface handler ) {
		this.model = model;
		this.view = view;
		this.handler = handler;
	}

	@Override
	public final void mouseDragged(MouseEvent e) {
		if (anchorType != 0) {
			handler.dragAnchor(start.get(), e.getModifiers(), elt.get(), e.getPoint());
		} else {
			handler.drag(start.get(), e.getModifiers(), elt, e.getPoint());
		} 
	}

	@Override
	public final void mousePressed(MouseEvent e) {
		start = Optional.of(e.getPoint());

		if (anchorType != 0) {
			handler.startAnchor(e.getPoint(), e.getModifiers(), elt.get(), anchorType);
		} else {
			handler.start(e.getPoint(), e.getModifiers(), elt);
		}
	}

	@Override
	public final void mouseReleased(MouseEvent e) {
		if (anchorType != 0) {
			handler.completeAnchor(e.getPoint(), e.getModifiers(), elt.get());
		} else {
			handler.complete(e.getPoint(), e.getModifiers(), elt);
		}
		anchorType = IActionInterface.NoAnchor;  // must be cleared.
	}

	/**
	 * Responsible for identifying anchorType when hovering over one, or just an ordinary
	 * element.
	 * 
	 * This method is critical, since a move event always occurs before a press event happens,
	 * and this method ensures that the state for the adapter is properly set up to work.
	 */
	@Override
	public final void mouseMoved(MouseEvent e) {
		Point pt = e.getPoint();

		// see if inside an anchor (only if selected!)
		boolean isAnchor = false;
		Element found = null;
		for (Element et : model) {
			if (et.contains(pt)) {
				found = et;
			}

			// if element is selected, might be in an anchor
			if (et.isSelected()) { 
				int anchor = IActionInterface.SouthWestAnchor;
				for (Rectangle r : et.getAnchors()) {
					if (r.contains(pt)) {
						anchorType = anchor;
						elt = Optional.of(et);
						handler.move(pt, e.getModifiers(), elt, anchorType);
						isAnchor = true;
						break;
					}
					anchor++;
				}
			}
		}

		// perhaps are on top of an element...
		if (!isAnchor) {
			anchorType = IActionInterface.NoAnchor;
			if (found != null) {
				elt = Optional.of(found);
				handler.move(pt, e.getModifiers(), elt, IActionInterface.NoAnchor);
			} else {
				elt = Optional.empty();
				handler.move(pt, e.getModifiers(), Optional.empty(), IActionInterface.NoAnchor);  // Missing capability from draw.0
			}
		}
	}

	/** 
	 * Helper method to detect is SHIFT key is done. 
	 * @param modifiers   Modifier keys from event
	 * @return status of SHIFT key
	 */
	public static boolean shiftDown(int modifiers) { return (modifiers & MouseEvent.SHIFT_MASK) != 0; }

	/** 
	 * Helper method to detect is CONTROL key is done. 
	 * @param modifiers   Modifier keys from event
	 * @return status of CONTROL key
	 */
	public static boolean controlDown(int modifiers) { return (modifiers & MouseEvent.CTRL_MASK) != 0; }

	/** Pass on to all registered handlers. */
	@Override
	public void paint(Graphics g) {
		handler.paint(g);
	}
}
