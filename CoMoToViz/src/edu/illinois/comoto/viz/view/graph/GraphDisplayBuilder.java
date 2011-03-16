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

import edu.illinois.comoto.viz.model.PrefuseGraphBuilder;
import edu.illinois.comoto.viz.model.predicates.VisibilityPredicate;
import edu.illinois.comoto.viz.view.BackendConstants;
import edu.illinois.comoto.viz.view.FrontendConstants;
import edu.illinois.comoto.viz.view.graph.actions.EdgeStrokeColorAction;
import edu.illinois.comoto.viz.view.graph.actions.NodeFillColorAction;
import edu.illinois.comoto.viz.view.graph.actions.NodeStrokeColorAction;
import edu.illinois.comoto.viz.view.graph.control.GraphControl;
import prefuse.Constants;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.FontAction;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.data.Graph;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.render.RendererFactory;
import prefuse.util.ColorLib;
import prefuse.util.force.ForceSimulator;
import prefuse.visual.VisualItem;

import java.util.LinkedList;
import java.util.List;

/**
 * Author:  Charlie Meyer <cemeyer2@illinois.edu>
 * Date:    3/15/11
 * Time:    5:20 PM
 * Package: edu.illinois.comoto.viz.view.graph
 * Created by IntelliJ IDEA.
 */
public class GraphDisplayBuilder {
    private static GraphDisplayBuilder builder;
    private List<String> visualizationActions;
    private long layoutEngineRunTime = FrontendConstants.DEFAULT_LAYOUT_ENGINE_RUN_TIME;
    private boolean isAnonymous = FrontendConstants.DEFAULT_ANONYMOUS;

    public static GraphDisplayBuilder getBuilder() {
        if (builder == null) {
            builder = new GraphDisplayBuilder();
        }
        return builder;
    }

    private GraphDisplayBuilder() {
        this.visualizationActions = new LinkedList<String>();
    }

    /**
     * Builds a GraphDisplay by calling buildPrefuseGraph on the PrefuseGraphBuilder
     *
     * @return a GraphDisplay object representing the current state of the PrefuseGraphBuilder
     * @see edu.illinois.comoto.viz.model.PrefuseGraphBuilder#buildPrefuseGraph()
     */
    public GraphDisplay buildDisplay() {
        this.visualizationActions.clear();
        Graph graph = PrefuseGraphBuilder.getBuilder().buildPrefuseGraph();
        VisibilityPredicate predicate = PrefuseGraphBuilder.getBuilder().getPredicate();
        Visualization visualization = buildVisualization(graph);
        GraphDisplay display = new GraphDisplay(visualization, this.visualizationActions);
        display.setHighQuality(true);
        addControls(display);
        return display;
    }

    private Visualization buildVisualization(Graph graph) {
        Visualization visualization = new Visualization();
        visualization.addGraph(BackendConstants.GRAPH, graph);
        visualization.setInteractive(BackendConstants.GRAPH + "." + BackendConstants.EDGES, null, true);
        visualization.setRendererFactory(getRendererFactory());

        //add the actions
        //laout
        visualization.putAction(BackendConstants.LAYOUT, getLayoutActions());
        this.visualizationActions.add(BackendConstants.LAYOUT);
        //color
        visualization.putAction(BackendConstants.COLOR, getColorActions());
        this.visualizationActions.add(BackendConstants.COLOR);
        //font
        visualization.putAction(BackendConstants.FONT, getFontActions());
        this.visualizationActions.add(BackendConstants.FONT);

        return visualization;
    }

    private RendererFactory getRendererFactory() {
        LabelRenderer labelRenderer = new LabelRenderer(BackendConstants.NETID);
        labelRenderer.setRoundedCorner(8, 8); // round the corners on nodes
        if (this.isAnonymous) {
            labelRenderer.setTextField(BackendConstants.PSEUDONYM);
        }
        return new DefaultRendererFactory(labelRenderer, new EdgeRenderer(Constants.EDGE_TYPE_CURVE));
    }

    private ActionList getLayoutActions() {
        ActionList layout = new ActionList(layoutEngineRunTime);
        ForceDirectedLayout l = new ForceDirectedLayout(BackendConstants.GRAPH);
        ForceSimulator sim = l.getForceSimulator();
        layout.add(l);
        layout.add(new RepaintAction());
        return layout;
    }

    private ActionList getColorActions() {

        // colors for coloring nodes, white background for regular nodes, red background for solution
        // evals based off of isSolution, which is either "false" or "true", since "true" is alphabetically
        // after "false", red is used for it
        NodeFillColorAction fill = new NodeFillColorAction(BackendConstants.GRAPH + "." + BackendConstants.NODES, VisualItem.FILLCOLOR);

        // Black outlines for nodes
        NodeStrokeColorAction stroke = new NodeStrokeColorAction(BackendConstants.GRAPH + "." + BackendConstants.NODES, VisualItem.STROKECOLOR);

        // use black for node text
        ColorAction text = new ColorAction(BackendConstants.GRAPH + "." + BackendConstants.NODES, VisualItem.TEXTCOLOR, ColorLib.gray(0));

        // use light grey for edges
        EdgeStrokeColorAction edges = new EdgeStrokeColorAction(BackendConstants.GRAPH + "." + BackendConstants.EDGES, VisualItem.STROKECOLOR);
        ColorAction hl = new ColorAction(BackendConstants.GRAPH + "." + BackendConstants.NODES, VisualItem.HIGHLIGHT, ColorLib.rgb(255, 200, 125));

        // Add the colors
        ActionList colorActions = new ActionList();
        colorActions.add(fill);
        colorActions.add(stroke);
        colorActions.add(text);
        colorActions.add(edges);
//        colorActions.add(hl); //cant figure out how to get this to work, just causes warnings to print out

        return colorActions;
    }

    private ActionList getFontActions() {
        ActionList fontActions = new ActionList();
        FontAction action = new FontAction(BackendConstants.GRAPH, BackendConstants.NODE_LABEL_FONT);
        fontActions.add(action);
        return fontActions;
    }

    private void addControls(GraphDisplay display) {
        display.addControlListener(new GraphControl());
        display.addControlListener(new DragControl());
        display.addControlListener(new PanControl());
        display.addControlListener(new ZoomControl());
//        display.addControlListener(new NeighborHighlightControl(BackendConstants.COLOR)); //cant figure out how to get this to work, it just causes warnings to print out
        display.addControlListener(new WheelZoomControl());
    }


    public long getLayoutEngineRunTime() {
        return this.layoutEngineRunTime;
    }

    public GraphDisplayBuilder setLayoutEngineRunTime(long layoutEngineRunTime) {
        this.layoutEngineRunTime = layoutEngineRunTime;
        return this;
    }

    public boolean isAnonymous() {
        return this.isAnonymous;
    }

    public void setAnonymous(boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

}
