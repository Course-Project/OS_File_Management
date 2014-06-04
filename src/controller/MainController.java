package controller;

import view.MainView;

// TODO - add event listener
public class MainController {
	
	private MainView view;
	
	// Constructor
	public MainController() {
		super();
		
		this.view = new MainView();
		
	}
	
	
	public void showMainView() {
		this.view.showView();
	}
}
