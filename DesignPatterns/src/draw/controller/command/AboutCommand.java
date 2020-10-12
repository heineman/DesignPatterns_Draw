package draw.controller.command;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import draw.model.Model;
import draw.view.DrawingPalette;

public class AboutCommand extends Command {
	public static final String appIcon = "/img/app/appIcon.png";

	public AboutCommand(Model model, DrawingPalette view) {
		super (model, view);
	}

	@Override
	public boolean execute() {
		ImageIcon icon = new ImageIcon(AboutCommand.class.getResource(appIcon));

		JOptionPane.showMessageDialog(view,
				"Sample Application demonstrating the combined use of:\n\n" +
						"  * Adapter\n" + 
						"  * Chain of Responsibility\n" + 
						"  * Command\n" + 
						"  * Composite\n" + 
						"  * Singleton \n" + 
						"  * Visitor\n" + 
						"\nGeorge Heineman",
						"Design Pattern Exercise",
						JOptionPane.INFORMATION_MESSAGE,
						icon);
		return true;
	}
}
