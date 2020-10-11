package draw.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Rectangle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import draw.controller.handler.ActiveToolHandler;
import draw.controller.handler.SelectHandler;
import draw.palette.PaletteEntry;
import draw.tools.CreateTool;
import draw.tools.OvalElt;
import draw.tools.RectangleElt;
import draw.tools.ResetTools;
import draw.tools.Tools;
import draw.view.DrawingPalette;
import draw.view.DrawingPanel;
import draw.controller.command.GroupCommand;

/**
 * @since draw.1
 */
public class TestGroup extends generic.MouseEventTestCase {
	Model model;
	DrawingPalette app;
	ActiveToolHandler ath;
	SelectHandler selectHandler;
	OvalElt oval;
	RectangleElt rect;
	
	@Before
	public void setUp() {
		model = new Model();
		app = new DrawingPalette(model);
		
		oval = new OvalElt(new Rectangle(500, 500, 100, 100));
		model.add(oval);
		
		rect = new RectangleElt(new Rectangle(100, 100, 300, 200));
		model.add(rect);
		
		Tools repository = ResetTools.resetSingleton();
		Rectangle empty = new Rectangle(0,0,0,0);

		repository.register(new CreateTool("rectangle", new RectangleElt(empty)).paletteEntry());
		repository.register(new CreateTool("oval", new OvalElt(empty)).paletteEntry());
		
		// once done, register tools with the frame. All tools will have button for it.
		app.registerTools();
		
		selectHandler = new SelectHandler(model, app);
		ath = new ActiveToolHandler(model, app, selectHandler);
		app.drawingPanel().registerHandler(ath);

		app.setVisible(true);
	}
	
	@After
	public void tearDown() {
		app.setVisible(false);
	}
	
	@Test
	public void testMultipleSelections() {
		PaletteEntry select = Tools.getInstance().getTool("select");
		app.chooseTool(select);
		DrawingPanel panel = app.drawingPanel();
		 
		// select only realizable after MOVE events.
		ath.mouseMoved(createMoved(panel, 550, 550));   // middle of oval
		ath.mousePressed(createPressed(panel, 550, 550)); 
		ath.mouseReleased(createReleased(panel, 550, 550));
		
		assertTrue (oval.isSelected());
		assertFalse (rect.isSelected());
		
		ath.mouseMoved(createMoved(panel, 150, 150));   // inside rectangle
		ath.mousePressed(createPressed(panel, 150, 150)); 
		ath.mouseReleased(createReleased(panel, 150, 150));
		
		assertFalse (oval.isSelected());
		assertTrue (rect.isSelected());		
		
		// now multiple shift to pick up oval
		ath.mouseMoved(createMoved(panel, 550, 550));   // middle of oval
		ath.mousePressed(addShift(createPressed(panel, 550, 550)));
		ath.mouseReleased(createReleased(panel, 550, 550));
		
		assertTrue (oval.isSelected());
		assertTrue (rect.isSelected());		
		
		// group together
		new GroupCommand(model, app).execute();
		
		int count = 0;
		for (Element e : model) {
			assertTrue(e.isSelected());
			count++;
		}
		
		assertEquals(1, count);
		
		// unselect
		ath.mouseMoved(createMoved(panel, 10, 10));   // middle of oval
		ath.mousePressed(createPressed(panel, 10, 10));
		ath.mouseReleased(createReleased(panel, 10, 10));
		
		for (Element e : model) {
			assertFalse (e.isSelected());
		}
		
		// select in the group by pressing on one of its elements
		ath.mouseMoved(createMoved(panel, 150, 150));   // inside rectangle in group
		ath.mousePressed(createPressed(panel, 150, 150)); 
		ath.mouseReleased(createReleased(panel, 150, 150));
		
		for (Element e : model) {
			assertTrue (e.isSelected());
		}
		
		// last long enough to make visible. This is not ideal but it will ensure the graphics have
		// a chance to at least draw the two elements.
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testMultipleSelectionsWithDrag() {
		PaletteEntry select = Tools.getInstance().getTool("select");
		app.chooseTool(select);
		DrawingPanel panel = app.drawingPanel();
		 
		// select only realizable after MOVE events.
		ath.mouseMoved(createMoved(panel, 10, 10));   // nowhere
		ath.mousePressed(createPressed(panel, 10, 10)); 
		ath.mouseDragged(createDragged(panel, 1500, 1500));   // drag over everything
		ath.mouseReleased(createReleased(panel, 1500, 1500));
		
		assertTrue (oval.isSelected());
		assertTrue (rect.isSelected());
	}
}
