package edu.utsa.tagger.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import edu.utsa.xcomponent.XComponent;

public class TagsComponentCollapse extends XComponent
{
	private static final Color COLLAPSER_COLLAPSED_BORDER = new Color(25, 25, 25);
	private static final Color COLLAPSER_UNCOLLAPSED_BORDER = Color.lightGray;
	private static final Color COLLAPSER_MOUSEOVER_BORDER = new Color(0, 200, 255);
	private static final Color COLLAPSER_COLLAPSED_FILL = new Color(115, 115, 115);
	
	private boolean collapsed;
	
	public TagsComponentCollapse()
	{
		collapsed = false;
	}
	
	public boolean isCollapsed()
	{
		return collapsed;
	}
	
	public void setCollapsed(boolean collapsed)
	{
		this.collapsed = collapsed;
	}
	
	@Override public Dimension getPreferredSize()
	{
		return new Dimension(getHeight(), getHeight());
	}

	@Override protected void draw(Graphics2D g)
	{

		double centerX = getWidth() / 2;
		double centerY = getHeight() / 2;
		double radius = getHeight() / 4;
		double thetaA, thetaB, thetaC;

		if (collapsed)
		{
			thetaA = Math.toRadians(-45);
			thetaB = Math.toRadians(45);
			thetaC = Math.toRadians(-135);
		} else
		{
			thetaA = Math.toRadians(0);
			thetaB = Math.toRadians(90);
			thetaC = Math.toRadians(-90);
		}

		Polygon poly = new Polygon();
		poly.addPoint((int)(centerX + radius * Math.cos(thetaA)), (int)(centerY - radius * Math.sin(thetaA)));
		poly.addPoint((int)(centerX + radius * Math.cos(thetaB)), (int)(centerY - radius * Math.sin(thetaB)));
		poly.addPoint((int)(centerX + radius * Math.cos(thetaC)), (int)(centerY - radius * Math.sin(thetaC)));

		Paint fill = collapsed && !isMouseover() ? COLLAPSER_COLLAPSED_FILL : null;
		Paint border = isMouseover() ? COLLAPSER_MOUSEOVER_BORDER : 
			collapsed ? COLLAPSER_COLLAPSED_BORDER : COLLAPSER_UNCOLLAPSED_BORDER;
		
		if (fill != null)
		{
			g.setPaint(fill);
			g.fill(poly);
		}
		if (border != null)
		{
			g.setPaint(border);
			g.draw(poly);
		}
	}

	@Override public void clicked()
	{
		collapsed = !collapsed;
		((TagsComponent) getParent()).collapsedChanged();
	}
}
