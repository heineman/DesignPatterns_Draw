package draw.controller.command;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import draw.controller.handler.ActiveToolHandler;
import draw.controller.handler.SelectHandler;
import draw.model.Element;
import draw.model.Group;
import draw.model.Model;
import draw.model.Style;
import draw.tools.CreateTool;
import draw.tools.OvalElt;
import draw.tools.RectangleElt;
import draw.tools.ResetTools;
import draw.tools.Tools;
import draw.view.DrawingPalette;

/**
 * @since draw.1
 */
public class TestFileStorage {

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
		
		OvalElt oval = new OvalElt(new Rectangle(500, 500, 100, 100));
		oval.setStyle(Style.defaultStyle.setFillColor(Color.red));
		
		RectangleElt rect = new RectangleElt(new Rectangle(100, 100, 300, 200));
		rect.setStyle(Style.defaultStyle.setPenColor(Color.green));
		
		group = new Group (rect, oval);
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
	public void testGroup() {
		File outputFile;
		try {
			outputFile = File.createTempFile("testing", "." + SaveCommand.drawExtension);
			
			assertEquals(SaveCommand.drawExtension, SaveCommand.getExtension(outputFile));
			assertTrue(new SaveCommand(model, app).saveToFile(outputFile));
			
			assertTrue(new NewCommand(model, app).execute());
			
			assertTrue(new OpenCommand(model, app).openFromFile(outputFile));
			
			int ct = 0;
			for (Element e : model) {
				ct++;
			}
			
			assertEquals(1, ct);
			System.out.println("removing " + outputFile);
			outputFile.delete();
			
		} catch (IOException e) {
			e.printStackTrace();
			fail("Unable to create temp file.");
		}
		
		
	}
}
