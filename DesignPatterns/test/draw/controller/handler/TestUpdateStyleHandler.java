package draw.controller.handler;

import static org.junit.Assert.assertEquals;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import draw.controller.visitors.CommonStyleVisitor;
import draw.model.Group;
import draw.model.Model;
import draw.model.Style;
import draw.palette.PaletteEntry;
import draw.tools.CreateTool;
import draw.tools.OvalElt;
import draw.tools.RectangleElt;
import draw.tools.ResetTools;
import draw.tools.Tools;
import draw.view.DrawingPalette;

/**
 * @since draw.2
 */
public class TestUpdateStyleHandler extends generic.MouseEventTestCase {
	Model model;
	DrawingPalette app;
	ActiveToolHandler ath;
	SelectHandler selectHandler;
	UpdateMenuHandler ch;
	OvalElt oval1;
	RectangleElt rect2;
	Group group1;
	OvalElt oval3;
	Style style1;
	Style style2;
	Style style3;
	Style style4;
	Rectangle grect;
	int ovalWidth = 80;
	int ovalHeight = 90;
	int rectWidth = 100;
	int rectHeight = 110;
	
	@Before
	public void setUp() {
		model = new Model();
		app = new DrawingPalette(model);
		
		style1 = new Style(Optional.of(Color.RED), Optional.of(Color.GREEN), Optional.of(new BasicStroke(2)));
		style2 = new Style(Optional.of(Color.RED), Optional.of(Color.BLUE), Optional.of(new BasicStroke(2)));
		style3 = new Style(Optional.of(Color.RED), Optional.of(Color.BLUE), Optional.of(new BasicStroke(4)));
		style4 = new Style(Optional.of(Color.YELLOW), Optional.of(Color.BLUE), Optional.of(new BasicStroke(4)));
		
		oval1 = new OvalElt(new Rectangle(500, 500, ovalWidth, ovalHeight));
		rect2 = new RectangleElt(new Rectangle(100, 100, rectWidth, rectHeight));
		oval1.setStyle(style1);
		rect2.setStyle(style1);
		oval3 = new OvalElt(new Rectangle(500, 500, ovalWidth, ovalHeight));
		oval3.setStyle(style2);
		
		group1 = new Group(oval1, rect2);
		grect = group1.getBoundingBox();
		model.add(group1);
		model.add(oval3);
		
		Tools repository = ResetTools.resetSingleton();
		Rectangle empty = new Rectangle(0,0,0,0);

		repository.register(new CreateTool("rectangle", new RectangleElt(empty)).paletteEntry());
		repository.register(new CreateTool("oval", new OvalElt(empty)).paletteEntry());
		
		// once done, register tools with the frame. All tools will have button for it.
		app.registerTools();
		
		// set up the chain go responsibility for all handlers
		Handler chain = new SelectHandler(model, app);
		ch = new UpdateMenuHandler(model, app);
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
		
		// start here
		group1.setSelected(true);

		CommonStyleVisitor csv = new CommonStyleVisitor();
		model.accept(csv);
		
		// validate group with same style works!
		assertEquals(Optional.of(style1), csv.getCommonStyle());
		
		group1.setSelected(false);
		csv = new CommonStyleVisitor();
		model.accept(csv);
		
		// nothing selected has no style
		assertEquals(Optional.empty(), csv.getCommonStyle());
		
		group1.setSelected(true);
		oval3.setSelected(true);
		csv = new CommonStyleVisitor();
		model.accept(csv);
		
		Style sharedStyle = new Style(Optional.of(Color.RED), Optional.empty(), Optional.of(new BasicStroke(2)));
		assertEquals(Optional.of(sharedStyle), csv.getCommonStyle());
	}
	
}
