package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;

public class Pin extends JComponent
{
	private boolean pinned = false;
	private Color mouseover_color = View.BLUE_DARK;
	private Color mouseout_color = View.GREY_DARK;
	
	public Pin()
	{
		setBackground(mouseout_color);
		addMouseListener(new MouseAdapter()
		{
			@Override public void mouseEntered(MouseEvent e)
			{
				Pin.this.setBackground(mouseover_color);
			}
			@Override public void mouseExited(MouseEvent e)
			{
				Pin.this.setBackground(mouseout_color);
			}
		});
	}
	
	public void setColors(Color mouseover_color, Color mouseout_color)
	{
		this.mouseover_color = mouseover_color;
		this.mouseout_color = mouseout_color;
	}
	
	public boolean isPinned()
	{
		return pinned;
	}
	
	public void setPinned(boolean pinned)
	{
		this.pinned = pinned;
	}
	
	@Override public Dimension getPreferredSize()
	{
		Rectangle r = getFont().getStringBounds(" ", ((Graphics2D) getGraphics()).getFontRenderContext()).getBounds();
		return new Dimension(r.height*9/10-1, r.height-1);
	}
	
	@Override protected void paintComponent(Graphics graphics)
	{
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(getBackground());
		int w = getWidth();
		int h = getHeight()*3/4;
		Shape poly = new Polygon(
			new int[]{0, w*1/3, w*1/3, w*11/20, w*17/20, w-1, w-1, w*17/20, w*11/20, w*1/3, w*1/3}, 
			new int[]{h*1/2, h*1/2, 0, h*1/3, h*1/3, h*1/6, 5*h/6, h*2/3, h*2/3, h-1, h*1/2}, 11);
		if (pinned)
		{
			g.rotate(Math.toRadians(-45), w/2, h/2);
			g.translate(-w/8, h/8);
			g.fill(poly);
		}
		g.draw(poly);
	}
}
