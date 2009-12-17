package edu.uiuc.cs.visualmoss.gui.graph;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.uiuc.cs.visualmoss.VisualMossConstants;
import edu.uiuc.cs.visualmoss.graph.VisualMossGraph;

public class VisualMossGraphDisplayContainer extends JPanel
{
	VisualMossGraphDisplay graphDisplay;
	private final int STATUS_AREA_HEIGHT = 30;
	JLabel statusLabel;
	int width, height;
	
	public VisualMossGraphDisplayContainer(VisualMossGraph graph, int width, int height)
	{
		this.width = width;
		this.height = height;
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(width, height));
		graphDisplay = new VisualMossGraphDisplay(graph, this);
		this.statusLabel = new JLabel();
		this.statusLabel.setFont(VisualMossConstants.STATUS_LABEL_FONT);
		add(graphDisplay.getDisplay(width, height-STATUS_AREA_HEIGHT), BorderLayout.CENTER);
		add(statusLabel, BorderLayout.SOUTH);
	}
	
	public VisualMossGraphDisplayContainer(int width, int height)
	{
		this.width = width;
		this.height = height;
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(width, height));
		graphDisplay = new VisualMossGraphDisplay(this);
		this.statusLabel = new JLabel();
		this.statusLabel.setFont(VisualMossConstants.STATUS_LABEL_FONT);
		add(graphDisplay.getDisplay(width, height-STATUS_AREA_HEIGHT), BorderLayout.CENTER);
		add(statusLabel, BorderLayout.SOUTH);
	}
	
	public void changeGraph(VisualMossGraph graph)
	{
		graphDisplay.setGraph(graph);
	}
	
	public void setStatus(String status)
	{
		statusLabel.setText(status);
	}
	
	public void clearStatus()
	{
		setStatus("");
	}
	
	public final VisualMossGraphDisplay getVisualMossGraphDisplay()
	{
		return graphDisplay;
	}
}
