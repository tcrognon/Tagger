package model;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.event.EventListenerList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public final class Model
{
	private DataEvent current_event = null;
	private Collection<DataEvent> events = new ArrayList<DataEvent>();
	private DataHED hed = null;
	private Collection<DataTag> tags = new ArrayList<DataTag>();

	public Model() {}

	public void loadTags(String tags_string)
	{
		try
		{
			StringReader stringreader = new StringReader(tags_string);
			JAXBContext context = JAXBContext.newInstance(DataHED.class);
			hed = (DataHED) context.createUnmarshaller().unmarshal(stringreader);
			tags = hed.getTags();
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
		}

		for (DataTag tag : tags)
		{
			updateTagParents(tag);
		}
		for (DataTag tag : tags)
		{
			updateTagPaths(tag);
		}
	}

	public void loadEvents(String[] event_strings)
	{
		for (String s : event_strings)
		{
			String[] data = s.split(",");
			if (data.length < 3)
			{
				System.out.println("Invalid event data format.");
				return;
			}

			DataEvent event = new DataEvent();
			event.setCode(data[0]);
			event.setLabel(data[1]);
			event.setDescription(data[2]);
			events.add(event);
			Collection<DataTag> event_tags = event.getTags();
			for (int i = 3; i < data.length; i++)
			{
				DataTag tag = getTagFromFullPath(data[i], tags);
				if (tag != null)
				{
					event_tags.add(tag);
				}
			}
			fireDataEventAdded(new ModelEvent(this, event));
		}
	}

	private static DataTag getTagFromFullPath(String fullpath, Collection<DataTag> tags)
	{
		for (DataTag tag : tags)
		{
			if ((tag.getPath() + tag.getLabel()).equals(fullpath))
			{
				return tag;
			}
			DataTag temp = getTagFromFullPath(fullpath, tag.getChildTags());
			if (temp != null)
			{
				return temp;
			}
		}
		return null;
	}

	public String[] getReturnString()
	{
		ArrayList<String> list = new ArrayList<String>();

		list.add(marshalTags());

		for (DataEvent event : events)
		{
			String event_string = "";
			event_string += event.getCode() + ",";
			event_string += event.getLabel() + ",";
			event_string += event.getDescription() + ",";

			Collection<DataTag> event_tags = event.getTags();
			for (DataTag event_tag : event_tags)
			{
				event_string += event_tag.getFullPath() + ",";
			}
			event_string = event_string.substring(0, event_string.length()-1);
			list.add(event_string);
		}

		String[] result = new String[list.size()];
		for (int i = 0; i < result.length; i++)
		{
			result[i] = list.get(i);
		}

		return result;
	}

	private String marshalTags()
	{
		try
		{
			StringWriter stringwriter = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(DataHED.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(hed, stringwriter);
			return stringwriter.toString();
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public Collection<DataTag> search(String s)
	{
		Collection<DataTag> results = new ArrayList<DataTag>();
		searchHelper(s, results, tags);
		return results;
	}

	private static void searchHelper(String s, Collection<DataTag> results, Collection<DataTag> tags)
	{
		for (DataTag tag : tags)
		{
			if (tag.getLabel().toLowerCase().indexOf(s.toLowerCase()) != -1)
			{
				results.add(tag);
			}
			searchHelper(s, results, tag.getChildTags());
		}
	}

	public DataTag addTag(DataTag parent)
	{
		DataTag tag = new DataTag();
		tag.setParent(parent);
		tag.setPath(parent.getFullPath() + "/");
		tag.setCustom(true);
		parent.getChildTags().add(tag);
		fireDataTagAdded(new ModelEvent(this, tag));
		return tag;
	}

	public void removeTag(DataTag tag)
	{
		tag.getParent().getChildTags().remove(tag);
		fireDataTagRemoved(new ModelEvent(this, tag));
		for (DataEvent event : events)
		{
			Collection<DataTag> event_tags = event.getTags();
			if (event_tags.contains(tag))
			{
				event_tags.remove(tag);
			}
			fireDataEventModified(new ModelEvent(this, event));
		}
	}

	public void updateTagName(DataTag tag, String name)
	{
		tag.setLabel(name);
		fireDataTagModified(new ModelEvent(this, tag));
		for (DataTag child_tag : tag.getChildTags())
		{
			updateTagPaths(child_tag);
		}
	}

	public void updateTagParents(DataTag tagdata)
	{
		for (DataTag child_tagdata : tagdata.getChildTags())
		{
			child_tagdata.setParent(tagdata);
			updateTagParents(child_tagdata);
		}
	}

	public void updateTagPaths(DataTag tagdata)
	{
		tagdata.updatePath();
		fireDataTagModified(new ModelEvent(this, tagdata));
		for (DataTag child_tagdata : tagdata.getChildTags())
		{
			updateTagPaths(child_tagdata);
		}
	}

	public void updateTagDescription(DataTag tag, String description)
	{
		tag.setDescription(description);
		fireDataTagModified(new ModelEvent(this, tag));
	}

	public void toggleTag(DataEvent event, DataTag tag)
	{
		if (event == null)
		{
			return;
		}
		Collection<DataTag> event_tags = event.getTags();
		if (event_tags.contains(tag))
		{
			event_tags.remove(tag);
		}
		else
		{
			event_tags.add(tag);
		}
		fireDataEventModified(new ModelEvent(this, event));
	}

	public Collection<DataTag> getTags()
	{
		return tags;
	}

	public void selectEvent(DataEvent event)
	{
		if (events.contains(event))
		{
			current_event = event;
			fireDataEventSelected(new ModelEvent(this, event));
		}
	}

	public DataEvent addEvent()
	{
		DataEvent event = new DataEvent();
		events.add(event);
		fireDataEventAdded(new ModelEvent(this, event));
		return event;
	}

	public void removeEvent(DataEvent event)
	{
		events.remove(event);
		fireDataEventRemoved(new ModelEvent(this, event));
	}

	public void updateEventCode(DataEvent event, String code)
	{
		event.setCode(code);
		fireDataEventModified(new ModelEvent(this, event));
	}

	public void updateEventName(DataEvent event, String name)
	{
		event.setLabel(name);
		fireDataEventModified(new ModelEvent(this, event));
	}

	public void updateEventDescription(DataEvent event, String description)
	{
		event.setDescription(description);
		fireDataEventModified(new ModelEvent(this, event));
	}

	public Collection<DataEvent> getEvents()
	{
		return events;
	}

	public DataEvent getCurrentEvent()
	{
		return current_event;
	}

	private EventListenerList listener_list = new EventListenerList();

	public void addModelListener(ModelListener listener)
	{
		listener_list.add(ModelListener.class, listener);
	}

	public void removeModelListener(ModelListener listener)
	{
		listener_list.remove(ModelListener.class, listener);
	}

	public void fireDataTagAdded(ModelEvent e)
	{
		Object[] list = listener_list.getListenerList();
		for (int i = 0; i < list.length; i += 2)
		{
			if (list[i] == ModelListener.class)
			{
				((ModelListener) list[i+1]).dataTagAdded(e);
			}
		}
	}

	public void fireDataTagRemoved(ModelEvent e)
	{
		Object[] list = listener_list.getListenerList();
		for (int i = 0; i < list.length; i += 2)
		{
			if (list[i] == ModelListener.class)
			{
				((ModelListener) list[i+1]).dataTagRemoved(e);
			}
		}
	}

	public void fireDataTagModified(ModelEvent e)
	{
		Object[] list = listener_list.getListenerList();
		for (int i = 0; i < list.length; i += 2)
		{
			if (list[i] == ModelListener.class)
			{
				((ModelListener) list[i+1]).dataTagModified(e);
			}
		}
	}

	public void fireDataEventAdded(ModelEvent e)
	{
		Object[] list = listener_list.getListenerList();
		for (int i = 0; i < list.length; i += 2)
		{
			if (list[i] == ModelListener.class)
			{
				((ModelListener) list[i+1]).dataEventAdded(e);
			}
		}
	}

	public void fireDataEventRemoved(ModelEvent e)
	{
		Object[] list = listener_list.getListenerList();
		for (int i = 0; i < list.length; i += 2)
		{
			if (list[i] == ModelListener.class)
			{
				((ModelListener) list[i+1]).dataEventRemoved(e);
			}
		}
	}

	public void fireDataEventModified(ModelEvent e)
	{
		Object[] list = listener_list.getListenerList();
		for (int i = 0; i < list.length; i += 2)
		{
			if (list[i] == ModelListener.class)
			{
				((ModelListener) list[i+1]).dataEventModified(e);
			}
		}
	}

	public void fireDataEventSelected(ModelEvent e)
	{
		Object[] list = listener_list.getListenerList();
		for (int i = 0; i < list.length; i += 2)
		{
			if (list[i] == ModelListener.class)
			{
				((ModelListener) list[i+1]).dataEventSelected(e);
			}
		}
	}
}
