package draw.controller.handler;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Optional;

import draw.controller.visitors.MoveIfSelectedVisitor;
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

	/**
	 * Selected base element from which all dragging computations are made.
	 * @since draw.4 
	 */
	Optional<Element> baseElt = Optional.empty(); 
	
	/** 
	 * Starting point of moving action. 
	 * @since draw.4
	 */
	Point start;
	
	/** 
	 * Standard arrow cursor.
	 * @since draw.4
	 */
	final Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	
	/** 
	 * Cursor with four arrows to show moving.
	 * @since draw.4
	 */
	final Cursor moveCursor = new Cursor(Cursor.MOVE_CURSOR);
	
	public SelectHandler(Model model, DrawingPalette view) {
		super(model, view);
	}

	@Override
	protected boolean interested(Action action) {
		return action.equals(Action.Select);
	}
	
	/**
	 * Takes on responsibility of changing cursor into a MoveCursor when over any element.
	 * 
	 * @since draw.4
	 */
	@Override
	public boolean move(Point pt, int modifiers, Optional<Element> element, int anchor) {
		if (isInterested()) {
			if (!element.isPresent()) {
				view.drawingPanel().setCursor(defaultCursor);
			} else {
				if (anchor == 0) {
					view.drawingPanel().setCursor(moveCursor);
				}
			}
		}
		
		return super.move(pt, modifiers, element, anchor);
	}


	/**
	 * Select upon press, if over an element.
	 * 
	 * In draw.4, modified to support moving selected objects
	 */
	@Override
	public boolean start(Point start, int modifiers, Optional<Element> elt) {
		if (isInterested()) {
			this.start = start;

			if (!elt.isPresent()) {
				// unselect everything else 
				for (Element e : model) {
					e.setSelected(false);
				}

				Rectangle r = new Rectangle(start);
				userSelection = Optional.of(r);
			} else {
				baseElt = elt;
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
			baseElt = Optional.empty();
			start = null;
			
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
	 * Updated to allow moving selected objects if there is a {@link #baseElt} present.
	 * @since draw.1
	 */
	@Override
	public boolean drag(Point ignore, int modifiers, Optional<Element> elt, Point current) {
		if (isInterested()) {
			if (baseElt.isPresent()) {
				MoveIfSelectedVisitor mv = new MoveIfSelectedVisitor(current.x - start.x, current.y - start.y);

				// ask everyone to move, and update 'start' for subsequent drags...
				model.accept(mv);
				start = current;
			} else {
				// used to extend (potentially) selected area...
				Rectangle r = new Rectangle(ignore);
				r.add(current);
				userSelection = Optional.of(r);
			}
			
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
