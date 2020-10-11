package draw.controller.handler;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Optional;

import draw.model.Element;
import draw.model.Model;
import draw.palette.Action;
import draw.view.DrawingPalette;

public class SelectHandler extends Handler {

	/**
	 * If user is in the middle of a selection operation by dragging a rectangle,
	 * this is available.
	 * @since draw.1
	 */
	Optional<Rectangle> userSelection = Optional.empty();

	public SelectHandler(Model model, DrawingPalette view) {
		super(model, view);
	}

	@Override
	protected boolean interested(Action action) {
		return action.equals(Action.Select);
	}

	/**
	 * Select upon press, if over an element.
	 */
	@Override
	public boolean start(Point start, int modifiers, Optional<Element> elt) {
		if (isInterested()) {

			if (!elt.isPresent()) {
				// unselect everything else 
				for (Element e : model) {
					e.setSelected(false);
				}

				Rectangle r = new Rectangle(start);
				userSelection = Optional.of(r);
			} else {
				if (elt.get().isSelected()) {
					// If BaseElt is selected AND we have shift key down, then toggle
					if (ActiveToolHandler.shiftDown(modifiers)) {
						elt.get().setSelected(false);
					} else {
						// already selected... keep selected... and pass through
					}
				} else {
					// with SHIFT key down, toggle selected status of the element.
					if (ActiveToolHandler.shiftDown(modifiers)) {
						// toggle selected state
						elt.get().setSelected(!elt.get().isSelected());
					} else {
						// un-select everything
						for (Element e : model) {
							e.setSelected(false);
						}
						elt.get().setSelected(true);
					}
					view.repaint();
				}

			}

			view.repaint();
		}

		return super.start(start, modifiers, elt); 
	}

	/**
	 * React to completion (i.e., mouseRelease) by selecting all elements within the user Selection
	 * 
	 * @since draw.1
	 */
	@Override
	public boolean complete(Point pt, int modifiers, Optional<Element> elt) {
		if (isInterested()) {
			if (userSelection.isPresent()) {
				// select everything within this rectangle
				for (Element e : model) {
					if (userSelection.get().contains(e.getBoundingBox())) {
						e.setSelected(true);
					}
				}
	
				userSelection = Optional.empty();
				view.repaint();
			}
		}
		
		return super.complete(pt, modifiers, elt);
	}

	/**
	 * Process drag events by extending the user selection, repainting to show the region
	 * 
	 * @since draw.1
	 */
	@Override
	public boolean drag(Point ignore, int modifiers, Optional<Element> elt, Point current) {
		if (isInterested()) {
			// used to extend (potentially) selected area...
			Rectangle r = new Rectangle(ignore);
			r.add(current);
			userSelection = Optional.of(r);
			view.repaint();
		}
		
		return super.drag(ignore, modifiers, elt, current);
	}

	/**
	 * Take responsibility for showing user's selection rectangle.
	 * 
	 * @since draw.1
	 */
	@Override
	public void paint(Graphics g) {
		if (userSelection.isPresent()) {
			Rectangle r = userSelection.get();

			g.setXORMode(Color.WHITE); // inverts colors underneath so you can see shapes
			g.fillRect(r.x, r.y, r.width, r.height);
		}
		
		super.paint(g);
	}
}
