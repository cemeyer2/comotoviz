package edu.uiuc.cs.visualmoss.gui.help;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.uiuc.cs.visualmoss.VisualMossConstants;

public class AboutDialog extends JDialog 
{
	private final int DIALOG_WIDTH = 500;
	private final int DIALOG_HEIGHT = 300;
	
	public AboutDialog()
	{
		super();
		this.setModal(true);
		JPanel panel = new JPanel(new GridBagLayout());
		JLabel titleLabel = new JLabel(VisualMossConstants.ABOUT_TITLE);
		JLabel authorsLabel = new JLabel(VisualMossConstants.ABOUT_AUTHORS);
		JLabel copyrightLabel = new JLabel(VisualMossConstants.ABOUT_COPYRIGHT);
		
		titleLabel.setFont(VisualMossConstants.HELP_TITLE_FONT);
		authorsLabel.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
		copyrightLabel.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
		
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		authorsLabel.setHorizontalAlignment(JLabel.CENTER);
		copyrightLabel.setHorizontalAlignment(JLabel.CENTER);
		
		titleLabel.setHorizontalTextPosition(JLabel.CENTER);
		authorsLabel.setHorizontalTextPosition(JLabel.CENTER);
		copyrightLabel.setHorizontalTextPosition(JLabel.CENTER);
		
	    GridBagConstraints c = new GridBagConstraints();
		
	    c.weightx = 0.0;
	    c.weighty = 0.0;
	    c.gridx = 0;
	    c.gridy = 0;
	    c.anchor = GridBagConstraints.FIRST_LINE_START;
	    c.gridwidth = 1;
	    c.gridheight = 2;
		panel.add(titleLabel,c);
		
		c.gridy = 3;
		c.gridheight = 3;
		panel.add(authorsLabel, c);
		
		c.gridy = 7;
		c.gridheight = 4;
		panel.add(copyrightLabel,c);
		
		panel.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
		
		this.setContentPane(panel);
		this.pack();
		this.setTitle("About");
		this.setVisible(true);
	}
}
