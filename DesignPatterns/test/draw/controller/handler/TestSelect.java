package draw.controller.handler;

import static org.junit.Assert.*;

import java.awt.Rectangle;

import org.junit.*;

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

public class TestSelect extends generic.MouseEventTestCase {
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
		 
		// select only realizable after MOVE events.
		ath.mouseMoved(createMoved(panel, 50, 50)); 
		ath.mousePressed(createPressed(panel, 50, 50)); 
		ath.mouseReleased(createReleased(panel, 50, 50)); 

		Element elt = model.iterator().next(); // get only Rect object
		assertFalse (elt.isSelected());
		
		ath.mouseMoved(createMoved(panel, 150, 150)); 
		ath.mouseReleased(createPressed(panel, 150, 150)); 
		ath.mousePressed(createReleased(panel, 150, 150)); 
		assertTrue (elt.isSelected());
		
		// apply with shift and make sure it turns off
		ath.mouseMoved(createMoved(panel, 150, 150)); 
		ath.mousePressed(addShift(createPressed(panel, 150, 150))); 
		ath.mouseReleased(createReleased(panel, 150, 150)); 
		assertFalse (elt.isSelected());
		
	}
}
