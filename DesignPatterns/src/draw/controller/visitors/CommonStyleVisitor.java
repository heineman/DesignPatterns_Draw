package draw.controller.visitors;

import java.util.Optional;

import draw.model.Element;
import draw.model.Group;
import draw.model.Style;
import draw.model.Visitor;

/**
 * Find the common style among targeted selections.
 * 
 * If a single element, then only one style to consider. However when
 * a group contains multiple elements (and possibly other sub-groups)
 * have to consider those as well.
 * 
 * @since draw.3 assessment
 */
public class CommonStyleVisitor implements Visitor {
	
	/** Common computed style (if one exists). */
	Optional<Style> commonStyle = Optional.empty(); 
	
	public CommonStyleVisitor () {}

	/**
	 * Given (optional) existing style, and a new style, return Optional style that 
	 * stores all common traits.
	 * 
	 * @param existing    Optional existing style.
	 * @param newOne      New style to be considered.
	 * @return Optional style that represents style attributes if common (if at all) between existing and newOne.
	 */
	Optional<Style> common (Optional<Style> existing, Style newOne) {
		// base case: start with new one
		if (!existing.isPresent()) { return Optional.of(newOne); }
		Style style = existing.get();
		
		// Fill, Pen, Stroke need to be checked
		if (!style.fillColor.equals(newOne.fillColor)) {
			style = style.clearFillColor();
		}
		if (!style.penColor.equals(newOne.penColor)) {
			style = style.clearPenColor();
		}
		if (!style.stroke.equals(newOne.stroke)) {
			style = style.clearStroke();
		}
		
		return Optional.of(style);
	}
	
	/** 
	 * Result of visitor.
	 * @return Common style among all selected elements.
	 */
	public Optional<Style> getCommonStyle() {
		return commonStyle;
	}
	
	/**
	 * If element is selected, or outermost group is selected,
	 * then process this style as well.
	 * @param elt    element in the model
	 */
	@Override
	public void visit(Element elt) {
		if (elt.outermostGroup().isSelected()) {
			if (commonStyle.isPresent()) {
				commonStyle = common(commonStyle, elt.getStyle());
			} else {
				commonStyle = Optional.of(elt.getStyle());
			}
		}
	}

	@Override
	public void visit(Group group) {
		// all point selection handled by leaf
	}
}