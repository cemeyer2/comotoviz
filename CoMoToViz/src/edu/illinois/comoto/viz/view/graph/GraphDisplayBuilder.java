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
import prefuse.Constants;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.data.Graph;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.render.RendererFactory;
import prefuse.util.force.ForceSimulator;

import java.util.LinkedList;

/**
 * Author:  Charlie Meyer <cemeyer2@illinois.edu>
 * Date:    3/15/11
 * Time:    5:20 PM
 * Package: edu.illinois.comoto.viz.view.graph
 * Created by IntelliJ IDEA.
 */
public class GraphDisplayBuilder {
    private static GraphDisplayBuilder builder;
    private LinkedList<String> visualizationActions;
    private long layoutEngineRunTime = FrontendConstants.DEFAULT_LAYOUT_ENGINE_RUN_TIME;

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
        return display;
    }

    private Visualization buildVisualization(Graph graph) {
        Visualization visualization = new Visualization();
        visualization.addGraph(BackendConstants.GRAPH, graph);
        visualization.setInteractive(BackendConstants.GRAPH + "." + BackendConstants.EDGES, null, true);
        visualization.setRendererFactory(getRendererFactory());
        visualization.putAction(BackendConstants.LAYOUT, getLayoutAction());
        this.visualizationActions.add(BackendConstants.LAYOUT);
        return visualization;
    }

    private RendererFactory getRendererFactory() {
        LabelRenderer labelRenderer = new LabelRenderer(BackendConstants.NETID);
        labelRenderer.setRoundedCorner(8, 8); // round the corners on nodes
        return new DefaultRendererFactory(labelRenderer, new EdgeRenderer(Constants.EDGE_TYPE_CURVE));
    }

    private ActionList getLayoutAction() {
        ActionList layout = new ActionList(layoutEngineRunTime);
        ForceDirectedLayout l = new ForceDirectedLayout(BackendConstants.GRAPH);
        ForceSimulator sim = l.getForceSimulator();
        layout.add(l);
        layout.add(new RepaintAction());
        return layout;
    }


    public long getLayoutEngineRunTime() {
        return this.layoutEngineRunTime;
    }

    public GraphDisplayBuilder setLayoutEngineRunTime(long layoutEngineRunTime) {
        this.layoutEngineRunTime = layoutEngineRunTime;
        return this;
    }

}
