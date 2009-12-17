package edu.uiuc.cs.visualmoss.gui.graph;

import java.awt.Color;

import edu.uiuc.cs.visualmoss.gui.graph.predicates.VisualMossNodeFillCurrentSemesterPredicate;
import edu.uiuc.cs.visualmoss.gui.graph.predicates.VisualMossNodeFillSolutionPredicate;

import prefuse.action.assignment.ColorAction;
import prefuse.data.expression.Predicate;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

public class VisualMossNodeFillColorAction extends ColorAction {

	private VisualMossNodeFillCurrentSemesterPredicate cursem = new VisualMossNodeFillCurrentSemesterPredicate();
	private VisualMossNodeFillSolutionPredicate sol = new VisualMossNodeFillSolutionPredicate();
	
	public VisualMossNodeFillColorAction(String group, String field) {
		super(group, field);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int getColor(VisualItem item) 
	{
		if(cursem.getBoolean(item))
		{
			return ColorLib.color(Color.WHITE);
		}
		if(sol.getBoolean(item))
		{
			return ColorLib.color(Color.red);
		}
		return ColorLib.gray(200);
	}

}
