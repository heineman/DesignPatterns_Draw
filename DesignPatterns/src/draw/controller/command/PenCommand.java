package draw.controller.command;

import java.awt.Color;
import javax.swing.JColorChooser;

import draw.model.Element;
import draw.model.Group;
import draw.model.Model;
import draw.model.Style;
import draw.model.Visitor;
import draw.view.DrawingPalette;

/**
 * Fill selected items, which is recursively passed down into groups.
 * 
 * @since draw.3
 */
public class PenCommand extends Command {

	/** Pen to be drawn in this color. */
	Color chosenColor;
	
	/** Old style that was replaced. */
	Style oldStyle = view.getStyle();
	
	class PenSelected implements Visitor {
		
		@Override
		public void visit(Element elt) {
			Style style = elt.getStyle();
			style = style.setPenColor(chosenColor);
			elt.setStyle(style);
		}

		@Override
		public void visit(Group group) {
			// nothing special to do
		}
	}
	
	public PenCommand(Model model, DrawingPalette view) {
		super (model, view);
	}
	
	@Override
	public boolean execute() {
		Color oldColor = view.getStyle().penColor;
		
		Color cc = JColorChooser.showDialog(view, "Pen Color", oldColor);
		if (cc == null) {
			return false;				// cancel chosen
		} 
		
		return complete(cc);
	}
	
	/**
	 * Method can be tested in JUnit.
	 * 
	 * @param cc  chosen color
	 * @return  success of operation
	 */
	public boolean complete(Color cc) {
		chosenColor = cc;
		
		// update style in view
		oldStyle = view.getStyle();
		view.updateStyle(oldStyle.setPenColor(chosenColor));
		
		for (Element e : model) {
			if (e.isSelected()) {
				e.accept(new PenSelected());
			}
		}
		
		view.repaint();
		return true;
	}
}
