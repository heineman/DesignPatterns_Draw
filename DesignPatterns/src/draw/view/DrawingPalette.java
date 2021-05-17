package draw.view;
import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import draw.controller.command.AlignLeftCommand;
import draw.controller.command.GroupCommand;
import draw.controller.command.UngroupCommand;
import draw.model.Model;
import draw.palette.PaletteEntry;
import draw.tools.Tools;

import javax.swing.GroupLayout.Alignment;

/**
 * Represents the top-level Boundary object in the application.
 */
public class DrawingPalette extends JFrame {

	/** To keep eclipse happy. */
	private static final long serialVersionUID = 5846597671139040801L;
	
	Model model;
	
	JPanel contentPane;
	DrawingPanel drawingPanel;
	JScrollPane scrollPane;
	JPanel toolPanel;
	
	JMenuItem mntmFillColor;
	JMenuItem mntmPenColor;
	JMenu strokes;
	
	/** Records the buttons constructed for available actions. */
	Hashtable<String,JButton> actionPalette = new Hashtable<>();
	
	/**
	 * Access action buttons.
	 * @return hashtable of action Buttons. 
	 */
	public Hashtable<String,JButton> getActionPalette() { return actionPalette;	}

	/**
	 * Access drawing panel.
	 * @return   Returns the {@link draw.view.DrawingPanel} inside of which elements are drawn.
	 */
	public DrawingPanel drawingPanel() { return drawingPanel; }

	/** 
	 * Access drawing area.
	 * @return  Returns {@link javax.swing.JScrollPane} scrolling pane which contains the drawing panel.
	 */
	public JScrollPane getDrawingArea() { return scrollPane; }
	
