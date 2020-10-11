package draw.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import draw.model.Element;

public class RectangleElt extends Element {
	
	public RectangleElt (Rectangle rect) {
		super(rect);
	}

	@Override
	public void drawElement(Graphics g) {
		Rectangle r = getBoundingBox();
		
		g.setColor(Color.black);
		g.drawRect(r.x, r.y, r.width, r.height);
	}

	@Override
	public boolean contains(Point p) {
		return getBoundingBox().contains(p);
	}

	/** 
	 * Covariant overriding of {@link Element#clone} method to return RectangleElt element.
	 * @return Oval Element copy.
	 */
	@Override
	public RectangleElt clone() {
		return new RectangleElt(getBoundingBox());
	}
	
}
