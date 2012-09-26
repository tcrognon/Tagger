package edu.utsa.xcomponent;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import edu.utsa.layouts.Anchor;
import edu.utsa.layouts.Margins;

public class XSplitPane extends XComponent
{
	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;
	
	private int direction;
	private JPanel a = new JPanel(){};
	private JPanel b = new JPanel(){};
	private Dragger dragger = new Dragger();
	
	public XSplitPane(int direction, int split_pos)
	{
		this.direction = direction;
		setLayer(dragger, 1);
		switch (direction)
		{
		case HORIZONTAL:
			a.setPreferredSize(new Dimension(0, split_pos));
			this.add(a, new Anchor(Anchor.TL, Anchor.HORIZONTAL));
			this.add(b, new Anchor(a, Anchor.BOTTOM, Anchor.STRETCHALL));
			break;
		case VERTICAL:
			a.setPreferredSize(new Dimension(split_pos, 0));
			this.add(a, new Anchor(Anchor.TL, Anchor.VERTICAL));
			this.add(b, new Anchor(a, Anchor.RIGHT, Anchor.STRETCHALL));
			break;
		default: throw new RuntimeException("Invalid direction.");
		}
		add(dragger, new Anchor(a, 1, Anchor.RIGHT, Anchor.VERTICAL, new Margins(0, -5, 0, 0)));
		
	}
	
	public JPanel getA()
	{
		return a;
	}
	
	public JPanel getB()
	{
		return b;
	}
	
	private class Dragger extends XComponent implements MouseMotionListener
	{
		private Dragger()
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
			setPreferredSize(new Dimension(10, 0));
			addMouseMotionListener(this);
		}
		
		@Override public void draw(Graphics2D g)
		{
			if (isMouseover())
			{
				g.setColor(new Color(225, 225, 255));
				g.draw(new Line2D.Double(5, 0, 5, getHeight()));
			}
		}

		@Override public void mouseDragged(MouseEvent e)
		{
			e = SwingUtilities.convertMouseEvent(this, e, XSplitPane.this);
			if (direction == VERTICAL)
			{
				int x = e.getPoint().x;
				if (x > 50 && x < XSplitPane.this.getWidth()-50)
				{
					a.setPreferredSize(new Dimension(x, 0));
				}
			}
			else
			{
				int y = e.getPoint().y;
				if (y > 50 && y < XSplitPane.this.getHeight()-50)
				{
					a.setPreferredSize(new Dimension(0, y));
				}
			}
			revalidate();
		}

		@Override public void mouseMoved(MouseEvent e) {}
	}
}
