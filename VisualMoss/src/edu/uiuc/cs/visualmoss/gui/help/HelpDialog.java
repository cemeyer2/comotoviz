package edu.uiuc.cs.visualmoss.gui.help;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.uiuc.cs.visualmoss.VisualMossConstants;

public class HelpDialog extends JDialog {

	private final int DIALOG_WIDTH = 575;
	private final int DIALOG_HEIGHT = 300;
	
	public HelpDialog()
	{
		super();
		this.setModal(true);
		JPanel panel = new JPanel(new BorderLayout());
		JLabel titleLabel = new JLabel(VisualMossConstants.HELP_TITLE);
		JLabel helpLabel = new JLabel(VisualMossConstants.HELP_TEXT);
		
		titleLabel.setFont(VisualMossConstants.HELP_TITLE_FONT);
		helpLabel.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
		
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		helpLabel.setHorizontalAlignment(JLabel.CENTER);
		
		titleLabel.setHorizontalTextPosition(JLabel.CENTER);
		helpLabel.setHorizontalTextPosition(JLabel.CENTER);
		
//	    GridBagConstraints c = new GridBagConstraints();
//		
//	    c.weightx = 1.0;
//	    c.weighty = 0.0;
//	    c.gridx = 0;
//	    c.gridy = 0;
//	    c.anchor = GridBagConstraints.FIRST_LINE_START;
//	    c.gridwidth = 1;
//	    c.gridheight = 2;
		panel.add(titleLabel,BorderLayout.NORTH);
		
//		c.gridy = 3;
//		c.gridheight = 3;
		panel.add(helpLabel, BorderLayout.CENTER);
		
		panel.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
		
		this.setContentPane(panel);
		this.pack();
		this.setTitle("Help");
		this.setVisible(true);
	}
}
