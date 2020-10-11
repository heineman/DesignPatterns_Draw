package draw.tools;

import draw.palette.PaletteEntry;
import draw.palette.Select;

/**
 * The select tool is responsible for selecting existing elements in the application, and ultimately,
 * handle resizing of selected elements upon request.
 */
public class SelectTool extends ToolTemplate {
	
	public PaletteEntry paletteEntry() {
		return new Select(getNormal(Select.selectName), getSelected(Select.selectName));
	}
}
