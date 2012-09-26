package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import model.DataEvent;
import model.DataTag;
import controller.Controller;
import edu.utsa.layouts.Anchor;
import edu.utsa.layouts.AnchorLayout;
import edu.utsa.layouts.ListLayout;
import edu.utsa.layouts.Margins;
import edu.utsa.xcomponent.XContainer;
import edu.utsa.xcomponent.XContentPane;
import edu.utsa.xcomponent.XScrollPane;
import edu.utsa.xcomponent.XSplitPane;

public class View extends JFrame
{
	public static final Color VERY_LIGHT_GREY = new Color(235, 235, 235);
	public static final Color GREY_DARK = new Color(110, 110, 110);
	public static final Color GREY_MEDIUM = new Color(205, 205, 205);
	public static final Color GREY_LIGHT = new Color(225, 225, 225);
	public static final Color BLUE_DARK = new Color(100, 140, 245);
	public static final Color BLUE_MEDIUM = new Color(205, 205, 255);
	public static final Color BLUE_LIGHT = new Color(225, 225, 255);

	private Font segoe_ui;
	private Font segoe_ui_light;
	private Font segoe_ui_symbol;

	private Controller controller;

	public View(Controller controller)
	{
		this.controller = controller;
		loadFonts();
		try
		{
			SwingUtilities.invokeAndWait(new Runnable()
			{
				public void run(){createGUI();}
			});
		}
		catch (Exception e)
		{ 
			System.err.println("Failed to create GUI.");
			e.printStackTrace();
		}
	}

	private void loadFonts()
	{
		try
		{
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			InputStream stream;

			stream = this.getClass().getResourceAsStream("/segoeui.ttf");
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, stream));
			stream.close();

