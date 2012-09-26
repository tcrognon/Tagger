package view;

import java.awt.Color;
import java.awt.KeyboardFocusManager;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class RichTextField extends JTextArea implements FocusListener, ViewListener
{
	private Border FOCUSED_BORDER = 
		BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(View.BLUE_MEDIUM, 1, false), 
			BorderFactory.createEmptyBorder(3, 3, 3, 3));
	private Border UNFOCUSED_BORDER = 
		BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(View.GREY_LIGHT, 1, false), 
			BorderFactory.createEmptyBorder(3, 3, 3, 3));
	private Border NO_BORDER = 
		BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 1, false), 
			BorderFactory.createEmptyBorder(3, 3, 3, 3));
	
	public RichTextField()
	{
		super();
		init();
	}
	
	public RichTextField(String text)
	{
		super(text);
		init();
	}
	
	public RichTextField(int rows, int columns)
	{
		super(rows, columns);
		init();
	}
	
	public RichTextField(String text, int rows, int columns)
	{
		super(text, rows, columns);
		init();
	}
	
	private void init()
	{
		setBorder(UNFOCUSED_BORDER);
		addFocusListener(this);
	}
	
	public void setBorderColors(Color focused, Color unfocused)
	{
		FOCUSED_BORDER = 
			BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(focused, 1, false), 
				BorderFactory.createEmptyBorder(3, 3, 3, 3));
		UNFOCUSED_BORDER = 
			BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(unfocused, 1, false), 
				BorderFactory.createEmptyBorder(3, 3, 3, 3));
		if (this.hasFocus())
		{
			setBorder(FOCUSED_BORDER);
		}
		else
		{
			setBorder(UNFOCUSED_BORDER);
		}
	}
	
	public void setLock(boolean lock)
	{
		setEditable(!lock);
		setFocusable(!lock);
		setOpaque(!lock);
		if (lock)
		{
			if (hasFocus())
			{
				KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
			}
			setBorder(NO_BORDER);
		}
		else
		{
			setBorder(UNFOCUSED_BORDER);
		}
	}

	@Override public void focusGained(FocusEvent e)
	{
		setBorder(FOCUSED_BORDER);
	}

	@Override public void focusLost(FocusEvent e)
	{
		setBorder(UNFOCUSED_BORDER);
	}

	@Override public void fontSizeChanged(ViewEvent e)
	{
		setFont(getFont().deriveFont(getFont().getSize() + e.getFontSizeDelta()));
	}

	@Override public void pinned(ViewEvent e){}

	@Override public void dataUpdated(ViewEvent e){}
}
