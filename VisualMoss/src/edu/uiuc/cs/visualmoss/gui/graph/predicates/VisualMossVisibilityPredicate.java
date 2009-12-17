package edu.uiuc.cs.visualmoss.gui.graph.predicates;

import java.util.Iterator;

import edu.uiuc.cs.visualmoss.VisualMossConstants;

import prefuse.data.Edge;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Tuple;
import prefuse.data.event.ExpressionListener;
import prefuse.data.expression.ExpressionVisitor;
import prefuse.data.expression.Predicate;
import prefuse.visual.EdgeItem;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;

public class VisualMossVisibilityPredicate implements Predicate {

	private double weight;
	private boolean showSingletons, showSolution, includePast, includePartners;
	
	private final String SOLUTION = "[solution]";
	
	private final VisualMossNodeFillCurrentSemesterPredicate cursem = new VisualMossNodeFillCurrentSemesterPredicate();
	private final VisualMossNodeFillSolutionPredicate sol = new VisualMossNodeFillSolutionPredicate();
	
	public VisualMossVisibilityPredicate(double weight, boolean showSingletons, boolean showSolution, boolean includePast, boolean includePartners)
	{
		this.weight = weight;
		this.showSingletons = showSingletons;
		this.showSolution = showSolution;
		this.includePast = includePast;
		this.includePartners = includePartners;
	}
	
	public void setMinWeight(double w)
	{
		this.weight = w;
	}
	
	public void setIncludePartners(boolean value)
	{
		this.includePartners = value;
	}
	
	public void setShowSolution(boolean value)
	{
		this.showSolution = value;
	}
	
	public void setShowSingletons(boolean value)
	{
		this.showSingletons = value;
	}
	
	public void setIncludePast(boolean includePast)
	{
		this.includePast = includePast;
	}
	
	
	public void addExpressionListener(ExpressionListener lstnr) {
		// TODO Auto-generated method stub
		
	}

	public Object get(Tuple t) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getBoolean(Tuple t) {
		
		VisualItem vitem = (VisualItem)t;
		
		if(vitem instanceof NodeItem)
		{
			return handleNode((NodeItem)vitem);
		}
		else if(vitem instanceof EdgeItem)
		{
			return handleEdge((EdgeItem)vitem);
		}
		
		return false;
	}
	
	private boolean handleNode(NodeItem node)
	{
		//check showSolution
		boolean isSolution = sol.getBoolean(node);
		if(isSolution == true && showSolution == false)
		{
			return false;
		}
		
		if(includePast == false)
		{
			if(cursem.getBoolean(node) == false && !isSolution)
			{
				return false;
			}
		}
		
		//check to see if it is a singleton
		if(showSingletons == false)
		{
			if(node.getDegree() == 0)
			{
				return false;
			}
			Iterator<Edge> iter = node.edges();
			boolean anyEdgeVisible = false;
			while(iter.hasNext())
			{
				Edge edge = iter.next();
				
				boolean isVisible = handleEdge(edge);				
				anyEdgeVisible = anyEdgeVisible || isVisible;
				
				if(isLinkedToSolution(node) && showSolution == false && node.getDegree() == 1)
				{
					return true;
				}
				
			}
			if(!anyEdgeVisible)
				return false;
		}
		
		return true;
	}
	
	private boolean handleEdge(Edge edge)
	{
		if(showSolution == false)
		{
			if(		sol.getBoolean(edge.getTargetNode()) || 
					sol.getBoolean(edge.getSourceNode()))
			{
				return false;
			}
		}
		
		if(includePartners == false)
		{
			if(edge.getBoolean("isPartner") == true)
			{
				return false;
			}
		}
		
		if(includePast == false)
		{
			Node source = edge.getSourceNode();
			Node target = edge.getTargetNode();
			if(cursem.getBoolean(source) == false && sol.getBoolean(source) == false)
			{
				return false;
			}
			if(cursem.getBoolean(target) == false && sol.getBoolean(target) == false)
			{
				return false;
			}
		}
		
		double w  = edge.getDouble("weight");
		if(w >= weight)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private boolean isLinkedToSolution(NodeItem node)
	{
		Iterator iter = node.edges();
		while(iter.hasNext())
		{
			Edge edge = (Edge)iter.next();
			if(		sol.getBoolean(edge.getTargetNode()) || 
					sol.getBoolean(edge.getSourceNode()))
			{
				return true;
			}
		}
		return false;
	}

	public double getDouble(Tuple t) {
		// TODO Auto-generated method stub
		return 0;
	}

	public float getFloat(Tuple t) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getInt(Tuple t) {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getLong(Tuple t) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Class getType(Schema s) {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeExpressionListener(ExpressionListener lstnr) {
		// TODO Auto-generated method stub
		
	}

	public void visit(ExpressionVisitor v) {
		// TODO Auto-generated method stub
		
	}

}
