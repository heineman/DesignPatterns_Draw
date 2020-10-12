package draw.controller.command;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

// retrieve from https://code.google.com/archive/p/json-simple/downloads
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import draw.model.Element;
import draw.model.Group;
import draw.model.Model;
import draw.model.Style;
import draw.view.DrawingPalette;

/**
 * Create JSON string for model.
 */
public class OpenCommand extends Command {

	/** Pulls together all styles, so they can be consolidated when duplicated. */
	Hashtable<Style,String> styleRepository= new Hashtable<>();
	Hashtable<String,Style> revStyleRepository= new Hashtable<>();
	
	/** Pull together group info. */
	Hashtable<Group,ArrayList<String>> partIDs= new Hashtable<>();
	
	/** Object repository. */
	Hashtable<String, Element> objectRepository= new Hashtable<>();
	
	/** Groups to process. */
	Hashtable<String, JSONObject> groupRepository= new Hashtable<>();
	
	public OpenCommand(Model model, DrawingPalette view) {
		super (model, view);
	}
	
	Color getColor(JSONObject colorEncoding) {
		long red = (long) colorEncoding.get("red");
		long green = (long) colorEncoding.get("green");
		long blue = (long) colorEncoding.get("blue");
		
		return new Color((int)red, (int)green, (int)blue);
	}
	
	/**
	  
	 "styles":{
		  "style2":{"fill":{"red":255,"green":0,"blue":0},"pen":{"red":0,"green":0,"blue":0},"stroke":1},
		 ...
	  },
	 
	 */
	void loadStyles(JSONObject styles) {
		for (Object key : styles.keySet()) {
			String styleid = (String) key;
			Style style = Style.defaultStyle;
			JSONObject styleEncoding = (JSONObject) styles.get(key);
			
			if (styleEncoding.containsKey("fill")) {
				style = style.setFillColor(getColor((JSONObject)(styleEncoding.get("fill"))));
			}
			if (styleEncoding.containsKey("pen")) {
				style = style.setPenColor(getColor((JSONObject)(styleEncoding.get("pen"))));
			}
			if (styleEncoding.containsKey("stroke")) {
				style = style.setStroke(new BasicStroke((long)(styleEncoding.get("stroke"))));
			}
			
			styleRepository.put(style,  styleid);
			revStyleRepository.put(styleid, style);
		}
	}
	
	
	/** 
	 * Parse up a JSON Object for a rectangle [x,y,width,height]. JSON assumes
	 * integer values are long; we know they are integer, so we parse and then cast. 
	 */
	Rectangle getRectangle(JSONObject elt) {
		long x = 0;
		long y = 0;
		long width = 0;
		long height = 0;
		
		if (elt.containsKey("x")) {
			x = (long)(elt.get("x"));
		}
		if (elt.containsKey("y")) {
			y = (long)(elt.get("y"));
		}
		if (elt.containsKey("width")) {
			width = (long)(elt.get("width"));
		}
		if (elt.containsKey("height")) {
			height = (long)(elt.get("height"));
		}
		
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}
	

	/**
	 * Load up a group. Recursively call processGroup if not found, because
	 * we know there are no cycles in dependency chain.
	 * 
	 */
	Group loadGroup(String eltId, JSONObject parts) {
		JSONArray array = (JSONArray) parts.get("parts");
		ArrayList<Element> elements = new ArrayList<>();
		for (Object oid : array) {
			String id = (String) oid;
			
			// if not found yet, recursively bring it in
			if (!objectRepository.containsKey(id)) {
				processGroup(id);
				groupRepository.remove(id);
			}
			
			// now it must exist.
			Element existing = objectRepository.get(id);
			elements.add( existing);  
			objectRepository.remove(id);  // placed into group! so it can be removed from repo which should only contain top-level 
		}

		return new Group(elements.toArray(new Element[0]));
	}
	
	/** Process this element. If already in objectRepository, we can discard. */
	void processGroup(String eltId) {
		if (objectRepository.containsKey(eltId)) {
			return;
		}
		Group g = loadGroup(eltId, groupRepository.get(eltId));
		objectRepository.put(eltId, g);
		groupRepository.remove(eltId);  // no longer to be processed.
	}
	
	/**
	 
	"elements":{ 
	   "std.tool.RectangleElt@1ee0776" : {
          "height":50,"x":50,"width":150,"style":"style1","y":50,"type":"std.tool.RectangleElt"},
      ...
     } 
      
	 */
	void loadElements(JSONObject elements) {
		for (Object key : elements.keySet()) {
			String eltId = (String) key;
			
			JSONObject val = (JSONObject) elements.get(key);
			String type = (String) val.get("type");   // find type of this element.
			String styleId = (String) val.get("style");
			Rectangle rect = getRectangle(val);
			
			if (type.equals(Group.class.getName())) {
				// load up a group and record part
				groupRepository.put(eltId, val);
			} else {
				// invoke constructor by class name, with rectangle as the single argument.
				// All elements are added to object repository
				try {
					@SuppressWarnings("unchecked")
					Class<Element> clazz = (Class<Element>) Class.forName(type);
					Constructor<Element> ctor = clazz.getConstructor(Rectangle.class);
					Element elt = ctor.newInstance(rect);
					elt.setStyle(revStyleRepository.get(styleId));
					objectRepository.put(eltId, elt);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}			
		}
		
		// do these in any order, processing groups recursively
		// as you encounter them.
		ArrayList<String> toProcess = new ArrayList<>();
		toProcess.addAll(groupRepository.keySet());
		for (String eltId : toProcess) {
			if (groupRepository.containsKey(eltId)) {
				processGroup(eltId);
			}
		}
	}
	
	@Override
	public boolean execute() {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("DRAW files", "draw");
		fileChooser.setFileFilter(filter);
        int option = fileChooser.showOpenDialog(view);
       
        if (option == JFileChooser.APPROVE_OPTION) {
           return openFromFile(fileChooser.getSelectedFile());
        } else {
           return false;
        }
	}

	/** Helper method to process from file. */
	boolean openFromFile(File input)  {
		JSONObject topLevel = null;
		try (Scanner sc = new Scanner (input)) {
			StringBuffer sb = new StringBuffer();
			while (sc.hasNextLine()) {
				sb.append(sc.nextLine());
			}
			
			topLevel = (JSONObject) new JSONParser().parse(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		loadStyles((JSONObject) topLevel.get("styles"));		
		loadElements((JSONObject)topLevel.get("elements"));
		
		// clear out old model and add top-level objects before refreshing view.
		model.clear();
		for (Element e : objectRepository.values()) {
			model.add(e);
		}
		
		view.repaint();
		return true;
	}
}
