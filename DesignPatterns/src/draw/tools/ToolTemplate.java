package draw.tools;

import javax.swing.ImageIcon;

import draw.model.Model;
import draw.palette.PaletteEntry;
import draw.view.DrawingPalette;

/**
 * As new tools are configured for the system, this class knows the steps that need to be completed, 
 * and the subclasses provide the low-level details.
 * 
 * 1. Create a PaletteEntry
 * 2. Register this with the Tools Repository
 * 3. Add to static chain of handlers
 * 
 * This class is responsible for maintaining a static Handler chain
 */
public abstract class ToolTemplate {
	Model model;
	DrawingPalette view;
	
	/** Images are in the <tt>/img/tools/</tt> folder. */
	public static final String imgDir = "/img/tools/";
	
	/** Images for selected tools are in the <tt>/img/tools/selected/</tt> folder. */
	public static final String imgSelectedDir = "/img/tools/selected/";
	
	protected ToolTemplate() { }
	
	/** 
	 * Access the normal icon for the tool type.
	 * @param type   Type of tool
	 * @return   return images stored in {@link #imgDir} directory.
	 */
	protected ImageIcon getNormal(String type) {
		String rsrcName = imgDir + type + ".png";
		return new ImageIcon(Tools.class.getResource(rsrcName));
	}
	
	/**
	 * Access the selected icon for the tool type.
	 * @param type    Type of tool
	 * @return   return images stored in {@link #imgSelectedDir} directory.
	 */
	protected ImageIcon getSelected(String type) {
		String rsrcName = imgSelectedDir + type + ".png";
		return new ImageIcon(Tools.class.getResource(rsrcName));
	}
	
	/** 
	 * Each subclass overrides this method to return customized PaletteEntry. 
	 * 
	 * @return customized PaletteEntry
	 */
	protected abstract PaletteEntry paletteEntry();
}
