package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.Timer;
import model.DataEvent;
import model.DataTag;
import edu.utsa.layouts.Anchor;
import edu.utsa.layouts.AnchorLayout;
import edu.utsa.layouts.Margins;
import edu.utsa.xcomponent.XComponent;
import edu.utsa.xcomponent.XScrollPane;

public class Properties extends XComponent implements ViewListener
{
	Object pinneddata = null;

	JLabel header = new JLabel("Properties");
	JLabel arrow = new JLabel("\uE010")
	{
		@Override protected void paintComponent(Graphics g)
		{
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setColor(View.GREY_DARK);
			g2d.fillOval(0, getHeight()-getWidth(), getWidth(), getWidth());
			super.paintComponent(g);
		}
	};
	RichTextField code_label = new RichTextField("Code");
	RichTextField name_label = new RichTextField("Name");
	RichTextField description_label = new RichTextField("Description");
	RichTextField code = new RichTextField(0, 20);
	RichTextField name = new RichTextField(0, 20);
	RichTextField description = new RichTextField();
	Pin pin = new Pin();
	JLabel delete = new JLabel("Delete \uE0A4");

	JComponent c1 = new JComponent(){};
	JComponent c2 = new JComponent(){};
	JComponent c3 = new JComponent(){};
	JComponent body = new JComponent(){};
	
	private Timer timer = new Timer(20, new ActionListener()
	{
		@Override public void actionPerformed(ActionEvent e)
		{
			int step = (desired_height - current_height) / 5;
			body.setPreferredSize(new Dimension(0, body.getHeight() + step));
			current_height += step;
			if (Math.abs(desired_height - current_height) < 5)
			{
				if (desired_height == 0)
				{
					body.setPreferredSize(new Dimension(0, 0));
				}
				else
				{
					body.setPreferredSize(null);
				}
				timer.stop();
				Properties.this.exited();
			}
			revalidate();
		}
	});
	
	int desired_height = 0;
	int current_height = 0;
	
	private void toggleShowProperties()
	{
		String s = arrow.getText();
		if (s.equals("\uE010")) // up arrow
		{
			arrow.setText("\uE011");
			Dimension d = body.getPreferredSize();
			body.setPreferredSize(null);
			desired_height = body.getPreferredSize().height;
			body.setPreferredSize(d);
			current_height = 0;
			timer.start();
		}
		else
		{
			arrow.setText("\uE010");
			desired_height = 0;
			current_height = body.getPreferredSize().height;
			timer.start();
		}
	}
	
