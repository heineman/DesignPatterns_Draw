package draw.controller.json;

import java.util.ArrayList;
import java.util.stream.Collectors;

import draw.model.Group;

public class GroupElement extends Element {

	/** Parts that form the group. */
	final Group group;
	
	/** Just the IDs are necessary. */
	final ArrayList<String> partIDs;
	
	/**
	 * Group Element needs to have the ids of its parts.
	 * @param group    Group to convert
	 * @param partIDs  Known IDs
	 */
	public GroupElement(Group group, ArrayList<String> partIDs) {
		super();
		this.group = group;
		this.partIDs = partIDs;
	}

	@Override
	public String toJSON() {
		String inner = partIDs.stream()
			.map(id -> quote(id))
			.collect(Collectors.joining(","));
		
		return quote(group.toString()) + " : {\n" +
			        quote("type") + ":" + quote(group.getClass().getName()) + ",\n" +
			        quote("parts") + ": [" + inner + "]\n}";
	}
}
