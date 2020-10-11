package draw.palette;

import static org.junit.Assert.assertEquals;

import java.awt.Rectangle;

import org.junit.Test;

import draw.tools.CreateTool;
import draw.tools.RectangleElt;

public class TestPaletteEntry {

	@Test
	public void testMethods() {
		Rectangle empty = new Rectangle(0,0,0,0);
		CreateTool ct = new CreateTool("rectangle", new RectangleElt(empty));
		PaletteEntry create = ct.paletteEntry();
		assertEquals ("rectangle", create.name);
	}
}
