package draw.controller.command;

import java.util.ArrayList;
import java.util.List;

import draw.controller.visitors.AlignIfSelectedVisitor;
import draw.model.Element;
import draw.model.Group;
import draw.model.Model;
import draw.view.DrawingPalette;

/**
 * Align all selected objects on left.
 * 
 * @since draw.2.align
 */
public class AlignLeftCommand extends Command {

	public AlignLeftCommand(Model model, DrawingPalette view) {
		super (model, view);
	}
	
	@Override
	public boolean execute() {

		// determine left-most X on which to align.
		int leftMostX = Integer.MAX_VALUE;
		for (Element e : model) {
			if (e.isSelected()) {
				int x = e.getBoundingBox().x;
				if (x < leftMostX) {
					leftMostX = x;
				}
			}
		}
		
		// nothing to align.
		if (leftMostX == Integer.MAX_VALUE) { return false; }
		
		// process using visitors
		model.accept(new AlignIfSelectedVisitor(leftMostX));
		
		view.repaint();
		return true;
	}
}
