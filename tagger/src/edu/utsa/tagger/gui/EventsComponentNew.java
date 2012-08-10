package edu.utsa.tagger.gui;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import edu.utsa.layouts.Anchor;
import edu.utsa.tagger.ShellGUI;
import edu.utsa.xcomponent.XComponent;

public class EventsComponentNew extends XComponent {

	RichTextField text;
	RichButton button;
	
	public EventsComponentNew() {
		text = new RichTextField("test");
		text.setFont(new Font("Arial", Font.PLAIN, 12));
		button = new RichButton("Add") {
			@Override public void clicked(XComponent xcomponent) {
				String s = text.getText();
				if (s != null && s.length() > 0) {
					text.setText("");
					ShellGUI.insertEvent(s);
				}
			}
		};
		button.setInsets(new Insets(5, 5, 5, 5));
		text.setInsets(new Insets(5, 5, 4, 5));
		add(button, new Anchor(null, 0, Anchor.TR, false, false, 0, 2, 2, 2));
		add(text, new Anchor(null, 0, Anchor.TL, true, false, 2, 2, 2, 2));
	}
	
	@Override protected void draw(Graphics2D g) {
		if ((float) text.getFont().getSize() != ShellGUI.getBaseFontSize()) {
			text.setFont(getFont().deriveFont(ShellGUI.getBaseFontSize()));
		}
		if ((float) button.getFont().getSize() != ShellGUI.getBaseFontSize()) {
			button.setFont(getFont().deriveFont(ShellGUI.getBaseFontSize()));
		}
	}
}
