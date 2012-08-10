package edu.utsa.tagger.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import javax.swing.JLabel;
import edu.utsa.layouts.Anchor;
import edu.utsa.tagger.DataWrappersTag;
import edu.utsa.tagger.ShellGUI;
import edu.utsa.xcomponent.XComponent;

public class TagsComponent extends XComponent {

	private static final Color TAG_MOUSEOVER_FILL1 = new Color(246, 247, 249, 125);
	private static final Color TAG_MOUSEOVER_FILL2 = new Color(220, 228, 237, 125);
	private static final Color TAG_MOUSEOVER_BORDER = new Color(160, 160, 255, 125);
	private static final Color TAG_SELECTED_FILL1 = new Color(230, 239, 255, 125);
	private static final Color TAG_SELECTED_FILL2 = new Color(178, 199, 245, 125);
	private static final Color TAG_SELECTED_BORDER = new Color(50, 90, 190, 125);
	
	private DataWrappersTag tag;
	private JLabel label;
	private Anchor label_anchor;
	private TagsComponentCollapse collapse_button;
	private int level;
	
	private TagsComponent parent;
	private ArrayList<TagsComponent> children;
	
	public TagsComponent(DataWrappersTag tag, int level, TagsComponent parent) {
		this.parent = parent;
		this.level = level;
		children = new ArrayList<TagsComponent>();
		setDraggable(true);
		setDroppable(true);
		this.tag = tag;
		label = new JLabel(tag.getLabel());
		label.setFont(new Font("Segoe UI", Font.PLAIN, (int) ShellGUI.getBaseFontSize()));
		collapse_button = new TagsComponentCollapse();
		label_anchor = new Anchor(null, 0, Anchor.TL, false, false, level * label.getPreferredSize().height, 1, 0, 1);
		add(label, label_anchor);
		add(collapse_button, new Anchor(label, Anchor.LEFT, 0, 0, 3, 0, label));
	}
	
	public ArrayList<TagsComponent> getChildren()
	{
		return children;
	}
	
	@Override public void clicked() {
		ShellGUI.toggleTag(tag);
	}
	
	@Override public void entered()
	{
		ShellGUI.showInfo(tag.getPath() + "\n" + tag.getDescription());
	}
	
	@Override public void exited()
	{
		ShellGUI.hideInfo();
	}
	
	@Override protected void draw(Graphics2D g) {
		
		boolean mouseover = isMouseover() || isBeingDragged() || this.isPendingDrop();
		boolean selected = ShellGUI.isTagCurrentlySelected(tag);

		if (selected || mouseover) {
			Color c1 = selected ? TAG_SELECTED_FILL1 : (mouseover ? TAG_MOUSEOVER_FILL1 : null);
			Color c2 = selected ? TAG_SELECTED_FILL2 : (mouseover ? TAG_MOUSEOVER_FILL2 : null);
			Color border = selected ? TAG_SELECTED_BORDER : (mouseover ? TAG_MOUSEOVER_BORDER : null);
			g.setPaint(new GradientPaint(
					new Point(0, 0),
					c1,
					new Point(0, getHeight()),
					c2));
			g.fill(new RoundRectangle2D.Double(2, 0, getWidth()-4, getHeight()-1, 5, 5));
			g.setColor(border);
			g.draw(new RoundRectangle2D.Double(2, 0, getWidth()-4, getHeight()-1, 5, 5));
		}
		if ((float) label.getFont().getSize() != ShellGUI.getBaseFontSize()) {
			label.setFont(getFont().deriveFont(ShellGUI.getBaseFontSize()));
			label_anchor.setOffsets(level * label.getPreferredSize().height, 1, 0, 1);
		}
		collapse_button.setVisible(children.size() > 0);
		
	}
	
	@Override public String getDragText() {
		return tag.getLabel();
	}
	@Override public String getDropActionText(XComponent being_dragged) {
		if (being_dragged instanceof TagsComponent) {
			return "Move to";
		}
		else {
			return "";
		}
	}
	@Override public String getDropText() {
		return tag.getLabel();
	}
	
	@Override public Object dragExport()
	{
		return this;
	}
	
	private boolean isCollapsed()
	{
		return collapse_button.isCollapsed();
	}
	
	private boolean isAnyParentCollapsed()
	{
		if (parent == null)
		{
			return false;
		}
		else
		{
			return parent.isCollapsed() ? true : parent.isAnyParentCollapsed();
		}
	}
	
	public void collapsedChanged()
	{
		if (isCollapsed())
		{
			for (TagsComponent tc : children) tc.hide();
		}
		else
		{
			for (TagsComponent tc : children) tc.unhide();
		}
		revalidate();
	}
	
	public void hide()
	{
		setMaximumSize(new Dimension(0, 0));
		for (TagsComponent tc : children) tc.hide();
	}
	
	public void unhide()
	{
		setMaximumSize(null);
		for (TagsComponent tc : children) tc.unhide();
	}
}
