package draw.controller.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Cursor;
import java.awt.Rectangle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import draw.model.Group;
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
public class TestResizeHandler extends generic.MouseEventTestCase {
	Model model;
	DrawingPalette app;
	ActiveToolHandler ath;
	SelectHandler selectHandler;
	ChangeCursorHandler ch;
	OvalElt oval;
	RectangleElt rect;
	Group group;
	Rectangle grect;
	int ovalWidth = 80;
	int ovalHeight = 90;
	int rectWidth = 100;
	int rectHeight = 110;
	
	@Before
	public void setUp() {
		model = new Model();
		app = new DrawingPalette(model);
		
		oval = new OvalElt(new Rectangle(500, 500, ovalWidth, ovalHeight));
		
		rect = new RectangleElt(new Rectangle(100, 100, rectWidth, rectHeight));
		
		group = new Group(oval, rect);
		grect = group.getBoundingBox();
		model.add(group);
		
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
	public void testResize() {
		PaletteEntry select = Tools.getInstance().getTool("select");
		app.chooseTool(select);
		DrawingPanel panel = app.drawingPanel();
		 
		// select only realizable after MOVE events. Place inside OVAL, which triggers selection of GROUP
		ath.mouseMoved(createMoved(panel, oval.getBoundingBox().x + ovalWidth/2, oval.getBoundingBox().y + ovalHeight/2));   
		ath.mousePressed(createPressed(panel, oval.getBoundingBox().x + ovalWidth/2, oval.getBoundingBox().y + ovalHeight/2)); 
		ath.mouseReleased(createReleased(panel, oval.getBoundingBox().x + ovalWidth/2, oval.getBoundingBox().y + ovalHeight/2)); 
		
		assertFalse (oval.isSelected());
		assertFalse (rect.isSelected());
		assertTrue (group.isSelected());

		ath.mouseMoved(createMoved(panel, grect.x + grect.width, grect.y + grect.height));   // SouthEast Anchor
		Cursor theCursor = app.drawingPanel().getCursor();
		assertEquals(theCursor, ch.se);
		
		// drag down some more
		ath.mousePressed(createPressed(panel, grect.x + grect.width, grect.y + grect.height));       // SouthEast Anchor
		ath.mouseDragged(createDragged(panel, grect.x + 2*grect.width, grect.y + 2*grect.height));     // double in size
		ath.mouseReleased(createReleased(panel, grect.x + 2*grect.width, grect.y + 2*grect.height));
		
		// check sizes which should be twice as large. Nice!
		assertEquals(ovalWidth*2, oval.getBoundingBox().width);
		assertEquals(ovalHeight*2, oval.getBoundingBox().height);
		
		assertEquals(rectWidth*2, rect.getBoundingBox().width);
		assertEquals(rectHeight*2, rect.getBoundingBox().height);
		
		
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
