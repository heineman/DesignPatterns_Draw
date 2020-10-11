package draw.controller.tools;

import static org.junit.Assert.assertEquals;

import java.awt.Rectangle;

import org.junit.Test;

import draw.tools.OvalElt;
import draw.tools.RectangleElt;

public class TestOvalElt {

	@Test
	public void testOval() {
		OvalElt oval = new OvalElt(new Rectangle(500, 500, 100, 100));
		OvalElt two = oval.clone();
		
		assertEquals(oval.getBoundingBox(), two.getBoundingBox());
	}
	
	@Test
	public void testRectangle() {
		RectangleElt rect = new RectangleElt(new Rectangle(500, 500, 100, 100));
		RectangleElt two = rect.clone();
		
		assertEquals(rect.getBoundingBox(), two.getBoundingBox());
	}
}
