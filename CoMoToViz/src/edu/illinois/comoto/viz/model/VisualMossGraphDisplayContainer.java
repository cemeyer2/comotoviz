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

package edu.illinois.comoto.viz.model;

import edu.illinois.comoto.viz.view.BackendConstants;

import javax.swing.*;
import java.awt.*;

public class VisualMossGraphDisplayContainer extends JPanel {
    VisualMossGraphDisplay graphDisplay;
    private final int STATUS_AREA_HEIGHT = 30;
    JLabel statusLabel;
    int width, height;

    public VisualMossGraphDisplayContainer(VisualMossGraph graph, int width, int height) {
        this.width = width;
        this.height = height;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(width, height));
        graphDisplay = new VisualMossGraphDisplay(graph, this);
        this.statusLabel = new JLabel();
        this.statusLabel.setFont(BackendConstants.STATUS_LABEL_FONT);
        add(graphDisplay.getDisplay(width, height - STATUS_AREA_HEIGHT), BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }

    public VisualMossGraphDisplayContainer(int width, int height) {
        this.width = width;
        this.height = height;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(width, height));
        graphDisplay = new VisualMossGraphDisplay(this);
        this.statusLabel = new JLabel();
        this.statusLabel.setFont(BackendConstants.STATUS_LABEL_FONT);
        add(graphDisplay.getDisplay(width, height - STATUS_AREA_HEIGHT), BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }

    public void changeGraph(VisualMossGraph graph) {
        graphDisplay.setGraph(graph);
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    public void clearStatus() {
        setStatus("");
    }

    public final VisualMossGraphDisplay getVisualMossGraphDisplay() {
        return graphDisplay;
    }
}
