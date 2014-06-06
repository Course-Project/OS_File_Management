package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import view.Config.FILE_TYPE;

public class FileIconPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1952213928294715915L;
	private ImageIcon icon;
	private JLabel fileNameLabel;

	// Constructor
	public FileIconPanel(FILE_TYPE fileType) {
		super();
		// TODO Auto-generated constructor stub
		
		this.setSize(new Dimension(Config.FILE_ICON_PANEL_SIZE, Config.FILE_ICON_PANEL_SIZE));
		this.setPreferredSize(new Dimension(Config.FILE_ICON_PANEL_SIZE, Config.FILE_ICON_PANEL_SIZE));
		
		// Set layout
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Set alignment
		this.setAlignmentX(CENTER_ALIGNMENT);
		this.setAlignmentY(CENTER_ALIGNMENT);
		
		// UI Methods
		this.configureIcon(fileType);
		this.configureFileNameLabel();
	}
	
	// UI Methods
	private void configureIcon(FILE_TYPE fileType) {
		System.out.println("configure icon");
		// initialize icon
		switch(fileType) {
		case FILE:
			this.icon = new ImageIcon("resource/DocumentIcon.png");
			break;
		case DIRECTORY:
			this.icon = new ImageIcon("resource/FolderIcon.png");
			break;
		default:
			System.out.println("error when load icon");
			break;
		}
		
		this.icon.setImage(this.icon.getImage().getScaledInstance(Config.FILE_ICON_SIZE, Config.FILE_ICON_SIZE, Image.SCALE_DEFAULT));
		
		JLabel t = new JLabel(this.icon);
		
		t.setBackground(Color.BLACK);
		t.setForeground(Color.CYAN);
		
		this.add(t);
	}
	
	private void configureFileNameLabel() {
		// initialize fileNameLabel
		this.fileNameLabel = new JLabel("sadfsda");
		
		this.add(this.fileNameLabel);
	}

}
