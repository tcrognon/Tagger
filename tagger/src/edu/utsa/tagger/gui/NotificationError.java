package edu.utsa.tagger.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JLabel;
import javax.swing.Timer;
import edu.utsa.layouts.Anchor;
import edu.utsa.xcomponent.XComponent;

public class NotificationError extends XComponent
{
	private JLabel message;
	private Insets insets;
	private Timer timer;
	private int ticks;
	private float alpha;

	private Queue<String> message_queue;

	public NotificationError()
	{
		setVisible(false);
		alpha = 0.0f;
		message_queue = new LinkedList<String>();
		message = new JLabel();
		message.setForeground(Color.white);
		insets = new Insets(10, 20, 10, 20);
		add(message, new Anchor(null, 0, Anchor.CC, false, false, 0, 0, 0, 0));

		timer = new Timer(50, new ActionListener() {

			@Override public void actionPerformed(ActionEvent e)
			{
				if (ticks > 100)
				{
					if (!message_queue.isEmpty()) {
						message.setText(message_queue.poll());
						alpha = 1.0f;
						ticks = 0;
					}
					else
					{
						timer.stop();
						setVisible(false);
					}
				}
				else if (ticks > 50)
				{
					alpha -= 0.02f;
				}
				repaint();
				ticks++;
			}});
	}

	@Override protected void draw(Graphics2D g)
	{
		Composite c = g.getComposite();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		
		super.draw(g);
		RoundRectangle2D r = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10);
		g.setColor(Color.red);
		g.fill(r);
		
		g.setComposite(c);
	}

	@Override public Dimension getPreferredSize()
	{
		return new Dimension(
			message.getPreferredSize().width + insets.left + insets.right,
			message.getPreferredSize().height + insets.top + insets.bottom);
	}

	public void showMessage(String s)
	{
		message_queue.offer(s);
		if (!timer.isRunning())
		{
			message.setText(message_queue.poll());
			alpha = 1.0f;
			setVisible(true);
			ticks = 0;
			timer.start();
		}
	}

}
