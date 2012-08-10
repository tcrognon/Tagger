package edu.utsa.tagger.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import javax.swing.JLabel;
import javax.swing.JPanel;
import edu.utsa.layouts.Anchor;
import edu.utsa.layouts.AnchorLayout;
import edu.utsa.layouts.ListLayout;
import edu.utsa.tagger.DataWrappersEvent;
import edu.utsa.tagger.DataWrappersSelectedTag;
import edu.utsa.tagger.ErrorStack;
import edu.utsa.tagger.ShellGUI;
import edu.utsa.xcomponent.XComponent;

public class EventsComponent extends XComponent {

	private DataWrappersEvent event;
	private JLabel label;
	private JPanel selectedtags_panel;
	private EventsComponentDelete delete_button;
	
	public EventsComponent(DataWrappersEvent item)
	{
		setDroppable(true);
		this.event = item;
		selectedtags_panel = new JPanel();
		selectedtags_panel.setLayout(new ListLayout(0));
		selectedtags_panel.setOpaque(false);
		setLayout(new AnchorLayout());
		label = new JLabel(item.getLabel());
		label.setFont(new Font("Segoe UI", Font.PLAIN, (int) ShellGUI.getBaseFontSize()));
		delete_button = new EventsComponentDelete(item.getUuid());
		
		this.setLayer(delete_button, 1);
		this.setLayer(label, 0);
		
		add(delete_button, new Anchor(null, 1, Anchor.TR, false, false, 2, 0, 2, 0));
		add(label, new Anchor(null, 0, Anchor.TL, false, false, 3, 2, 0, 0));
		add(selectedtags_panel, new Anchor(label, 0, Anchor.BOTTOM, true, false, 15, 3, 10, 2));
	}

	@Override protected void draw(Graphics2D g)
	{
		
		boolean selected = ShellGUI.isEventSelected(event);
		int w = getWidth();
		int h = getHeight();
		
		if (selected)
		{
			label.setForeground(Color.white);
			g.setColor(new Color(51, 153, 255));
			g.fill(new Rectangle2D.Double(0, 0, w, h));
			
			int r = 8;
			Polygon poly = new Polygon(
				new int[]{w - r, w, w}, 
				new int[]{h/2, h/2 - r, h/2 + r}, 
				3);
			g.setColor(Color.white);
			g.fill(poly);
		}
		else
		{
			label.setForeground(Color.black);
		}
		
		if (!selected && (isMouseover() || isPendingDrop()))
		{
			Composite c = g.getComposite();
			g.setPaint(new Color(0, 0, 255));
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f));
			g.fill(new Rectangle2D.Double(0, 2, getWidth(), getHeight()-4));
			g.setComposite(c);
		}
		
		for (Component component : selectedtags_panel.getComponents())
		{
			((SelectedtagsComponent) component).setForeground(label.getForeground());
		}
		
		if ((float) label.getFont().getSize() != ShellGUI.getBaseFontSize())
		{
			label.setFont(getFont().deriveFont(ShellGUI.getBaseFontSize()));
		}
	}

	public DataWrappersEvent getEvent()
	{
		return event;
	}

	public void addSelectedTag(DataWrappersSelectedTag selected_tag)
	{
		if (selected_tag.getItemUuid().equals(event.getUuid()))
		{
			selectedtags_panel.add(new SelectedtagsComponent(selected_tag));
		}
		else
		{
			ErrorStack.push(444, "Internal error: event uuid does not match selected tag uuid.");
		}
	}

	@Override public void clicked(XComponent xcomponent)
	{
		ShellGUI.selectEvent(event);
	}
	
	@Override public String getDropActionText(XComponent being_dragged)
	{
		if (being_dragged instanceof SelectedtagsComponent)
		{
			return "Copy to";
		}
		else if (being_dragged instanceof TagsComponent)
		{
			return "Move to";
		}
		else
		{
			return "";
		}
	}
	
	@Override public String getDropText()
	{
		return event.getLabel();
	}
	
	@Override public void entered()
	{
		delete_button.setVisible(true);
	}
	
	@Override public void exited()
	{
		delete_button.setVisible(false);
	}
}
