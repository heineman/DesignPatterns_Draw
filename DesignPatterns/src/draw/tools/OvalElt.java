package draw.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import draw.model.Element;

public class OvalElt extends Element {
	
	public OvalElt (Rectangle rect) {
		super(rect);
	}

	@Override
	public void drawElement(Graphics g) {
		Rectangle r = getBoundingBox();

		g.setColor(Color.black);
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
