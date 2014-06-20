package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import model.sys.FCB;

// TODO - layout views
public class EditView extends JFrame implements DocumentListener {

	private static final long serialVersionUID = 5359647733388619559L;
	private JTextPane textPane;

	private FCB dataFCB;
	
	public boolean saveOnExit = true;
	public boolean edited = false;

	public EditView(FCB fcb, String content) {
		super();

		// initialize
		this.dataFCB = fcb;
		this.textPane = new JTextPane();

		this.configureMenuBar();
		this.configureTextPane(content);

		// Main View
		this.configureJFrame();
	}

	// UI Method
	private void configureJFrame() {
		this.setTitle("Edit");// Set title
		this.setSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);// Set size of
																// window
		this.setResizable(false);// Can't change the size
		this.setLocationRelativeTo(null);// Set the position of the window -
											// Screen's Center
		this.setBackground(Color.WHITE);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private void configureMenuBar() {
		// Components
		JMenuBar menuBar;
		JMenu elevatorMenu;
		JMenu helpMenu;
		JMenuItem elevatorMenuItem;
		JMenuItem helpMenuItem;

		// Create the Menu Bar
		menuBar = new JMenuBar();

		// Build Elevator Menu
		elevatorMenu = new JMenu("File System");
		elevatorMenu.setMnemonic(KeyEvent.VK_E);

		// Add Menu Items to Menu "Elevator"
		elevatorMenuItem = new JMenuItem("Quit", KeyEvent.VK_Q);
		elevatorMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				ActionEvent.CTRL_MASK));
		elevatorMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(DISPOSE_ON_CLOSE);
			}

		});
		elevatorMenu.add(elevatorMenuItem);

		// Build About Menu
		helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);

		// Add Menu Items to Menu "About"
		helpMenuItem = new JMenuItem("About", KeyEvent.VK_A);
		helpMenu.add(helpMenuItem);

		helpMenuItem = new JMenuItem("Help", KeyEvent.VK_H);
		helpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
				ActionEvent.CTRL_MASK));
		helpMenu.add(helpMenuItem);

		// Add Menus "File" and "Help" to Menu Bar
		menuBar.add(elevatorMenu);
		menuBar.add(helpMenu);

		// Add Components
		this.setJMenuBar(menuBar);
	}

	private void configureTextPane(String content) {
		this.textPane.setText(content);
		this.textPane.getDocument().addDocumentListener(this);
		this.add(this.textPane, BorderLayout.CENTER);
	}

	/**
	 * 获取当前TextPane的内容
	 * 
	 * @return TextPane中所有文字内容
	 */
	public String getContent() {
		return this.textPane.getText();
	}
	
	public FCB getDataFCB() {
		return dataFCB;
	}

	// Document Listener
	@Override
	public void insertUpdate(DocumentEvent e) {
		this.edited = true;
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		this.edited = true;
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		
	}

}
