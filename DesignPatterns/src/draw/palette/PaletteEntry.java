package draw.palette;

import javax.swing.Icon;

/**
 * Any entry in the palette extends this class.
 * 
 * Select -- select existing elements.
 * Create -- any number of (CreateRectangle, CreateOval, ...)
 * 
 * Delegates all drawing requests to the associated action.
 */
public class PaletteEntry {
	public final Icon normal;
	public final Icon selected;
	public final String name;
	public final Action action;

	public PaletteEntry (String name, Icon normal, Icon selected, Action action) {
		this.name = name;
		this.normal = normal;
		this.selected = selected;
		this.action = action;
	}
}
