package draw.controller.command;

import java.util.ArrayList;
import java.util.List;

import draw.model.Element;
import draw.model.Model;
import draw.view.DrawingPalette;

/**
 * Only top-level elements can be selected. There is no need for a visitor here.
 */
public class DeleteCommand extends Command {

	public DeleteCommand(Model model, DrawingPalette view) {
		super (model, view);
	}
	
	@Override
	public boolean execute() {
		List<Element> toDelete = new ArrayList<>();
		for (Element e : model) {
			if (e.isSelected()) {
				e.setSelected(false);   // not selected anymore...
				toDelete.add(e);
			}
		}
		
		// nothing to remove.
		if (toDelete.isEmpty()) {
			return false;
		}
		
		// Have to process in two steps.
		for (Element e : toDelete) {
			model.remove(e);
		}
		
		view.repaint();
		return true;
	}

}
