package edu.utsa.tagger;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

public final class ShellCLI
{
	private static DataWrappersEvent current_event = null;
	private static Collection<DataWrappersEvent> events = new ArrayList<DataWrappersEvent>();
	private static Collection<DataWrappersSelectedTag> selectedtags = new ArrayList<DataWrappersSelectedTag>();
	private static DataWrappersHED hed = null;

	private ShellCLI() {}

	static String getReturnValue()
	{
		String s = "";
		for (DataWrappersEvent e : events)
		{
			s += e.getLabel() + ";";
			for (DataWrappersSelectedTag st : selectedtags)
			{
				if (st.getItemUuid().equals(e.getUuid()))
				{
					s += getTag(st.getTagUuid()).getPath() + ";";
				}
			}
			s += "\n";
		}
		return s;
	}
	
	static void loadTags(String xml)
	{
		hed = null;
		try
		{
			StringReader xml_stringreader = new StringReader(xml);
			JAXBContext jaxbContext = JAXBContext.newInstance(DataWrappersHED.class);
			hed = (DataWrappersHED) jaxbContext.createUnmarshaller().unmarshal(xml_stringreader);
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
		}
		
		for (DataWrappersTag tag : hed.getTags())
		{
			setTagPath(tag, "");
		}
	}
	
	private static void setTagPath(DataWrappersTag tag, String parent_path)
	{
		String path = parent_path + "/" + tag.getLabel();
		tag.setPath(path);
		for (DataWrappersTag child_tag : tag.getChildTags())
		{
			setTagPath(child_tag, path);
		}
	}

	static void loadEvents(String events_string)
	{
		String[] s = events_string.split("\\n");
		for (String event : s)
		{
			String[] event_data = event.split(";");
			if (event_data.length == 3)
			{
				events.add(new DataWrappersEvent(event_data[0], event_data[1], event_data[2]));
			}
			else
			{
				System.out.println("event_data.split != 3");
			}
		}
	}

	static boolean isTagCurrentlySelected(DataWrappersTag tag) {
		boolean result = false;
		if (current_event != null) {
			DataWrappersSelectedTag st = new DataWrappersSelectedTag(current_event.getUuid(), tag.getUuid());
			result = selectedtags.contains(st);
		}
		return result;
	}

	static boolean selectEvent(DataWrappersEvent event) {
		boolean result = false;
		if (events.contains(event)) {
			current_event = event;
			result = true;
		}
		else {
			ErrorStack.push(240, "Could not find event.");
		}
		return result;
	}

	static boolean toggleTag(DataWrappersTag tag) {
		boolean result = false;
		if (current_event != null) {
			DataWrappersSelectedTag st = new DataWrappersSelectedTag(current_event.getUuid(), tag.getUuid());
			if (selectedtags.contains(st)) {
				selectedtags.remove(st);
				result = true;
			}
			else {
				selectedtags.add(st);
				result = true;
			}
		}
		else {
			ErrorStack.push(230, "Toggle tag failed.  Current event is null.");
		}
		return result;
	}

	static boolean removeEvent(UUID uuid)
	{
		boolean result = false;
		try {
			Iterator<DataWrappersSelectedTag> iter1 = selectedtags.iterator();
			while (iter1.hasNext()) {
				if (iter1.next().getItemUuid().equals(uuid)) {
					iter1.remove();
				}
			}
			Iterator<DataWrappersEvent> iter2 = events.iterator();
			while (iter2.hasNext()) {
				if (iter2.next().getUuid().equals(uuid)) {
					iter2.remove();
				}
			}
			result = true;
		}
		catch (Exception e) {
			ErrorStack.push(250, "Error deleting event: " + e.toString());
		}
		return result;
	}

	static Collection<DataWrappersEvent> getEvents() {
		return events;
	}

	static DataWrappersHED getHED()
	{
		return hed;
	}

	static Collection<DataWrappersSelectedTag> getSelectedTags() {
		return selectedtags;
	}

	static DataWrappersEvent insertEvent(String label) {
		DataWrappersEvent result = null;
		DataWrappersEvent event = new DataWrappersEvent("", label, "");
		if (!events.contains(event)) {
			events.add(event);
			result = event;
		}
		else {
			ErrorStack.push(221, "Cannot create event.  Event already exists.");
		}
		return result;
	}

	public static DataWrappersEvent getCurrentEvent() {
		return current_event;
	}

	public static DataWrappersTag getTag(UUID uuid) {
		DataWrappersTag result;
		for (DataWrappersTag tag : hed.getTags()) {
			result = getTag(tag, uuid);
			if (result != null)
			{
				return result;
			}
		}
		return null;
	}

	private static DataWrappersTag getTag(DataWrappersTag tag, UUID uuid)
	{
		if (tag.getUuid().equals(uuid))
		{
			return tag;
		}
		else
		{
			for (DataWrappersTag t : tag.getChildTags())
			{
				DataWrappersTag child_tag = getTag(t, uuid);
				if (child_tag != null)
				{
					return child_tag;
				}
			}
		}
		return null;
	}
}
