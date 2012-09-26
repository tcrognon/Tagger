package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import model.DataEvent;
import model.DataTag;
import model.Model;
import model.ModelEvent;
import model.ModelListener;
import view.View;

public class Controller implements ModelListener
{
	private Model model;
	private View view;
	
	public Controller(String tags_string, String[] events_string)
	{
		model = new Model();
		view = new View(this);
		model.loadTags(tags_string);
		view.recreateTags(model.getTags());
		model.loadEvents(events_string);
		view.recreateEvents(model.getEvents());
	}
	
	public static String[] showDialog(String tags_string, String[] events_string) throws InterruptedException
	{
		Controller controller = new Controller(tags_string, events_string);
		while (controller.view.isShowing())
		{
			Thread.sleep(100);
		}
		return controller.model.getReturnString();
	}
	
	public DataEvent addEvent()
	{
		return model.addEvent();
	}
	public void updateEventData(DataEvent eventdata, String code, String name, String description)
	{
		model.updateEventCode(eventdata, code);
		model.updateEventName(eventdata, name);
		model.updateEventDescription(eventdata, description);
		view.refreshData(eventdata);
	}
	public void removeEvent(DataEvent event)
	{
		model.removeEvent(event);
		view.recreateEvents(model.getEvents());
	}
	public void selectEvent(DataEvent eventdata)
	{
		model.selectEvent(eventdata);
		view.reloadEvents();
	}
	public DataEvent getCurrentEvent()
	{
		return model.getCurrentEvent();
	}
	
	public DataTag addTag(DataTag parent_tagdata)
	{
		DataTag tagdata = model.addTag(parent_tagdata);
		view.addTag(tagdata);
		view.reloadTags();
		return tagdata;
	}
	public void updateTagData(DataTag tagdata, String name, String description)
	{
		model.updateTagName(tagdata, name);
		model.updateTagDescription(tagdata, description);
		view.refreshData(tagdata);
	}
	public void removeTag(DataTag tagdata)
	{
		model.removeTag(tagdata);
		view.recreateTags(model.getTags());
		view.recreateEvents(model.getEvents());
	}
	public void toggleTag(DataEvent event, DataTag tag)
	{
		model.toggleTag(event, tag);
		view.reloadEvents();
	}
	public void toggleTag(DataTag tag)
	{
		toggleTag(model.getCurrentEvent(), tag);
	}
	public Collection<DataTag> searchTagsData(String s)
	{
		return model.search(s);
	}
	
	@Override public void dataTagAdded(ModelEvent e){}

	@Override public void dataTagRemoved(ModelEvent e){}

	@Override public void dataTagModified(ModelEvent e){}

	@Override public void dataEventAdded(ModelEvent e){}

	@Override public void dataEventRemoved(ModelEvent e){}

	@Override public void dataEventModified(ModelEvent e){}

	@Override public void dataEventSelected(ModelEvent e){}
	
	// for testing only
	public static void main(String[] args)
	{
		String tags = "";
		String[] events =
		{
			"1,some event 1,some event 1's description,/Time-Locked Event/Stimulus/Visual/Shape/Rotated,/Time-Locked Event/Stimulus/Visual/Shape/Star",
			"2,some event 2,some event 2's description",
			"3,some event 3,some event 3's description",
			"4,some event 4,some event 4's description",
			"5,some event 5,some event 5's description"
		};
		try
		{
			File file = new File("C:\\Users\\Thomas\\Desktop\\HED Specification 1.0.xml");
			tags = fileToString(file, Charset.forName("UTF-8"));
			String[] result = showDialog(tags, events);
			for (String s : result)
			{
				System.out.println(s);
			}
		}
		catch (Exception e)
		{
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
}
