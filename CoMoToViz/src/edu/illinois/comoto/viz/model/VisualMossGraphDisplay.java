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

import edu.illinois.comoto.viz.model.predicates.VisualMossVisibilityPredicate;
import edu.illinois.comoto.viz.view.BackendConstants;
import edu.illinois.comoto.viz.view.FrontendConstants;
import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.FontAction;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.controls.*;
import prefuse.data.Edge;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.util.force.ForceSimulator;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * a wrapper around prefuse classes that customizes them for visualmoss
 *
 * @author chuck
 */
public class VisualMossGraphDisplay {

    // Components for displaying the graph
    private VisualMossGraph graph;
    private Display display;
    private Visualization visualization;
    private List<String> actions;
    private VisualMossVisibilityPredicate predicate;
    private VisualMossGraphDisplayContainer parent;
    private LabelRenderer labelRenderer;
    private boolean anonymous;

    /**
     * Displays the graph given a non-empty graph
     *
     * @param graph     The graph to display
     * @param parent    The parent window
     */
    protected VisualMossGraphDisplay(VisualMossGraph graph, VisualMossGraphDisplayContainer parent) {
        this.graph = graph;
        this.parent = parent;
        initialize();
    }

    /**
     * Displays an empty panel where the graph should be
     *
     * @param parent    The parent window
     */
    protected VisualMossGraphDisplay(VisualMossGraphDisplayContainer parent) {
        this.parent = parent;
        this.graph = null;
        initialize();
    }

    /**
     * Helper function to initialize this display
     */
    private void initialize() {

        // Set default flags on this graph
        boolean hasGraph = graph != null;
        double minimumWeightToDisplay = 0;
        boolean showSingletons = true;
        boolean showSolution = true;
        boolean includePast = false;
        boolean includePartners = false;
        anonymous = false;
        actions = new LinkedList<String>();


        // Add the graph itself, and make it interactive
        visualization = new Visualization();
        if (hasGraph) {
            visualization.addGraph(BackendConstants.GRAPH, graph.getPrefuseGraph());
        }
        visualization.setInteractive(BackendConstants.GRAPH + "." + BackendConstants.EDGES, null, true);

        // Initialize the colors, and run the layout engine
        setRenderers();
        setColors(BackendConstants.COLOR);
        setLayout(BackendConstants.LAYOUT, 5000); //run the layout engine for 5 seconds

        // Set visibility of nodes
        predicate = new VisualMossVisibilityPredicate(minimumWeightToDisplay, showSingletons, showSolution, includePast, includePartners);

        // Add the controls to this page and the listeners
        display = new Display(visualization, predicate);
        display.addControlListener(new VisualMossControl(parent));
        display.setHighQuality(true);
        addControls();
    }

    /**
     * Add the renderers for the graph
     */
    private void setRenderers() {
        labelRenderer = new LabelRenderer(BackendConstants.NETID);
        labelRenderer.setRoundedCorner(8, 8); // round the corners on nodes
        visualization.setRendererFactory(new DefaultRendererFactory(labelRenderer, new EdgeRenderer(Constants.EDGE_TYPE_CURVE)));
    }

