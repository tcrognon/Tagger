package edu.utsa.layouts;

import java.awt.Component;
import java.util.ArrayList;

public class Anchor {

	public static final int TL = 1;
	public static final int TC = 2;
	public static final int TR = 3;
	public static final int CL = 4;
	public static final int CC = 5;
	public static final int CR = 6;
	public static final int BL = 7;
	public static final int BC = 8;
	public static final int BR = 9;

	public static final int TOP = 1;
	public static final int BOTTOM = 2;
	public static final int LEFT = 3;
	public static final int RIGHT = 4;

	public int layer;
	public int container_anchor;
	public int component_anchor;
	public boolean horizontal_stretch;
	public boolean vertical_stretch;
	public int offset_x1;
	public int offset_y1;
	public int offset_x2;
	public int offset_y2;
	public Component fit_to;

	Component component;
	Component parent_node;
	ArrayList<Component> child_nodes;

	public void destroy() {
		component = null;
		parent_node = null;
		child_nodes = null;
	}

	public Anchor() {
		layer = 0;
		container_anchor = TL;
		component_anchor = RIGHT;
		horizontal_stretch = false;
		vertical_stretch = false;
		offset_x1 = 0;
		offset_y1 = 0;
		offset_x2 = 0;
		offset_y2 = 0;
		fit_to = null;

		component = null;
		parent_node = null;
		child_nodes = new ArrayList<Component>();
	}
	
	public void setAnchorTo(Component anchor_to) { this.parent_node = anchor_to; }
	public void setContainerAnchor(int anchor) { this.container_anchor = anchor; }
	public void setComponentAnchor(int anchor) { this.component_anchor = anchor; }
	public void setLayer(int layer) { this.layer = layer; }
	public void setStretching(boolean h_stretch, boolean v_stretch) {
		this.horizontal_stretch = h_stretch;
		this.vertical_stretch = v_stretch;
	}
	public void setOffsets(int offset_x1, int offset_y1, int offset_x2, int offset_y2) {
		this.offset_x1 = offset_x1;
		this.offset_y1 = offset_y1;
		this.offset_x2 = offset_x2;
		this.offset_y2 = offset_y2;
	}

	public Anchor(int anchor, boolean horizontal_stretch, boolean vertical_stretch) {

		this();
		this.container_anchor = anchor;
		this.horizontal_stretch = horizontal_stretch;
		this.vertical_stretch = vertical_stretch;
	}
	
	public Anchor(int anchor, boolean horizontal_stretch, boolean vertical_stretch, 
		int offset_x1, int offset_y1, int offset_x2, int offset_y2) {

		this();
		this.container_anchor = anchor;
		this.horizontal_stretch = horizontal_stretch;
		this.vertical_stretch = vertical_stretch;
		this.offset_x1 = offset_x1;
		this.offset_y1 = offset_y1;
		this.offset_x2 = offset_x2;
		this.offset_y2 = offset_y2;
	}

	public Anchor(Component anchor_to, int layer, 
		int container_anchor, int component_anchor, 
		boolean horizontal_stretch, boolean vertical_stretch, 
		int offset_x1, int offset_y1, int offset_x2, int offset_y2) {

		this();

		parent_node = anchor_to;

		this.layer = layer;
		this.container_anchor = container_anchor;
		this.component_anchor = component_anchor;
		this.horizontal_stretch = horizontal_stretch;
		this.vertical_stretch = vertical_stretch;
		this.offset_x1 = offset_x1;
		this.offset_y1 = offset_y1;
		this.offset_x2 = offset_x2;
		this.offset_y2 = offset_y2;
	}

	public Anchor(Component anchor_to, int layer, int anchor,
		boolean horizontal_stretch, boolean vertical_stretch,
		int offset_x1, int offset_y1, int offset_x2, int offset_y2) {

		this();
		this.parent_node = anchor_to;
		if (anchor_to == null) {
			this.container_anchor = anchor;
		} else {
			this.component_anchor = anchor;
		}
		this.layer = layer;
		this.horizontal_stretch = horizontal_stretch;
		this.vertical_stretch = vertical_stretch;
		this.offset_x1 = offset_x1;
		this.offset_y1 = offset_y1;
		this.offset_x2 = offset_x2;
		this.offset_y2 = offset_y2;
	}

	public Anchor(Component anchor_to, int anchor, 
		int offset_x1, int offset_y1, int offset_x2, int offset_y2,
		Component fit_to) {

		this();
		this.parent_node = anchor_to;
		if (anchor_to == null) {
			this.container_anchor = anchor;
		} else {
			this.component_anchor = anchor;
		}
		this.offset_x1 = offset_x1;
		this.offset_y1 = offset_y1;
		this.offset_x2 = offset_x2;
		this.offset_y2 = offset_y2;
		this.fit_to = fit_to;
	}
}
