package edu.utsa.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;

public class ListLayout implements LayoutManager2 {

	private int gap;
	
	public ListLayout(int gap) {
		this.gap = gap;
	}

	@Override public void layoutContainer(Container target) {

		int w, h, bottom = gap;
		for (Component comp : target.getComponents()) {
			w = target.getWidth();
			h = comp.getPreferredSize().height;
			if (comp.getMinimumSize() != null) {
				if (comp.getMinimumSize().width >=0 && w < comp.getMinimumSize().width)
					w = comp.getMinimumSize().width;
				if (comp.getMinimumSize().height >=0 && h < comp.getMinimumSize().height)
					h = comp.getMinimumSize().height;
			}
			if (comp.getMaximumSize() != null) {
				if (comp.getMaximumSize().width >=0 && w > comp.getMaximumSize().width)
					w = comp.getMaximumSize().width;
				if (comp.getMaximumSize().height >=0 && h > comp.getMaximumSize().height)
					h = comp.getMaximumSize().height;
			}
			if (!comp.isVisible()) {
				w = 0;
				h = 0;
			}
			comp.setBounds(0, bottom, w, h);
			bottom += h + gap;
		}
	}

	@Override public void addLayoutComponent(String str, Component comp) {}
	@Override public void removeLayoutComponent(Component comp) {}
	@Override public Dimension minimumLayoutSize(Container target) { return preferredLayoutSize(target); }
	@Override public Dimension preferredLayoutSize(Container target) {
		int x = 0, y = 0;
		for (Component comp : target.getComponents()) {
			if (comp.isVisible()) {
				if (comp.getPreferredSize().width > x)
					x = comp.getPreferredSize().width;
				y += comp.getPreferredSize().height;
			}
		}
		return new Dimension(x, y);
	}

	@Override public void addLayoutComponent(Component arg0, Object arg1) {}

	@Override public float getLayoutAlignmentX(Container arg0) { return 0.5f; }

	@Override public float getLayoutAlignmentY(Container arg0) { return 0.5f; }

	@Override public void invalidateLayout(Container arg0) {}

	@Override public Dimension maximumLayoutSize(Container arg0) { return null;}
}
