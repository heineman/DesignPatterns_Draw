package draw.model;

import java.util.*;

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
	
}
