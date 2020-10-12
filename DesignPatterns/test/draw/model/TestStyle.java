package draw.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.Optional;

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
import draw.controller.command.FillCommand;
import draw.controller.command.PenCommand;
import draw.controller.command.StrokeCommand;

/**
 * Augmented to also validate UngroupCommand in draw.2
 * 
 * @since draw.1
 */
public class TestStyle extends generic.MouseEventTestCase {
	Model model;
	DrawingPalette app;
	ActiveToolHandler ath;
	SelectHandler selectHandler;
	OvalElt oval;
	RectangleElt rect;
	Group group;
	
	@Before
	public void setUp() {
		model = new Model();
		app = new DrawingPalette(model);
		
		oval = new OvalElt(new Rectangle(500, 500, 100, 100));
		oval.setStyle(Style.defaultStyle);  // important
		
		rect = new RectangleElt(new Rectangle(100, 100, 300, 200));
		rect.setStyle(Style.defaultStyle);  // important
		group = new Group (rect, oval);
		group.setSelected(true);   // IMPORTANT
		model.add(group);
		
		Tools repository = ResetTools.resetSingleton();
		Rectangle empty = new Rectangle(0,0,0,0);

		repository.register(new CreateTool("rectangle", new RectangleElt(empty)).paletteEntry());
		repository.register(new CreateTool("oval", new OvalElt(empty)).paletteEntry());
		
		// once done, register tools with the frame. All tools will have button for it.
		app.registerTools();
		app.updateStyle(Style.defaultStyle);   // IMPORTANT
		
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
	public void testStyleChanges() {
		PaletteEntry select = Tools.getInstance().getTool("select");
		app.chooseTool(select);
		
		// act as if user had selected style
		Style newStyle = app.getStyle().setFillColor(Color.red).setPenColor(Color.green).setStroke(new BasicStroke(2));
		app.updateStyle(newStyle);
		
		// affects selected elements
		new FillCommand(model, app, true).complete(Optional.of(Color.red));
		new StrokeCommand(model, app, 2).execute();
		new PenCommand(model, app).complete(Color.green);
		
		assertEquals (oval.getStyle(), newStyle);
		assertEquals (rect.getStyle(), newStyle);
		
		new FillCommand(model, app, false).complete(Optional.empty());
		assertFalse (oval.getStyle().fillColor.isPresent());
		
		
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
	public void testStyleObject() {
		Style s = Style.defaultStyle;
		assertTrue (s.fillColor.isPresent());
		Style other = s.clearFillColor();
		assertFalse (other.fillColor.isPresent());
	}
}
