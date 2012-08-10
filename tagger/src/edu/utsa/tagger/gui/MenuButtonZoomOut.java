package edu.utsa.tagger.gui;

import edu.utsa.tagger.ShellGUI;

public class MenuButtonZoomOut extends MenuButton {
	
	public MenuButtonZoomOut() {
		super("-");
		setMenu(false);
	}
	
	@Override public void clicked() {
		ShellGUI.zoomOut();
	}
	
	@Override public void entered() {
//		App.clearControlPanel();
	}

}
