package edu.uiuc.cs.visualmoss.gui.graph.predicates;

import edu.uiuc.cs.visualmoss.VisualMossConstants;
import prefuse.data.Schema;
import prefuse.data.Tuple;
import prefuse.data.event.ExpressionListener;
import prefuse.data.expression.ExpressionVisitor;
import prefuse.data.expression.Predicate;

public class VisualMossNodeFillCurrentSemesterPredicate implements Predicate {

	public void addExpressionListener(ExpressionListener lstnr) {
		// TODO Auto-generated method stub

	}

	public Object get(Tuple t) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getBoolean(Tuple t) {
		String season = t.getString("season");
		String year = t.getString("year");
		if(season.equals(VisualMossConstants.CURRENT_SEASON) && year.equals(VisualMossConstants.CURRENT_YEAR))
		{
			return true;
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