	public Properties()
	{
		header.setForeground(View.GREY_DARK);

		arrow.setForeground(Color.white);
		arrow.addMouseListener(new MouseAdapter()
		{
			@Override public void mouseClicked(MouseEvent e)
			{
				Properties.this.toggleShowProperties();
			}
			@Override public void mouseEntered(MouseEvent e)
			{
				arrow.setForeground(Color.black);
			}
			@Override public void mouseExited(MouseEvent e)
			{
				arrow.setForeground(Color.white);
			}
		});

		delete.addMouseListener(new MouseAdapter()
		{
			@Override public void mouseEntered(MouseEvent e)
			{
				delete.setForeground(Color.red);
			}
			@Override public void mouseExited(MouseEvent e)
			{
				delete.setForeground(Color.black);
			}
		});

		code_label = new RichTextField("Code");
		code_label.setForeground(View.GREY_DARK);
		code_label.setLock(true);
		code_label.setForeground(View.VERY_LIGHT_GREY);

		name_label.setForeground(View.GREY_DARK);
		name_label.setLock(true);
		name_label.setForeground(View.VERY_LIGHT_GREY);

		description_label.setForeground(View.GREY_DARK);
		description_label.setLock(true);
		description_label.setForeground(View.VERY_LIGHT_GREY);

		code.setBackground(new Color(138, 138, 138));
		code.setBorderColors(View.VERY_LIGHT_GREY, new Color(158, 158, 158));
		code.setForeground(Color.white);
		code.addKeyListener(new KeyAdapter()
		{

		});

		name.setBackground(new Color(138, 138, 138));
		name.setBorderColors(View.VERY_LIGHT_GREY, new Color(158, 158, 158));
		name.setForeground(Color.white);

		description.setLineWrap(true);
		description.setBackground(new Color(150, 150, 150));
		description.setBorderColors(View.VERY_LIGHT_GREY, new Color(170, 170, 170));
		description.setForeground(Color.white);

		c1.setLayout(new AnchorLayout());
		c1.add(code_label);
		c1.add(name_label, new Anchor(code_label, Anchor.BOTTOM));

		c2.setLayout(new AnchorLayout());
		c2.add(code);
		c2.add(name, new Anchor(code, Anchor.BOTTOM));

		c3.setLayout(new AnchorLayout());
		c3.add(description_label);

		body.setLayout(new AnchorLayout());
		body.add(delete, new Anchor(Anchor.BR, Anchor.NOSTRETCH));
		body.add(c1);
		body.add(c2, new Anchor(c1, Anchor.RIGHT, Anchor.NOSTRETCH, new Margins(0, 5, 0, 0)));
		body.add(c3, new Anchor(c2, Anchor.RIGHT, Anchor.NOSTRETCH, new Margins(0, 5, 0, 0)));
		body.add(new XScrollPane(description), new Anchor(c3, Anchor.RIGHT, Anchor.STRETCHALL));
		body.setPreferredSize(new Dimension(0, 0));

		setLayer(arrow, 1);
		setLayout(new AnchorLayout());
		add(arrow, new Anchor(Anchor.TC, Anchor.NOSTRETCH));
		add(header, new Anchor(Anchor.TL, Anchor.NOSTRETCH, new Margins(0, 5, 0, 0)));
		add(pin, new Anchor(header, Anchor.RIGHT, Anchor.NOSTRETCH, new Margins(0, 5, 0, 0)));
		add(body, new Anchor(header, Anchor.BOTTOM, Anchor.HORIZONTAL, new Margins(0, 0, 5, 5)));
	}
	
	public JLabel getHeaderLabel(){return header;}
	public Pin getPin(){return pin;}
	public JLabel getArrow(){return arrow;}
	public RichTextField getCodeTextField(){return code;}
	public RichTextField getNameTextField(){return name;}
	public RichTextField getDescriptionTextField(){return description;}
	public JLabel getDeleteButton(){return delete;}
	public Object getPinnedData(){return pinneddata;}

	@Override public void entered()
	{
		setVisible(true);
	}
	
	@Override public void exited()
	{
		setVisible(pinneddata != null || body.getPreferredSize().height > 0);
	}
	
	@Override public void draw(Graphics2D g)
	{
		g.setColor(new Color(0, 0, 0, 105));
		g.fillRect(0, arrow.getHeight() - arrow.getWidth() + arrow.getWidth()/2, getWidth(), getHeight());
	}

	@Override public void fontSizeChanged(ViewEvent e){}

	@Override public void pinned(ViewEvent e)
	{
		this.pinneddata = null;
		showProperties(e.getPinnedData());
		this.pinneddata = e.getPinnedData();

		pin.setPinned(pinneddata != null);
		if (isVisible() && pinneddata == null && body.getPreferredSize().height == 0)
		{
			setVisible(false);
		}
		if (!isVisible() && pinneddata != null)
		{
			setVisible(true);
			if (body.getPreferredSize().height == 0)
			{
				toggleShowProperties();
			}
		}
	}
	@Override public void dataUpdated(ViewEvent e){}

	public void showProperties(Object data)
	{
		if (pinneddata != null)
		{
			return;
		}

		if (data instanceof DataEvent)
		{
			DataEvent eventdata = (DataEvent) data;
			code.setText(eventdata.getCode());
			name.setText(eventdata.getLabel());
			description.setText(eventdata.getDescription());

		}
		else if (data instanceof DataTag)
		{
			DataTag tagdata = (DataTag) data;
			code.setText("");
			name.setText(tagdata.getLabel());
			description.setText(tagdata.getDescription());
		}
		else
		{
			code.setText("");
			name.setText("");
			description.setText("");
		}
	}
}
