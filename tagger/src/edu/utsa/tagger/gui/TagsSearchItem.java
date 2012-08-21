package edu.utsa.tagger.gui;

import javax.swing.JLabel;
import edu.utsa.layouts.Anchor;
import edu.utsa.tagger.DataWrappersTag;
import edu.utsa.tagger.ShellGUI;
import edu.utsa.xcomponent.XComponent;

public class TagsSearchItem extends XComponent
{
	DataWrappersTag tag;
	JLabel label = new JLabel();
	
	public TagsSearchItem(DataWrappersTag tag)
	{
		this.tag = tag;
		label.setText(tag.getPath());
		add(label, new Anchor(null, 0, Anchor.TL, true, false, 0, 0, 0, 0));
	}
	
	@Override public void clicked()
	{
		ShellGUI.scrollTo(tag);
	}
}
