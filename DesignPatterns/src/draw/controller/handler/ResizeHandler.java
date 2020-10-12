package draw.controller.handler;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.Hashtable;

import draw.model.Element;
import draw.model.Group;
import draw.model.Model;
import draw.model.Visitor;
import draw.palette.Action;
import draw.view.DrawingPalette;

/**
 * React to anchor points on MouseMotion by resizing.
 */
public class ResizeHandler extends Handler {

	/** Original rectangle of selected shape being resized. */
	Rectangle base;
	
	/**
	 * When resizing an element from an anchor, there is always one fixed point, namely, the one that 
	 * is diagonally opposite of anchorStart when the process starts.
	 */
	Point fixedPoint;
	
	/**
	 * Ratios records relative location for each element within group. 
	 * Dimensions records (width,height) for each original element.
	 */
	Hashtable<Element,Point2D.Double> ratios = new Hashtable<>();
	Hashtable<Element,Dimension> dimensions = new Hashtable<>();
	
	@Override
	protected boolean interested(Action action) {
		return action.equals(Action.Select);
	}
	
	/** Retrieve positional locations for every element within selected group. */
	public class RelativeLocation implements Visitor {

		@Override
		public void visit(Element elt) {
			Element outer = elt.outermostGroup();
			Rectangle outerBox = outer.getBoundingBox();
			if (outer.isSelected()) {
				Rectangle box = elt.getBoundingBox();
				Point2D.Double ratio =  new Point2D.Double(
						        1.0*(box.x-outerBox.x)/outerBox.width, 
								1.0*(box.y-outerBox.y)/outerBox.height);

				ratios.put(elt,  ratio);
				dimensions.put(elt, new Dimension(box.width, box.height));
			}
		}

		@Override
		public void visit(Group group) {
			// nothing to do for groups.
		}
	}
	
	/** Update based on ratios/dimensions. */
	public class AdjustLocation implements Visitor {

		// base has been changed into updatedRect and we have ratios 
		// for x and y changes. Only one object is selected.
		Rectangle updatedRect;
		double xr;
		double yr;
		
		AdjustLocation (Rectangle rect) {
			this.updatedRect = rect;
			xr = (updatedRect.width * 1.0) / base.width;
			yr = (updatedRect.height * 1.0) / base.height;
		}
		
		@Override
		public void visit(Element elt) {
			Element outer = elt.outermostGroup();
			if (outer.isSelected()) {
				Point2D.Double ratio = ratios.get(elt);
				Dimension dimen = dimensions.get(elt);
				
				Rectangle box = elt.getBoundingBox();
				box.width  = (int) (dimen.width * xr);   
				box.height = (int) (dimen.height * yr);

				// Only one selected item is adjusted, based on the adjusted dimensions/location
				box.x = updatedRect.x + (int) ((ratio.x*updatedRect.width));
				box.y = updatedRect.y + (int) ((ratio.y*updatedRect.height));

				elt.setBoundingBox(box);
			}
		}

		@Override
		public void visit(Group group) {
			group.updateBoundingBox();
		}
	}
	
	public ResizeHandler(Model model, DrawingPalette view) {
		super(model, view);
	}

	/** Record where the starting point is. */
	@Override
	public boolean startAnchor(Point start, int modifiers, Element elt, int anchor) {
		if (isInterested()) {
			base = new Rectangle(elt.getBoundingBox());
			
			fixedPoint = opposite(anchor, base);
			
			// turn off all other selections except the one whose anchor was pressed.
			for (Element e : model) {
				e.setSelected(false);
			}
			elt.setSelected(true);
			
			// record relative locations of (potentially) all sub-elements
			model.accept(new RelativeLocation());
		}
		
		return super.startAnchor(start, modifiers, elt, anchor);
	}

	/**
	 * Based on the anchor selected, choose opposite point as the fixed point.
	 */
	static Point opposite(int anchorStart, Rectangle base) {
		if (anchorStart == NorthWestAnchor) {
			return new Point(base.x + base.width, base.y + base.height);
		} else if (anchorStart == NorthEastAnchor) {
			return new Point(base.x, base.y + base.height);
		} else if (anchorStart == SouthEastAnchor) {
			return new Point(base.x, base.y);
		} else { // Must be southWest
			return new Point(base.x + base.width, base.y);
		}
	}

	@Override
	public boolean dragAnchor(Point start, int modifiers, Element elt, Point current) {
		if (isInterested()) {
			Rectangle r= new Rectangle(current);
			r.add(fixedPoint);
			
			AdjustLocation rv = new AdjustLocation(r);
			model.accept(rv);
			view.repaint();
		}
		
		return super.dragAnchor(start, modifiers, elt, current);
	}
}
