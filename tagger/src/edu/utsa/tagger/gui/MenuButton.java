package edu.utsa.tagger.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JLabel;
import edu.utsa.layouts.Anchor;
import edu.utsa.xcomponent.XComponent;

public abstract class MenuButton extends XComponent {

	private JLabel label;
	private boolean menu;
	private boolean active;
	
	public MenuButton(String s) {
		menu = true;
		active = false;
		label = new JLabel(s);
		label.setFont(new Font("Segoe UI Light", Font.PLAIN, 36));
		label.setForeground(new Color(112, 112, 112));
		add(label, new Anchor(null, 0, Anchor.TL, true, true, 0, 0, 0, 0));
	}
	
	protected void setMenu(boolean menu) {
		this.menu = menu;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	@Override protected void draw(Graphics2D g)
	{
		if (isMouseover() || active)
		{
			label.setForeground(Color.black);
		}
		else
		{
			label.setForeground(new Color(112, 112, 112));
		}
	}
}
