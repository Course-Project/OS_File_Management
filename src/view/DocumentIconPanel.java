package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.sys.Config.FILE_TYPE;

public class DocumentIconPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1952213928294715915L;
	private JLabel mainLabel;
	private String filename;
	private FILE_TYPE type;

	// Constructor
	public DocumentIconPanel(FILE_TYPE type, String filename) {
		super();
		// TODO Auto-generated constructor stub
		
		this.filename = filename;
		this.type = type;

		this.setSize(new Dimension(Config.FILE_ICON_PANEL_SIZE,
				Config.FILE_ICON_PANEL_SIZE));
		this.setPreferredSize(new Dimension(Config.FILE_ICON_PANEL_SIZE,
				Config.FILE_ICON_PANEL_SIZE));

		// Set layout
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Set background color
		this.setBackground(Color.WHITE);

		// UI Methods
		this.configureDocumentIcon(type, filename);
		// this.configureFileNameLabel();
	}

	// UI Methods
	private void configureDocumentIcon(FILE_TYPE type, String filename) {

		// initialize icon
		ImageIcon icon = null;
		switch (type) {
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

		this.mainLabel = new JLabel(filename, icon, JLabel.CENTER);
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
			this.mainLabel.setForeground(Color.BLACK);
		}
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public FILE_TYPE getType() {
		return type;
	}

	public void setType(FILE_TYPE type) {
		this.type = type;
	}

}
