package draw.model;

import java.awt.Color;
import java.awt.Stroke;
import java.util.Optional;

/** 
 * Group together everything that is relevant to the style of an element.
 * 
 * Style objects are immutable. This decision offers following benefits:
 *  
 *   1. They can be used as key in hashtable
 *   2. When there are lots of shapes, they can be reused without fear
 *   
 * @since draw.3
 */
public class Style {
	public static Style defaultStyle = new Style(Optional.of(new Color(215, 228, 189)), Optional.of(Color.black), Optional.empty());

	/** Fill Color (if set). */
	public final Optional<Color> fillColor;
	
	/** Pen color to use (defaults to black). */
	public final Optional<Color> penColor;
	
	/** Pen width to use (only available within Graphics2D). */
	public final Optional<Stroke> stroke;
	
	/** 
	 * Set the fill color. 
	 * @param c new Fill color
	 * @return new Style object with desired fill color
	 */
	public Style setFillColor(Color c) {
		return new Style(Optional.of(c), penColor, stroke);
	}
	
	/** 
	 * Clear fill color.
	 * @return new Style object with cleared fill color
	 */
	public Style clearFillColor() {
		return new Style(Optional.empty(), penColor, stroke);
	}
	
	/** 
	 * Set the pen color.
	 * @param c  desired pen color
	 * @return new Style object with updated pen color
	 */
	public Style setPenColor(Color c) { 
		return new Style(fillColor, Optional.of(c), stroke);
	}
	
	/**
	 * Clear pen color from style.
	 * @return new Style object with cleared pen color
	 */
	public Style clearPenColor() {
		return new Style(fillColor, Optional.empty(), stroke);
	}
	
	/** 
	 * Set the pen stroke.
	 * @param s  desired stroke
	 * @return new Style object with updated stroke
	 */
	public Style setStroke(Stroke s) {
		return new Style(fillColor, penColor, Optional.of(s));
	}
	
	/**
	 * Clear stroke from style.
	 * @return new Style object with cleared stroke.
	 */
	public Style clearStroke() {
		return new Style(fillColor, penColor, Optional.empty());
	}
	
	/**
	 * Construct new Style object with desired criteria
	 * @param fillColor   Fill color, or Optional.empty() if none
	 * @param penColor    Pen color to use
	 * @param stroke      Stroke to use
	 */
	public Style (Optional<Color> fillColor, Optional<Color> penColor, Optional<Stroke> stroke) {
		this.fillColor = fillColor;
		this.penColor = penColor;
		this.stroke = stroke;
	}
	
	/**
	 * Return a copy of current style. 
	 * @return duplicate of style
	 */
	public Style copy() {
		return new Style(fillColor, penColor, stroke);
	}
	
	/** Reasonable hashCode method. */
	@Override
	public int hashCode() {
		return fillColor.hashCode() + penColor.hashCode() + stroke.hashCode();
	}
	
	/** Reasonable equals method for Style objects. */
	@Override
	public boolean equals (Object o) {
		if (o == null) { return false; }
		if (o instanceof Style) {
			Style other = (Style) o;
			return fillColor.equals(other.fillColor) &&
					penColor.equals(other.penColor) &&
					stroke.equals(other.stroke);
		}
		
		return false;
	}

}
