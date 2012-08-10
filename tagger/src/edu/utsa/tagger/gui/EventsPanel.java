package edu.utsa.tagger.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.UUID;
import javax.swing.JLabel;
import javax.swing.JPanel;
import edu.utsa.layouts.Anchor;
import edu.utsa.layouts.AnchorLayout;
import edu.utsa.layouts.ListLayout;
import edu.utsa.tagger.DataWrappersEvent;
import edu.utsa.tagger.DataWrappersSelectedTag;
import edu.utsa.xcomponent.XComponent;

public class EventsPanel extends XComponent
{
	private JPanel list;
	private EventsComponentNew new_item;
	
	public EventsPanel() {
		setPreferredSize(new Dimension(250, 0));
		setLayout(new AnchorLayout());
		list = new JPanel();
		list.setOpaque(false);
		list.setLayout(new ListLayout(0));
		new_item = new EventsComponentNew();
		JLabel header = new JLabel("Events");
		header.setForeground(new Color(112, 112, 112));
		header.setFont(new Font("Segoe UI Light", Font.PLAIN, 36));
		add(header, new Anchor(null, 0, Anchor.TL, true, false, 0, 0, 0, 0));
		add(list, new Anchor(header, 0, Anchor.BOTTOM, true, false, 0, 0, 0, 0));
		add(new_item, new Anchor(list, 0, Anchor.BOTTOM, true, false, 0, 0, 0, 0));
	}
	
	@Override protected void draw(Graphics2D g)
	{
		Rectangle2D r = new Rectangle2D.Double(0, 0, getWidth(), getHeight()); 
		g.setColor(Color.white);
		g.fill(r);
		
	}
	
	public void load(Collection<DataWrappersEvent> items, Collection<DataWrappersSelectedTag> selected_tags)
	{
		list.removeAll();
		for (DataWrappersEvent item : items)
		{
			list.add(new EventsComponent(item));
		}
		for (Component c : list.getComponents())
		{
			EventsComponent ic = (EventsComponent) c;
			for (DataWrappersSelectedTag st : selected_tags)
			{
				UUID uuid1 = ic.getEvent().getUuid();
				UUID uuid2 = st.getItemUuid();
				UUID uuid3 = st.getTagUuid();
				if (uuid1.equals(uuid2))
				{
					DataWrappersSelectedTag selected_tag = new DataWrappersSelectedTag(uuid1, uuid3);
					ic.addSelectedTag(selected_tag);
				}
			}
		}
		revalidate();
	}
}
