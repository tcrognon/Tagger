package view;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import model.DataTag;
import edu.utsa.layouts.Anchor;
import edu.utsa.layouts.Margins;
import edu.utsa.xcomponent.XComponent;

public class SearchResult extends XComponent implements ViewListener
{
	private DataTag tagdata;
	
	JLabel label = new JLabel();
	Button select = new Button("  Select  ");

	public SearchResult(DataTag tagdata)
	{
		this.tagdata = tagdata;
		
		setOpaque(true);
		setMouseoverBg(View.VERY_LIGHT_GREY);
		setMouseoutBg(Color.white);
		label.setText(tagdata.getFullPath());
		select.setVisible(false);
		add(select, new Anchor(Anchor.TR, Anchor.NOSTRETCH, new Margins(0, 0, 0, 5)));
		add(label, new Anchor(Anchor.TL, Anchor.HORIZONTAL, new Margins(0, 5, 0, 0)));
	}
	
	@Override public void setFont(Font font)
	{
		super.setFont(font);
		label.setFont(font);
		select.setFont(font.deriveFont(font.getSize() - 2f));
	}
	
	public Button getSelectButton()
	{
		return select;
	}
	
	public DataTag getTagData()
	{
		return tagdata;
	}
	
	@Override public void entered()
	{
		select.setVisible(true);
	}
	
	@Override public void exited()
	{
		select.setVisible(false);
	}
	
	@Override public void fontSizeChanged(ViewEvent e)
	{
		label.setFont(label.getFont().deriveFont(label.getFont().getSize() + e.getFontSizeDelta()));
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
