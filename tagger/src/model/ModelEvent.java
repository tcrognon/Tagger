package model;

import java.util.EventObject;

public class ModelEvent extends EventObject
{
	private DataEvent event = null;
	private DataTag tag = null;
	
	public ModelEvent(Object source, DataEvent event)
	{
		super(source);
		this.event = event;
	}
	
	public ModelEvent(Object source, DataTag tag)
	{
		super(source);
		this.tag = tag;
	}
	
	public DataEvent getDataEvent()
	{
		return event;
	}
	
	public DataTag getDataTag()
	{
		return tag;
	}
}
