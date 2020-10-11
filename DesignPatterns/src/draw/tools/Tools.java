package draw.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import draw.palette.PaletteEntry;
import draw.palette.Select;

/**
 * Registry of all tools.
 * 
 * Follows Singleton Design pattern.
 * 
 * Only one tool is active at a time. The select tool is known in advance and is stored once it is registered.
 */
public class Tools implements Iterable<PaletteEntry> {
	/** Singleton instance. */
	static Tools instance;
	
	/** Available tools. */
	List<PaletteEntry> list = new ArrayList<>();
	
	/** Which tool is active. */
	PaletteEntry active;
	
	/** The Select tool is special, and must always present */
	PaletteEntry select;
	
	/** Instantiate and pre-load Select, which is always present. */
	private Tools() { 
		active = select = new SelectTool().paletteEntry();
		register(select); 
	}
	
	/** 
	 * Retrieve singleton instance of Tools.
	 * @return   Singleton instance.
	 */
	public static Tools getInstance() {
		if (instance == null) {
			instance = new Tools();
		}
		
		return instance;
	}

	/** 
	 * Access the selected tool.
	 * 
	 * This is a special tool that always exists.
	 * @return    Return the (always available) {@link draw.palette.Select Select} action.
	 */
	public PaletteEntry getSelectTool() {
		return select;
	}
	
	/** 
	 * Retrieves the desired tool by name.
	 * @param name  desired tool
	 * @return  PaletteEntry with the give name, should it exist.
	 */
	public PaletteEntry getTool(String name) {
		for (PaletteEntry pe: list) {
			if (pe.name.equals(name)) { return pe; }
		}
		
		return null;
	}
	
	/**
	 * Set active tool.
	 * 
	 * @param entry  entry to select
	 * @throws IllegalArgumentException if no entry already exists to matches entry
	 */
	public void setActiveTool(PaletteEntry entry) {
		if (!list.contains(entry)) {
			throw new IllegalArgumentException("Entry already exists:" + entry);
		}
		
		active = entry;
	}

	/** 
	 * Get active tool.
	 * @return  Action associated with the active tool.
	 * @throws IllegalStateException if no active tool yet set.
	 */
	public PaletteEntry getActiveTool() throws IllegalStateException {
		return active;
	}
	
	/**
	 * Register a new entry with the tool repository.
	 * 
	 * The first added tool becomes the active one. When a tool with name {@link Select#selectName} is added,
	 * its entry is stored as being the special Select tool. 
	 * 
	 * @param entry new entry to be added 
	 * @throws IllegalArgumentException if entry already exists that matches entry.
	 */
	public void register(PaletteEntry entry) {
		if (list.contains(entry)) {
			throw new IllegalArgumentException("Entry already exists:" + entry);
		}
		
		// choose first tool as the active one.
		list.add(entry);
	}

	/** Returns list of all registered actions. */
	@Override
	public Iterator<PaletteEntry> iterator() {
		return list.iterator();
	}
}
 