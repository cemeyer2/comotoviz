/*
 * University of Illinois/NCSA
 * Open Source License
 *
 * Copyright (c) 2011 University of Illinois at Urbana-Champaign.
 * All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal with the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimers.
 *
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimers in the documentation and/or other materials provided
 *       with the distribution.
 *
 *     * Neither the names of the CoMoTo Project team, the University of
 *       Illinois at Urbana-Champaign, nor the names of its contributors
 *       may be used to endorse or promote products derived from this
 *       Software without specific prior written permission.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS WITH THE SOFTWARE.
 */

package edu.illinois.comoto.viz.model.predicates;

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

import java.util.Iterator;

public class VisibilityPredicate implements Predicate {

    private double weight;
    private boolean showSingletons, showSolution, includePast, includePartners;

    private final String SOLUTION = "[solution]";

    private final NodeFillCurrentSemesterPredicate cursem = new NodeFillCurrentSemesterPredicate();
    private final NodeFillSolutionPredicate sol = new NodeFillSolutionPredicate();

    public VisibilityPredicate(double weight, boolean showSingletons, boolean showSolution, boolean includePast, boolean includePartners) {
        this.weight = weight;
        this.showSingletons = showSingletons;
        this.showSolution = showSolution;
        this.includePast = includePast;
        this.includePartners = includePartners;
    }

    public void setMinWeight(double w) {
        this.weight = w;
    }

    public void setIncludePartners(boolean value) {
        this.includePartners = value;
    }

    public void setShowSolution(boolean value) {
        this.showSolution = value;
    }

    public void setShowSingletons(boolean value) {
        this.showSingletons = value;
    }

    public void setIncludePast(boolean includePast) {
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

        VisualItem vitem = (VisualItem) t;

        if (vitem instanceof NodeItem) {
            return handleNode((NodeItem) vitem);
        } else if (vitem instanceof EdgeItem) {
            return handleEdge((EdgeItem) vitem);
        }

        return false;
    }

    private boolean handleNode(NodeItem node) {
        //check showSolution
        boolean isSolution = sol.getBoolean(node);
        if (isSolution == true && showSolution == false) {
            return false;
        }

        if (includePast == false) {
            if (cursem.getBoolean(node) == false && !isSolution) {
                return false;
            }
        }

        //check to see if it is a singleton
        if (showSingletons == false) {
            if (node.getDegree() == 0) {
                return false;
            }
            Iterator<Edge> iter = node.edges();
            boolean anyEdgeVisible = false;
            while (iter.hasNext()) {
                Edge edge = iter.next();

                boolean isVisible = handleEdge(edge);
                anyEdgeVisible = anyEdgeVisible || isVisible;

                if (isLinkedToSolution(node) && showSolution == false && node.getDegree() == 1) {
                    return true;
                }

            }
            if (!anyEdgeVisible)
                return false;
        }

        return true;
    }

    private boolean handleEdge(Edge edge) {
        if (showSolution == false) {
            if (sol.getBoolean(edge.getTargetNode()) ||
                    sol.getBoolean(edge.getSourceNode())) {
                return false;
            }
        }

        if (includePartners == false) {
            if (edge.getBoolean("isPartner") == true) {
                return false;
            }
        }

        if (includePast == false) {
            Node source = edge.getSourceNode();
            Node target = edge.getTargetNode();
            if (cursem.getBoolean(source) == false && sol.getBoolean(source) == false) {
                return false;
            }
            if (cursem.getBoolean(target) == false && sol.getBoolean(target) == false) {
                return false;
            }
        }

        double w = edge.getDouble("weight");
        if (w >= weight) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isLinkedToSolution(NodeItem node) {
        Iterator iter = node.edges();
        while (iter.hasNext()) {
            Edge edge = (Edge) iter.next();
            if (sol.getBoolean(edge.getTargetNode()) ||
                    sol.getBoolean(edge.getSourceNode())) {
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
