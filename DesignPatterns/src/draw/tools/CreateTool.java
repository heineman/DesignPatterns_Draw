package draw.tools;

import draw.model.Element;
import draw.palette.Create;
import draw.palette.PaletteEntry;

/**
 * The Create tool is responsible for adding new elements to the document.
 * 
 * Currently RectangleElt and OvalElt are the two initial elements to be added. 
 */
public class CreateTool extends ToolTemplate {
	
	final Element prototype;
	final String type;

	public CreateTool (String type, Element prototype) {
		super();
		this.prototype = prototype;
		this.type = type;
	}
	
	@Override
	public PaletteEntry paletteEntry() {
		return new Create(type, getNormal(type), getSelected(type), prototype);
	}
}
