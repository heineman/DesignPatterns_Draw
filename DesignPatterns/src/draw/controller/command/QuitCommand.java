package draw.controller.command;


import javax.swing.JOptionPane;

import draw.model.Model;
import draw.view.DrawingPalette;

/**
 * Exit Application
 */
public class QuitCommand extends Command {

	public QuitCommand(Model model, DrawingPalette view) {
		super (model, view);
	}
	
	@Override
	public boolean execute() {
		boolean rc = JOptionPane.showConfirmDialog (view, "Do you wish to exit Application?") == JOptionPane.OK_OPTION;
		if (!rc) {
			return false;
		}
		
		view.setVisible(false);
		view.dispose();
		System.exit(0);
		
		// never gets here...
		return true;
	}

}
