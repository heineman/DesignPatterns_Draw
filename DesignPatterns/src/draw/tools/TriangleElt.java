package draw.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import draw.model.Element;

/**
 * Shows how to add a triangle tool to the draw.0 branch.
 * 
 * Uses basic geometry and algebra to determine when point is inside the triangle.
 * 
 * @since draw.0.assessment
 */
public class TriangleElt extends Element {
	
	int[] xs;
	int[] ys;
	public TriangleElt (Rectangle rect) {
		super(rect);
		xs = new int[3];
		ys = new int[3];
	}

	@Override
	public void drawElement(Graphics g) {
		Rectangle r = getBoundingBox();
		
		xs[0] = r.x + (r.width / 2);   ys[0] = r.y;
		xs[1] = r.x + r.width;         ys[1] = r.y + r.height;
		xs[2] = r.x;                   ys[2] = r.y + r.height;
		
		g.setColor(Color.black);
		g.drawPolygon(xs, ys, 3);
	}

	@Override
	public boolean contains(Point p) {
		Rectangle r = getBoundingBox();
		
		// not even contained in the rectangle
		if (!r.contains(p)) { return false; }
		
		// not even visible? not contained unless on line
		if (r.height == 0) { return p.x == r.x; }
		
		// for line y=mx+b the point (x0,y0) is above line if y0> > m*x0 + b.
		// Java is inverted Cartesian coordinates (0,0) in upper left so we 
		// convert to Cartesian and compute. We can set up y-intercept, b, to 0.
		double m = r.height/(r.width/2.0); 
		int y0 = (r.y + r.height) - p.y;
		
		// left half
		if (p.x < r.x + r.height/2) {
			int x0 = p.x - r.x;
			return y0 <= m*x0;
		} else {
			int x0 = p.x - (r.x + r.width); 
			System.out.println("right:" + x0 + "," + y0 + "," + m + "," + -m*x0 + "[" + !(y0 > -m*x0) + "]");
			return y0 <= -m*x0;   // slope is negative on right side.
		}
	}

	/** 
	 * Covariant overriding of {@link Element#clone} method to return OvalElt element.
	 * @return Oval Element copy.
	 */
	@Override
	public TriangleElt clone() {
		return new TriangleElt(getBoundingBox());
	}

}
