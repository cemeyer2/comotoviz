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

package edu.illinois.comoto.viz.controller;

import edu.illinois.comoto.viz.model.PrefuseGraphBuilder;
import edu.illinois.comoto.viz.view.AssignmentChooserPanel;
import edu.illinois.comoto.viz.view.BackendConstants;
import edu.illinois.comoto.viz.view.FrontendConstants;
import edu.illinois.comoto.viz.view.graph.GraphPanel;

import javax.swing.BorderFactory;
import javax.swing.JSlider;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * User: Jon
 * Date: 3/6/11
 * <p/>
 * Stores all mouse listeners,
 */
public enum MouseListenerActions {

    // When the assignment chooser is clicked
    assignmentChooser {
        @Override
        MouseListener getMouseListenerAction(final Object... parameters) {
            return new MouseListener() {

                // The assignment chooser object
                private AssignmentChooserPanel thisAssignmentChooser = (AssignmentChooserPanel) parameters[0];

                /**
                 * Change the assignment
                 *
                 * @param event All data passed from the mouse event
                 */
                public void mouseClicked(MouseEvent event) {
                    if (event.getClickCount() > 1) {
                        JTree tree = thisAssignmentChooser.getTree();
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
                        thisAssignmentChooser.changeAssignment(node);
                    }
                }

                // Other required user input events
                public void mousePressed(MouseEvent e) {
                }

                public void mouseReleased(MouseEvent e) {
                }

                public void mouseEntered(MouseEvent e) {
                }

                public void mouseExited(MouseEvent e) {
                }
            };
        }
    },

    // When the edge threshold is changed
    threshold {
        @Override
        MouseListener getMouseListenerAction(final Object... parameters) {

            // Pull some of the GUI components from the parameters
            final JSlider threshholdSlider = (JSlider) parameters[0];
            final GraphPanel graphPanel = (GraphPanel) parameters[1];

            return new MouseListener() {
                public void mouseClicked(MouseEvent mouseEvent) {
                }

                public void mousePressed(MouseEvent mouseEvent) {
                }

                public void mouseReleased(MouseEvent mouseEvent) {
                    TitledBorder bb = BorderFactory.createTitledBorder(FrontendConstants.MINIMUM_EDGE_WEIGHT + ": " + threshholdSlider.getValue());
                    bb.setTitleFont(BackendConstants.COMPONENT_LABEL_FONT);
                    threshholdSlider.setBorder(bb);
                    PrefuseGraphBuilder.getBuilder().setMinimumEdgeWeight(threshholdSlider.getValue());
                    graphPanel.reloadGraph();
                }

                public void mouseEntered(MouseEvent mouseEvent) {
                }

                public void mouseExited(MouseEvent mouseEvent) {
                }
            };
        }
    };

    abstract MouseListener getMouseListenerAction(Object... parameters);
}
