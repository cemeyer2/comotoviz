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

package edu.illinois.comoto.viz.view.graph.layout;

import edu.illinois.comoto.viz.view.BackendConstants;
import org.apache.log4j.Logger;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.util.force.ForceSimulator;
import prefuse.visual.EdgeItem;

import java.util.Iterator;

/**
 * Author:  Charlie Meyer <cemeyer2@illinois.edu>
 * Date:    3/31/11
 * Time:    1:15 PM
 * Package: edu.illinois.comoto.viz.view.graph.layout
 * Created by IntelliJ IDEA.
 */
public class CoMoToVizForceDirectedLayout extends ForceDirectedLayout {

    private Graph graph;
    private int minDegree, maxDegree;
    private static final Logger LOGGER = Logger.getLogger(CoMoToVizForceDirectedLayout.class);

    public CoMoToVizForceDirectedLayout(Graph g, String graph) {
        this(g, graph, false, false);
    }

    public CoMoToVizForceDirectedLayout(Graph g, String group, boolean enforceBounds) {
        this(g, group, enforceBounds, false);
    }

    public CoMoToVizForceDirectedLayout(Graph g, String group,
                                        boolean enforceBounds, boolean runonce) {
        super(group, enforceBounds, runonce);
        this.graph = g;
        computeMaxDegree();
    }

    private void computeMaxDegree() {
        this.minDegree = 1;
        this.maxDegree = 1;
        Iterator<Edge> edgeIterator = this.graph.edges();
        while (edgeIterator.hasNext()) {
            Edge edge = edgeIterator.next();
            int degree = edge.getInt(BackendConstants.MAX_DEGREE);
            if (degree > this.maxDegree) {
                this.maxDegree = degree;
            }
        }
    }


    public CoMoToVizForceDirectedLayout(Graph g, String group,
                                        ForceSimulator fsim, boolean enforceBounds) {
        this(g, group, fsim, enforceBounds, false);
    }


    public CoMoToVizForceDirectedLayout(Graph g, String group, ForceSimulator fsim,
                                        boolean enforceBounds, boolean runonce) {
        super(group, fsim, enforceBounds, runonce);
        this.graph = g;
        computeMaxDegree();
    }

    @Override
    protected float getSpringLength(EdgeItem e) {
        return -1.f;
    }

    @Override
    protected float getSpringCoefficient(EdgeItem e) {
        int degree = e.getInt(BackendConstants.MAX_DEGREE);
        double pct = 1d - ((degree - this.minDegree) / ((double) (this.maxDegree - this.minDegree)));
        LOGGER.debug("degree: " + degree + " -- pct: " + (100 * pct));
        return (float) (((BackendConstants.DEFAULT_MAX_SPRING_COEFF - BackendConstants.DEFAULT_MIN_SPRING_COEFF) * pct) + BackendConstants.DEFAULT_MIN_SPRING_COEFF);
    }

}
