package draw.controller.handler;

import java.awt.Rectangle;

import org.junit.*;

import draw.model.Model;
import draw.palette.PaletteEntry;
import draw.tools.CreateTool;
import draw.tools.OvalElt;
import draw.tools.RectangleElt;
import draw.tools.ResetTools;
import draw.tools.Tools;
import draw.view.DrawingPalette;
import draw.view.DrawingPanel;

public class TestAnchor extends generic.MouseEventTestCase {
	Model model;
	DrawingPalette app;
	ActiveToolHandler ath;
	SelectHandler selectHandler;
	
	@Before
	public void setUp() {
		model = new Model();
		app = new DrawingPalette(model);
		
		Tools repository = ResetTools.resetSingleton();
		Rectangle empty = new Rectangle(0,0,0,0);

		repository.register(new CreateTool("rectangle", new RectangleElt(empty)).paletteEntry());
		repository.register(new CreateTool("oval", new OvalElt(empty)).paletteEntry());
		
		// once done, register tools with the frame. All tools will have button for it.
		app.registerTools();
		
		
		RectangleElt rect = new RectangleElt(new Rectangle(100, 100, 300, 200));
		rect.setSelected(true);  // IMPORTANT!
		model.add(rect);

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
	public void testSelect() {
		PaletteEntry select = Tools.getInstance().getTool("select");
		app.chooseTool(select);
		DrawingPanel panel = app.drawingPanel();
		 
		// Move into the anchor zone for NorthWest and press.
		ath.mouseMoved(createMoved(panel, 100, 100)); 
		ath.mousePressed(createPressed(panel, 50, 50)); 

		// now drag.
		ath.mouseDragged(createDragged(panel, 150, 150)); 
		ath.mouseReleased(createReleased(panel, 150, 150)); 
	}
}
