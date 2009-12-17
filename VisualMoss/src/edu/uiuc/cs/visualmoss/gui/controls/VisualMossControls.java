package edu.uiuc.cs.visualmoss.gui.controls;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.uiuc.cs.visualmoss.VisualMossConstants;
import edu.uiuc.cs.visualmoss.gui.diff.WebpageDialog;
import edu.uiuc.cs.visualmoss.gui.graph.VisualMossGraphDisplay;

public class VisualMossControls extends JPanel{
	private static final long serialVersionUID = 1L;	

	public void addVisualMossControls(final VisualMossGraphDisplay graphDisplay){
		this.setLayout(new GridBagLayout());
	    GridBagConstraints c = new GridBagConstraints();
	    final JCheckBox pastStudentButton, singletonsButton, partnersButton, solutionButton, anonymousButton;
		final JSlider threshholdSlider, zoomSlider;
		final JButton textReportButton;
		
		//Threshold Slider
		threshholdSlider = new JSlider();
	    threshholdSlider.setValue(0);//show all edges by default on load
		threshholdSlider.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
		final TitledBorder b0 = BorderFactory.createTitledBorder("Minimum Edge Weight: "
	    		+ threshholdSlider.getValue());
		b0.setTitleFont(VisualMossConstants.COMPONENT_LABEL_FONT);
	    threshholdSlider.setBorder(b0);
	    threshholdSlider.setMajorTickSpacing(20);
	    threshholdSlider.setMinorTickSpacing(10);
	    threshholdSlider.setPaintTicks(true);
	    threshholdSlider.setPaintLabels(true);
	    threshholdSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				//b0.setTitle("Minimum Edge Weight: "+ threshholdSlider.getValue());
	            TitledBorder bb = BorderFactory.createTitledBorder("Minimum Edge Weight: "+ threshholdSlider.getValue());
	            bb.setTitleFont(VisualMossConstants.COMPONENT_LABEL_FONT);
	            threshholdSlider.setBorder(bb);
				graphDisplay.setMinimumEdgeWeightToDisplay(threshholdSlider.getValue());
			}
	    });
	    c.weightx = 0.0;
	    c.weighty = 0.0;
	    c.gridx = 0;
	    c.gridy = 0;
	    c.anchor = GridBagConstraints.FIRST_LINE_START;
	    this.add(threshholdSlider, c);
	    
	    //Zoom Slider
	    zoomSlider = new JSlider(0, 100);
	    zoomSlider.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
	    final TitledBorder b1 = BorderFactory.createTitledBorder("Zoom: "
	    		+ zoomSlider.getValue() + "%");
	    b1.setTitleFont(VisualMossConstants.COMPONENT_LABEL_FONT);
	    zoomSlider.setBorder(b1);
	    zoomSlider.setMajorTickSpacing(25);
	    zoomSlider.setMinorTickSpacing(5);
	    zoomSlider.setPaintTicks(true);
	    zoomSlider.setPaintLabels(true);
	    zoomSlider.addChangeListener(new ChangeListener(){
	    	double oldVal = 50;
	    	double newVal;
			public void stateChanged(ChangeEvent e) {
				TitledBorder bb = BorderFactory.createTitledBorder("Zoom: "+ zoomSlider.getValue() + "%");
				bb.setTitleFont(VisualMossConstants.COMPONENT_LABEL_FONT);
				zoomSlider.setBorder(bb);
				if((newVal = zoomSlider.getValue()) == 0.0)
					newVal = 0.001;
				graphDisplay.setZoom(newVal / oldVal);
				oldVal = newVal;
			}
	    });
	    c.gridy = 1;
	    this.add(zoomSlider, c);
	    
		pastStudentButton = new JCheckBox("Include past students");
		pastStudentButton.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
	    pastStudentButton.setSelected(false);
	    pastStudentButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				graphDisplay.setIncludePast(pastStudentButton.isSelected());
			}
		});
	    c.gridy = 2;
	    this.add(pastStudentButton, c);
	    
	    singletonsButton = new JCheckBox("Include Singletons");
	    singletonsButton.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
	    singletonsButton.setSelected(true); 
	    singletonsButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (singletonsButton.isSelected())
					graphDisplay.setShowSingletons(true);
				else 
					graphDisplay.setShowSingletons(false);				
			}
	    });
	    c.gridy = 3;
	    this.add(singletonsButton, c);
	    
	    partnersButton = new JCheckBox("Include Partner Edges");
	    partnersButton.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
	    partnersButton.setSelected(false);
	    partnersButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				graphDisplay.setIncludePartners(partnersButton.isSelected());
			}
		});
	    c.gridy = 4;
	    this.add(partnersButton, c);
	    
	    solutionButton = new JCheckBox("Include Solution");
	    solutionButton.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
	    solutionButton.setSelected(true);
	    solutionButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (solutionButton.isSelected())
					graphDisplay.setShowSolution(true);
				else 
					graphDisplay.setShowSolution(false);
			}
	    });
	    c.gridy = 5;
	    this.add(solutionButton, c);
	    
	    anonymousButton = new JCheckBox("Anonymous Graph");
	    anonymousButton.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
	    anonymousButton.setSelected(true);
	    graphDisplay.setAnonymous(true);
	    anonymousButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (anonymousButton.isSelected())
					graphDisplay.setAnonymous(true);
				else 
					graphDisplay.setAnonymous(false);
			}
	    });
	    c.gridy = 6;
	    this.add(anonymousButton, c);

	    textReportButton = new JButton("Launch Text Report");
	    textReportButton.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
	    c.weightx = 0.1;
	    c.weighty = 0.1;
	    c.ipady = 32;
	    c.gridy = 7;
	    textReportButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					new WebpageDialog(graphDisplay.getReportURL(), true);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	    this.add(textReportButton, c); 
	    
	}
}
