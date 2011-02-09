package edu.uiuc.cs.visualmoss.gui.graph;

import edu.uiuc.cs.visualmoss.gui.graph.predicates.VisualMossEdgeIsPartnerPredicate;
import prefuse.action.assignment.ColorAction;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

import java.awt.*;

public class VisualMossEdgeStrokeColorAction extends ColorAction {

	private VisualMossEdgeIsPartnerPredicate isPartner = new VisualMossEdgeIsPartnerPredicate();
	
	public VisualMossEdgeStrokeColorAction(String group, String field) {
		super(group, field);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int getColor(VisualItem item) 
	{
		if(isPartner.getBoolean(item))
		{
			return ColorLib.color(Color.GREEN);
		}
		return ColorLib.gray(0); //black
	}

}
