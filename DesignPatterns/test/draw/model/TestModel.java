package draw.model;

import static org.junit.Assert.assertEquals;

import java.awt.Rectangle;
import org.junit.Test;

import draw.tools.OvalElt;

/**
 * @since draw.1
 */
public class TestModel {
	@Test
	public void testGroup() {
		Model m = new Model();
		OvalElt oval = new OvalElt(new Rectangle(500, 500, 100, 100));
		
		m.add(oval);
		int ct = 0;
		for (Element e : m) { ct++; }
		assertEquals (1, ct);
		m.clear();
		ct = 0;
		for (Element e : m) { ct++; }
		assertEquals (0, ct);
	}
}