	/**
	 * Create the frame.
	 * @param   model storing information
	 */
	public DrawingPalette(Model model) {
		this.model = model;
		
		setTitle("Drawing Demonstration");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		registerMenus();
		
		drawingPanel = new DrawingPanel(model);
		drawingPanel.setPreferredSize(new Dimension(2000, 2000));
		drawingPanel.setBackground(Color.lightGray);
		scrollPane = new JScrollPane(drawingPanel);
		
		toolPanel = new JPanel();
		toolPanel.setBackground(Color.red);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(toolPanel, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(toolPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
						.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE))
					.addContainerGap())
		);
		toolPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		contentPane.setLayout(gl_contentPane);
	}
	
	public void chooseTool(PaletteEntry chosenOne) {
		Tools registry = Tools.getInstance();
		PaletteEntry current = registry.getActiveTool();
		JButton oldButton = getActionPalette().get(current.name);
		oldButton.setIcon(current.normal); // go back to normal one
		
		registry.setActiveTool(chosenOne);
		JButton newButton = getActionPalette().get(chosenOne.name);
		newButton.setIcon(chosenOne.selected);
	}
	
	/**
	 * Customizes the tools that exist.
	 */
	public void registerTools() {
		final Tools registry = Tools.getInstance();
		for (PaletteEntry entry : registry) {
			
			JButton btn = new JButton("");
			actionPalette.put(entry.name, btn);
			if (entry == registry.getActiveTool()) {
				btn.setIcon(entry.selected);
			} else {
				btn.setIcon(entry.normal);
			}
			
			btn.setPreferredSize(new Dimension(32,32));
			toolPanel.add(btn);
			
			btn.addActionListener(new ActionListener() {
				
				/** Act upon the user pressing the button. */
				@Override
				public void actionPerformed(ActionEvent e) {
					chooseTool(entry);
				}
			});
		}
	}
	
	/**
	 * Register all menus.
	 */
	void registerMenus() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnFile.add(mntmNew);
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("New not yet implemented...");
			}
		});
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnFile.add(mntmOpen);
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Open not yet implemented...");
			}
		});
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnFile.add(mntmSave);
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Save not yet implemented...");
			}
		});
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		mnFile.add(mntmQuit);
		mntmQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean rc = JOptionPane.showConfirmDialog (DrawingPalette.this, "Do you wish to exit Application?") == JOptionPane.OK_OPTION;
				if (rc) {
					setVisible(false);
					dispose();
					System.exit(0);
				}
			}
		});
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmDelete = new JMenuItem("Delete");
		mntmDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		mnEdit.add(mntmDelete);
		mntmDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Delete not yet implemented...");
			}
		});
		
		JMenuItem mntmCut = new JMenuItem("Cut");
		mntmCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		mnEdit.add(mntmCut);
		mntmCut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Cut not yet implemented...");
			}
		});
		
		JMenuItem mntmCopy = new JMenuItem("Copy");
		mntmCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		mnEdit.add(mntmCopy);
		mntmCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Copy not yet implemented...");
			}
		});
		
		JMenuItem mntmPaste = new JMenuItem("Paste");
		mntmPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
		mnEdit.add(mntmPaste);
		mntmPaste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Paste not yet implemented...");
			}
		});
		
		JSeparator separator_1 = new JSeparator();
		mnEdit.add(separator_1);
		
		JMenuItem mntmSelectAll = new JMenuItem("Select All");
		mntmSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
		mnEdit.add(mntmSelectAll);
		mntmSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Select All not yet implemented...");
			}
		});
		
		JMenuItem mntmAlignLeft = new JMenuItem("Align Left");
		mntmAlignLeft.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_MASK));
		mnEdit.add(mntmAlignLeft);
		mntmAlignLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AlignLeftCommand(model, DrawingPalette.this).execute();
			}
		});
		
		JMenu mnDesign = new JMenu("Design");
		menuBar.add(mnDesign);
		
		JMenuItem mntmGroup = new JMenuItem("Group");
		mntmGroup.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK));
		mnDesign.add(mntmGroup);
		mntmGroup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new GroupCommand(model, DrawingPalette.this).execute();
			}
		});
		
		JMenuItem mntmUngroup = new JMenuItem("Ungroup");
		mntmUngroup.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK));
		mnDesign.add(mntmUngroup);
		mntmUngroup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new UngroupCommand(model, DrawingPalette.this).execute();
			}
		});
		
		JSeparator separator_2 = new JSeparator();
		mnDesign.add(separator_2);
		
		JMenuItem mntmDuplicate = new JMenuItem("Duplicate");
		mntmDuplicate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));
		mnDesign.add(mntmDuplicate);
		mntmDuplicate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Duplicate not yet implemented...");
			}
		});
		
		JMenu mnStyle = new JMenu("Style");
		menuBar.add(mnStyle);
		
		mntmFillColor = new JMenuItem("Fill Color");
		mnStyle.add(mntmFillColor);
		mntmFillColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Fill Color not yet implemented...");
			}
		});
		
		JMenuItem mntmNoFillColor = new JMenuItem("No Fill Color");
		mnStyle.add(mntmNoFillColor);
		mntmNoFillColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("No Fill Color not yet implemented...");
			}
		});
		
		mntmPenColor = new JMenuItem("Pen Color");
		mnStyle.add(mntmPenColor);
		mntmPenColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Pen color not yet implemented...");
			}
		});
		
		strokes = new JMenu("Pen Size");
		JMenuItem stroke1 = new JMenuItem("1");
		strokes.add(stroke1);
		stroke1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Pen Size 1 not yet implemented...");
			}
		});
		
		JMenuItem stroke2 = new JMenuItem("2");
		strokes.add(stroke2);
		stroke2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Pen Size 2 not yet implemented...");
			}
		});

		JMenuItem stroke4 = new JMenuItem("4");
		strokes.add(stroke4);
		stroke4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Pen Size 4 not yet implemented...");
			}
		});
		
		JMenuItem stroke8 = new JMenuItem("8");
		strokes.add(stroke8);
		stroke8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Pen Size 8 not yet implemented...");
			}
		});
		mnStyle.add(strokes);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImageIcon icon = new ImageIcon(DrawingPalette.class.getResource("/img/app/appIcon.png"));
				
				JOptionPane.showMessageDialog(DrawingPalette.this,
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
			}
		});
	}
}
