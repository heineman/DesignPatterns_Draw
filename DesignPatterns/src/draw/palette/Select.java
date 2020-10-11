package draw.palette;

import javax.swing.Icon;

/**
 * User is choosing to select available elements. If user presses mouse on a region without an element, 
 * then the user is initiating a drag of a rectangle that will be used for selecting elements that are
 * wholly contained within that rectangle.
 * 
 */
public class Select extends PaletteEntry {
	
	public static final String selectName = "select";
	
	public Select (Icon normal, Icon selected) {
		super(selectName, normal, selected, Action.Select);
	}
}
