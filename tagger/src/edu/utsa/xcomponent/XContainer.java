package edu.utsa.xcomponent;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import edu.utsa.layouts.Anchor;
import edu.utsa.layouts.AnchorLayout;

public class XContainer extends JLayeredPane
{
	private ArrayList<XComponent> new_list = new ArrayList<XComponent>();

	private XContentPane content_pane;
	private XDragPane drag_pane;
	private XComponent potential_drag;

	public XContainer()
	{
		setLayout(new AnchorLayout());
		content_pane = new XContentPane();
		drag_pane = new XDragPane();
		potential_drag = null;
		setLayer(content_pane, 0);
		setLayer(drag_pane, 1);
		add(content_pane, new Anchor(Anchor.TL, Anchor.STRETCHALL));
		add(drag_pane, new Anchor(1, Anchor.TL, Anchor.STRETCHALL));
		
		EventQueue.invokeLater(new Runnable()
		{
			@Override public void run()
			{
				EventQueue event_queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
				event_queue.push(new EventQueue()
				{
					@Override protected void dispatchEvent(AWTEvent e)
					{
						try
						{
							if (e instanceof MouseEvent)
							{
								MouseEvent mouse_event = (MouseEvent) e;
								mouse_event = SwingUtilities.convertMouseEvent(mouse_event.getComponent(), mouse_event, content_pane);
								handleMouseEvent(mouse_event);
							}
							if (e instanceof KeyEvent)
							{
								KeyEvent key_event = (KeyEvent) e;
								handleKeyEvent(key_event);
							}
							super.dispatchEvent(e);
						}
						catch (Throwable t)
						{
							t.printStackTrace();
						}
					}
				});
			}
		});
	}

	public XContentPane getContentPane()
	{
		return content_pane;
	}

	private void handleKeyEvent(KeyEvent e) {}

	private void handleMouseEvent(MouseEvent e)
	{
		Component deepest = SwingUtilities.getDeepestComponentAt(content_pane, e.getX(), e.getY());
		XComponent deepestx =  getDeepestXComponentAt(deepest); 

		if (e.getID() == MouseEvent.MOUSE_CLICKED)
		{
			for (XComponent xc : new_list)
			{
				if (xc == deepest) xc.clicked();
				xc.clicked(deepest);
			}
		}

		if (e.getID() == MouseEvent.MOUSE_PRESSED)
		{
			potential_drag = getDeepestDraggableXComponentAt(deepest);
		}
		if (e.getID() == MouseEvent.MOUSE_DRAGGED)
		{
			if (potential_drag != null)
			{
				drag_pane.setDragged(potential_drag);
				potential_drag = null;
			}
			else
			{
				if (deepestx != drag_pane.getDragged())
				{
					drag_pane.setPendingDrop(getDeepestDroppableXComponentAt(deepest));
				}
				else
				{
					drag_pane.setPendingDrop(null);
				}
				drag_pane.repaint();
			}
		}
		if (e.getID() == MouseEvent.MOUSE_RELEASED)
		{
			if (drag_pane.getDragged() != null)
			{
				drag_pane.release();
				content_pane.repaint();
			}
		}

		if (e.getID() == MouseEvent.MOUSE_MOVED || 
			e.getID() == MouseEvent.MOUSE_EXITED ||
			e.getID() == MouseEvent.MOUSE_RELEASED)
		{
			ArrayList<XComponent> old_list = new ArrayList<XComponent>(new_list);
			new_list = getNewList(deepestx);

			Queue<XComponent> exited_stack = new LinkedList<XComponent>();
			Stack<XComponent> entered_queue = new Stack<XComponent>();

			for (XComponent xc : old_list)
			{
				if (!new_list.contains(xc))
				{
					xc.setMouseover(false);
					xc.setMousedirectlyover(false);
					exited_stack.offer(xc);
					xc.repaint();
				}
			}

			for (XComponent xc : new_list)
			{
				xc.setMousedirectlyover(xc == deepest);
				if (!old_list.contains(xc))
				{
					xc.setMouseover(true);
					entered_queue.push(xc);
				}
				xc.repaint();
			}

			while (!exited_stack.isEmpty()) exited_stack.poll().exited();
			while (!entered_queue.isEmpty()) entered_queue.pop().entered();
		}
	}

	private XComponent getDeepestDraggableXComponentAt(Component c)
	{
		while (c != null && !(c instanceof XComponent && ((XComponent) c).isDraggable()))
		{
			c = c.getParent();
		}
		return (XComponent) c;
	}

	private XComponent getDeepestDroppableXComponentAt(Component c)
	{
		while (c != null && !(c instanceof XComponent && ((XComponent) c).isDroppable()))
		{
			c = c.getParent();
		}
		return (XComponent) c;
	}

	private XComponent getDeepestXComponentAt(Component c)
	{
		while (c != null && !(c instanceof XComponent))
		{
			c = c.getParent();
		}
		return (XComponent) c;
	}

	private ArrayList<XComponent> getNewList(Component c)
	{
		ArrayList<XComponent> list = new ArrayList<XComponent>();
		while (c != null)
		{
			if (c instanceof XComponent)
			{
				list.add((XComponent) c);
			}
			c = c.getParent();
		}
		return list;
	}

}
