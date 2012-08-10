package edu.utsa.tagger.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import edu.utsa.layouts.Anchor;
import edu.utsa.xcomponent.XComponent;

public class RichTextField extends XComponent {

	JTextField text;
	Anchor anchor;
	Insets insets;
	
	public RichTextField() {
		text = new JTextField();
		init();
	}
	
	public RichTextField(String s) {
		text = new JTextField(s);
		init();
	}
	
	public RichTextField(int n) {
		text = new JTextField(n);
		init();
	}
	
	private void init() {
		text.setOpaque(false);
		text.setBorder(BorderFactory.createEmptyBorder());
		insets = new Insets(0, 0, 0, 0);
		anchor = new Anchor(null, 0, Anchor.TL, true, true, 0, 0, 0, 0);
		add(text, anchor);
	}
	
	@Override protected void draw(Graphics2D g) {
		RoundRectangle2D r = new RoundRectangle2D.Double(0, 0, getWidth()-1, getHeight()-1, 5, 5);
		g.setPaint(new GradientPaint(0, 0, new Color(200, 200, 255), 0, getHeight(), new Color(125, 125, 255)));
		g.draw(r);
	}
	
	public void setInsets(Insets insets) {
		this.insets = insets;
		anchor.setOffsets(insets.left, insets.top, insets.right, insets.bottom);
		revalidate();
	}
	
	public String getText() {
		return text.getText();
	}
	
	public void setText(String s) {
		this.text.setText(s);
	}
	
	@Override public void setFont(Font font) {
		text.setFont(font);
		super.setFont(font);
	}
	
	@Override public Dimension getPreferredSize() {
		return new Dimension(
			text.getPreferredSize().width + insets.left + insets.right,
			text.getPreferredSize().height + insets.top + insets.bottom);
	}
}
