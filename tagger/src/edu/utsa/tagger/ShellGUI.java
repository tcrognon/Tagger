package edu.utsa.tagger;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.UUID;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import edu.utsa.layouts.Anchor;
import edu.utsa.layouts.AnchorLayout;
import edu.utsa.tagger.gui.EventsPanel;
import edu.utsa.tagger.gui.MenuButtonZoomIn;
import edu.utsa.tagger.gui.MenuButtonZoomOut;
import edu.utsa.tagger.gui.MenuPanel;
import edu.utsa.tagger.gui.NotificationError;
import edu.utsa.tagger.gui.NotificationInfo;
import edu.utsa.tagger.gui.TagsPanel;
import edu.utsa.xcomponent.XContainer;
import edu.utsa.xcomponent.XContentPane;

public class ShellGUI extends JDialog {

	private static MenuPanel menu_panel = new MenuPanel();
	private static EventsPanel events_panel = new EventsPanel();
	private static TagsPanel tags_panel = new TagsPanel();
	private static NotificationError notification_error = new NotificationError();
	private static NotificationInfo notification_info = new NotificationInfo();
	private static float base_font_size = 12.0f;
	
	public static void main(String[] args)
	{
		String tags = null;
		String events = null;
		try
		{
			File file = new File("C:\\Users\\Thomas\\Desktop\\HED Specification 1.0.xml");
			tags = fileToString(file, Charset.forName("UTF-8"));
			events = 
				"1;some event 1;some event 1's description\n" +
				"2;some event 2;some event 2's description\n" +
				"3;some event 3;some event 3's description\n" +
				"4;some event 4;some event 4's description\n" +
				"5;some event 5;some event 5's description\n";
			String s = ShellGUI.showDialog(tags, events);
			System.out.println(s);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static String fileToString(File file, Charset charset) throws IOException
	{
		int len = (int) file.length();
		if (len <= 0 || len >= Integer.MAX_VALUE)
		{
			return null;
		}
		byte[] b = new byte[len];
		InputStream in = new FileInputStream(file);
		int total = 0;
		while (total < len)
		{
			int result = in.read(b, total, len - total);
			if (result == -1)
			{
				break;
			}
			total += result;
		}
		if (in != null)
		{
			in.close();
		}
		if (total != b.length)
		{
			return null;
		}

		return new String(b, charset);
	}
	
	public ShellGUI(String tags, String events)
	{
		super((JDialog) null, true);
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
		}
		catch (Exception e)
		{
			System.out.println("Couldn't load fonts.");
			e.printStackTrace();
		}
		ShellCLI.loadTags(tags);
		ShellCLI.loadEvents(events);
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					createGUI();
				}
			});
		} catch (Exception e) { 
			System.err.println("createGUI failed.");
			e.printStackTrace();
		}
	}
	
	private void createGUI() {

		XContainer xcontainer = new XContainer();

		XContentPane content_pane = xcontainer.getContentPane();
		content_pane.setLayout(new AnchorLayout());

		content_pane.setLayer(notification_error,  10);
		content_pane.setLayer(notification_info,  9);
		content_pane.setLayer(menu_panel, 4);
		content_pane.setLayer(events_panel,  0);
		content_pane.setLayer(tags_panel, 0);

		content_pane.add(menu_panel, new Anchor(null, 4, Anchor.TL, true, false, 0, 0, 0, 0));
		content_pane.add(events_panel, new Anchor(null, 0, Anchor.TL, false, true, 15, 5, 0, 0));
		content_pane.add(tags_panel, new Anchor(events_panel, 0, Anchor.RIGHT, true, true, 15, 0, 0, 0));
		content_pane.add(notification_error, new Anchor(null, 10, Anchor.CC, false, false, 0, 0, 0, 0));
		content_pane.add(notification_info, new Anchor(null, 9, Anchor.BL, true, true, 0, 0, 0, 0));

		this.setLayout(new AnchorLayout());
		this.add(xcontainer, new Anchor(Anchor.TL, true, true));
		
		events_panel.load(ShellCLI.getEvents(), ShellCLI.getSelectedTags());
		tags_panel.load(ShellCLI.getHED());
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 600);
		setVisible(true);
	}
	
	public static String showDialog(String tags, String events)
	{
		new ShellGUI(tags, events);
		return ShellCLI.getReturnValue();
	}
	
	public static void showInfo(String s)
	{
		notification_info.showInfo(s);
	}
	
	public static void hideInfo()
	{
		notification_info.hideInfo();
	}
	
	public static boolean isEventSelected(DataWrappersEvent event)
	{
		return event == ShellCLI.getCurrentEvent();
	}

	public static DataWrappersTag getTag(UUID uuid)
	{
		return ShellCLI.getTag(uuid);
	}

	public static float getBaseFontSize()
	{
		return base_font_size;
	}
	
	public static boolean isTagCurrentlySelected(DataWrappersTag tag)
	{
		return ShellCLI.isTagCurrentlySelected(tag);
	}

	public static void selectEvent(DataWrappersEvent event)
	{
		if (ShellCLI.selectEvent(event))
		{
			events_panel.repaint();
			tags_panel.repaint();
		}
	}

	public static void toggleTag(DataWrappersTag tag)
	{
		if (ShellCLI.toggleTag(tag))
		{
			events_panel.load(ShellCLI.getEvents(), ShellCLI.getSelectedTags());
		}
		else
		{
			if (ErrorStack.peek().code == 230)
			{
				notification_error.showMessage("Please select an event first.");
			}
		}
	}

	public static void zoomIn()
	{
		base_font_size += 4;
		events_panel.repaint();
		tags_panel.repaint();
	}
	public static void zoomOut()
	{
		base_font_size -= 4;
		events_panel.repaint();
		tags_panel.repaint();
	}
	
	public static void removeEvent(UUID event_uuid)
	{
		ShellCLI.removeEvent(event_uuid);
		events_panel.load(ShellCLI.getEvents(), ShellCLI.getSelectedTags());
	}

	public static void insertEvent(String s)
	{
		DataWrappersEvent item = ShellCLI.insertEvent(s);
		events_panel.load(ShellCLI.getEvents(), ShellCLI.getSelectedTags());
	}
}
