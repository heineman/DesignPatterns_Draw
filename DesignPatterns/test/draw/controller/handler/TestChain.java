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

public class TestChain extends generic.MouseEventTestCase {
	Model model;
	DrawingPalette app;
	ActiveToolHandler ath;
	CreateHandler chain;
	
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
		
		
		chain = new CreateHandler(model, app);
		Handler two = new SelectHandler(model, app);
		chain.setNext(two);
		
		ath = new ActiveToolHandler(model, app, chain);
		app.drawingPanel().registerHandler(ath);

		app.setVisible(true);
	}
	
	@After
	public void tearDown() {
		app.setVisible(false);
	}
	
	@Test
	public void testChain() {
		PaletteEntry create = Tools.getInstance().getTool("rectangle");
		app.chooseTool(create);
		DrawingPanel panel = app.drawingPanel();
		int x = 100, y = 100;
		ath.mousePressed(createPressed(panel, x, y)); 
		
		assertTrue(chain.createdElement.isPresent());
		ath.mouseDragged(createPressed(panel, 300, 200));

		// has grown to this size.
		assertEquals(new Rectangle(x, y, 200, 100), chain.createdElement.get().getBoundingBox());
		
		int endx = 300, endy = 300;
		
		// must drag one more time before release, since that is the final coordinate retrieved.
		ath.mouseDragged(createDragged(panel, endx, endy));
		ath.mouseReleased(createReleased(panel, endx, endy));
		Element elt = model.iterator().next(); 
		assertTrue(elt.isSelected());  // newly created object is always selected.
		
		// check some anchors. SW, SE, NW, NE is the order returned
		int sz = Element.anchorSize;
		Rectangle[] anchors = elt.getAnchors();
		assertEquals(new Rectangle(x-sz, endy-sz, 2*sz, 2*sz), anchors[0]);      // SW
		assertEquals(new Rectangle(endx-sz, endy-sz, 2*sz, 2*sz), anchors[1]);   // SE
		assertEquals(new Rectangle(x-sz, y-sz, 2*sz, 2*sz), anchors[2]);         // NW
		assertEquals(new Rectangle(endx-sz, y-sz, 2*sz, 2*sz), anchors[3]);      // NE
		
		PaletteEntry select = Tools.getInstance().getTool("select");
		app.chooseTool(select);
		
		// select only realizable after MOVE events.
		ath.mouseMoved(createMoved(panel, 50, 50)); 
		ath.mousePressed(createPressed(panel, 50, 50)); 

		elt = model.iterator().next(); // get only Rect object
		assertFalse (elt.isSelected());
		
		ath.mouseMoved(createMoved(panel, 150, 150)); 
		ath.mousePressed(createPressed(panel, 150, 150)); 
		assertTrue (elt.isSelected());
	}
}
