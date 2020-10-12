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
public class OvalElt extends Element {

	public OvalElt (Rectangle rect) {
		super(rect);
	}

	@Override
	public void drawElement(Graphics g) {
		Rectangle r = getBoundingBox();
		Style style = getStyle();

		Optional<Color> fill = style.fillColor;
		if (fill.isPresent()) {
			g.setColor(fill.get());
			g.fillOval(r.x, r.y, r.width, r.height);
		}

		Optional<Stroke> stroke = style.stroke;
		if (stroke.isPresent()) {
			((Graphics2D)g).setStroke(stroke.get());
		}
		g.setColor(style.penColor);
		g.drawOval(r.x, r.y, r.width, r.height);
	}

	@Override
	public boolean contains(Point p) {
		Rectangle r = getBoundingBox();

		// Code idea Idea taken from Ellipse2D class.
		// Normalize the coordinates compared to the ellipse
		// having a center at 0,0 and a radius of 0.5. 
		double ellw = r.width;
		if (ellw <= 0.0) {
			return false;
		}
		double normx = (p.x - r.x) / ellw - 0.5;
		double ellh = r.height;
		if (ellh <= 0.0) {
			return false;
		}
		double normy = (p.y - r.y) / ellh - 0.5;
		return (normx * normx + normy * normy) < 0.25;
	}

	/** 
	 * Covariant overriding of {@link Element#clone} method to return OvalElt element.
	 * @return Oval Element copy.
	 */
	@Override
	public OvalElt clone() {
		return new OvalElt(getBoundingBox());
	}

}
