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

import org.apache.log4j.Logger;
import prefuse.Display;
import prefuse.Visualization;

import java.util.List;

/**
 * Author:  Charlie Meyer <cemeyer2@illinois.edu>
 * Date:    3/15/11
 * Time:    5:19 PM
 * Package: edu.illinois.comoto.viz.view.graph
 * Created by IntelliJ IDEA.
 */
public class GraphDisplay extends Display {

    private Visualization visualization;
    private List<String> visualizationActions;
    private static final Logger LOGGER = Logger.getLogger(GraphDisplay.class);

    protected GraphDisplay(Visualization visualization, List<String> visualizationActions) {
        super(visualization);
        this.visualization = visualization;
        this.visualizationActions = visualizationActions;
    }

    protected void runAllActions() {
        LOGGER.info("Running all actions");
        if (this.visualizationActions != null && this.visualization != null) {
            for (String action : visualizationActions) {
                LOGGER.info("Running action: " + action);
                this.visualization.run(action);
            }
        }
    }

    protected void runAction(String action) {
        if (this.visualizationActions != null && this.visualization != null) {
            if (this.visualizationActions.contains(action)) {
                LOGGER.info("Running action: " + action);
                this.visualization.run(action);
            } else {
                LOGGER.error("Tried to run action '" + action + "' that is not loaded into this visualization");
            }
        }
    }

}
