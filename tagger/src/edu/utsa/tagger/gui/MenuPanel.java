package edu.utsa.tagger.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import edu.utsa.layouts.Anchor;
import edu.utsa.layouts.AnchorLayout;
import edu.utsa.tagger.ShellGUI;
import edu.utsa.xcomponent.XComponent;

public class MenuPanel extends XComponent
{
	private MenuButtonZoomOut zoom_out_button = new MenuButtonZoomOut();
	private MenuButtonZoomIn zoom_in_button = new MenuButtonZoomIn();
	
	public MenuPanel()
	{
		setOpaque(false);
		setLayout(new AnchorLayout());
		setPreferredSize(new Dimension(0, 42));
		zoom_out_button.setPreferredSize(new Dimension(36, 36));
		zoom_in_button.setPreferredSize(new Dimension(36, 36));
		add(zoom_out_button, new Anchor(Anchor.TR, false, false, 0, 3, 0, 3));
		add(zoom_in_button, new Anchor(zoom_out_button, 0, Anchor.LEFT, false, false, 0, 0, 0, 0));
	}
	
	@Override public void draw(Graphics2D g)
	{
//		Color c1 = new Color(91, 121, 191);
//		Color c2 = new Color(61, 81, 171);
//		Color c3 = new Color(0, 0, 0, 50);
//		Color c4 = new Color(0, 0, 0, 0);
//		
//		Point2D p1 = new Point2D.Double(0, 0);
//		Point2D p2 = new Point2D.Double(getWidth(), getHeight()-10);
//		Point2D p3 = new Point2D.Double(0, getHeight()-10);
//		Point2D p4 = new Point2D.Double(getWidth(), getHeight());
//		
//		Rectangle2D r1 = new Rectangle2D.Double(p1.getX(), p1.getY(), p2.getX()-p1.getX(), p2.getY()-p1.getY());
//		Rectangle2D r2 = new Rectangle2D.Double(p3.getX(), p3.getY(), p4.getX()-p3.getX(), p4.getY()-p3.getY());
//		Line2D line1 = new Line2D.Double(0, 0, getWidth(), 0);
//		Line2D line2 = new Line2D.Double(0, p3.getY()-1, getWidth(), p3.getY()-1);
//		
//		
//		Paint paint1 = new GradientPaint(0, 0, c1, 0, (float) p2.getY(), c2);
//		Paint paint2 = new GradientPaint(0, (float) p3.getY(), c3, 0, (float) p4.getY(), c4);
//		
//		g.setPaint(paint1);
//		g.fill(r1);
//		g.setPaint(paint2);
//		g.fill(r2);
//		g.setColor(c1);
//		g.draw(line2);
//		g.setColor(c2);
//		g.draw(line1);
	}
}
