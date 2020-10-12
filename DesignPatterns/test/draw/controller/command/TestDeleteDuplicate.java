package draw.controller.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Rectangle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import draw.controller.handler.ActiveToolHandler;
import draw.controller.handler.SelectHandler;
import draw.model.Element;
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
 * @since draw.4
 */
public class TestDeleteDuplicate extends generic.MouseEventTestCase {
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
		oval.setSelected(true);  // IMPORTANT!
		
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
		 
		int ct = 0;
		for (Element e : model) {
			ct++;
		}
		
		assertEquals(2, ct);
		
		assertTrue(new DeleteCommand(model, app).execute());
		
		ct = 0;
		for (Element e : model) {
			ct++;
		}
		
		assertEquals(1, ct);
		
		for (Element e : model) {
			e.setSelected(true);
		}
		
		assertTrue (new DuplicateCommand(model, app).execute());
		
		ct = 0;
		for (Element e : model) {
			ct++;
		}
		
		assertEquals(2, ct);
	}
}
