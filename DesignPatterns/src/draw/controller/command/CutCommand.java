package draw.controller.command;

import java.util.ArrayList;

import draw.clipboard.ClipboardManager;
import draw.model.Element;
import draw.model.Model;
import draw.view.DrawingPalette;

/**
 * Cut selected elements from model and put into clipboard.
 */
public class CutCommand extends Command {

	public CutCommand(Model model, DrawingPalette view) {
		super (model, view);
	}
	
	@Override
	public boolean execute()  {
		
		// clear out old model and add top-level objects before refreshing view.
		ArrayList<Element> toRemove = new ArrayList<>();
		for (Element e : model) {
			if (e.isSelected()) {
				toRemove.add(e);     // real thing. Not cloned.
				e.setSelected(false);
			}
		}
		
		for (Element e : toRemove) {
			model.remove(e);
		}
		
		ClipboardManager clipboard = ClipboardManager.getInstance();
		clipboard.cut(toRemove);
		
		view.repaint();
		return true;
	}
}
