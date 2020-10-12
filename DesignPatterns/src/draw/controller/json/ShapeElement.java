package draw.controller.json;

import java.awt.Rectangle;
import java.util.Hashtable;
import java.util.stream.Collectors;

public class ShapeElement extends Element {
	
	/** Element for whom we generate JSON. */
	final draw.model.Element elt;
	
	/** Associated styleid for this element (unique in entire document). */
	final String styleid;
	
	/** Tags to add to the JSON. */
	Hashtable<String,String> tags = new Hashtable<>();

	/**
	 * Extracts information from the {@link draw.model.Element} object.
	 * 
	 * The final JSON will look like this:
	 * 
	 "std.tool.RectangleElt@1964d46" : {
	    "type" : "std.tool.RectangleElt",
	    "style": "styleid",
	    "x" : 10, "y" : 20", "width":130, "height":80,
	  }
	 
	 * @param elt      {@link draw.model.Element} object
	 * @param styleid  unique styleid needed for storing/loading
	 */
	public ShapeElement(draw.model.Element elt, String styleid) {
		super();
		
		this.elt = elt;
		this.styleid = styleid;
		
		tags.put("type", quote(elt.getClass().getName()));
		
		tags.put("style", quote(styleid));
		Rectangle bbox = elt.getBoundingBox();
		tags.put("x", "" + bbox.x);
		tags.put("y", "" + bbox.y);
		tags.put("width", "" + bbox.width);
		tags.put("height", "" + bbox.height);
	}
	
	public String toJSON() {
		String allTags = tags.keySet().stream()
				.map(key -> String.format("%s:%s", quote(key), tags.get(key)))
				.collect(Collectors.joining(","));

		return quote(elt.toString()) + " : {\n" +
						allTags.toString() + 
				     "}";
	}
	
}
