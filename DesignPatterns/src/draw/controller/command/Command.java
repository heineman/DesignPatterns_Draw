package draw.controller.command;

import draw.model.Model;
import draw.view.DrawingPalette;

/**
 * Base class for each Commmand.
 * 
 * Conforms to the Command Design Pattern.
 * 
 * @since draw.1
 */
public abstract class Command {

	Model model;
	DrawingPalette view;
	
	public Command(Model model, DrawingPalette view) {
		this.model = model;
		this.view = view;
	}	
	
	/**
	 * Invoke the command. 
	 * 
	 * @return status of execution.
	 */
	public abstract boolean execute();
}
