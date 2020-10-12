package draw.controller.command;

import draw.model.Model;
import draw.view.DrawingPalette;

/**
 * Create JSON string for model.
 */
public class NewCommand extends Command {

	public NewCommand(Model model, DrawingPalette view) {
		super (model, view);
	}
	

	@Override
	public boolean execute()  {
		
		// clear out old model and add top-level objects before refreshing view.
		model.clear();
		
		view.repaint();
		return true;
	}
}
