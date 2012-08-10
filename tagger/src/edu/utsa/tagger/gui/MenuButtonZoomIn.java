package edu.utsa.tagger.gui;

import edu.utsa.tagger.ShellGUI;

public class MenuButtonZoomIn extends MenuButton {
	
	public MenuButtonZoomIn() {
		super("+");
		setMenu(false);
	}
	
	@Override public void clicked() {
		ShellGUI.zoomIn();
	}
	
	@Override public void entered() {
//		App.clearControlPanel();
	}
}
