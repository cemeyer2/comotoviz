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

package edu.illinois.comoto.viz.view.graph.actions;

import edu.illinois.comoto.viz.view.BackendConstants;
import edu.illinois.comoto.viz.view.FrontendConstants;
import prefuse.action.assignment.ColorAction;
import prefuse.util.ColorLib;
import prefuse.visual.EdgeItem;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;

import java.util.Iterator;

public class NodeColorAction extends ColorAction {

    public NodeColorAction(String group, String field) {
        super(group, field);
        // TODO Auto-generated constructor stub
    }


    /**
     * sets the color for node borders in the graph. The algorithm works as follows:<br/>
     * <ul>
     * <li>the border of the node is colored on a gradient scale based on the edge that
     * node has that is of maximum weight. It is colored between pure green for a max weight edge with 0 weight
     * and pure red for a max weight edge with a weight of 100</li>
     * </ul>
     *
     * @param item the node that we are assigning a color to
     * @return a color packed in a 32 bit integer, as RGBA (8 bits each)
     */
    @Override
    public int getColor(VisualItem item) {
        double maxEdgeWeight = 0;
        NodeItem node = (NodeItem) item;

        Iterator<EdgeItem> edgeIter = node.edges();

        while (edgeIter.hasNext()) {
            EdgeItem edge = edgeIter.next();
            double weight = edge.getDouble(BackendConstants.WEIGHT);
            if (weight > maxEdgeWeight && !edge.getBoolean(BackendConstants.IS_PARTNER)) {
                maxEdgeWeight = weight;
            }
        }

        double pct = maxEdgeWeight / 100d;
        return ColorLib.interp(FrontendConstants.MIN_WEIGHT_STROKE_COLOR_INT, FrontendConstants.MAX_WEIGHT_STROKE_COLOR_INT, pct);
    }
}
