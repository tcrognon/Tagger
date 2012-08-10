package edu.utsa.xcomponent;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLayeredPane;
import javax.swing.Timer;
import edu.utsa.layouts.AnchorLayout;

public abstract class XComponent extends JLayeredPane implements Comparable<XComponent> {

	private boolean mouseover = false;
	private boolean mousedirectlyover = false;
	private boolean mousepressed = false;
	private boolean draggable = false;
	private boolean droppable = false;
	private boolean being_dragged = false;
	private boolean pending_drop = false;
	
	public XComponent() {
		setLayout(new AnchorLayout());
	}
	
	@Override protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//Composite c = g2d.getComposite();
		//g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		
		//super.paintComponent(g2d);
		draw(g2d);
		
		//g2d.setComposite(c);
	}
	
	protected void draw(Graphics2D g) {
		super.paintComponent(g);
	}
	
//	private final int interval = 50;
//	private int duration = 0;
//	private int direction = 1; // only set to +1 or -1
//	private float alpha = 1.0f;
//	private float delta_alpha = 0.0f;
//	private Timer timer = new Timer(interval, new ActionListener() {
//
//		@Override public void actionPerformed(ActionEvent e)
//		{
//			alpha += direction * delta_alpha;
//			System.out.println(alpha);
//			if (alpha < 0.0f || alpha > 1.0f)
//			{
//				alpha += -direction * delta_alpha;
//				timer.stop();
//				duration = 0;
//				//setVisible(false);
//			}
//			repaint();
//		}});
//	
//	public void fadeOut(float initial_alpha, int milliseconds)
//	{
//		this.alpha = initial_alpha;
//		this.duration = milliseconds;
//		this.delta_alpha = initial_alpha / ((float) duration / (float) interval);
//		this.direction = -1;
//		timer.start();
//	}
	
	final public boolean isMouseover() { return mouseover; }
	final public boolean isMousedirectlyover() { return mousedirectlyover; }
	final public boolean isMousepressed() { return mousepressed; }
	final public boolean isDraggable() { return draggable; }
	final public boolean isDroppable() { return droppable; }
	final public boolean isBeingDragged() { return being_dragged; }
	final public boolean isPendingDrop() { return pending_drop; }
	
	void setMouseover(boolean mouseover) {
		this.mouseover = mouseover;
	}
	void setMousedirectlyover(boolean mousedirectlyover) {
		this.mousedirectlyover = mousedirectlyover;
	}
	void setMousePressed(boolean mousepressed) { this.mousepressed = mousepressed; }
	final public void setDraggable(boolean draggable) { this.draggable = draggable; }
	final public void setDroppable(boolean droppable) { this.droppable = droppable; }
	void setBeingDragged(boolean being_dragged) { this.being_dragged = being_dragged; }
	void setPendingDropped(boolean pending_drop) { this.pending_drop = pending_drop; }
	
	public void entered() {}
	public void enteredDescendent(Component descendent) {}
	public void exited() {}
	public void exitedDescendent(Component descendent) {}
	public void moved() {}
	public void movedDescendent(Component descendent) {}
	public void clicked() {}
	public void clicked(XComponent xcomponent) {}
	public Object dragExport() { return null; }
	public void dropImport(Object o) {}
	public String getDragText() { return null; }
	public String getDropActionText(XComponent being_dragged) { return null; }
	public String getDropText() { return null; }
	
	@Override public boolean equals(Object obj) {
		return this == obj;
	}
	
	@Override public int compareTo(XComponent xc) {
		return this.equals(xc) ? 0 : 1;
	}
	
}
