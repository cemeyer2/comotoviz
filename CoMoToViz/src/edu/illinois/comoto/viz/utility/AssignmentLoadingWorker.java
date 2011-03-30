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

package edu.illinois.comoto.viz.utility;

import edu.illinois.comoto.api.object.Assignment;
import edu.illinois.comoto.viz.model.PrefuseGraphBuilder;
import edu.illinois.comoto.viz.view.MainWindow;
import edu.illinois.comoto.viz.view.graph.GraphPanel;
import org.apache.log4j.Logger;

import javax.swing.*;

public class AssignmentLoadingWorker extends SwingWorker<Void, Void> {

    private Assignment assignment;
    private GraphPanel graphPanel;
    private MainWindow frame;
    private static final Logger LOGGER = Logger.getLogger(AssignmentLoadingWorker.class);

    public AssignmentLoadingWorker(Assignment assignment, GraphPanel graphPanel, MainWindow frame) {
        this.assignment = assignment;
        this.graphPanel = graphPanel;
        this.frame = frame;
    }

    @Override
    protected Void doInBackground() {
        try {
            LOGGER.info("SwingWorker running...");
            PrefuseGraphBuilder.getBuilder().setAssignment(assignment);
            graphPanel.reloadGraph();
            frame.updateTitle(assignment);

            frame.searchStudents();
        } catch (Exception e) {
            LOGGER.fatal("Exception taken in SwingWorker", e);
        }
        return null;
    }

    public void runSynchronous() {
        doInBackground();
    }

}
