package edu.utsa.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;

public class ListLayout implements LayoutManager2
{
	private int gap = 0;
	int x1_offset = 0;
	int y1_offset = 0;
	int x2_offset = 0;
	int y2_offset = 0;
	
	public ListLayout(int gap)
	{
		this.gap = gap;
	}
	
	public ListLayout(int x1_offset, int y1_offset, int x2_offset, int y2_offset)
	{
		this.x1_offset = x1_offset;
		this.y1_offset = y1_offset;
		this.x2_offset = x2_offset;
		this.y2_offset = y2_offset;
	}

	@Override public void layoutContainer(Container target)
	{
		int w, h = 0;
		int bottom = y1_offset;
		for (Component comp : target.getComponents())
		{
			w = target.getWidth();
			h = comp.getPreferredSize().height;
			/*
			if (comp.getMinimumSize() != null)
			{
				if (comp.getMinimumSize().width >=0 && w < comp.getMinimumSize().width)
					w = comp.getMinimumSize().width;
				if (comp.getMinimumSize().height >=0 && h < comp.getMinimumSize().height)
					h = comp.getMinimumSize().height;
			}*/
			if (comp.getMaximumSize() != null)
			{
				if (comp.getMaximumSize().width >=0 && w > comp.getMaximumSize().width)
					w = comp.getMaximumSize().width;
				if (comp.getMaximumSize().height >=0 && h > comp.getMaximumSize().height)
					h = comp.getMaximumSize().height;
			}
			if (!comp.isVisible())
			{
				w = 0;
				h = 0;
			}
			comp.setBounds(x1_offset, bottom, w - x1_offset - x2_offset, h);
			bottom += h + gap;
		}
	}

	@Override public void addLayoutComponent(String str, Component comp) {}
	@Override public void removeLayoutComponent(Component comp) {}
	@Override public Dimension minimumLayoutSize(Container target) { return null; }
	@Override public Dimension preferredLayoutSize(Container target)
	{
		int x = 0, y = 0;
		for (Component comp : target.getComponents())
		{
			if (comp.isVisible())
			{
				if (comp.getPreferredSize().width > x)
					x = comp.getPreferredSize().width;
				y += comp.getPreferredSize().height;
			}
		}
		return new Dimension(x1_offset + x + x2_offset, y1_offset + y + y2_offset);
	}

	@Override public void addLayoutComponent(Component arg0, Object arg1) {}

	@Override public float getLayoutAlignmentX(Container arg0) { return 0.5f; }

	@Override public float getLayoutAlignmentY(Container arg0) { return 0.5f; }

	@Override public void invalidateLayout(Container arg0) {}

	@Override public Dimension maximumLayoutSize(Container arg0) { return null;}
}
