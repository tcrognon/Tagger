package edu.utsa.tagger.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import edu.utsa.layouts.Anchor;

public class RichTextField extends JTextField {

	Anchor anchor;
	Insets insets;
	
	public RichTextField() {
		init();
	}
	
	public RichTextField(String s) {
		super(s);
		init();
	}
	
	public RichTextField(int n) {
		super(n);
		init();
	}
	
	private void init() {
		this.setBorder(BorderFactory.createEmptyBorder());
		insets = new Insets(0, 0, 0, 0);
	}
	
	@Override protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		RoundRectangle2D r = new RoundRectangle2D.Double(0, 0, getWidth()-1, getHeight()-1, 5, 5);
		g.setPaint(new GradientPaint(0, 0, new Color(200, 200, 255), 0, getHeight(), new Color(125, 125, 255)));
		g.draw(r);
	}
	
	public void setInsets(Insets insets)
	{
		this.insets = insets;
	}
	
	@Override public Dimension getPreferredSize() {
		return new Dimension(
			super.getPreferredSize().width + insets.left + insets.right,
			super.getPreferredSize().height + insets.top + insets.bottom);
	}
}
