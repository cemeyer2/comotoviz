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

import edu.illinois.comoto.viz.utility.CoMoToVizException;
import edu.illinois.comoto.viz.view.AssignmentChooserPanel;
import edu.illinois.comoto.viz.view.BackendConstants;
import org.apache.log4j.Logger;
import prefuse.util.ColorLib;
import prefuse.visual.NodeItem;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * Author:  Charlie Meyer <cemeyer2@illinois.edu>
 * Date:    3/15/11
 * Time:    5:17 PM
 * Package: edu.illinois.comoto.viz.view.graph
 * Created by IntelliJ IDEA.
 */
public class GraphPanel extends JPanel {

    private GraphDisplay currentDisplay;
    private AssignmentChooserPanel assignmentChooserPanel;
    private JLabel messageLabel;
    // For keeping track of data in this window
    private NodeItem lastNode;
    private int lastFillColor;
    private static final Logger LOGGER = Logger.getLogger(GraphPanel.class);
    private static GraphPanel pseudoSingleton;

    public GraphPanel(int width, int height) {
        pseudoSingleton = this;
        setLayout(new BorderLayout());
        Dimension size = new Dimension(width, height);
        setPreferredSize(size);
        setSize(size);
        currentDisplay = null;
        messageLabel = new JLabel();
        messageLabel.setFont(BackendConstants.STATUS_LABEL_FONT);
        add(messageLabel, BorderLayout.SOUTH);
    }

    public void reloadGraph() {
        LOGGER.info("Reloading graph");
        try {
            if (currentDisplay != null) {
                remove(currentDisplay);
            }
            currentDisplay = GraphDisplayBuilder.getBuilder().buildDisplay();
            if (assignmentChooserPanel != null) {
                assignmentChooserPanel.populateCurrentAssignmentNodeWithStudents();
            }
            add(currentDisplay, BorderLayout.CENTER);
            revalidate();
            repaint();
            currentDisplay.runAllActions();
        } catch (CoMoToVizException e) {
            LOGGER.error("Tried to reload graph on invalid builder", e);
        }
        LOGGER.info("Reloaded");
    }

    public void setAssignmentChooserPanel(AssignmentChooserPanel assignmentChooserPanel) {
        this.assignmentChooserPanel = assignmentChooserPanel;
    }

    /**
     * zooms the display about the point that is currently centered
     *
     * @param level the scale factor to zoom by, 1.05 would zoom in 5%, .95 would zoom out 5%, etc
     */
    public void setZoom(double level) {
        this.currentDisplay.zoom(new Point2D.Double(this.currentDisplay.getWidth() / 2d, this.currentDisplay.getHeight() / 2d), (double) level);
        this.currentDisplay.repaint();
    }

    /**
     * Gets the current zoom scale factor
     *
     * @return The zoom factor
     */
    public double getZoom() {
        return this.currentDisplay.getScale();
    }

    /**
     * animates a pan to a node with the netid supplied, if it exists in the viz. the speed of the
     * animation is given by the number of milliseconds the pan should take
     *
     * @param netid    The netid of the node to which to pan
     * @param lengthMS The number of milliseconds to take to pan
     */
    public void panToNode(String netid, int lengthMS) {
        Iterator<NodeItem> iter = currentDisplay.getVisualization().items(BackendConstants.GRAPH + "." + BackendConstants.NODES);
        NodeItem node = null;
        while (iter.hasNext()) {
            NodeItem temp = iter.next();
            if (temp.getString(BackendConstants.NETID).equals(netid)) {
                node = temp;
                break;
            }
        }
        if (node == null) {
            return;
        }
        // Handle coloring the node
        if (lastNode != null) {
            lastNode.setFillColor(lastFillColor);
        }
        lastNode = node;
        lastFillColor = node.getFillColor();
        node.setFillColor(ColorLib.rgb(0, 255, 0));
        double x = node.getX();
        double y = node.getY();
        currentDisplay.animatePanToAbs(new Point2D.Double(x, y), lengthMS);
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void clearMessage() {
        setMessage("");
    }

    public GraphDisplay getCurrentGraphDisplay() {
        return this.currentDisplay;
    }

    public static GraphPanel getGraphPanel() {
        return pseudoSingleton;
    }

    /**
     * writes out the current visible portion of the viz in the display to file as a png
     *
     * @param outFile
     * @throws IOException
     */
    public void writeToImage(File outFile) throws IOException {
        FileOutputStream os = new FileOutputStream(outFile);
        this.currentDisplay.saveImage(os, "PNG", 1);
        os.close();
    }


}
