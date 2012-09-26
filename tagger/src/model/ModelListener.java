package model;

import java.util.EventListener;

public interface ModelListener extends EventListener
{
	public void dataTagAdded(ModelEvent e);
	public void dataTagRemoved(ModelEvent e);
	public void dataTagModified(ModelEvent e);
	
	public void dataEventAdded(ModelEvent e);
	public void dataEventRemoved(ModelEvent e);
	public void dataEventModified(ModelEvent e);
	public void dataEventSelected(ModelEvent e);
}
