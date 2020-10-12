package draw.clipboard;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;

import draw.model.Element;

/** 
 * Singleton class to manage clipboard. 
 *
 * @since draw.4
 */
public class ClipboardManager implements Iterable<Element> {

	/** Singleton instance. */
	static ClipboardManager inst = null;
	
	/** 
	 * Were the last elements to be added to the clipboard copied or cut?
	 * This is important when it comes time to paste, since the behaviors
	 * are different (one shifts pasted elements by a delta). 
	 */
	boolean cutElements;
	
	/** Used for initial delta when pasting elements. */
	final Dimension initial = new Dimension(20,20);
	
	/** 
	 * Offset for repeated paste.
	 * 
	 * Updates with repeated paste requests.  
	 */
	Dimension offset = new Dimension(initial.width, initial.height);
	
	/** Contents of clipboard are initially empty. */
	Iterable<Element> contents = new ArrayList<Element>();
	
	/** Lock down the constructor for Singleton design pattern. */
	private ClipboardManager()  { }
	
	/** 
	 * Returns whether last items placed on clipboard were cut or copied. 
	 * @return whether objects placed onto clipboard were cut or copied.
	 */
	public boolean elementsCut() { return cutElements; }

	/** Shift future offsets. */
	public void updateOffset() {
		offset = new Dimension(offset.width + initial.width, offset.height + initial.height);
	}
	
	/** 
	 * With each subsequent paste, objects from the clipboard are offset a specific amount.
	 * @return  paste offset.
	 */
	public Dimension getOffset() { return offset; }
	
	/** Reset cut status once pasted. */
	public void resetCutStatus() {
		cutElements = false;
		offset = new Dimension(initial.width, initial.height);
	}
	
	/** 
	 * Singleton access. 
	 * @return singleton Clipboard
	 */
	public static ClipboardManager getInstance() {
		if (inst == null) {
			inst = new ClipboardManager();
		}
		
		return inst;
	}
	
	/** 
	 * Copy selected elements to the clipboard.
	 * @param elts  elements to be copied onto the clipboard.
	 */
	public void copy(Iterable<Element> elts) {
		contents = elts;
		cutElements = false;
		offset = new Dimension(initial.width, initial.height);
	}
	
	/** 
	 * Retrieve the contents of the clipboard as iterator.
	 * @return  iterator of elements on the clipboard
	 */
	public Iterable<Element> getClipboardContents() { 
		return contents;
	}	
	
	/** 
	 * Cut selected elements to the clipboard.
	 * @param elts  elements to be removed
	 */
	public void cut(Iterable<Element> elts) {
		contents = elts;
		cutElements = true;
	}

	@Override
	public Iterator<Element> iterator() {
		return contents.iterator();
	}
}