			stream = this.getClass().getResourceAsStream("/segoeuil.ttf");
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, stream));
			stream.close();
			
			stream = this.getClass().getResourceAsStream("/seguisym.ttf");
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, stream));
			stream.close();

			segoe_ui = new Font("Segoe UI", Font.PLAIN, (int) base_font_size);
			segoe_ui_light = new Font("Segoe UI Light", Font.PLAIN, (int) base_font_size);
			segoe_ui_symbol = new Font("Segoe UI Symbol", Font.PLAIN, (int) base_font_size);
		}
		catch (Exception e)
		{
			System.out.println("Couldn't load fonts.");
			e.printStackTrace();
		}
	}

	private JComponent tags_list;
	private RichTextField search_box;
	private JPanel search_results;
	private XScrollPane tags_scrollpane;
	private JLayeredPane tags_panel;
	private Collection<Tag> tags;

	private JLabel events_panel_header;
	private JPanel events_list;
	private XScrollPane events_scrollpane;
	private Button add_event;
	private JPanel events_panel;
	private Collection<Event> events;
	
	private Properties properties;
	private JComponent properties_hitarea;
	
	private JLabel zoomin;
	private JLabel zoomout;
	private Menu menu_panel;
	
	private XContainer xcontainer;
	private XSplitPane xsplitpane;

	private void createGUI()
	{
		// *************************
		//    TAGS
		// *************************
		tags_list = new JComponent(){};
		tags_list.setLayout(new ListLayout(0));
		tags_scrollpane = new XScrollPane(tags_list);
		search_box = new RichTextField();
		search_box.addKeyListener(new KeyAdapter()
		{
			@Override public void keyReleased(KeyEvent e)
			{
				searchTags(search_box.getText());
			}
		});
		search_results = new JPanel();
		search_results.setOpaque(false);
		search_results.setBorder(new DropShadowBorder(Color.BLACK, 0, 8, .2f, 16, false, true, true, true));
		search_results.setLayout(new ListLayout(8, 12, 8, 8));
		search_results.setVisible(false);
		tags_panel = new JLayeredPane();
		tags_panel.setBackground(Color.WHITE);
		tags_panel.setLayout(new AnchorLayout());
		tags_panel.setLayer(search_box, 2);
		tags_panel.setLayer(search_results, 1);
		tags_panel.add(search_box, new Anchor(2, Anchor.TL, Anchor.HORIZONTAL, new Margins(10, 10, 0, 10)));
		tags_panel.add(tags_scrollpane, new Anchor(search_box, 0, Anchor.BOTTOM, Anchor.STRETCHALL, new Margins(5, -10, 0, 0)));
		tags_panel.add(search_results, new Anchor(search_box, 1, Anchor.BOTTOM, Anchor.HORIZONTAL, new Margins(-13, -5, 0, 5)));
		tags = new ArrayList<Tag>();

		// *************************
		//    EVENTS
		// *************************
		events_panel_header = new JLabel("Events");
		events_panel_header.setFont(segoe_ui_light.deriveFont(36f));
		events_panel_header.setForeground(BLUE_DARK);
		events_list = new JPanel();
		events_list.setOpaque(false);
		events_list.setLayout(new ListLayout(0));
		events_scrollpane = new XScrollPane(events_list);
		add_event = new Button("  Add Event  ");
		add_event.addMouseListener(new MouseAdapter()
		{
			@Override public void mouseClicked(MouseEvent e)
			{
				DataEvent eventdata = controller.addEvent();
				addEvent(eventdata);
				reloadEvents();
			}
		});
		events_panel = new JPanel();
		events_panel.setOpaque(false);
		events_panel.setLayout(new AnchorLayout());
		events_panel.setPreferredSize(new Dimension(300, 0));
		events_panel.add(events_panel_header, new Anchor(Anchor.TL, Anchor.HORIZONTAL, new Margins(10, 15, 0, 10)));
		events_panel.add(add_event, new Anchor(events_panel_header, Anchor.BOTTOM, Anchor.NOSTRETCH, new Margins(10, 0, 0, 10)));
		events_panel.add(events_scrollpane, new Anchor(add_event, Anchor.BOTTOM, Anchor.STRETCHALL, new Margins(10, -15, 0, 0)));
		events = new ArrayList<Event>();
		
		// *************************
		//    PROPERTIES
		// *************************
		properties_hitarea = new JComponent(){};
		properties_hitarea.addMouseListener(new MouseAdapter()
		{
			@Override public void mouseEntered(MouseEvent e)
			{
				properties.setVisible(true);
			}
		});
		properties_hitarea.setPreferredSize(new Dimension(0, 20));
		properties = new Properties();
		properties.getHeaderLabel().setFont(segoe_ui_light.deriveFont(segoe_ui_light.getSize() + 12f));
		properties.getHeaderLabel().setForeground(Color.black);
		properties.getPin().addMouseListener(new MouseAdapter()
		{
			@Override public void mouseClicked(MouseEvent e)
			{
				firePinned(new ViewEvent(this, null));
			}
		});
		properties.getArrow().setFont(segoe_ui_symbol.deriveFont(24f));
		KeyAdapter keyadapter = new KeyAdapter()
		{
			@Override public void keyReleased(KeyEvent e)
			{
				Object pinneddata = properties.getPinnedData();
				if (properties.getPinnedData() != null)
				{
					if (pinneddata instanceof DataEvent)
					{
						controller.updateEventData(
							(DataEvent) pinneddata, 
							properties.getCodeTextField().getText(), 
							properties.getNameTextField().getText(), 
							properties.getDescriptionTextField().getText());
					}
					else if (pinneddata instanceof DataTag)
					{
						controller.updateTagData(
							(DataTag) pinneddata,  
							properties.getNameTextField().getText(), 
							properties.getDescriptionTextField().getText());
					}
				}
			}
		};
		properties.getCodeTextField().addKeyListener(keyadapter);
		properties.getNameTextField().addKeyListener(keyadapter);
		properties.getDescriptionTextField().addKeyListener(keyadapter);
		properties.getDeleteButton().setFont(segoe_ui_symbol);
		properties.getDeleteButton().addMouseListener(new MouseAdapter()
		{
			@Override public void mouseClicked(MouseEvent e)
			{
				Object pinneddata = properties.getPinnedData();
				firePinned(new ViewEvent(this, null));
				if (pinneddata instanceof DataEvent)
				{
					controller.removeEvent((DataEvent) pinneddata);
				}
				else if (pinneddata instanceof DataTag)
				{
					controller.removeTag((DataTag) pinneddata);
				}
			}
		});
		properties.setVisible(false);
		
		// *************************
		//    MENU
		// *************************
		zoomin = new JLabel("+");
		zoomin.setFont(segoe_ui_light.deriveFont(24f));
		zoomin.setForeground(GREY_DARK);
		zoomin.addMouseListener(new MouseAdapter()
		{
			@Override public void mouseClicked(MouseEvent e){zoomIn();}
		});
		zoomout = new JLabel("-");
		zoomout.setFont(segoe_ui_light.deriveFont(24f));
		zoomout.setForeground(GREY_DARK);
		zoomout.addMouseListener(new MouseAdapter()
		{
			@Override public void mouseClicked(MouseEvent e){zoomOut();}
		});
		menu_panel = new Menu();
		menu_panel.setLayout(new AnchorLayout());
		menu_panel.add(zoomout, new Anchor(Anchor.TR, Anchor.NOSTRETCH, new Margins(0, 0, 0, 10)));
		menu_panel.add(zoomin, new Anchor(zoomout, Anchor.LEFT, Anchor.NOSTRETCH, new Margins(0, 0, 0, 10)));

		// *************************
		//    ALL TOGETHER NOW
		// *************************
		xcontainer = new XContainer();
		xsplitpane = new XSplitPane(XSplitPane.VERTICAL, 300);
		xsplitpane.getA().setLayout(new AnchorLayout());
		xsplitpane.getA().setBackground(VERY_LIGHT_GREY);
		xsplitpane.getB().setLayout(new AnchorLayout());
		xsplitpane.getB().setBackground(Color.WHITE);
		xsplitpane.getA().add(events_panel, new Anchor(Anchor.TL, Anchor.STRETCHALL));
		xsplitpane.getB().add(tags_panel, new Anchor(Anchor.TL, Anchor.STRETCHALL));

		XContentPane content_pane = xcontainer.getContentPane();
		content_pane.setLayout(new AnchorLayout());
		content_pane.setLayer(menu_panel, 4);
		content_pane.setLayer(properties, 2);
		content_pane.setLayer(properties_hitarea, 1);
		content_pane.setLayer(xsplitpane, 0);
		content_pane.add(properties, new Anchor(2, Anchor.BL, Anchor.HORIZONTAL));
		content_pane.add(properties_hitarea, new Anchor(1, Anchor.BL, Anchor.HORIZONTAL));
		content_pane.add(xsplitpane, new Anchor(0, Anchor.TL, Anchor.STRETCHALL));
		//content_pane.add(menu_panel, new Anchor(4, Anchor.TL, Anchor.HORIZONTAL));

		setLayout(new AnchorLayout());
		add(xcontainer, new Anchor(Anchor.TL, Anchor.STRETCHALL));
		
		addViewListener(properties);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 600);
		setVisible(true);
	}
	
	public Controller getController()
	{
		return controller;
	}
	
	///////////////////
	//               //
	//  TAG METHODS  //
	//               //
	///////////////////
	
	public void recreateTags(Collection<DataTag> tagsdata)
	{
		tags.clear();
		addTags(tagsdata);
		reloadTags();
	}
	
	public void addTags(Collection<DataTag> tagsdata)
	{
		for (DataTag tagdata : tagsdata)
		{
			addTag(tagdata);
			addTags(tagdata.getChildTags());
		}
	}
	
	public void addTag(DataTag tagdata)
	{
		// Make the tag.
		final Tag tag = new Tag(tagdata)
		{
			@Override public void clicked(Component c)
			{
				if (c != this.getCollapser() && c != this.getAddButton() && c != this.getPin())
				{
					controller.toggleTag(this.getTagData());
				}
				if (c == this.getPin())
				{
					Object pinneddata = this.getPin().isPinned() ? null : this.getTagData();
					View.this.firePinned(new ViewEvent(this, pinneddata));
				}
			}
			
			@Override public void entered()
			{
				this.getPin().setVisible(true);
				properties.showProperties(this.getTagData());
			}
			
			@Override public void exited()
			{
				if (!this.getPin().isPinned())
				{
					this.getPin().setVisible(false);
					properties.showProperties(null);
				}
			}
		};
		tag.setFont(segoe_ui);
		tag.getCollapser().addMouseListener(new MouseAdapter()
		{
			@Override public void mouseClicked(MouseEvent e)
			{
				toggleCollapse(tag);
			}
		});
		if (tag.getAddButton() != null)
		{
			tag.getAddButton().addMouseListener(new MouseAdapter()
			{
				@Override public void mouseClicked(MouseEvent e)
				{
					DataTag tagdata = controller.addTag(tag.getTagData());
					firePinned(new ViewEvent(this, tagdata));
					scrollTo(tagdata);
				}
			});
		}
		addViewListener(tag);
		
		// Add tag in appropriate place.
		Tag parent = getTag(tags, tagdata.getParent());
		if (parent == null)
		{
			tags.add(tag);
		}
		else
		{
			parent.getChildren().add(tag);
		}
	}
	
	private static Tag getTag(Collection<Tag> tags, DataTag tagdata)
	{
		for (Tag tag : tags)
		{
			if (tag.getTagData() == tagdata)
			{
				return tag;
			}
			Tag tmp = getTag(tag.getChildren(), tagdata);
			if (tmp != null)
			{
				return tmp;
			}
		}
		return null;
	}
	
	public void reloadTags()
	{
		tags_list.removeAll();
		loadTags(tags);
		revalidate();
	}
	
	private void loadTags(Collection<Tag> tags)
	{
		for (Tag tag : tags)
		{
			tags_list.add(tag);
			tag.getCollapser().setVisible(tag.getTagData().getChildTags().size() > 0);
			loadTags(tag.getChildren());
		}
	}
	
	private void toggleCollapse(Tag tag)
	{
		for (Tag child_tag : tag.getChildren())
		{
			child_tag.setVisible(!child_tag.isVisible());
			toggleCollapse(child_tag);
		}
		revalidate();
	}

	private void searchTags(String search_string)
	{
		search_box.setText(search_string);
		if (search_string.equals(""))
		{
			revalidate();
			search_results.setVisible(false);
			return;
		}
		search_results.removeAll();
		search_results.setVisible(true);
		final ArrayList<DataTag> results = (ArrayList<DataTag>) controller.searchTagsData(search_string);
		for (int i = 0; i < results.size(); i++)
		{
			final DataTag tagdata = results.get(i);
			SearchResult result = new SearchResult(tagdata)
			{
				@Override public void clicked(Component c)
				{
					scrollTo(tagdata);
					searchTags("");
					if (c == getSelectButton())
					{
						controller.toggleTag(getTagData());
					}
				}
			};
			result.setFont(segoe_ui);
			search_results.add(result);
		}
		revalidate();
	}
	
	private void scrollTo(DataTag tagdata)
	{
		for (Component c : tags_list.getComponents())
		{
			Tag tag = (Tag) c;
			if (tag.getTagData().equals(tagdata))
			{
				tags_scrollpane.scrollTo(tag.getY(), 0);
				return;
			}
		}
	}
	
	/////////////////////
	//                 //
	//  EVENT METHODS  //
	//                 //
	/////////////////////
	
	public void recreateEvents(Collection<DataEvent> eventsdata)
	{
		events.clear();
		addEvents(eventsdata);
		reloadEvents();
	}
	
	public void addEvents(Collection<DataEvent> eventsdata)
	{
		for (DataEvent eventdata : eventsdata)
		{
			addEvent(eventdata);
		}
	}
	
	private void addEvent(final DataEvent eventdata)
	{
		Event event = new Event(eventdata)
		{
			@Override public void clicked(Component c)
			{
				if (c != this.getPin())
				{
					controller.selectEvent(eventdata);
				}
				if (c == this.getPin())
				{
					Object pinneddata = this.getPin().isPinned() ? null : this.getEventData();
					View.this.firePinned(new ViewEvent(this, pinneddata));
				}
			}
			
			@Override public void entered()
			{
				this.getPin().setVisible(true);
				properties.showProperties(this.getEventData());
			}
			
			@Override public void exited()
			{
				if (!this.getPin().isPinned())
				{
					this.getPin().setVisible(false);
					properties.showProperties(null);
				}
			}
			
			@Override public void dropImport(Object o)
			{
				if (o instanceof DataTag)
				{
					DataTag tagdata = (DataTag) o;
					if (!eventdata.containsTagData(tagdata))
					{
						controller.toggleTag(eventdata, tagdata);
					}
				}
			}
		};
		event.setFont(segoe_ui.deriveFont(segoe_ui.getSize() + 4f));
		reloadEvent(event);
		events.add(event);
		addViewListener(event);
	}
	
	private void reloadEvent(Event event)
	{
		event.getTagsList().removeAll();
		for (DataTag tagdata : event.getEventData().getTags())
		{
			Selectedtag selectedtag = new Selectedtag(event.getEventData(), tagdata)
			{
				@Override public void clicked(Component c)
				{
					if (!(c instanceof Button))
					{
						scrollTo(this.getTagData());
					}
					else
					{
						controller.toggleTag(this.getEventData(), this.getTagData());
					}
				}
			};
			selectedtag.setFont(segoe_ui);
			event.getTagsList().add(selectedtag);
			addViewListener(selectedtag);
		}
	}
	
	public void reloadEvents()
	{
		events_list.removeAll();
		for (Event event : events)
		{
			reloadEvent(event);
			
			// if selected
			DataEvent eventdata = controller.getCurrentEvent();
			if (event.getEventData() == eventdata)
			{
				event.setSelected(true);
				for (Component c : tags_list.getComponents())
				{
					Tag tag = (Tag) c;
					if (eventdata.containsTagData(tag.getTagData()))
					{
						tag.setMouseoverBg(BLUE_MEDIUM);
						tag.setMouseoutBg(BLUE_MEDIUM);
					}
					else
					{
						tag.setMouseoverBg(BLUE_LIGHT);
						tag.setMouseoutBg(null);
					}
				}
			}
			else
			{
				event.setSelected(false);
			}
			events_list.add(event);
		}
		revalidate();
	}

	private float base_font_size = 12;
	
	private void zoomIn()
	{
		if (base_font_size < 26)
		{
			base_font_size += 1;
			segoe_ui = segoe_ui.deriveFont(base_font_size);
			segoe_ui_light = segoe_ui_light.deriveFont(base_font_size);
			fireFontSizeChanged(new ViewEvent(this, 1));
		}
	}
	private void zoomOut()
	{
		if (base_font_size > 8)
		{
			base_font_size -= 1;
			segoe_ui = segoe_ui.deriveFont(base_font_size);
			segoe_ui_light = segoe_ui_light.deriveFont(base_font_size);
			fireFontSizeChanged(new ViewEvent(this, -1));
		}
	}
	
	////////////////////////
	//                    //
	//  LISTENER METHODS  //
	//                    //
	////////////////////////

	private EventListenerList listener_list = new EventListenerList();

	public void addViewListener(ViewListener listener)
	{
		listener_list.add(ViewListener.class, listener);
	}

	public void removeViewListener(ViewListener listener)
	{
		listener_list.remove(ViewListener.class, listener);
	}

	private void fireFontSizeChanged(ViewEvent e)
	{
		Object[] list = listener_list.getListenerList();
		for (int i = 0; i < list.length; i += 2)
		{
			if (list[i] == ViewListener.class)
			{
				((ViewListener) list[i+1]).fontSizeChanged(e);
			}
		}
	}
	
	private void firePinned(ViewEvent e)
	{
		Object[] list = listener_list.getListenerList();
		for (int i = 0; i < list.length; i += 2)
		{
			if (list[i] == ViewListener.class)
			{
				((ViewListener) list[i+1]).pinned(e);
			}
		}
	}
	
	public void refreshData(Object data)
	{
		fireDataUpdated(new ViewEvent(this, data));
	}
	
	private void fireDataUpdated(ViewEvent e)
	{
		Object[] list = listener_list.getListenerList();
		for (int i = 0; i < list.length; i += 2)
		{
			if (list[i] == ViewListener.class)
			{
				((ViewListener) list[i+1]).dataUpdated(e);
			}
		}
	}
}