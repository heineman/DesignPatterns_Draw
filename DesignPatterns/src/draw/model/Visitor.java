package draw.model;

/**
 * This visitor interface returns no values. Rather it is a side-effect visitor where the return value
 * is to be retrieved directly from the subclass that implements this interface.
 */
public interface Visitor {
	
	/** 
	 * Visit each individual leaf element.
	 *  
	 * @param elt   Leaf element being visited
	 */
	public void visit(Element elt);
	
	/** 
	 * Visit a group
	 * 
	 * @param group   Group element being visited
	 */
	public void visit(Group group);
}
