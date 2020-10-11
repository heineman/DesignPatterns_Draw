package draw;

import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import draw.controller.handler.ActiveToolHandler;
import draw.controller.handler.CreateHandler;
import draw.controller.handler.Handler;
import draw.controller.handler.SelectHandler;
import draw.model.Element;
import draw.model.Group;
import draw.model.Model;
import draw.tools.CreateTool;
import draw.tools.OvalElt;
import draw.tools.RectangleElt;
import draw.tools.Tools;
import draw.view.DrawingPalette;

/**
 * Helper class that contains {@link #main(String[])} method to launch application.
 */
public class Main { 

	/** 
	 * Launch the application.
	 * @param args    initial arguments from command line. 
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				// Scaffolding: populate with instances oval1, rect1, rect2 to have something
				// to work with initially
				Model model = new Model();
				model.add(new RectangleElt(new Rectangle(20, 20, 100, 100)));
				model.add(new RectangleElt(new Rectangle(40, 40, 100, 100)));
				model.add(new OvalElt(new Rectangle(300, 20, 80, 80))); 
				
				Element r1 = new RectangleElt(new Rectangle(180, 20, 100, 100));
				Element o1 = new OvalElt(new Rectangle(180, 140, 50, 50));
				Group group = new Group(r1, o1);
				Element r2 = new RectangleElt(new Rectangle(50, 200, 80, 80));
				Group group2 = new Group(group, r2);
				model.add(group2);

				// Create GUI from this initial model
				DrawingPalette frame = new DrawingPalette(model);

				// install tools to use. Highly configurable
				Rectangle empty = new Rectangle(0,0,0,0);
				
				Tools repository = Tools.getInstance();
				repository.register(new CreateTool("rectangle", new RectangleElt(empty)).paletteEntry());
				repository.register(new CreateTool("oval", new OvalElt(empty)).paletteEntry());

				// once done, register tools with the frame. All tools will have button for it.
				frame.registerTools();
				
				// set up the chain go responsibility for all handlers
				Handler chain = new SelectHandler(model, frame);
				chain.setNext(new CreateHandler(model, frame));
				frame.drawingPanel().registerHandler(new ActiveToolHandler(model, frame, chain));
				
				// Confirm any attempt to exit
				frame.addWindowListener(new WindowAdapter() {

					@Override
					public void windowClosing(WindowEvent e) {
						boolean rc = JOptionPane.showConfirmDialog (frame, "Do you wish to exit Application?") == JOptionPane.OK_OPTION;
						if (rc) {
							frame.setVisible(false);
							frame.dispose();
							System.exit(0);
						}
					}
				});
				
				frame.setVisible(true);
			}
		});
	}
}
