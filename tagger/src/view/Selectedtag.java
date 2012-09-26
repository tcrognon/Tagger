package view;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import model.DataEvent;
import model.DataTag;
import edu.utsa.layouts.Anchor;
import edu.utsa.xcomponent.XComponent;

public class Selectedtag extends XComponent implements ViewListener
{
	private final Color MOUSEOVER_FG = Color.BLACK;
	private final Color MOUSEOUT_FG = View.GREY_DARK;
	
	private DataEvent eventdata;
	private DataTag tagdata;
	private JLabel label;
	Button unselect;
	
	public Selectedtag(DataEvent eventdata, DataTag tagdata)
	{
		this.eventdata = eventdata;
		this.tagdata = tagdata;
		
		label = new JLabel(tagdata.getFullPath());
		label.setForeground(View.GREY_DARK);
		
		unselect = new Button(" X ");
		unselect.setVisible(false);
		
		setDraggable(true);
		setMouseoverFg(MOUSEOVER_FG);
		setMouseoutFg(MOUSEOUT_FG);
		add(unselect);
		add(label, new Anchor(unselect, Anchor.RIGHT, Anchor.HORIZONTAL));
	}
	
	@Override public void setFont(Font font)
	{
		super.setFont(font);
		label.setFont(font);
		unselect.setFont(font.deriveFont(font.getSize() - 4f));
	}
	
	@Override public void fontSizeChanged(ViewEvent e)
	{
		label.setFont(label.getFont().deriveFont(label.getFont().getSize() + e.getFontSizeDelta()));
	}
	
	public DataEvent getEventData()
	{
		return eventdata;
	}
	
	public DataTag getTagData()
	{
		return tagdata;
	}
	
	@Override public String getDragText()
	{
		return tagdata.getPath();
	}
	
	@Override public void entered()
	{
		label.setForeground(Color.BLACK);
		unselect.setVisible(true);
	}
	
	@Override public void exited()
	{
		label.setForeground(View.GREY_DARK);
		unselect.setVisible(false);
	}

	@Override public void pinned(ViewEvent e){}

	@Override public void dataUpdated(ViewEvent e)
	{
		if (e.getPinnedData() == tagdata)
		{
			label.setText(tagdata.getFullPath());
		}
	}

}