    /**
     * Helper function to set the colors on the graph (nodes & edges)
     */
    private void setColors(String actionName) {

        // colors for coloring nodes, white background for regular nodes, red background for solution
        // evals based off of isSolution, which is either "false" or "true", since "true" is alphabetically
        // after "false", red is used for it
        VisualMossNodeFillColorAction fill = new VisualMossNodeFillColorAction(BackendConstants.GRAPH + "." + BackendConstants.NODES, VisualItem.FILLCOLOR);

        // Black outlines for nodes
        VisualMossNodeStrokeColorAction stroke = new VisualMossNodeStrokeColorAction(BackendConstants.GRAPH + "." + BackendConstants.NODES, VisualItem.STROKECOLOR);

        // use black for node text
        ColorAction text = new ColorAction(BackendConstants.GRAPH + "." + BackendConstants.NODES, VisualItem.TEXTCOLOR, ColorLib.gray(0));

        // use light grey for edges
        VisualMossEdgeStrokeColorAction edges = new VisualMossEdgeStrokeColorAction(BackendConstants.GRAPH + "." + BackendConstants.EDGES, VisualItem.STROKECOLOR);
        ColorAction hl = new ColorAction(BackendConstants.GRAPH + "." + BackendConstants.NODES, VisualItem.HIGHLIGHT, ColorLib.rgb(255, 200, 125));

        // Add the colors
        ActionList color = new ActionList();
        color.add(fill);
        color.add(stroke);
        color.add(text);
        color.add(edges);
        color.add(hl);
        visualization.putAction(actionName, color);
        actions.add(actionName);

        // Put verdana as font
        visualization.putAction(BackendConstants.FONT, new FontAction(BackendConstants.GRAPH, BackendConstants.NODE_LABEL_FONT));
        actions.add(BackendConstants.FONT);
    }

    /**
     * Helper function for interacting with Prefuse engine
     */
    private void setLayout(String actionName, long time) {
        ActionList layout = new ActionList(time);
        ForceDirectedLayout l = new ForceDirectedLayout(BackendConstants.GRAPH);
        ForceSimulator sim = l.getForceSimulator();
        layout.add(l);
        layout.add(new RepaintAction());
        visualization.putAction(actionName, layout);
        actions.add(actionName);
    }

    /**
     * Add the control listeners for this graph
     */
    private void addControls() {
        display.addControlListener(new DragControl());
        display.addControlListener(new PanControl());
        display.addControlListener(new ZoomControl());
        display.addControlListener(new NeighborHighlightControl());
        display.addControlListener(new WheelZoomControl());
    }

    /**
     * gets the Display for this visualization
     *
     * @param width  the desired width of the display
     * @param height the desired height of the display
     * @return the Display
     */
    public Display getDisplay(int width, int height) {
        this.display.setSize(width, height);
        return this.display;
    }

    /**
     * runs all actions associated with this visualization, including coloring and layout
     */
    public void run() {
        for (String action : actions) {
            visualization.run(action);
        }
    }

    /**
     * runs a specific action for this visualization if it exists, otherwise does nothing
     *
     * @param action the action name to run
     */
    public void run(String action) {
        if (actions.contains(action))
            visualization.run(action);
    }

    /**
     * Sets the new graph to be displayed, and reruns the layout engine
     *
     * @param graph The new graph to display
     */
    public void setGraph(VisualMossGraph graph) {
        System.out.println("Starting to Change " + BackendConstants.GRAPH);
        visualization.removeGroup(BackendConstants.GRAPH);
        System.out.println("Removed Old " + BackendConstants.GRAPH + ", Adding New");
        visualization.addGraph(BackendConstants.GRAPH, graph.getPrefuseGraph());
        System.out.println(BackendConstants.GRAPH + " Changed");
        this.graph = graph;
        run();
        System.out.println("Run Complete");
    }

    /**
     * Simple getter
     */
    public VisualMossGraph getGraph() {
        return graph;
    }

    /**
     * shows or hides singletons
     *
     * @param showSingletons
     */
    public void setShowSingletons(boolean showSingletons) {
        this.predicate.setShowSingletons(showSingletons);
        repaint();
    }

    /**
     * Sets the minimum edge weight to display, and repaints the canvas to refresh the changes
     *
     * @param weight The new minimum edge weight
     */
    public void setMinimumEdgeWeightToDisplay(double weight) {
        this.predicate.setMinWeight(weight);
        repaint();
    }

    /**
     * Show or hide the solution(s) node(s)
     *
     *  @param showSolutions Whether or not to show the solution nodes
     */
    public void setShowSolution(boolean showSolutions) {
        this.predicate.setShowSolution(showSolutions);
        repaint();
    }

