package edu.uiuc.cs.visualmoss.gui.utility;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.TitledBorder;

import edu.uiuc.cs.visualmoss.VisualMossConstants;

public class LoadingProgressDialog extends JDialog
{
	private final int DIALOG_WIDTH = 400;
	private final int DIALOG_HEIGHT = 50;
	private JProgressBar bar;
	private String title, label;
	
	public LoadingProgressDialog(JFrame owner, String title, String label)
	{
		super(owner, false); //make this not modal
		bar = new JProgressBar();
		this.title = title;
		this.label = label;
//		init();
	}
	
	public LoadingProgressDialog(String title, String label)
	{
		super();
		this.setModal(false);
		bar = new JProgressBar();
		this.title = title;
		this.label = label;
//		init();
	}
	
	public void init()
	{
		this.setTitle(title);
		JPanel panel = new JPanel(new GridLayout(1,1));
		panel.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
		
		TitledBorder b = BorderFactory.createTitledBorder(label);
		b.setTitleFont(VisualMossConstants.COMPONENT_LABEL_FONT);
		bar.setBorder(b);
		bar.setIndeterminate(true);
		bar.setStringPainted(true);
		panel.add(bar);
		this.setContentPane(panel);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
	
	public void setIndeterminate(boolean value)
	{
		bar.setIndeterminate(value);
	}
	
	public void setTaskLength(int length)
	{
		bar.setMaximum(length);
		bar.setMinimum(0);
		bar.setValue(0);
	}
	
	public void setValue(int value)
	{
		bar.setValue(value);
		int pct = (int)(((double)bar.getValue()/(double)bar.getMaximum())*100);
		bar.setString(pct+"%");
	}
}
