package edu.utsa.tagger.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;
import edu.utsa.layouts.Anchor;
import edu.utsa.xcomponent.XComponent;

public class XScrollPane extends XComponent implements MouseWheelListener
{
	JPanel content_panel;
	Anchor content_anchor;
	int vert_pos = 0;
	VerticalScrollBar vsb = new VerticalScrollBar();
	Anchor vsb_anchor;
	ScrollButton down = new ScrollButton(10, 0);
	ScrollButton up = new ScrollButton(-10, 0);
	
	public XScrollPane(JPanel content_panel) {
		this.content_panel = content_panel;
		down.setPreferredSize(new Dimension(10, 10));
		up.setPreferredSize(new Dimension(10, 10));
		add(down, new Anchor(null, 0, Anchor.BR, false, false, 0, 0, 0, 0));
		add(up, new Anchor(null, 0, Anchor.TR, false, false, 0, 0, 0, 0));
		vsb_anchor = new Anchor(up, 0, Anchor.BOTTOM, false, false, 0, 0, 0, 0);
		vsb.setPreferredSize(new Dimension(10, 10));
		add(vsb, vsb_anchor);
		content_anchor = new  Anchor(null, 0, Anchor.TL, true, false, 0, 0, 0, 0);
		add(content_panel, content_anchor);
		this.addMouseWheelListener(this);
	}
	
	@Override public void mouseWheelMoved(MouseWheelEvent e)
	{
	       int notches = e.getWheelRotation();
	       if (notches < 0) {
	           scroll(-20, 0);
	       } else {
	           scroll(20, 0);
	       }
	    }

	
	public void refresh()
	{
		double p = vert_pos;
		double b = content_panel.getHeight();
		double a = this.getHeight() - up.getHeight() - down.getHeight();
		
		if (vert_pos > b - a) vert_pos = (int) (b - a);
		if (vert_pos < 0) vert_pos = 0;
		
		content_anchor.setOffsets(0, -vert_pos, 0, 0);
		double sb_height = (a / b) * a;
		double sb_top = (p / b) * a;
		vsb.setPreferredSize(new Dimension(vsb.getPreferredSize().width, (int) sb_height));
		vsb_anchor.setOffsets(0, (int) sb_top, 0, 0);
		revalidate();
	}
	
	@Override public Dimension getPreferredSize()
	{
		refresh();
		return super.getPreferredSize();
	}
	
	public void scrollTo(int y, int x)
	{
		vert_pos = y;
		refresh();
	}
	
	public void scroll(int y, int x)
	{
		vert_pos += y;
		refresh();
	}
	
	public class VerticalScrollBar extends XComponent
	{
		@Override protected void draw(Graphics2D g)
		{
			g.setColor(new Color(100, 100, 100));
			g.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 5, 5));
		}
		
		public void clicked()
		{
			XScrollPane.this.refresh();
		}
	}
	
	public class ScrollButton extends XComponent
	{
		int vert_scroll;
		int horz_scroll;
		
		public ScrollButton(int vert_scoll, int horz_scroll)
		{
			this.vert_scroll = vert_scoll;
			this.horz_scroll = horz_scroll;
		}
		public void clicked()
		{
			XScrollPane.this.scroll(vert_scroll, 0);
		}
		
		@Override protected void draw(Graphics2D g)
		{
			g.setColor(new Color(100, 100, 100));
			int w = getWidth();
			int h = getHeight();
			double angle = Math.atan2(vert_scroll, horz_scroll);
			Shape s = new Polygon(new int[]{0,w,0}, new int[]{0,h/2,h}, 3);
			AffineTransform tx = new AffineTransform();
			tx.rotate(angle, getWidth()/2, getHeight()/2);
			Shape newShape = tx.createTransformedShape(s);
			g.fill(newShape);
		}
		
	}
}
