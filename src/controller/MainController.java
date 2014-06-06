package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

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
		this.view.addDocumentIconPanelMouseListener(this.documentIconPanelMouseListener);
	}
	
	public void showMainView() {
		this.view.showView();
	}
	
	// Listener
	private MouseListener rightClickListener = new MouseListener() {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			if (e.getButton()==MouseEvent.BUTTON3)
			{
				JPopupMenu menu=new JPopupMenu();
				JMenuItem openMenu=new JMenuItem("打开");
				menu.add(openMenu);
				
				JMenuItem deleteAll=new JMenuItem("格式化");
				menu.add(deleteAll);
				
				menu.show(e.getComponent(),e.getX(),e.getY());
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
			((DocumentIconPanel)e.getComponent()).setSelected(true);
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
}
