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

package edu.illinois.comoto.viz.view.graph;

import edu.illinois.comoto.viz.view.BackendConstants;
import edu.illinois.comoto.viz.view.MainWindow;
import prefuse.Display;
import prefuse.data.Graph;

import javax.swing.*;
import java.awt.*;

public class GraphDisplayContainer extends JPanel {
    GraphDisplay graphDisplay;
    Display display;
    private final int STATUS_AREA_HEIGHT = 30;
    JLabel statusLabel;
    int width, height;
    private MainWindow parent;

    public GraphDisplayContainer(Graph graph, int width, int height, MainWindow parent) {
        this.width = width;
        this.height = height;
        this.parent = parent;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(width, height));
        graphDisplay = new GraphDisplay(graph, this);
        this.statusLabel = new JLabel();
        this.statusLabel.setFont(BackendConstants.STATUS_LABEL_FONT);
        display = graphDisplay.getDisplay(width, height - STATUS_AREA_HEIGHT);
        add(display, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }

    public GraphDisplayContainer(int width, int height, MainWindow parent) {
        this.width = width;
        this.height = height;
        this.parent = parent;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(width, height));
        graphDisplay = new GraphDisplay(this);
        this.statusLabel = new JLabel();
        this.statusLabel.setFont(BackendConstants.STATUS_LABEL_FONT);
        add(graphDisplay.getDisplay(width, height - STATUS_AREA_HEIGHT), BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }

    public void changeGraph(Graph graph) {
        graphDisplay.setGraph(graph);
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    public void clearStatus() {
        setStatus("");
    }

    public final GraphDisplay getGraphDisplay() {
        return graphDisplay;
    }

    public MainWindow getParent() {
        return parent;
    }
}