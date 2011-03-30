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
import prefuse.action.assignment.SizeAction;
import prefuse.visual.EdgeItem;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;

import java.util.Iterator;

/**
 * Author:  Charlie Meyer <cemeyer2@illinois.edu>
 * Date:    3/30/11
 * Time:    2:38 PM
 * Package: edu.illinois.comoto.viz.view.graph.actions
 * Created by IntelliJ IDEA.
 */
public class NodeSizeAction extends SizeAction {

    public NodeSizeAction() {
        super();
    }

    public NodeSizeAction(String group) {
        super(group);
    }

    public NodeSizeAction(String group, double defaultSize) {
        super(group, defaultSize);
    }

    public double getSize(VisualItem item) {
        if (!(item instanceof NodeItem)) {
            return FrontendConstants.DEFAULT_SIZE;
        }
        NodeItem node = (NodeItem) item;
        double maxEdgeWeight = 0;

        Iterator<EdgeItem> edgeIter = node.edges();

        while (edgeIter.hasNext()) {
            EdgeItem edge = edgeIter.next();
            double weight = edge.getDouble(BackendConstants.WEIGHT);
            if (weight > maxEdgeWeight && !edge.getBoolean(BackendConstants.IS_PARTNER)) {
                maxEdgeWeight = weight;
            }
        }
        double pct = maxEdgeWeight / 100d;

        double size = ((FrontendConstants.MAXIMUM_NODE_SIZE - FrontendConstants.MINIMUM_NODE_SIZE) * pct) + FrontendConstants.MINIMUM_NODE_SIZE;
        return size;
    }
}
