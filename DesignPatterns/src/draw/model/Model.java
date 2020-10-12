package draw.model;

import java.util.*;

/**
 * Represents the collection of all Entity objects in the application.
 */
public class Model implements Iterable<Element> {
	List<Element> topLevelElements = new ArrayList<>();
	
	public Model() {

	}

	public void add(Element e) {
		topLevelElements.add(e);
	}
	
	public void remove(Element e) {
		topLevelElements.remove(e);
	}
	
	public void clear() {
		topLevelElements.clear();
	}

	@Override
	public Iterator<Element> iterator() {
		return topLevelElements.iterator();
	}
	
	/**
	 * Model supports the request to accept a visitor for all toplevel elements.
	 * This will recursively be applied to all sub-elements in grouped elements.
	 */
	public void accept(Visitor v) {
		for (Element e : topLevelElements) {
			e.accept(v);
		}
	}
}
