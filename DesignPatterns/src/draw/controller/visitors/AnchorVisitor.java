package draw.controller.visitors;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Optional;

import draw.model.Element;
import draw.model.Group;
import draw.model.Visitor;

/**
 * Visitor that visits all elements and determines whether the target point is contained 
 * within any anchor of a selected element (which might be a group one).
 * 
 * @since draw.2
 */
public class AnchorVisitor implements Visitor {
	
	/** Desired point. */
	final Point pt;
	
	/** Once traversal is complete, if corner is not zero, element will be the affected element. */
	int corner;
	
	/** The selected element computed during the traversal. */
	Optional<Element> element = Optional.empty(); 
	
	/**
	 * Constructs the visitor.
	 * 
	 * @param pt    Desired point
	 */
	public AnchorVisitor (Point pt) {
		this.pt = pt;
	}

	/** Return computations of this Visitor. */
	public Optional<Element> getElement() { return element; }
	public int getCorner() { return corner; }
	
	@Override
	public void visit(Element elt) {
		if (elt.isSelected()) {
			Rectangle anchors[] = elt.getAnchors();
			if (anchors[0].contains(pt)) {
				corner = Cursor.SW_RESIZE_CURSOR;
				element = Optional.of(elt);
			} else if (anchors[1].contains(pt)) {
				corner = Cursor.SE_RESIZE_CURSOR;
				element = Optional.of(elt);
			} else if (anchors[2].contains(pt)) {
				corner = Cursor.NW_RESIZE_CURSOR;
				element = Optional.of(elt);
			} else if (anchors[3].contains(pt)) {
				corner = Cursor.NE_RESIZE_CURSOR;
				element = Optional.of(elt);
			}
		}
	}

	/**
	 * Once a group is selected, none of its interior elements can be selected, so this terminates the search.
	 * If the group is not selected, then dive into each elment to see if one of them is selected.
	 */
	@Override
	public void visit(Group group) {
		if (group.isSelected()) {
			Rectangle anchors[] = group.getAnchors();
			
			if (anchors[0].contains(pt)) {
				corner = Cursor.SW_RESIZE_CURSOR;
				element = Optional.of(group);
			} else if (anchors[1].contains(pt)) {
				corner = Cursor.SE_RESIZE_CURSOR;
				element = Optional.of(group);
			} else if (anchors[2].contains(pt)) {
				corner = Cursor.NW_RESIZE_CURSOR;
				element = Optional.of(group);
			} else if (anchors[3].contains(pt)) {
				corner = Cursor.NE_RESIZE_CURSOR;
				element = Optional.of(group);
			}
		}
	}
}