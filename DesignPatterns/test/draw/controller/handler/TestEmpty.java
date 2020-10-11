package draw.controller.handler;

import java.awt.Rectangle;

import org.junit.*;

import draw.model.Model;
import draw.palette.Action;
import draw.palette.PaletteEntry;
import draw.tools.CreateTool;
import draw.tools.OvalElt;
import draw.tools.RectangleElt;
import draw.tools.ResetTools;
import draw.tools.Tools;
import draw.view.DrawingPalette;
import draw.view.DrawingPanel;

public class TestEmpty extends generic.MouseEventTestCase {
	Model model;
	DrawingPalette app;
	ActiveToolHandler ath;
	Handler chain;
	
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
		

		OvalElt oval = new OvalElt(new Rectangle(500, 500, 100, 100));
		model.add(oval);
		
		RectangleElt rect = new RectangleElt(new Rectangle(100, 100, 300, 200));
		rect.setSelected(true);  // IMPORTANT!
		model.add(rect);
		
		Handler emptyHandler = new Handler(model, app) { 

			@Override
			protected boolean interested(Action action) {
				return true;
			}
			 
		};
		
		Handler create = new CreateHandler(model, app);
		Handler select = new SelectHandler(model, app);
		emptyHandler.setNext(create);
		create.setNext(select);
		
		ath = new ActiveToolHandler(model, app, emptyHandler);
		app.drawingPanel().registerHandler(ath);

		app.setVisible(true);
	}
	
	@After
	public void tearDown() {
		app.setVisible(false);
	}
	
	@Test
	public void testHandler() {
		PaletteEntry create = Tools.getInstance().getTool("select");
		app.chooseTool(create);
		DrawingPanel panel = app.drawingPanel();
		
		// these are on the anchors
		int x = 100;
		int y = 100;
		ath.mouseMoved(createMoved(panel, x, y));      // always lead with the move...
		ath.mousePressed(createPressed(panel, x, y)); 
		
		x = 110;
		y = 110;
		ath.mouseDragged(createDragged(panel, x, y));
		ath.mouseReleased(createReleased(panel, x, y));

		x = 50;
		y = 50;
		ath.mouseMoved(createMoved(panel, x, y));
		ath.mousePressed(createPressed(panel, x, y)); 
		ath.mouseDragged(createDragged(panel, x, y));
		ath.mouseReleased(createReleased(panel, x, y));
		

		
	}
}
