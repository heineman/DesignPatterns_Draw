package draw.model;

import static org.junit.Assert.assertEquals;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import draw.tools.OvalElt;
import draw.tools.RectangleElt;

/**
 * @since draw.1
 */
public class TestGroupEntity {

	@Test
	public void testGroup() {
		OvalElt oval = new OvalElt(new Rectangle(500, 500, 100, 100));
		
		RectangleElt rect = new RectangleElt(new Rectangle(100, 100, 300, 200));
		
		Group g = new Group (oval, rect);
		Group g2 = g.clone();
		
		List<Element> gparts = new ArrayList<>();
		for (Element e : g) { gparts.add(e); }
		List<Element> g2parts = new ArrayList<>();
		for (Element e : g2) { g2parts.add(e); }
		
		assertEquals(gparts.get(0).getBoundingBox(), g2parts.get(0).getBoundingBox());
		assertEquals(gparts.get(1).getBoundingBox(), g2parts.get(1).getBoundingBox());
	}
}
