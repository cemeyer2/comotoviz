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

import edu.illinois.comoto.viz.model.PrefuseGraphBuilder;
import edu.illinois.comoto.viz.view.BackendConstants;
import org.apache.log4j.Logger;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.util.force.DragForce;
import prefuse.util.force.Force;
import prefuse.util.force.ForceSimulator;
import prefuse.util.force.NBodyForce;
import prefuse.util.force.SpringForce;
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
        init(g);
    }

    public CoMoToVizForceDirectedLayout(Graph g, String group,
                                        ForceSimulator fsim, boolean enforceBounds) {
        this(g, group, fsim, enforceBounds, false);
    }


    public CoMoToVizForceDirectedLayout(Graph g, String group, ForceSimulator fsim,
                                        boolean enforceBounds, boolean runonce) {
        super(group, fsim, enforceBounds, runonce);
        init(g);
    }

    private void init(Graph g) {
        this.graph = g;
        computeMaxDegree();
        editForces();
    }

    private void editForces() {
        ForceSimulator simulator = this.getForceSimulator();
        Force[] forces = simulator.getForces();
        for (Force force : forces) {
            if (force instanceof DragForce) {
                editDragForce((DragForce) force);
            } else if (force instanceof NBodyForce) {
                editNBodyForce((NBodyForce) force);
            }
        }
    }

    private void editNBodyForce(NBodyForce force) {
        LOGGER.info("Editing n body force");
        editGravity(force);
        editDistance(force);
        editTheta(force);
    }

    //until i read the links on what this is, im leaving it as the default
    private void editTheta(NBodyForce force) {
        int index = BackendConstants.NBODY_FORCE_BARNES_HUT_THETA_PARAM_INDEX;
        force.setMaxValue(index, BackendConstants.MAX_THETA);
        force.setMinValue(index, BackendConstants.MIN_THETA);

        String name = force.getParameterName(index);
        float theta = BackendConstants.DEFAULT_THETA;
        LOGGER.info("setting param: " + name + " to " + theta);
        force.setParameter(index, theta);
    }

    //until i read the links on what this is, im leaving it as the default
    private void editDistance(NBodyForce force) {
        int index = BackendConstants.NBODY_FORCE_MIN_DISTANCE_PARAM_INDEX;
        force.setMinValue(index, BackendConstants.MIN_DISTANCE);
        force.setMaxValue(index, BackendConstants.MAX_DISTANCE);

        String name = force.getParameterName(index);
        float distance = BackendConstants.DEFAULT_DISTANCE;
        LOGGER.info("setting param: " + name + " to " + distance);
        force.setParameter(index, distance);
    }

    //until i read the links on what this is, im leaving it as the default
    private void editGravity(NBodyForce force) {
        int index = BackendConstants.NBODY_FORCE_GRAV_CONSTANT_PARAM_INDEX;
        force.setMaxValue(index, BackendConstants.MAX_GRAV_CONSTANT);
        force.setMinValue(index, BackendConstants.MIN_GRAV_CONSTANT);

        String name = force.getParameterName(index);
        float gravity = BackendConstants.DEFAULT_GRAV_CONSTANT;
        LOGGER.info("setting param: " + name + " to " + gravity);
        force.setParameter(BackendConstants.NBODY_FORCE_GRAV_CONSTANT_PARAM_INDEX, gravity);
    }

    //the drag coeff is calculated by determining the percentage of nodes are in the displayed graph against
    //the number of nodes in the graph before it was filtered by the predicate, then taking 1-that percentage
    //and interpolating it between the max and min values defined in the backend constants file
    private void editDragForce(DragForce force) {
        LOGGER.info("Editing drag force");
        int index = BackendConstants.DRAG_FORCE_COEFF_PARAM_INDEX;
        force.setMinValue(index, BackendConstants.MIN_DRAG_COEFF);
        force.setMaxValue(index, BackendConstants.MAX_DRAG_COEFF);

        String name = force.getParameterName(index);
        int maxNodeCount = PrefuseGraphBuilder.getBuilder().getMaxNodeCount();
        int currentNodeCount = PrefuseGraphBuilder.getBuilder().getCurrentNodeCount();
        float pct = 1f - (float) currentNodeCount / (float) maxNodeCount;

        //set the drag coefficient
        float coeff = interpolate(BackendConstants.MIN_DRAG_COEFF, BackendConstants.MAX_DRAG_COEFF, pct);
        LOGGER.info("setting param: " + name + " to " + coeff);
        force.setParameter(index, coeff);
    }

    //finds the maximum degree over all nodes in the graph
    //and stores it in instance variables
    private void computeMaxDegree() {
        this.minDegree = 1;
        this.maxDegree = 1;
        Iterator<Node> nodeIterator = this.graph.nodes();
        while (nodeIterator.hasNext()) {
            Node node = nodeIterator.next();
            int degree = node.getDegree();
            if (degree > this.maxDegree) {
                this.maxDegree = degree;
            }
        }
    }

    //spring length is similarly calculated to spring coeff, except higher edges connecting to higher degree
    //nodes have a longer spring length (inverse of how coeff is calculated)
    @Override
    protected float getSpringLength(EdgeItem e) {
        int degree = e.getInt(BackendConstants.MAX_DEGREE);
        float pct = ((degree - this.minDegree) / ((float) (this.maxDegree - this.minDegree)));
        return interpolate(BackendConstants.MIN_SPRING_LENGTH, BackendConstants.MAX_SPRING_LENGTH, pct);
    }

    //the coeff for a given spring in the graph is calculated by determining the maximum between
    //the degree of the source node and target node that are attached to the spring, then determining
    //the proportion that that number is compared to the node in the graph with maximum degree, then interpolating
    //1-that percentage between the max and min constants defined below
    @Override
    protected float getSpringCoefficient(EdgeItem e) {
        int degree = e.getInt(BackendConstants.MAX_DEGREE);
        float pct = 1f - ((degree - this.minDegree) / ((float) (this.maxDegree - this.minDegree)));
        LOGGER.debug("degree: " + degree + " -- pct: " + (100 * pct));
        return interpolate(BackendConstants.MIN_SPRING_COEFF, BackendConstants.MAX_SPRING_COEFF, pct);
    }

    private float interpolate(float min, float max, float pct) {
        return ((max - min) * pct) + min;
    }

    public void run(double frac) {
        setSpringsMaxMin();
        super.run(frac);
    }

    private void setSpringsMaxMin() {
        Force[] forces = this.getForceSimulator().getForces();
        for (Force force : forces) {
            if (force instanceof SpringForce) {
                SpringForce springForce = (SpringForce) force;

                int coeff_index = BackendConstants.SPRING_COEFF_PARAM_INDEX;
                int length_index = BackendConstants.SPRING_LENGTH_PARAM_INDEX;

                springForce.setMaxValue(coeff_index, BackendConstants.MAX_SPRING_COEFF);
                springForce.setMinValue(coeff_index, BackendConstants.MIN_SPRING_COEFF);

                springForce.setMaxValue(length_index, BackendConstants.MAX_SPRING_LENGTH);
                springForce.setMinValue(length_index, BackendConstants.MIN_SPRING_LENGTH);
            }
        }
    }

}
