package edu.utsa.tagger.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JLabel;
import edu.utsa.layouts.Anchor;
import edu.utsa.xcomponent.XComponent;

public class RichButton extends XComponent {

	JLabel label;
	Anchor label_anchor;
	RoundRectangle2D r;
	Insets insets;
	
	public RichButton(String s) {
		label = new JLabel(s);
		label_anchor = new Anchor(null, 0, Anchor.CC, false, false, 0, 0, 0, 0);
		r = new RoundRectangle2D.Double();
		add(label, label_anchor);
		setBackground(new Color(100, 150, 235));
		setForeground(Color.white);
		insets = new Insets(0, 0, 0, 0);
	}
	
	public void setInsets(Insets insets) {
		this.insets = insets;
		label_anchor.setOffsets(insets.left, insets.top, insets.right, insets.bottom);
	}
	
	@Override public void setForeground(Color fg) {
		super.setForeground(fg);
		label.setForeground(fg);
	}
	
	@Override public void setFont(Font font) {
		super.setFont(font);
		label.setFont(font);
	}
	
	@Override protected void draw(Graphics2D g) {
		
		r.setRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
		
		if (isMouseover()) {
			g.setColor(new Color(25, 75, 225));
		}
		else {
			g.setColor(getBackground());
		}
		g.fill(r);
	}
}
