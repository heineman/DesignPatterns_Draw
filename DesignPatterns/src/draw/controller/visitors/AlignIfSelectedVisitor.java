package draw.controller.visitors;

import java.awt.Rectangle;

import draw.model.Element;
import draw.model.Group;
import draw.model.Visitor;

/**
 * Visitor to align elements (even groups) on x-position
 */
public class AlignIfSelectedVisitor implements Visitor {
	final int xPosition; 

	public AlignIfSelectedVisitor(int xPosition) {
		this.xPosition = xPosition; 
	}

	public void visit(Element elt) {
		if (elt.isSelected()) {
			Rectangle bbox = elt.getBoundingBox();
			bbox.x = xPosition; 
			elt.setBoundingBox(bbox);
		} else if (elt.outermostGroup().isSelected()) {
			// in a group. Need to be careful here. Move
			// distance based on outermost updates
			Rectangle outer = elt.outermostGroup().getBoundingBox();
			int delta = outer.x - xPosition;
			Rectangle bbox = elt.getBoundingBox();
			bbox.x -= delta;
			elt.setBoundingBox(bbox);
		}
	}

	public void visit(Group group) {
		if (group.outermostGroup().isSelected()) {
			group.updateBoundingBox();
		}
	}
}