    /**
     * Show or hide partner edges
     *
     * @param includePartners   Whether or not to include partner edges
     */
    public void setIncludePartners(boolean includePartners) {
        this.predicate.setIncludePartners(includePartners);
        repaint();
    }

    /**
     * zooms the display about the point that is currently centered
     *
     * @param level the scale factor to zoom by, 1.05 would zoom in 5%, .95 would zoom out 5%, etc
     */
    public void setZoom(double level) {
        this.display.zoom(new Point2D.Double(display.getWidth() / 2d, display.getHeight() / 2d), (double) level);
        this.display.repaint();
    }

    /**
     * Gets the current zoom scale factor
     *
     * @return The zoom factor
     */
    public double getZoom() {
        return this.display.getScale();
    }

    /**
     * Rotates the display by the number of radians supplied
     *
     * @param radians The amount to rotate
     */
    public void rotate(double radians) {
        this.display.rotate(new Point2D.Double(display.getWidth() / 2, display.getHeight() / 2), radians);
        this.display.repaint();
    }

    /**
     * Gets the running frame rate for the viz
     *
     * @return The average frame rate
     */
    public double getAverageFrameRate() {
        return this.display.getFrameRate();
    }

    /**
     * Sets the display to show nodes as anonymous or not
     *
     * @param anonymous Whether or not this graph is anonymouse
     */
    public void setAnonymous(boolean anonymous) {
        if (anonymous) {
            labelRenderer.setTextField(BackendConstants.PSEUDONYM);
        } else {
            labelRenderer.setTextField(BackendConstants.NETID);
        }
        repaint();
        this.anonymous = anonymous;
    }

    /**
     * Returns if we are showing anonymous or not
     *
     * @return Whether this graph is anonymous
     */
    public boolean isAnonymous() {
        return this.anonymous;
    }

    /**
     * writes out the current visible portion of the viz in the display to file as a png
     *
     * @param outFile   The file to which to write the image of the graph
     * @throws IOException  On error writing to disk
     */
    public void writeToImage(File outFile) throws IOException {
        FileOutputStream os = new FileOutputStream(outFile);
        this.display.saveImage(os, FrontendConstants.PNG, 1);
    }

    /**
     * Forces an entire repaint on the display by marking the whole display as damaged then calling a repaint
     */
    public void repaint() {
        this.display.damageReport();
        this.display.repaint();
    }

    /**
     * Sets whether  past students should be included, and reruns the layout engine
     *
     * @param includePast   Whether or not to include past students
     */
    public void setIncludePast(boolean includePast) {
        this.predicate.setIncludePast(includePast);
        this.repaint();
        setGraph(graph);
    }

    /**
     * Gets an iterator to navigate through the nodes of the graph
     *
     * @return  An iterator for the nodes of the graph
     */
    public Iterator<NodeItem> getNodes() {
        if (visualization == null) return null;
        return visualization.items(BackendConstants.GRAPH + "." + BackendConstants.NODES);
    }

    /**
     * animates a pan to a node with the netid supplied, if it exists in the viz. the speed of the
     * animation is given by the number of milliseconds the pan should take
     *
     * @param netid     The netid of the node to which to pan
     * @param lengthMS  The number of milliseconds to take to pan
     */
    public void panToNode(String netid, int lengthMS) {
        Iterator<NodeItem> iter = visualization.items(BackendConstants.GRAPH + "." + BackendConstants.NODES);
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
        double x = node.getX();
        double y = node.getY();
        display.animatePanToAbs(new Point2D.Double(x, y), lengthMS);
    }

    /**
     * Get the URL for the report (the link to view an analysis in the web browser)
     *
     * @return  The URL
     */
    public String getReportURL() {
        Edge edge = (Edge) graph.getPrefuseGraph().edges().next();
        String link = edge.getString(BackendConstants.LINK);
        String url = link.substring(0, link.lastIndexOf("/") + 1) + "FIX ME";
        return url;
    }

    public VisualMossVisibilityPredicate getVisibilityPredicate() {
        return predicate;
    }
}