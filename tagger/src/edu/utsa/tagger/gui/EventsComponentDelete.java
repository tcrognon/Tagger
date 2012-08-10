package edu.utsa.tagger.gui;

import java.awt.Color;
import java.awt.Font;
import java.util.UUID;
import javax.swing.JLabel;
import edu.utsa.layouts.Anchor;
import edu.utsa.tagger.ShellGUI;
import edu.utsa.xcomponent.XComponent;

public class EventsComponentDelete extends XComponent {

	JLabel label;
	UUID item_uuid;
	
	public EventsComponentDelete(UUID item_uuid) {
		setVisible(false);
		this.item_uuid = item_uuid;
		label = new JLabel("x");
		label.setFont(new Font("Arial", Font.BOLD, 12));
		label.setForeground(Color.red);
		add(label, new Anchor(null, 0, Anchor.TL, false, false, 2, 2, 2, 2));
	}
	
	@Override public void clicked(XComponent xcomponent) {
		ShellGUI.removeEvent(item_uuid);
	}
}
