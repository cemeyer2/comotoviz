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

import edu.illinois.comoto.viz.model.predicates.NodeFillConnectedToPastPredicate;
import edu.illinois.comoto.viz.model.predicates.NodeFillCurrentSemesterPredicate;
import edu.illinois.comoto.viz.model.predicates.NodeFillSolutionPredicate;
import edu.illinois.comoto.viz.view.FrontendConstants;
import prefuse.action.assignment.ColorAction;
import prefuse.visual.VisualItem;

public class NodeFillColorAction extends ColorAction {

    private NodeFillCurrentSemesterPredicate cursem = new NodeFillCurrentSemesterPredicate();
    private NodeFillSolutionPredicate sol = new NodeFillSolutionPredicate();
    private NodeFillConnectedToPastPredicate past = new NodeFillConnectedToPastPredicate();

    public NodeFillColorAction(String group, String field) {
        super(group, field);
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getColor(VisualItem item) {
        if (past.getBoolean(item)) {
            return FrontendConstants.CONNECTED_TO_PAST_NODE_FILL_COLOR_INT;
        } else if (cursem.getBoolean(item)) {
            return FrontendConstants.CURRENT_SEMESTER_NODE_FILL_COLOR_INT;
        } else if (sol.getBoolean(item)) {
            return FrontendConstants.SOLUTION_NODE_FILL_COLOR_INT;
        } else {
            return FrontendConstants.PAST_SEMESTER_NODE_FILL_COLOR_INT;
        }

    }

}
