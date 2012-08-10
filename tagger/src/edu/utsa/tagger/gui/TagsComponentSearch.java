package edu.utsa.tagger.gui;

import javax.swing.JPanel;
import edu.utsa.xcomponent.XComponent;

public class TagsComponentSearch extends XComponent {

	RichTextField search_box;
	JPanel search_results;
	
	public TagsComponentSearch()
	{
		search_box = new RichTextField();
		search_results = new JPanel();
	}
}
