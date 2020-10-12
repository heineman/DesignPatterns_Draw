package draw.controller.command;

import java.awt.Color;
import java.util.Optional;

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
public class FillCommand extends Command {

	/* When clearing the fill, present is false. */
	boolean present;

	/** New fill to use (chosen by color selected. */
	Optional<Color> newFill = Optional.empty();

	/** Old style that was replaced. */
	Style oldStyle = view.getStyle();

	// Fill in every element recursively. This can be applied to
	// an individual element or a group
	class FillInGroup implements Visitor {
		@Override
		public void visit(Element elt) {
			Style style = elt.getStyle();
			if (newFill.isPresent()) {
				style = style.setFillColor(newFill.get());
			} else {
				style = style.clearFillColor();
			}

			elt.setStyle(style);
		}

		@Override
		public void visit(Group group) {
			// nothing special to do...
		}
	}

	public FillCommand(Model model, DrawingPalette view, boolean present) {
		super (model, view);
		this.present = present;
	}

	/**
	 * Execute method involves user interaction (when present) so the helper complete method
	 * is provided for testing purposes.
	 */
	@Override
	public boolean execute() {
		Optional<Color> chosenColor = Optional.empty();
		if (present) {
			Optional<Color> fill = view.getStyle().fillColor;
			Color existingColor = null;
			if (fill.isPresent()) { existingColor = fill.get(); }
			Color chosen = JColorChooser.showDialog(view, "Fill Color", existingColor);
			if (chosen == null) {
				return false;				// cancel chosen color
			}
			chosenColor = Optional.of(chosen);
		}

		return complete(chosenColor);
	}

	/**
	 * Helper method that supports {@link #execute()} and can be tested using JUnit.
	 * @param chosenColor    color to use for filling selected elements
	 * @return  status of operation
	 */
	public boolean complete(Optional<Color> chosenColor) {
		newFill = chosenColor;

		// update style in view and apply to model
		oldStyle = view.getStyle();
		if (newFill.isPresent()) {
			view.updateStyle(oldStyle.setFillColor(newFill.get()));
		} else {
			view.updateStyle(oldStyle.clearFillColor());
		}

		for (Element e : model) {
			if (e.isSelected()) {
				e.accept(new FillInGroup());
			}
		}

		view.repaint();
		return true;
	}
}
