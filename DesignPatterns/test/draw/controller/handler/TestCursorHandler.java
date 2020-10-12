package draw.controller.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Rectangle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import draw.model.Model;
import draw.palette.PaletteEntry;
import draw.tools.CreateTool;
import draw.tools.OvalElt;
import draw.tools.RectangleElt;
import draw.tools.ResetTools;
import draw.tools.Tools;
import draw.view.DrawingPalette;
import draw.view.DrawingPanel;

/**
 * @since draw.2
 */
public class TestCursorHandler extends generic.MouseEventTestCase {
	Model model;
	DrawingPalette app;
	ActiveToolHandler ath;
	SelectHandler selectHandler;
	ChangeCursorHandler ch;
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
		
		// set up the chain go responsibility for all handlers
		Handler chain = new SelectHandler(model, app);
		ch = new ChangeCursorHandler(model, app);
		chain.
		    setNext(new CreateHandler(model, app)).
		    setNext(ch).
		    setNext(new ResizeHandler(model, app));
		ath = new ActiveToolHandler(model, app, chain);
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
		
		ath.mouseMoved(createMoved(panel, 500, 500));   // NorthWest Anchor
		java.awt.Cursor theCursor = app.drawingPanel().getCursor();
		assertEquals(theCursor, ch.nw);
		
		ath.mouseMoved(createMoved(panel, 600, 500));   // NorthEast Anchor
		theCursor = app.drawingPanel().getCursor();
		assertEquals(theCursor, ch.ne);

		ath.mouseMoved(createMoved(panel, 600, 600));   // SouthEast Anchor
		theCursor = app.drawingPanel().getCursor();
		assertEquals(theCursor, ch.se);
		
		ath.mouseMoved(createMoved(panel, 500, 600));   // SouthWest Anchor
		theCursor = app.drawingPanel().getCursor();
		assertEquals(theCursor, ch.sw);
		
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
