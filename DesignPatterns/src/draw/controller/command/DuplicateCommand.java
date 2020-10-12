package draw.controller.command;

import java.util.ArrayList;

import draw.controller.visitors.MoveIfSelectedVisitor;
import draw.model.Element;
import draw.model.Model;
import draw.view.DrawingPalette;

/**
 * Only top-level elements can be selected. There is no need for a visitor here.
 * 
 * Copies are added with an offset of (dx=20, dy=20).
 */
public class DuplicateCommand extends Command {

	public DuplicateCommand(Model model, DrawingPalette view) {
		super (model, view);
	}
	
	@Override
	public boolean execute() {
		ArrayList<Element> toAdd = new ArrayList<>();
		for (Element e : model) {
			if (e.isSelected()) {
				Element copy = e.clone();
				
				e.setSelected(false);   // not selected anymore...
				toAdd.add(copy);
			}
		}
		
		for (Element e : toAdd) {
			e.setSelected(true);    // newly duplicated element is selected.
			
			// can now invoke this move visitor BECAUSE e is selected.
			e.accept(new MoveIfSelectedVisitor(20, 20));
			model.add(e);
		}

		view.repaint();
		return true;
	}
}
