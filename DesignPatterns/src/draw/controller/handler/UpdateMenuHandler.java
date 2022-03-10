package draw.controller.handler;

import java.awt.Point;
import java.util.Optional;

import draw.controller.visitors.CommonStyleVisitor;
import draw.model.Element;
import draw.model.Model;
import draw.model.Style;
import draw.palette.Action;
import draw.view.DrawingPalette;

/**
 * Update menu Style based on selections.
 * 
 * @since draw.3.styles
 */
public class UpdateMenuHandler extends Handler {

	public UpdateMenuHandler(Model model, DrawingPalette view) {
		super(model, view);
	}

	@Override
	protected boolean interested(Action action) {
		return action.equals(Action.Select);
	}
	
	/**
	 * React to completion (i.e., mouseRelease) by finding common style among all selected elements,
	 * and updating menu contents accordingly
	 */
	@Override
	public boolean complete(Point pt, int modifiers, Optional<Element> elt) {
		if (isInterested()) {
			CommonStyleVisitor csv = new CommonStyleVisitor();
			model.accept(csv);
			
			// If there is anything in common, update view accordingly
			Optional<Style> common = csv.getCommonStyle();
			if (common.isPresent()) {
				view.updateStyle(common.get());
			}
		}
		
		return super.complete(pt, modifiers, elt);
	}
}
