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

package edu.uiuc.cs.visualmoss.gui.worker;

import edu.uiuc.cs.visualmoss.dataimport.DataImport;
import edu.uiuc.cs.visualmoss.dataimport.api.objects.Assignment;
import edu.uiuc.cs.visualmoss.graph.VisualMossGraph;
import edu.uiuc.cs.visualmoss.graph.VisualMossGraphStudent;
import edu.uiuc.cs.visualmoss.gui.graph.VisualMossGraphDisplayContainer;
import edu.uiuc.cs.visualmoss.gui.layout.VisualMossLayout;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class AssignmentLoadingWorker extends SwingWorker<Void, Void> {

    private Assignment assignment;
    private VisualMossGraphDisplayContainer container;
    private VisualMossLayout frame;
    private DataImport importer;
    private JTree tree;
    private DefaultMutableTreeNode node;

    public AssignmentLoadingWorker(Assignment assignment, VisualMossGraphDisplayContainer container, VisualMossLayout frame, DataImport importer, JTree tree, DefaultMutableTreeNode node) {
        this.assignment = assignment;
        this.container = container;
        this.frame = frame;
        this.importer = importer;
        this.tree = tree;
        this.node = node;
    }

    @Override
    protected Void doInBackground() throws Exception {

        VisualMossGraph graph = importer.buildGraph(assignment, true, frame);

        //add students as children
        for (VisualMossGraphStudent student : graph.getStudents()) {
            node.add(new DefaultMutableTreeNode(student));
        }
        ((DefaultTreeModel) tree.getModel()).reload();

        container.changeGraph(graph);
        frame.updateTitle(assignment);
        frame.searchStudents();

        return null;
    }

    public void runSynchronous() {
        try {
            doInBackground();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
