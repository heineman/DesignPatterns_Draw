package draw.controller.json;

/**
 * Return JSON string for each element in the model.
 * 
 * This minimal structure also aligns with Composite, though there is no need for a visitor here.
 */
public abstract class Element {

	/** 
	 * Add quotes around the given string. 
	 * @param s  string to embed
	 * @return quoted string
	 */
	public String quote(String s) {
		return "\"" + s + "\"";
	}
	
	/** 
	 * Convert to JSON.
	 * @return  JSON string for element
	 */
	public abstract String toJSON();
	
	public static final String styles = "styles";
	public static final String elements = "elements";
}
