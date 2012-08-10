package edu.utsa.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class AnchorLayout implements LayoutManager2 {
	
	private ArrayList<Anchor> anchor_list;

	public AnchorLayout() {
		anchor_list = new ArrayList<Anchor>();
	}

	@Override public void addLayoutComponent(Component c, Object obj) {
		if (c == null) {
			throw new NullPointerException();
		}
		if (obj == null) {
			obj = new Anchor();
		}
		if (!(obj instanceof Anchor)) {
			throw new IllegalArgumentException("Object must be of type Anchor");
		}

		Anchor anchor = (Anchor) obj;
		if (anchor.parent_node != null) {
			boolean found_parent = false;
			for (Anchor a : anchor_list) {
				if (a.component == anchor.parent_node) {
					a.child_nodes.add(c);
					found_parent = true;
				}
			}
			if (!found_parent) {
				throw new IllegalArgumentException("Could not find anchor_to component");
			}
		}
		
		anchor.component = c;
		anchor_list.add(anchor);
	}
	
	private Anchor getAnchor(Component component) {
		for (Anchor a : anchor_list) {
			if (a.component == component) {
				return a;
			}
		}
		return null;
	}
	
	private ArrayList<Anchor> getAnchorsFromParentNode(Component parent_node) {
		ArrayList<Anchor> anchors = new ArrayList<Anchor>();
		for (Anchor a : anchor_list) {
			if (a.parent_node == parent_node) {
				anchors.add(a);
			}
		}
		return anchors;
	}

	@Override public void removeLayoutComponent(Component c) {
		Anchor anchor = getAnchor(c);
		if (anchor == null) {
			throw new RuntimeException("Could not find anchor");
		} else {
			ArrayList<Anchor> child_anchors = getAnchorsFromParentNode(c);
			if (anchor.parent_node != null) {
				Anchor parent_anchor = getAnchor(anchor.parent_node);
				if (parent_anchor == null) {
					throw new RuntimeException("Could not find parent anchor");
				}
				if (!parent_anchor.child_nodes.remove(c)) {
					throw new RuntimeException("Could not remove component from parent anchor");
				}
				for (Anchor child_anchor : child_anchors) {
					if (!parent_anchor.child_nodes.add(child_anchor.component)) {
						throw new RuntimeException("Could not add child component to parent anchor");
					}
				}
			}
			for (Anchor child_anchor : child_anchors) {
				child_anchor.parent_node = anchor.parent_node;
			}
		}
	}

	@Override public void layoutContainer(Container target) {

		ArrayList<Anchor> temp_anchor_list = new ArrayList<Anchor>(anchor_list);
		Queue<Anchor> temp_queue = new LinkedList<Anchor>();
		Queue<Anchor> queue = new LinkedList<Anchor>();
		temp_queue.offer(null);
		
		// flatten tree of anchors to a queue in an unusual way, 
		// where each full level follows after the previous full level
		while (temp_queue.size() > 0) {
			Anchor anchor = temp_queue.poll();
			Component component = null;
			if (anchor != null) {
				component = anchor.component;
			}
			
			for (Iterator<Anchor> iter = temp_anchor_list.iterator(); iter.hasNext(); ) {
				Anchor a = iter.next();
				if (a.parent_node == component) {
					temp_queue.offer(a);
					iter.remove();
				}
			}
			
			queue.offer(anchor);
		}

		queue.poll(); // remove the first element which is null

		while (queue.size() > 0) {


			Anchor a = queue.poll();
			Component c = a.component;

			c.setSize(c.getPreferredSize());

			int x=0, y=0;

			if (a.parent_node == null) {
				switch (a.container_anchor) {
				case Anchor.TL:
					x = a.offset_x1;
					y = a.offset_y1;
					break;
				case Anchor.TC:
					x = (target.getWidth() - c.getWidth()) / 2;
					y = a.offset_y1;
					break;
				case Anchor.TR:
					x = target.getWidth() - c.getWidth() - a.offset_x2;
					y = a.offset_y1;
					break;
				case Anchor.CL:
					x = a.offset_x1;
					y = (target.getHeight() - c.getHeight()) / 2;
					break;
				case Anchor.CC:
					x = (target.getWidth() - c.getWidth()) / 2;
					y = (target.getHeight() - c.getHeight()) / 2;
					break;
				case Anchor.CR:
					x = target.getWidth() - c.getWidth() - a.offset_x2;
					y = (target.getHeight() - c.getHeight()) / 2;
					break;
				case Anchor.BL:
					x = a.offset_x1;
					y = target.getHeight() - c.getHeight() - a.offset_y2;
					break;
				case Anchor.BC:
					x = (target.getWidth() - c.getWidth()) / 2;
					y = target.getHeight() - c.getHeight() - a.offset_y2;
					break;
				case Anchor.BR:
					x = target.getWidth() - c.getWidth() - a.offset_x2;
					y = target.getHeight() - c.getHeight() - a.offset_y2;
					break;
				}
			}
			else {
				switch (a.component_anchor) {
				case Anchor.LEFT:
					x = a.parent_node.getX() - c.getWidth() - a.offset_x2;
					y = a.parent_node.getY() + a.offset_y1;
					break;
				case Anchor.RIGHT:
					x = a.parent_node.getX() + a.parent_node.getWidth() + a.offset_x1;
					y = a.parent_node.getY() + a.offset_y1;
					break;
				case Anchor.TOP:
					x = a.parent_node.getX() + a.offset_x1;
					y = a.parent_node.getY() - c.getHeight() - a.offset_y2;
					break;
				case Anchor.BOTTOM:
					x = a.parent_node.getX() + a.offset_x1;
					y = a.parent_node.getY() + a.parent_node.getHeight() + a.offset_y1;
					break;
				}
			}
			
			c.setLocation(x, y);
			
			if (a.horizontal_stretch && a.vertical_stretch) {
				Rectangle bounds = new Rectangle(c.getX(), c.getY(), target.getWidth() - c.getX(), target.getHeight() - c.getY());
				bounds = stretchDownAndAcross(bounds, a.layer, a.offset_x2, a.offset_y2);
				c.setBounds(bounds);
			}
			else if (a.horizontal_stretch) {
				Rectangle bounds = new Rectangle(c.getX(), c.getY(), target.getWidth() - c.getX(), c.getHeight());
				bounds = stretchAcross(bounds, a.layer, a.offset_x2);
				c.setBounds(bounds);
			}
			else if (a.vertical_stretch) {
				Rectangle bounds = new Rectangle(c.getX(), c.getY(), c.getWidth(), target.getHeight() - c.getY());
				bounds = stretchDown(bounds, a.layer, a.offset_y2);
				c.setBounds(bounds);
			}
			
			if (c != null && a.fit_to != null) {
				switch (a.component_anchor) {
				case Anchor.TOP:
				case Anchor.BOTTOM:
					c.setSize(a.fit_to.getWidth(), c.getHeight());
					break;
				case Anchor.LEFT:
				case Anchor.RIGHT:
					c.setSize(c.getWidth(), a.fit_to.getHeight());
					break;
				}
			}
			
//			respectMinimumSize(c);
//			respectMaximumSize(c);
//			respectVisibility(c);
		}
	}

	private Rectangle stretchAcross(Rectangle r, int layer, int offset_x) {
		for (Anchor a : anchor_list) {
			Component c = a.component;
			if (a.layer == layer) {
				if (c.getX() > r.x && c.getX() < r.x + r.width && c.getY() < r.y + r.height && c.getY() + c.getHeight() > r.y) {
					r.width = c.getX() - r.x;
				}
			}
		}
		return new Rectangle(r.x, r.y, r.width - offset_x, r.height);
	}

	private Rectangle stretchDown(Rectangle r, int layer, int offset_y) {
		for (Anchor a : anchor_list) {
			Component c = a.component;
			if (a.layer == layer) {
				if (c.getY() > r.y && c.getY() < r.y + r.height && c.getX() < r.x + r.width && c.getX() + c.getWidth() > r.x) {
					r.height = c.getY() - r.y;
				}
			}
		}
		return new Rectangle(r.x, r.y, r.width, r.height - offset_y);
	}

	private Rectangle stretchDownAndAcross(Rectangle r, int layer, int offset_x, int offset_y) {
		Rectangle old_r = new Rectangle(r);
		for (Anchor a : anchor_list) {
			Component c = a.component;
			if (a.layer == layer) {
				if (c.getX() > r.x && c.getX() < r.x + r.width && c.getY() < r.y + r.height && c.getY() + c.getHeight() > r.y) {
					r.width = c.getX() - r.x;
				}
				if (c.getY() > r.y && c.getY() < r.y + r.height && c.getX() < r.x + r.width && c.getX() + c.getWidth() > r.x) {
					r.height = c.getY() - r.y;
				}
			}
		}
		if (r.equals(old_r)) {
			return new Rectangle(r.x, r.y, r.width - offset_x, r.height - offset_y);
		} else {
			return stretchDownAndAcross(r, layer, offset_x, offset_y);
		}
	}

	private void respectMinimumSize(Component c) {

		if (c.getMinimumSize() != null) {

			int w = c.getWidth();
			int h = c.getHeight();
			int minWidth = c.getMinimumSize().width;
			int minHeight = c.getMinimumSize().height;

			if (minWidth >=0 && w < minWidth)
				w = minWidth;
			if (minHeight >=0 && h < minHeight)
				h = minHeight;

			c.setSize(w, h);
		}
	}

	private void respectMaximumSize(Component c) {

		if (c.getMaximumSize() != null) {

			int w = c.getWidth();
			int h = c.getHeight();
			int maxWidth = c.getMaximumSize().width;
			int maxHeight = c.getMaximumSize().height;

			if (maxWidth >=0 && w < maxWidth)
				w = maxWidth;
			if (maxHeight >=0 && h < maxHeight)
				h = maxHeight;

			c.setSize(w, h);
		}
	}

	private void respectVisibility(Component comp) {
		if (!comp.isVisible())
			comp.setSize(0, 0);
	}

	@Override public void addLayoutComponent(String str, Component comp) {}
	@Override public void invalidateLayout(Container target) {}
	@Override public float getLayoutAlignmentX(Container target) {return 0.5f;}
	@Override public float getLayoutAlignmentY(Container target) {return 0.5f;}
	@Override public Dimension maximumLayoutSize(Container container) {return null;}
	@Override public Dimension minimumLayoutSize(Container target) { return getModelBounds(target); }
	@Override public Dimension preferredLayoutSize(Container target) { return getModelBounds(target); }

	private Dimension getModelBounds(Container target) {
		layoutContainer(target);
		int max_right = 0, max_bottom = 0;

		for (Anchor a : anchor_list) {
			Component c = a.component;
			if (c.isVisible()) {
				
				int right = c.getX() + c.getWidth() + a.offset_x2;
				if (right > max_right) {
					max_right = right;
				}
				
				int bottom = c.getY() + c.getHeight() + a.offset_y2;
				if (bottom > max_bottom) {
					max_bottom = bottom;
				}
			}
		}
		return new Dimension(max_right, max_bottom);
	}

}
