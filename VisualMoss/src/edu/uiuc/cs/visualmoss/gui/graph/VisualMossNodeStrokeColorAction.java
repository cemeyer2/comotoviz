package edu.uiuc.cs.visualmoss.gui.graph;

import java.awt.Color;
import java.util.Iterator;

import prefuse.action.assignment.ColorAction;
import prefuse.util.ColorLib;
import prefuse.visual.EdgeItem;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;

public class VisualMossNodeStrokeColorAction extends ColorAction {
	
	public VisualMossNodeStrokeColorAction(String group, String field) {
		super(group, field);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int getColor(VisualItem item) 
	{
		double maxEdgeWeight = 0;
		NodeItem node = (NodeItem)item;
		
		Iterator<EdgeItem> edgeIter = node.edges();
		
		while(edgeIter.hasNext())
		{
			EdgeItem edge = edgeIter.next();
			double weight = edge.getDouble("weight");
			if(weight > maxEdgeWeight && edge.getBoolean("isPartner") == false)
			{
				maxEdgeWeight = weight;
			}
		}
		
		double normalized = maxEdgeWeight*2.55;
		
		int r = (int) Math.round(normalized);
		int g = (int) (255 - Math.round(normalized));
		int b = 0;
		
		return ColorLib.rgb(r, g, b);
	}
}
