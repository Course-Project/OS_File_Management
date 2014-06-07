package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import view.Config.FILE_TYPE;
import view.DocumentIconPanel;
import view.MainView;

// TODO - add event listener
public class MainController {

	private MainView view;

	// Constructor
	public MainController() {
		super();

		// UI Methods
		this.configureMainView();

	}

	// UI Methods
	private void configureMainView() {
		this.view = new MainView();

		this.view.addRightClickListener(this.rightClickListener);

		this.configureContentPanel();
	}

	private void configureContentPanel() {
		this.view
				.addDocumentIconPanelMouseListener(this.documentIconPanelMouseListener);
	}

	public void showMainView() {
		this.view.showView();
	}

	// Listener
	private MouseListener rightClickListener = new MouseListener() {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			// Deselect documents
			if (e.getButton() == MouseEvent.BUTTON1) {
				MainController.this.view.deselectDocuments();
			}

			// Popup menu
			if (e.getButton() == MouseEvent.BUTTON3) {
				JPopupMenu menu = new JPopupMenu();
				JMenuItem newFileMenu = new JMenuItem("New File", KeyEvent.VK_N);
				newFileMenu.setAccelerator(KeyStroke.getKeyStroke(
						KeyEvent.VK_N, ActionEvent.CTRL_MASK));
				newFileMenu
						.addActionListener(MainController.this.newFileActionListener);
				menu.add(newFileMenu);

				JMenuItem newFolderMenu = new JMenuItem("New Folder",
						KeyEvent.VK_F);
				newFolderMenu.setAccelerator(KeyStroke.getKeyStroke(
						KeyEvent.VK_F, ActionEvent.CTRL_MASK));
				newFolderMenu
						.addActionListener(MainController.this.newFolderActionListener);
				menu.add(newFolderMenu);

				menu.show(e.getComponent(), e.getX(), e.getY());
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	};

	private MouseListener documentIconPanelMouseListener = new MouseListener() {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			// Select current document
			MainController.this.view.deselectDocuments();
			((DocumentIconPanel) e.getComponent()).setSelected(true);

			// Double click
			if (e.getClickCount() == 2) {
				System.out.println("Double Click");
			}

			// Right click
			if (e.getButton() == MouseEvent.BUTTON3) {
				JPopupMenu documentMenu = new JPopupMenu();
				JMenuItem openMenuItem = new JMenuItem("Open", KeyEvent.VK_O);
				documentMenu.add(openMenuItem);

				JMenuItem editMenuItem = new JMenuItem("Edit", KeyEvent.VK_E);
				documentMenu.add(editMenuItem);

				JMenuItem renameMenuItem = new JMenuItem("Rename",
						KeyEvent.VK_R);
				documentMenu.add(renameMenuItem);

				documentMenu.addSeparator();

				JMenuItem getInfoMenuItem = new JMenuItem("Get info",
						KeyEvent.VK_I);
				documentMenu.add(getInfoMenuItem);

				documentMenu.addSeparator();

				JMenuItem deleteAll = new JMenuItem("Delete", KeyEvent.VK_D);
				documentMenu.add(deleteAll);

				documentMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	};

	private ActionListener newFileActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			DocumentIconPanel d = new DocumentIconPanel(FILE_TYPE.FILE,
					"QWEhetyjR");
			d.addMouseListener(MainController.this.documentIconPanelMouseListener);
			MainController.this.view.addDocument(d);
		}

	};

	private ActionListener newFolderActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			DocumentIconPanel d = new DocumentIconPanel(FILE_TYPE.DIRECTORY,
					"QWEerythfghetyjR");
			d.addMouseListener(MainController.this.documentIconPanelMouseListener);
			MainController.this.view.addDocument(d);
		}

	};
}
