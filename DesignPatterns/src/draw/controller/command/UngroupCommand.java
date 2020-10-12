package draw.controller.command;

import java.util.ArrayList;
import java.util.List;

import draw.model.Element;
import draw.model.Group;
import draw.model.Model;
import draw.model.Visitor;
import draw.view.DrawingPalette;

public class UngroupCommand extends Command {

	public UngroupCommand(Model model, DrawingPalette view) {
		super (model, view);
	}
	
	/** Grabs all top-level groups that are selected. These can be ungrouped. */
	class AllGroupedAndSelected implements Visitor {
		List<Group> collect = new ArrayList<>();
		
		@Override
		public void visit(Element elt) {
			// nothing here...
		} 

		@Override
		public void visit(Group group) {
			if (group.isSelected()) {
				collect.add(group);
			}
		}
		
	}
	
	@Override
	public boolean execute() {
		AllGroupedAndSelected gs = new AllGroupedAndSelected();
		model.accept(gs);
		
		for (Group g : gs.collect) {
			model.remove(g);
			
			for (Element e : g) {
				e.setSelected(true);
				e.removeParent();  // no longer GROUPED!
				e.resetAnchors();
				model.add(e);
			}
		}
		
		view.repaint();
		return true;
	}
}
