package edu.utsa.xcomponent;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import edu.utsa.layouts.AnchorLayout;

public abstract class XComponent extends JLayeredPane
{
	private boolean mouseover = false;
	private boolean mousedirectlyover = false;
	private boolean mousepressed = false;
	private boolean draggable = false;
	private boolean droppable = false;
	private boolean being_dragged = false;
	private boolean pending_drop = false;

	private Color mouseover_bg = null;
	private Color mouseout_bg = null;
	private Color mouseover_fg = null;
	private Color mouseout_fg = null;
	
	public XComponent()
	{
		setLayout(new AnchorLayout());
	}
	
	@Override final protected void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		boolean highlight = mouseover || being_dragged || pending_drop;
		
		if (highlight && mouseover_bg != null)
		{
			setBackground(mouseover_bg);
			g2d.setColor(getBackground());
			Insets insets = getInsets();
			g2d.fill(new Rectangle(
				insets.left, 
				insets.top, 
				getWidth()-insets.left-insets.right, 
				getHeight()-insets.top-insets.bottom));
		}
		if (!highlight && mouseout_bg != null)
		{
			setBackground(mouseout_bg);
			g2d.setColor(getBackground());
			Insets insets = getInsets();
			g2d.fill(new Rectangle(
				insets.left, 
				insets.top, 
				getWidth()-insets.left-insets.right, 
				getHeight()-insets.top-insets.bottom));
		}
		if (mouseover_fg != null || mouseout_fg != null)
		{
			setForeground(highlight ? mouseover_fg : mouseout_fg);
		}
		
		draw(g2d);
	}
	
	final public boolean isMouseover() { return mouseover; }
	final public boolean isMousedirectlyover() { return mousedirectlyover; }
	final public boolean isMousepressed() { return mousepressed; }
	final public boolean isDraggable() { return draggable; }
	final public boolean isDroppable() { return droppable; }
	final public boolean isBeingDragged() { return being_dragged; }
	final public boolean isPendingDrop() { return pending_drop; }
	final public boolean isActive() { return mouseover || being_dragged || pending_drop; }
	
	final void setMouseover(boolean mouseover) {
		this.mouseover = mouseover;
	}
	final void setMousedirectlyover(boolean mousedirectlyover) {
		this.mousedirectlyover = mousedirectlyover;
	}
	final void setMousePressed(boolean mousepressed) { this.mousepressed = mousepressed; }
	final public void setDraggable(boolean draggable) { this.draggable = draggable; }
	final public void setDroppable(boolean droppable) { this.droppable = droppable; }
	final void setBeingDragged(boolean being_dragged) { this.being_dragged = being_dragged; }
	final void setPendingDropped(boolean pending_drop) { this.pending_drop = pending_drop; }
	
	final public Color getMouseoverBg() { return mouseover_bg; }
	final public Color getMouseoutBg() { return mouseout_bg; }
	final public Color getMouseoverFg() { return mouseover_fg; }
	final public Color getMouseoutFg() { return mouseout_fg; }
	final public void setMouseoverBg(Color mouseover_bg) { this.mouseover_bg = mouseover_bg; }
	final public void setMouseoutBg(Color mouseout_bg)
	{
		this.mouseout_bg = mouseout_bg;
		setBackground(mouseout_bg);
	}
	final public void setMouseoverFg(Color mouseover_fg) { this.mouseover_fg = mouseover_fg; }
	final public void setMouseoutFg(Color mouseout_fg)
	{
		this.mouseout_fg = mouseout_fg;
		setForeground(mouseout_fg);
	}
	
	// *******************
	// OVERRIDABLE METHODS
	// *******************
	
	public void entered() {}
	public void enteredDescendent(Component descendent) {}
	public void exited() {}
	public void exitedDescendent(Component descendent) {}
	public void moved() {}
	public void movedDescendent(Component descendent) {}
	public void pressed() {}
	public void released() {}
	public void clicked() {}
	public void clicked(Component xcomponent) {}
	public Object dragExport() { return null; }
	public void dropImport(Object o) {}
	public String getDragText() { return null; }
	public String getDropActionText(XComponent being_dragged) { return null; }
	public String getDropText() { return null; }
	public void draw(Graphics2D g) { super.paintComponent(g); }
	
}
