package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import view.Config.FILE_TYPE;

public class DocumentIconPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1952213928294715915L;
	private JLabel mainLabel;

	// Constructor
	public DocumentIconPanel(FILE_TYPE fileType, String fileName) {
		super();
		// TODO Auto-generated constructor stub

		this.setSize(new Dimension(Config.FILE_ICON_PANEL_SIZE,
				Config.FILE_ICON_PANEL_SIZE));
		this.setPreferredSize(new Dimension(Config.FILE_ICON_PANEL_SIZE,
				Config.FILE_ICON_PANEL_SIZE));

		// Set layout
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Set background color
		this.setBackground(Color.WHITE);

		// UI Methods
		this.configureDocumentIcon(fileType, fileName);
		// this.configureFileNameLabel();
	}

	// UI Methods
	private void configureDocumentIcon(FILE_TYPE fileType, String fileName) {

		// initialize icon
		ImageIcon icon = null;
		switch (fileType) {
		case FILE:
			icon = new ImageIcon("resource/DocumentIcon.png");
			break;
		case DIRECTORY:
			icon = new ImageIcon("resource/FolderIcon.png");
			break;
		default:
			System.out.println("error when load icon");
			break;
		}

		// Scale image
		icon.setImage(icon.getImage().getScaledInstance(Config.FILE_ICON_SIZE,
				Config.FILE_ICON_SIZE, Image.SCALE_DEFAULT));

		this.mainLabel = new JLabel(fileName, icon, JLabel.CENTER);
		this.mainLabel.setBorder(BorderFactory.createEmptyBorder(0,
				(Config.FILE_ICON_PANEL_SIZE - Config.FILE_ICON_SIZE) / 2, 0,
				(Config.FILE_ICON_PANEL_SIZE - Config.FILE_ICON_SIZE) / 2));
		this.mainLabel.setVerticalTextPosition(JLabel.BOTTOM);
		this.mainLabel.setHorizontalTextPosition(JLabel.CENTER);

		// Add to fileIconPanel
		this.add(this.mainLabel);
	}
	
	public void setSelected(boolean selected) {
		if (selected) {
			this.setBackground(Color.BLUE);
			this.mainLabel.setForeground(Color.WHITE);
		} else {
			this.setBackground(Color.WHITE);
			this.mainLabel.setForeground(Color.WHITE);
		}
	}

}
