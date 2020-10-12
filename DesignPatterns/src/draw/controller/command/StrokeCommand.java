package draw.controller.command;

import java.awt.BasicStroke;

import draw.model.Element;
import draw.model.Group;
import draw.model.Model;
import draw.model.Style;
import draw.model.Visitor;
import draw.view.DrawingPalette;

/**
 * Set stroke for selected items, which is recursively passed down into groups.
 * 
 * @since draw.3
 */
public class StrokeCommand extends Command {

	/* Stroke size. */
	final int size;
	
	/** Old style that was replaced. */
	Style oldStyle = view.getStyle();
	
	// fills all below in a group, recursively...
	class StrokeSelected implements Visitor {
		
		@Override
		public void visit(Element elt) {
			Style style = elt.getStyle();
			style = style.setStroke(new BasicStroke(size));
			elt.setStyle(style); 
		}
		
		@Override
		public void visit(Group group) {
			// nothing special to do...
		}
	}
	
	public StrokeCommand(Model model, DrawingPalette view, int size) {
		super(model, view);
		this.size = size;
	}
	
	@Override
	public boolean execute() {
		
		// update style in view
		oldStyle = view.getStyle();
		view.updateStyle(oldStyle.setStroke(new BasicStroke(size)));

		for (Element e : model) {
			if (e.isSelected()) {
				e.accept(new StrokeSelected());
			}
		}
		
		view.repaint();
		return true;
	}
}
