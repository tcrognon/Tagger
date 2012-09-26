package view;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JLabel;
import model.DataTag;
import edu.utsa.layouts.Anchor;
import edu.utsa.layouts.Margins;
import edu.utsa.xcomponent.XComponent;

public class Tag extends XComponent implements ViewListener
{
	private DataTag tagdata;
	private Collection<Tag> children = new ArrayList<Tag>();
	
	private JLabel indentation;
	private JLabel name;
	private JLabel qualifier;
	private Collapser collapser = new Collapser();
	private Button add_button;
	Pin pin;
	
	public Tag(DataTag tagdata)
	{
		this.tagdata = tagdata;
		
		// indentation
		String s = "";
		int depth = tagdata.getTagDepth();
		for (int i = 0; i < depth+1; i++) s += "      ";
		indentation = new JLabel(s);
		
		// name
		name = new JLabel(tagdata.getLabel());
		
		// qualifier
		qualifier = new JLabel("");
		if (tagdata.isChildRequired())
		{
			qualifier.setText("(child required)");
		}
		else if (tagdata.isExtensionAllowed())
		{
			qualifier.setText("(extensions allowed)");
		}
		
		// pin
		pin = new Pin();
		pin.setVisible(false);
		
		// this
		setMouseoverBg(View.BLUE_LIGHT);
		setDraggable(!tagdata.isChildRequired());
		setDroppable(!tagdata.isChildRequired());
		add(indentation, new Anchor(Anchor.TL, Anchor.NOSTRETCH));
		add(name, new Anchor(indentation, Anchor.RIGHT, Anchor.NOSTRETCH));
		add(qualifier, new Anchor(name, Anchor.RIGHT, Anchor.NOSTRETCH, new Margins(0, 5, 0, 0)));
		add(collapser, new Anchor(name, Anchor.LEFT));
		add(pin, new Anchor(qualifier, Anchor.RIGHT, Anchor.NOSTRETCH, new Margins(0, 5, 0, 0)));
		if (tagdata.isCustom() || tagdata.isExtensionAllowed() || tagdata.isChildRequired())
		{
			add_button = new Button("  Add  ");
			add(add_button, new Anchor(Anchor.TR, Anchor.NOSTRETCH, new Margins(0, 0, 0, 5)));
		}
	}
	
	@Override public void setFont(Font font)
	{
		super.setFont(font);
		indentation.setFont(font);
		name.setFont(font);
		qualifier.setFont(font);
		collapser.setFont(font);
		if (add_button != null)
		{
			add_button.setFont(font.deriveFont(font.getSize() - 2f));
		}
		pin.setFont(font);
	}
	
	public Collection<Tag> getChildren()
	{
		return children;
	}
	
	public Collapser getCollapser()
	{
		return collapser;
	}
	
	public Button getAddButton()
	{
		return add_button;
	}
	
	public Pin getPin()
	{
		return pin;
	}
	
	@Override public void fontSizeChanged(ViewEvent e)
	{
		for (Component c : getComponents())
		{
			c.setFont(c.getFont().deriveFont(c.getFont().getSize() + e.getFontSizeDelta()));
		}
	}
	
	@Override public String getDragText()
	{
		return tagdata.getLabel();
	}
	
	@Override public String getDropActionText(XComponent being_dragged)
	{
		if (being_dragged instanceof Tag)
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
		return tagdata.getLabel();
	}
	
	@Override public Object dragExport()
	{
		return this;
	}
	
	public DataTag getTagData()
	{
		return tagdata;
	}

	@Override public void pinned(ViewEvent e)
	{
		pin.setPinned(e.getPinnedData() == tagdata);
		if (!isMouseover())
		{
			pin.setVisible(pin.isPinned());
		}
	}

	@Override public void dataUpdated(ViewEvent e)
	{
		if (e.getPinnedData() == tagdata)
		{
			name.setText(tagdata.getLabel());
		}
	}
}
