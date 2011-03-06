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
import prefuse.visual.VisualGraph;
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
    private VisualMossGraph graph;
    private Display display;
    private Visualization vis;
    private List<String> actions;
    private VisualMossVisibilityPredicate predicate;
    private VisualMossGraphDisplayContainer parent;
    private LabelRenderer labelRenderer;
    private boolean anonymous;

    protected VisualMossGraphDisplay(VisualMossGraph graph, VisualMossGraphDisplayContainer parent) {
        this.graph = graph;
        this.parent = parent;
        init();
    }

    protected VisualMossGraphDisplay(VisualMossGraphDisplayContainer parent) {
        this.parent = parent;
        this.graph = null;
        init();
    }

    private void init() {
        boolean hasGraph = graph != null;

        double minimumWeightToDisplay = 0;
        boolean showSingletons = true;
        boolean showSolution = true;
        boolean includePast = false;
        boolean includePartners = false;
        anonymous = false;
        actions = new LinkedList<String>();

        vis = new Visualization();
        if (hasGraph) {
            VisualGraph vgraph = vis.addGraph("graph", graph.getPrefuseGraph());
        }
        vis.setInteractive("graph.edges", null, true);

        setRenderers();
        setColors("color");
        setLayout("layout", 5000); //run the layout engine for 5 seconds

        predicate = new VisualMossVisibilityPredicate(minimumWeightToDisplay, showSingletons, showSolution, includePast, includePartners);

        display = new Display(vis, predicate);
        display.addControlListener(new VisualMossControl(parent));
        display.setHighQuality(true);

        addControls();
    }

    private void setRenderers() {
        labelRenderer = new LabelRenderer("netid");
        labelRenderer.setRoundedCorner(8, 8); // round the corners on nodes
        vis.setRendererFactory(new DefaultRendererFactory(labelRenderer, new EdgeRenderer(Constants.EDGE_TYPE_CURVE)));
    }

    private void setColors(String actionName) {
        //colors for coloring nodes, white background for regular nodes, red background for solution
        //evals based off of isSolution, which is either "false" or "true", since "true" is alphabetically
        //after "false", red is used for it
        VisualMossNodeFillColorAction fill = new VisualMossNodeFillColorAction("graph.nodes", VisualItem.FILLCOLOR);

        //black lines for nodes
        VisualMossNodeStrokeColorAction stroke = new VisualMossNodeStrokeColorAction("graph.nodes", VisualItem.STROKECOLOR);

        // use black for node text
        ColorAction text = new ColorAction("graph.nodes", VisualItem.TEXTCOLOR, ColorLib.gray(0));

        // use light grey for edges
        VisualMossEdgeStrokeColorAction edges = new VisualMossEdgeStrokeColorAction("graph.edges", VisualItem.STROKECOLOR);
        ColorAction hl = new ColorAction("graph.nodes", VisualItem.HIGHLIGHT, ColorLib.rgb(255, 200, 125));
        ActionList color = new ActionList();
        color.add(fill);
        color.add(stroke);
        color.add(text);
        color.add(edges);
        color.add(hl);
        vis.putAction(actionName, color);
        actions.add(actionName);

        //put verdana as font
        vis.putAction("font", new FontAction("graph", BackendConstants.NODE_LABEL_FONT));
        actions.add("font");
    }

    private void setLayout(String actionName, long time) {
        ActionList layout = new ActionList(time);
        ForceDirectedLayout l = new ForceDirectedLayout("graph");
        //set springs here
        ForceSimulator sim = l.getForceSimulator();
        layout.add(l);
        layout.add(new RepaintAction());
        vis.putAction(actionName, layout);
        actions.add(actionName);
    }

    private void addControls() {
        display.addControlListener(new DragControl());
        display.addControlListener(new PanControl());
        display.addControlListener(new ZoomControl());
        display.addControlListener(new NeighborHighlightControl());
        display.addControlListener(new WheelZoomControl());
    }

    /**
     * gets the Display for this vis
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
     * runs all actions associated with this vis, including coloring and layout
     */
    public void run() {
        for (String action : actions) {
            vis.run(action);
        }
    }

    /**
     * runs a specific action for this vis if it exists, otherwise does nothing
     *
     * @param action the action name to run
     */
    public void run(String action) {
        if (actions.contains(action))
            vis.run(action);
    }

    public VisualMossGraph getGraph() {
        return graph;
    }

    public void setGraph(VisualMossGraph graph) {
        System.out.println("starting to change graph");
        vis.removeGroup("graph");
        System.out.println("removed old graph, adding new");
        vis.addGraph("graph", graph.getPrefuseGraph());
        System.out.println("graph changed");
        this.graph = graph;
        run();
        System.out.println("run complete");
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
     * sets the minimum edge weight to display
     *
     * @param weight
     */
    public void setMinimumEdgeWeightToDisplay(double weight) {
        this.predicate.setMinWeight(weight);
        repaint();
    }

    /**
     * shows or hides the solution
     *
     * @param showSolutions
     */
    public void setShowSolution(boolean showSolutions) {
        this.predicate.setShowSolution(showSolutions);
        repaint();
    }

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
     * gets the current zoom scale factor
     *
     * @return
     */
    public double getZoom() {
        return this.display.getScale();
    }

    /**
     * rotates the display by the number of radians supplied
     *
     * @param radians
     */
    public void rotate(double radians) {
        this.display.rotate(new Point2D.Double(display.getWidth() / 2, display.getHeight() / 2), radians);
        this.display.repaint();
    }

    /**
     * gets the running frame rate for the viz
     *
     * @return
     */
    public double getAverageFrameRate() {
        return this.display.getFrameRate();
    }

    /**
     * sets the display to show nodes as anonymous or not
     *
     * @param anonymous
     */
    public void setAnonymous(boolean anonymous) {
        if (anonymous) {
            labelRenderer.setTextField("pseudonym");
        } else {
            labelRenderer.setTextField("netid");
        }
        repaint();
        this.anonymous = anonymous;
    }

    /**
     * returns if we are showing anonymous or not
     *
     * @return
     */
    public boolean isAnonymous() {
        return this.anonymous;
    }

    /**
     * writes out the current visible portion of the viz in the display to file as a png
     *
     * @param outFile
     * @throws IOException
     */
    public void writeToImage(File outFile) throws IOException {
        FileOutputStream os = new FileOutputStream(outFile);
        this.display.saveImage(os, "PNG", 1);
    }

    /**
     * forces an entire repaint on the display by marking the whole display as damaged then calling a repaint
     */
    public void repaint() {
        this.display.damageReport();
        this.display.repaint();
    }

    public void setIncludePast(boolean includePast) {
        this.predicate.setIncludePast(includePast);
        this.repaint();
        setGraph(graph);
    }

    public Iterator<NodeItem> getNodes() {
        if (vis == null) return null;
        return vis.items("graph.nodes");
    }

    /**
     * animates a pan to a node with the netid supplied, if it exists in the viz. the speed of the
     * animation is given by the number of milliseconds the pan should take
     *
     * @param netid
     */
    public void panToNode(String netid, int lengthMS) {
        Iterator<NodeItem> iter = vis.items("graph.nodes");
        NodeItem node = null;
        while (iter.hasNext()) {
            NodeItem temp = iter.next();
            if (temp.getString("netid").equals(netid)) {
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

    public String getReportURL() {
        Edge edge = (Edge) graph.getPrefuseGraph().edges().next();
        String link = edge.getString("link");
        String url = link.substring(0, link.lastIndexOf("/") + 1) + "FIX ME";
        return url;
    }

    public VisualMossVisibilityPredicate getVisibilityPredicate() {
        return predicate;
    }
}