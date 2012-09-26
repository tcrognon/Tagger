package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import edu.utsa.layouts.Anchor;
import edu.utsa.layouts.AnchorLayout;
import edu.utsa.layouts.Margins;
import edu.utsa.xcomponent.XComponent;

public class Menu extends XComponent
{
	public Menu(){}
	
	@Override public void draw(Graphics2D g)
	{
		if (isMouseover())
		{
			g.setColor(new Color(0, 0, 0, 160));
			g.fill(new Rectangle(0, 0, getWidth(), getHeight()));
		}
	}
	
	@Override public void entered()
	{
		for (Component c : getComponents()) c.setForeground(Color.WHITE);
	}
	
	@Override public void exited()
	{
		for (Component c : getComponents()) c.setForeground(Color.GRAY);
	}
}
