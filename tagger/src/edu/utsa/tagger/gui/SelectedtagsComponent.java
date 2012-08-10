package edu.utsa.tagger.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JLabel;
import edu.utsa.layouts.Anchor;
import edu.utsa.layouts.AnchorLayout;
import edu.utsa.tagger.DataWrappersSelectedTag;
import edu.utsa.tagger.DataWrappersTag;
import edu.utsa.tagger.ShellGUI;
import edu.utsa.xcomponent.XComponent;

public class SelectedtagsComponent extends XComponent
{
	private DataWrappersTag tag;
	private JLabel label;
	
	public SelectedtagsComponent(DataWrappersSelectedTag selectedtag)
	{
		setDraggable(true);
		tag = ShellGUI.getTag(selectedtag.getTagUuid());
		if (tag == null) {
			throw new RuntimeException("Could not find tag for SelectedTagComponent.");
		}
		label = new JLabel(tag.getPath());
		label.setFont(new Font("Segoe UI", Font.PLAIN, (int) ShellGUI.getBaseFontSize()));
		setLayout(new AnchorLayout());
		add(label, new Anchor(Anchor.TL, true, false));
	}
	
	@Override protected void draw(Graphics2D g)
	{
		if (isMouseover())
		{
			label.setForeground(Color.black);
		}
		else
		{
			label.setForeground(this.getForeground());
		}
		
		if ((float) label.getFont().getSize() != ShellGUI.getBaseFontSize())
		{
			label.setFont(getFont().deriveFont(ShellGUI.getBaseFontSize()));
		}
	}
	
	@Override public void setForeground(Color fg)
	{
		super.setForeground(fg);
		label.setForeground(fg);
	}
	
	@Override public String getDragText()
	{
		return tag.getPath();
	}
	
	@Override public void entered()
	{
		ShellGUI.showInfo(tag.getPath() + "\n" + tag.getDescription());
	}
	
	@Override public void exited()
	{
		ShellGUI.hideInfo();
	}

}
