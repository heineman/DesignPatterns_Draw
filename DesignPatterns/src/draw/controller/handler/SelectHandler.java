package draw.controller.handler;

import java.awt.Point;
import java.util.Optional;

import draw.model.Element;
import draw.model.Model;
import draw.palette.Action;
import draw.view.DrawingPalette;

public class SelectHandler extends Handler {

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
			// unselect everything else 
			for (Element e : model) {
				e.setSelected(false);
			}
			
			if (elt.isPresent()) {
				elt.get().setSelected(true);
			} 
	
			view.repaint();
		}
		
		return super.start(start, modifiers, elt); 
	}
}
