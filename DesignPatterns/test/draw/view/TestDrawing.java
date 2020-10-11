package draw.view;

import java.awt.Rectangle;

import org.junit.*;

import draw.model.Model;
import draw.tools.OvalElt;
import draw.tools.RectangleElt;

public class TestDrawing extends generic.MouseEventTestCase {
	Model model;
	DrawingPalette app;
	
	@Before
	public void setUp() {
		model = new Model();
		app = new DrawingPalette(model);

		OvalElt oval = new OvalElt(new Rectangle(500, 500, 100, 100));
		model.add(oval);
		
		RectangleElt rect = new RectangleElt(new Rectangle(100, 100, 300, 200));
		model.add(rect);
		
		app.setVisible(true);
	}
	
	@After
	public void tearDown() {
		app.setVisible(false);
	}
	
	@Test
	public void testHandler() {
		// last long enough to make visible. This is not ideal but it will ensure the graphics have
		// a chance to at least draw the two elements.
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
