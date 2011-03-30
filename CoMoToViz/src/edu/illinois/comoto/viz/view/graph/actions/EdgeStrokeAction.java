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
import prefuse.action.assignment.StrokeAction;
import prefuse.visual.EdgeItem;
import prefuse.visual.VisualItem;

import java.awt.*;

/**
 * Author:  Charlie Meyer <cemeyer2@illinois.edu>
 * Date:    3/30/11
 * Time:    2:22 PM
 * Package: edu.illinois.comoto.viz.view.graph.actions
 * Created by IntelliJ IDEA.
 */
public class EdgeStrokeAction extends StrokeAction {

    public EdgeStrokeAction() {
        super();
    }

    public EdgeStrokeAction(String group) {
        super(group);
    }

    public EdgeStrokeAction(String group, BasicStroke defaultStroke) {
        super(group, defaultStroke);
    }

    public BasicStroke getStroke(VisualItem item) {
        if (!(item instanceof EdgeItem)) {
            return new BasicStroke(FrontendConstants.DEFAULT_STROKE_WDITH);
        }
        EdgeItem edge = (EdgeItem) item;
        int weight = edge.getInt(BackendConstants.WEIGHT);

        double pct = weight / 100d;

        int width = (int) (((FrontendConstants.MAXIMUM_EDGE_WIDTH - FrontendConstants.MINIMUM_EDGE_WIDTH) * pct) + FrontendConstants.MINIMUM_EDGE_WIDTH);
        return new BasicStroke(width);
    }
}
