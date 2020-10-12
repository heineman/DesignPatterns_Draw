package draw.controller.json;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.stream.Collectors;

import draw.model.Style;

public class JSON_Export implements draw.model.Visitor {

	/** Repository of unique styles. */
	Hashtable<Style,String> styleRepository;
	
	/** All part decompositions for groups. Values is List<String> of ids. */
	Hashtable<draw.model.Group,ArrayList<String>> partIDs;
	
	/** Computed StyleHeader. */
	final String styleHeader;
	
	/** Computed elements to be output. */
	ArrayList<String> elements = new ArrayList<>();
	
	/** 
	 * Embed a string in JSON
	 * @param s   string to be quoted
	 * @return  Add quotes around the given string. 
	 */
	public String quote(String s) {
		return "\"" + s + "\"";
	}
	
	/** 
	 * REturn the size of the stroke.
	 * @param s  Stroke used for drawing
	 * @return   size of stroke as integer. 
	 */
	int penSize(Stroke s) {
		int size = 1;
		if (s instanceof BasicStroke) {
			size = (int)((BasicStroke)s).getLineWidth();
		}
		
		return size;
	}
	
	/** Return color as a JSON encoding. */
	String color(Color c) {
		return "{" +
					quote("red") + ":" + c.getRed() + "," + 
					quote("green") + ":" + c.getGreen() + "," +
					quote("blue") + ":" + c.getBlue() +
				"}";
	}
	
	/**
	 * Return style as JSON string
	 * @param style
	 * @return
	 */
	String styleinfo(Style style) {
		Color fillColor = null;
		if (style.fillColor.isPresent()) {
			fillColor = style.fillColor.get();
		}
		Stroke stroke = null;
		if (style.stroke.isPresent()) {
			stroke = style.stroke.get();
		}
		Color penColor = style.penColor;
		String mightFill = "";
		if (fillColor != null) {
			mightFill = quote("fill") + ":" + color(fillColor) + ",";
		}
		return "{" +
				   mightFill + 
				   quote("pen") + ":" + color(penColor) + "," +
				   quote("stroke") + ":" + penSize(stroke) + 
				"}";
	}
	
	public JSON_Export(Hashtable<Style,String> styleRepository, Hashtable<draw.model.Group,ArrayList<String>> partIDs) {
		this.styleRepository = styleRepository;
		this.partIDs = partIDs;
		
		// nothing to output!
		if (styleRepository.isEmpty()) {
			styleHeader = "";
			return;
		}
		
		// collect together as "style-n" : { actual-json-style }
		styleHeader = styleRepository.keySet().stream()
			.map(key ->String.format("%s:%s", 
						quote(styleRepository.get(key)),
						styleinfo(key)))
			.collect(Collectors.joining(","));
	}
	
	/**
	 * Return encoding suitable for retrieval later.
	 * @return JSON encoding for entire document
	 */
	public String genEncoding() {
		String inner = String.join(",", elements);
		
		return "{" + 
		      quote("styles") + ": {" + styleHeader + "}, \n" + 
		      quote("elements") + ": {" + inner + "}\n" + 
		      "}";
	}

	@Override
	public void visit(draw.model.Element elt) {
		ShapeElement se = new ShapeElement(elt, styleRepository.get(elt.getStyle()));
		elements.add(se.toJSON());
	}

	@Override
	public void visit(draw.model.Group group) {
		GroupElement g = new GroupElement(group, partIDs.get(group));
		elements.add(g.toJSON());
	}
}
