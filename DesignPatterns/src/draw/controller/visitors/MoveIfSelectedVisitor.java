package draw.controller.visitors;

import java.awt.Rectangle;

import draw.model.Element;
import draw.model.Group;
import draw.model.Visitor;

/**
 * Move all selected items by the desired (deltaX, deltaY) relative to the one being chosen.
 * At a minimum, you know that chosen is selected; it could be a group, or it could be independent.
 * There could be any number of elements that are selected; all should move by this same amount.
 * 
 * @since draw.4
 */
public class MoveIfSelectedVisitor implements Visitor {
	
	/** Move directives. */
	final int deltaX;
	final int deltaY;
	
	public MoveIfSelectedVisitor (int deltaX, int deltaY) {
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}
	
	@Override
	public void visit(Element elt) {
		if (elt.outermostGroup().isSelected()) {
			Rectangle bbox = elt.getBoundingBox();
			bbox.x += deltaX;
			bbox.y += deltaY;
			elt.setBoundingBox(bbox);
		}
	}

	/**
	 * If selected, it is possible that a bounding box has changed, so update.
	 */
	@Override
	public void visit(Group group) {
		if (group.outermostGroup().isSelected()) {
			group.updateBoundingBox();
		}
	}
}