package draw.controller.command;

import java.util.ArrayList;
import java.util.List;

import draw.model.Element;
import draw.model.Group;
import draw.model.Model;
import draw.view.DrawingPalette;

/**
 * Group together selected elements.
 * 
 * @since draw.1
 */
public class GroupCommand extends Command {

	public GroupCommand(Model model, DrawingPalette view) {
		super (model, view);
	}
	
	@Override
	public boolean execute() {
		List<Element> grouping = new ArrayList<>();
		for (Element e : model) {
			if (e.isSelected()) {
				e.setSelected(false);   // not selected anymore...
				grouping.add(e);
			}
		}
		
		// nothing to group.
		if (grouping.isEmpty()) { return false;	}
		
		// Remove each individually and then create new group to add to the model
		for (Element e : grouping) { model.remove(e); }
		
		Group group = new Group(grouping.toArray(new Element[] {}));  // access varargs constructor
		group.setSelected(true);
		
		model.add(group);
		view.repaint();
		return true;
	}

}
