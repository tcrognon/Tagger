package edu.utsa.xcomponent;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JLayeredPane;

public class XContentPane extends JLayeredPane {

	@Override protected void paintComponent(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
}
