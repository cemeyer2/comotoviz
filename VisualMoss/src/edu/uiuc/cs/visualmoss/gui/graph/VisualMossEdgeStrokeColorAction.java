package edu.uiuc.cs.visualmoss.gui.graph;

import java.awt.Color;

import edu.uiuc.cs.visualmoss.gui.graph.predicates.VisualMossEdgeIsPartnerPredicate;
import edu.uiuc.cs.visualmoss.gui.graph.predicates.VisualMossNodeFillCurrentSemesterPredicate;
import edu.uiuc.cs.visualmoss.gui.graph.predicates.VisualMossNodeFillSolutionPredicate;

import prefuse.action.assignment.ColorAction;
import prefuse.data.expression.Predicate;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

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
