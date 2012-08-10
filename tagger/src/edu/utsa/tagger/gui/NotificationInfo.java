package edu.utsa.tagger.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.Rectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import edu.utsa.layouts.Anchor;
import edu.utsa.xcomponent.XComponent;

public class NotificationInfo extends XComponent {

	private JTextArea notification;
	private Insets insets;

	public NotificationInfo() {
		setVisible(false);
		notification = new JTextArea(); 
		notification.setEditable(false); 
//		notification.setLineWrap(true); 
		notification.setOpaque(false); 
		notification.setBorder(BorderFactory.createEmptyBorder()); 
		notification.setForeground(Color.white);
		setBackground(new Color(0, 0, 150, 150));
		insets = new Insets(10, 20, 10, 20);
		add(notification, new Anchor(null, 0, Anchor.TL, false, false, 0, 0, 0, 0));
	}

	@Override protected void draw(Graphics2D g) {
		Rectangle2D r = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
		g.setColor(getBackground());
		g.fill(r);
	}

	@Override public Dimension getPreferredSize() {
		return new Dimension(
			notification.getPreferredSize().width + insets.left + insets.right,
			notification.getPreferredSize().height + insets.top + insets.bottom);
	}

	public void showInfo(String s) {
		notification.setText(s);
		setVisible(true);
	}
	
	public void hideInfo()
	{
		setVisible(false);
	}

}
