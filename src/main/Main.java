package main;

import view.MainView;

public class Main {

	public static void main(String[] args) {
		MainView view = MainView.getInstance();
		view.setVisible(true);
	}
	
}