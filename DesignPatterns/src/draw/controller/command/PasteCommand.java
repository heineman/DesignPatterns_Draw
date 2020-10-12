package draw.controller.command;

import java.awt.Dimension;

import draw.clipboard.ClipboardManager;
import draw.controller.visitors.MoveIfSelectedVisitor;
import draw.model.Element;
import draw.model.Model;
import draw.view.DrawingPalette;

/**
 * Paste selected elements from model and put into clipboard.
 * 
 * If elements had been copied, then all newly pasted objects are offset
 * by 20,20.  If they had been cut, then no offset is performed. All newly
 * pasted objects are selected.
 */
public class PasteCommand extends Command {

	public PasteCommand(Model model, DrawingPalette view) {
		super (model, view);
	}
	
	@Override
	public boolean execute()  {
		
		// reset any selections
		for (Element e : model) {
			e.setSelected(false);
		}
		
		// clear out old model and add top-level objects before refreshing view.
		ClipboardManager clipboard = ClipboardManager.getInstance();
		
		if (clipboard.elementsCut()) {
			for (Element e : clipboard) {
				Element copy = e.clone();
				copy.setSelected(true);
				model.add(copy);
			}
			
			clipboard.resetCutStatus();  // once pasted, reset status.
		} else {
			for (Element e : clipboard) {
				Element copy = e.clone();
				copy.setSelected(true);
				
				// can now invoke this move visitor BECAUSE e is selected.
				Dimension offset = clipboard.getOffset();
				copy.accept(new MoveIfSelectedVisitor(offset.width, offset.height));
				
				model.add(copy);
			}
			
			clipboard.updateOffset();
		}
		
		view.repaint();
		return true;
	}
}
