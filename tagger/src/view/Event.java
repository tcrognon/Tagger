package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JComponent;
import javax.swing.JLabel;
import model.DataEvent;
import model.DataTag;
import edu.utsa.layouts.Anchor;
import edu.utsa.layouts.ListLayout;
import edu.utsa.layouts.Margins;
import edu.utsa.xcomponent.XComponent;

public class Event extends XComponent implements ViewListener
{
	private DataEvent eventdata;
	private boolean selected = false;
	private JLabel label;
	private JComponent tags_list = new JComponent(){};
	private Collection<DataTag> tags = new ArrayList<DataTag>();
	private Pin pin = new Pin();
	
	public Event(DataEvent eventdata)
	{
		this.eventdata = eventdata;
		
		label = new JLabel("     " + eventdata.getLabel());
		
		tags_list.setLayout(new ListLayout(0));
		tags_list.setOpaque(false);
		
		pin.setColors(View.BLUE_DARK, View.GREY_DARK);
		pin.setVisible(false);
		
		setDroppable(true);
		setLayer(pin, 1);
		add(pin, new Anchor(1, Anchor.TR, Anchor.NOSTRETCH, new Margins(0, 0, 0, 5)));
		add(label, new Anchor(Anchor.TL, Anchor.HORIZONTAL, new Margins(2, 0, 2, 0)));
		add(tags_list, new Anchor(label, Anchor.BOTTOM, Anchor.HORIZONTAL, new Margins(3, 2, 2, 10)));
	}
	
	@Override public void draw(Graphics2D g)
	{
		if (isActive() || selected)
		{
			g.setColor(getBackground());
			g.fill(new Rectangle(0, 0, getWidth(), label.getHeight() + 7));
		}
	}
	
	@Override public void setFont(Font font)
	{
		super.setFont(font);
		label.setFont(font);
		pin.setFont(font);
	}
	
	public void setSelected(boolean selected)
	{
		this.selected = selected;
		if (selected)
		{
			setBackground(View.BLUE_DARK);
			label.setForeground(Color.WHITE);
			pin.setColors(View.VERY_LIGHT_GREY, View.GREY_DARK);
		}
		else
		{
			setBackground(View.GREY_LIGHT);
			label.setForeground(View.GREY_DARK);
			pin.setColors(View.BLUE_DARK, View.GREY_DARK);
		}
	}
	
	public JLabel getNameLabel()
	{
		return label;
	}
	
	public JComponent getTagsList()
	{
		return tags_list;
	}

	public DataEvent getEventData()
	{
		return eventdata;
	}
	
	@Override public String getDropActionText(XComponent being_dragged)
	{
		if (being_dragged instanceof Selectedtag)
		{
			return "Copy to";
		}
		else if (being_dragged instanceof Tag)
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
		return eventdata.getLabel();
	}

	@Override public void fontSizeChanged(ViewEvent e)
	{
		label.setFont(label.getFont().deriveFont(label.getFont().getSize() + e.getFontSizeDelta()));
	}

	@Override public void pinned(ViewEvent e)
	{
		pin.setPinned(e.getPinnedData() == eventdata);
		if (!isMouseover())
		{
			pin.setVisible(pin.isPinned());
		}
	}
	
	public Pin getPin()
	{
		return pin;
	}

	@Override public void dataUpdated(ViewEvent e)
	{
		if (e.getPinnedData() == eventdata)
		{
			label.setText("     " + eventdata.getLabel());
		}
	}
}
