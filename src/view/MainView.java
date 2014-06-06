package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

import view.Config.FILE_TYPE;

// TODO - layout views
public class MainView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6313156717813295316L;
	
	public JButton backButton;
	public JTextField addressTextField;
	public JButton goButton;
	public JTextField searchTextField;
	public JButton searchButton;
	public JPanel contentPanel;
	
	
	// Constructor
	public MainView() {
		super();
	}
	
	// UI Method
	private void configureJFrame() {
		this.setTitle("File System Simulator");// Set title
		this.setSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);// Set size of window
		this.setResizable(false);// Can't change the size
		this.setLocationRelativeTo(null);// Set the position of the window -
											// Screen's Center
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
		elevatorMenu = new JMenu("Elevator");
		elevatorMenu.setMnemonic(KeyEvent.VK_E);

		// Add Menu Items to Menu "Elevator"
		elevatorMenuItem = new JMenuItem("Quit", KeyEvent.VK_Q);
		elevatorMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				ActionEvent.CTRL_MASK));
		elevatorMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(EXIT_ON_CLOSE);
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
	
	private void configureToolPanel() {
		// initialize toolPanel
		JPanel toolPanel = new JPanel();
		
		// for debug
		toolPanel.setBackground(Color.BLACK);
		
		// Set Layout
		toolPanel.setLayout(new BoxLayout(toolPanel, BoxLayout.X_AXIS));
		toolPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
		
		
		
		// initialize backButton
		this.backButton = new JButton("Back");
		
		
		
		// initialize addressTextField
		this.addressTextField = new JTextField("test");
		
		
		
		// initialize goButton
		this.goButton = new JButton("Go");
		
		
		
		// initialize searchTextField
		this.searchTextField = new JTextField("search");
		
		
		
		// initialize searchButton
		this.searchButton = new JButton("search");
		
		
		// Add to toolPanel
		toolPanel.add(this.backButton);
		toolPanel.add(this.addressTextField);
		toolPanel.add(this.goButton);
		toolPanel.add(this.searchTextField);
		toolPanel.add(this.searchButton);
		
		// Add to mainView
		this.add(toolPanel, BorderLayout.PAGE_START);
	}
	
	private void configureContentScrollPane() {
		// initialize contentPanel
		this.contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		// Add component listener for contentPanel
		this.contentPanel.addComponentListener(new ComponentListener() {

			@Override
			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub
				System.out.println("resized");
				
				Dimension d = MainView.this.contentPanel.getPreferredSize();
				int con = MainView.this.contentPanel.getComponents().length;
				int col = Config.WINDOW_WIDTH / (Config.FILE_ICON_PANEL_SIZE + 5);
				int row = con / col + 1;
				int newHeight = row * (Config.FILE_ICON_PANEL_SIZE + 5) + 5;
				d.height = newHeight;
				d.width = Config.WINDOW_WIDTH;
				MainView.this.contentPanel.setPreferredSize(d);
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				System.out.println("moved");
			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				System.out.println("shown");
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				System.out.println("hidden");
			}
		});
		
		// For test
		for (int i = 0; i < 60; i++ ) {
			FileIconPanel t = new FileIconPanel(FILE_TYPE.FILE);
			
			this.contentPanel.add(t);
		}
		
		// initialize content scroll pane
		JScrollPane contentScrollPane = new JScrollPane(this.contentPanel);
		contentScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		// Add to mainView
		this.add(contentScrollPane, BorderLayout.CENTER);
	}
	
	public void showView() {
		// UI Methods
		this.configureMenuBar();
		this.configureToolPanel();
		this.configureContentScrollPane();
		
		// Main View
		this.configureJFrame();
		
		// Show View
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
