package edu.utsa.tagger.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import edu.utsa.layouts.Anchor;
import edu.utsa.layouts.AnchorLayout;
import edu.utsa.layouts.ListLayout;
import edu.utsa.tagger.DataWrappersHED;
import edu.utsa.tagger.DataWrappersTag;
import edu.utsa.tagger.ShellGUI;
import edu.utsa.xcomponent.XComponent;

public class TagsPanel extends XComponent
{
	JPanel list;
	
	public TagsPanel()
	{
		setOpaque(false);
		setLayout(new AnchorLayout());
		list = new JPanel();
		list.setOpaque(false);
		list.setLayout(new ListLayout(0));
		JLabel header = new JLabel("Tags");
		header.setFont(new Font("Segoe UI Light", Font.PLAIN, 36));
		header.setForeground(new Color(112, 112, 112));
		JScrollPane scrollpane = new JScrollPane();
		scrollpane.setOpaque(false);
		scrollpane.getViewport().setOpaque(false);
		scrollpane.setBorder(BorderFactory.createEmptyBorder());
		scrollpane.setViewportView(list);
		add(header, new Anchor(null, 0, Anchor.TL, true, false, 0, 0, 0, 0));
		add(scrollpane, new Anchor(header, 0, Anchor.BOTTOM, true, true, 0, 0, 0, 0));
	}
	
	public void load(DataWrappersHED hed)
	{
		list.removeAll();
		for (DataWrappersTag tag : hed.getTags())
		{
			addTagComponents(tag, 1, null);
		}
	}
	
	private void addTagComponents(DataWrappersTag tag, int level, TagsComponent parent)
	{
		TagsComponent tag_component = new TagsComponent(tag, level, parent);
		if (parent != null)
		{
			parent.getChildren().add(tag_component);
		}
		list.add(tag_component);
		parent = tag_component;
		for (DataWrappersTag t : tag.getChildTags())
		{
			addTagComponents(t, level + 1, parent);
		}
	}
	
	@Override protected void draw(Graphics2D g)
	{
		g.setColor(Color.white);
		g.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
	}
}
