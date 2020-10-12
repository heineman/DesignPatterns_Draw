package draw.controller.visitors;

import java.awt.Point;
import java.util.Optional;

import draw.model.Element;
import draw.model.Group;
import draw.model.Visitor;

/**
 * Find the most recent element that intersects given point.
 * 
 * @since draw.2
 */
public class ChooseVisitor implements Visitor {
	
	/** Target point. */
	final Point pt;
	
	/** Last element which contains point. */
	Optional<Element> lastChosen = Optional.empty(); 
	
	public ChooseVisitor (Point pt) {
		this.pt = pt;
	}
	
	/** Return the last one selected. */
	public Optional<Element> getLastChosen() {
		return lastChosen;
	}
	
	@Override
	public void visit(Element elt) {
		if (elt.contains(pt)) {
			lastChosen = Optional.of(elt.outermostGroup());
		}
	}

	@Override
	public void visit(Group group) {
		// all point selection handled by leaf
	}
}