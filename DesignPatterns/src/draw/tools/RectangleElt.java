package draw.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.Optional;

import draw.model.Element;
import draw.model.Style;

/**
 * Represents the drawing of an Oval element.
 * 
 * Style capabilities added in draw.3
 */
public class RectangleElt extends Element {
	
	public RectangleElt (Rectangle rect) {
		super(rect);
	}

	@Override
	public void drawElement(Graphics g) {
		Rectangle r = getBoundingBox();
		Style style = getStyle();
		
		Optional<Color> fill = style.fillColor;
		if (fill.isPresent()) {
			g.setColor(fill.get());
			g.fillRect(r.x, r.y, r.width, r.height);
		}
		
		Optional<Stroke> stroke = style.stroke;
		if (stroke.isPresent()) {
			((Graphics2D)g).setStroke(stroke.get());
		}
		
		if (style.penColor.isPresent()) { g.setColor(style.penColor.get()); }
		g.drawRect(r.x, r.y, r.width, r.height);
	}

	@Override
	public boolean contains(Point p) {
		return getBoundingBox().contains(p);
	}

	/** 
	 * Covariant overriding of {@link Element#clone} method to return RectangleElt element.
	 * Modified in draw.5 to fix defect with styles
	 * @return RectangleElt Element copy.
	 */
	@Override
	public RectangleElt clone() {
		RectangleElt copy = new RectangleElt(getBoundingBox());
		copy.setStyle(style);
		return copy;
	}
}
