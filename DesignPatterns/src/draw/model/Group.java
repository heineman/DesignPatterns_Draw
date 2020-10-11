package draw.model;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Treat a group of Elements as an Element in its own right.
 * 
 * Essential part of the Composite design pattern.
 * 
 * <img src="doc-files/composite.png" alt="Composite Pattern">
 *  
 * @since draw.1
 */
public class Group extends Element implements Iterable<Element> {

	/** Parts that form the group. */
	List<Element> parts = new ArrayList<Element>();
	
	/**
	 * The initial rectangle will likely be computed from a collection of elements
	 * that are being grouped together.
	 * 
	 * Computes cached_bb based on elements.
	 * 
	 * Properly updates parent link for elements
	 * @param elements    Variable-length collection of Elements (an array works also)
	 */
	public Group(Element... elements) {
		super();
		
		bbox = EmptyRectangle;
		for (Element e : elements) {
			parts.add(e);
			if (bbox.isEmpty()) {
				bbox = e.getBoundingBox();
			} else {
				bbox.add(e.getBoundingBox());
			}
		}
	}

	/** Draw a group by individually drawing each element. */
	@Override
	public void drawElement(Graphics g) {
		for (Element elt : parts) {
			elt.draw(g);
		}
	}

	@Override
	public Iterator<Element> iterator() {
		return parts.iterator();
	}

	@Override
	public Group clone() {
		Group groupClone = new Group();
		for (Element elt : parts) {
			Element clone = elt.clone();
			groupClone.parts.add(clone);
		}
		
		return groupClone;
	}

	/** Check for containment by comparing any of sub elements. */
	@Override
	public boolean contains(Point p) {
		for (Element elt : parts) {
			if (elt.contains(p)) { return true; }
		}

		return false;
	}
	
}