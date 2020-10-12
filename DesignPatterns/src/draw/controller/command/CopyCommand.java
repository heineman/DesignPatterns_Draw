package draw.controller.command;

import java.util.ArrayList;

import draw.clipboard.ClipboardManager;
import draw.model.Element;
import draw.model.Model;
import draw.view.DrawingPalette;

/**
 * Copy selected elements from model and put into clipboard.
 */
public class CopyCommand extends Command {

	public CopyCommand(Model model, DrawingPalette view) {
		super (model, view);
	}
	
	@Override
	public boolean execute()  {
		
		// clear out old model and add top-level objects before refreshing view.
		ArrayList<Element> toCopy = new ArrayList<>();
		for (Element e : model) {
			if (e.isSelected()) {
				Element copy = e.clone();
				copy.setSelected(false);
				toCopy.add(copy);
			}
		}
		
		ClipboardManager clipboard = ClipboardManager.getInstance();
		clipboard.copy(toCopy);
		
		return true;
	}
}
