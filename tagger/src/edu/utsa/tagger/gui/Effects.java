package edu.utsa.tagger.gui;

import java.awt.Color;
import java.awt.Component;

public final class Effects {

	private Effects() {}
	
	public static void fadeForeground(Component c, int fade) {
		Color f = c.getForeground();
		c.setForeground(new Color(
			f.getRed(),
			f.getGreen(),
			f.getBlue(),
			fade));
	}
	
	public static void fadeBackground(Component c, int fade) {
		Color f = c.getBackground();
		c.setBackground(new Color(
			f.getRed(),
			f.getGreen(),
			f.getBlue(),
			fade));
	}
	
}
