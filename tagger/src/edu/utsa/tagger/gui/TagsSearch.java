package edu.utsa.tagger.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import javax.swing.JComponent;
import javax.swing.JLabel;
import edu.utsa.layouts.Anchor;
import edu.utsa.layouts.ListLayout;
import edu.utsa.tagger.DataWrappersTag;
import edu.utsa.tagger.ShellGUI;
import edu.utsa.xcomponent.XComponent;

public class TagsSearch extends XComponent {

	private RichTextField search_box;
	private JComponent search_results;
	
	public TagsSearch()
	{
		search_box = new RichTextField();
		search_box.setInsets(new Insets(5, 3, 5, 3));
		search_results = new JComponent()
		{
			@Override protected void paintComponent(Graphics g)
			{
				g.setColor(new Color(255, 255, 255, 200));
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		search_results.setLayout(new ListLayout(0));
		search_box.addKeyListener(new KeyListener() {
			@Override public void keyPressed(KeyEvent e) {}
			@Override public void keyReleased(KeyEvent e) { TagsSearch.this.search(); }
			@Override public void keyTyped(KeyEvent e) {}
		});
		
		add(search_box, new Anchor(null, 0, Anchor.TL, true, false, 0, 0, 0, 0));
		add(search_results, new Anchor(search_box, 0, Anchor.BOTTOM, true, false, 0, 0, 0, 0));
	}
	
	public void search(String s)
	{
		System.out.println("searching for " + s);
		search_box.setText(s);
		search();
	}
	
	public void search()
	{
		search_results.removeAll();
		String search_string = search_box.getText();
		if (search_string.equals(""))
		{
			revalidate();
			return;
		}
		Collection<DataWrappersTag> results = ShellGUI.search(search_string);
		for (DataWrappersTag t : results)
		{
			System.out.println(t.getPath());
			search_results.add(new TagsSearchItem(t));
		}
		revalidate();
	}
}
