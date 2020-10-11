package draw.palette;

import javax.swing.Icon;

import draw.model.Element;

/**
 * User requests to create a new selected element (i.e., Rectangle or Oval).
 * 
 * There will me multiple create PaletteEntry objects, differentiated only by their prototype. There is 
 * going to be a single CreateHandler that reacts and chooses the element to create based on this prototype. 
 * 
 * Class supports using the Prototype design pattern to create new elements based on the prototype.
 */
public class Create extends PaletteEntry {
	
	public static final String createName = "create";
	
	/** Prototype element from which all new ones are cloned, during create. */
	public final Element prototype;
	
	/**
	 * The create tool requires a prototype class from which all subsequent created ones are generated.
	 * 
	 * @param createName  name to use, which embeds the prototype object's name for uniqueness
	 * @param normal      normal icon to draw.
	 * @param selected    selected icon to draw 
	 * @param prototype   Element to use for prototype.
	 */
	public Create (String createName, Icon normal, Icon selected, Element prototype) {
		super(createName, normal, selected, Action.Create);
		this.prototype = prototype;
	}
	
}
