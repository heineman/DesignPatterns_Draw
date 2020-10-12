package draw.visitors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.awt.Rectangle;

import org.junit.Test;

import draw.controller.IActionInterface;
import draw.controller.visitors.AnchorVisitor;
import draw.model.Element;
import draw.model.Group;
import draw.model.Model;
import draw.tools.OvalElt;
import draw.tools.RectangleElt;

public class TestAnchorVisitor {
	
	void validateElement(Model m, Element elt, Rectangle rect) {
		Point pt = new Point(rect.x + rect.width/2, rect.y + rect.height/2); // in middle, not on any anchor
		AnchorVisitor av = new AnchorVisitor(pt);
		m.accept(av);
		
		assertFalse(av.getElement().isPresent());
		assertEquals(IActionInterface.NoAnchor, av.getCorner());
		
		av = new AnchorVisitor(new Point(rect.x, rect.y));
		m.accept(av);
		assertTrue(av.getElement().isPresent());
		assertTrue(elt == av.getElement().get());
		assertEquals(IActionInterface.NorthWestAnchor, av.getCorner());
		
		av = new AnchorVisitor(new Point(rect.x+rect.width, rect.y));
		m.accept(av);
		assertTrue(av.getElement().isPresent());
		assertTrue(elt == av.getElement().get());
		assertEquals(IActionInterface.NorthEastAnchor, av.getCorner());
		
		av = new AnchorVisitor(new Point(rect.x, rect.y+rect.height));
		m.accept(av);
		assertTrue(av.getElement().isPresent());
		assertTrue(elt == av.getElement().get());
		assertEquals(IActionInterface.SouthWestAnchor, av.getCorner());
		
		av = new AnchorVisitor(new Point(rect.x+rect.width, rect.y+rect.height));
		m.accept(av);
		assertTrue(av.getElement().isPresent());
		assertTrue(elt == av.getElement().get());
		assertEquals(IActionInterface.SouthEastAnchor, av.getCorner());
	}
	
	@Test
	public void testAnchors() {
		Model m = new Model();
		OvalElt oval = new OvalElt(new Rectangle(500, 300, 100, 100));
		Rectangle rect = oval.getBoundingBox();
		m.add(oval);
		oval.setSelected(true);   // IMPORTANT! Otherwise anchor points are not evident
		
		RectangleElt r1 = new RectangleElt(new Rectangle(10, 20, 30, 40));
		RectangleElt r2 = new RectangleElt(new Rectangle(70, 20, 30, 40));
		Group g = new Group(r1, r2);
		Rectangle grect = g.getBoundingBox();
		m.add(g);
		g.setSelected(true); // both selected.
		
		// works on both elements AND grouped elements...
		validateElement(m, oval, rect);
		validateElement(m, g, grect);
	}
}
