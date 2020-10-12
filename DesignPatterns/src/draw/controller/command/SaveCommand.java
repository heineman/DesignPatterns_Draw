package draw.controller.command;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import draw.controller.json.JSON_Export;
import draw.model.Element;
import draw.model.Group;
import draw.model.Model;
import draw.model.Style;
import draw.model.Visitor;
import draw.view.DrawingPalette;

/**
 * Create JSON string for model.
 */
public class SaveCommand extends Command {
	
	public static final String drawExtension = "draw";

	/** Pulls together all styles, so they can be consolidated when duplicated. */
	Hashtable<Style,String> styleRepository = new Hashtable<>();

	/** Pull together group info. */
	Hashtable<Group,ArrayList<String>> partIDs = new Hashtable<>();

	public SaveCommand(Model model, DrawingPalette view) {
		super (model, view);
	}

	/** 
	 * Fills styles with all unique styles, assigning unique string ids to each one.
	 * Works because of hashCode() and equals() methods in Style class. 
	 * 
	 * Also constructs group info for each grouped object (its parts list).
	 */
	class InformationExtractor implements Visitor {
		int id = 1;

		@Override
		public void visit(Element elt) {
			Style style = elt.getStyle();
			if (!styleRepository.containsKey(style)) {
				styleRepository.put(style, "style" + id);
				id++;
			}
		}

		@Override
		public void visit(Group group) {
			ArrayList<String> parts = new ArrayList<>();
			for (Element elt : group) {
				parts.add(elt.toString());
			}
			partIDs.put(group, parts);
		} 
	}

	static String getExtension(File f) {

	    String ext = null;
	    String s = f.getName();
	    int i = s.lastIndexOf('.');

	    if (i > 0 &&  i < s.length() - 1) {
	        ext = s.substring(i+1).toLowerCase();
	    }
	    return ext;
	}
	
	@Override
	public boolean execute() {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("DRAW files", drawExtension);
		fileChooser.setFileFilter(filter);
		int option = fileChooser.showSaveDialog(view);

		if (option == JFileChooser.APPROVE_OPTION) {
			File output = fileChooser.getSelectedFile();
			String extension = getExtension(output);
			if (extension == null || !extension.equals(drawExtension)) {
				output = new File (output.getAbsolutePath() + "." + drawExtension);
			}
			return saveToFile(output);
		} else {
			return false;
		}
	}

	/** Helper method for storing information to disk. */
	boolean saveToFile(File output) {
		// extract all common styles and partID lists.
		model.accept(new InformationExtractor());   

		JSON_Export jv = new JSON_Export(styleRepository, partIDs);
		model.accept(jv);
		
		try (PrintWriter pw = new PrintWriter(output)) {
			pw.print(jv.genEncoding());
			return true;
		} catch (java.io.IOException ioe) {
			ioe.printStackTrace();
			return false;
		}
	}
}
