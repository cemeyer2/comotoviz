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

package edu.illinois.comoto.viz.controller;

import edu.illinois.comoto.viz.view.BackendConstants;
import edu.illinois.comoto.viz.view.FrontendConstants;
import edu.illinois.comoto.viz.view.graph.GraphPanel;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * User: Jon
 * Date: 3/30/11
 * <p/>
 * <p/>
 * Stores all change listeners
 */
public enum ChangeListenerActions {

    // When the zoom level is changed
    zoom {
        @Override
        ChangeListener getChangeListenerAction(final Object... parameters) {

            // Pull some of the GUI components from the parameters
            final JSlider zoomSlider = (JSlider) parameters[0];
            final GraphPanel graphPanel = (GraphPanel) parameters[1];
            final JPanel controlsPanel = (JPanel) parameters[2];

            return new ChangeListener() {
                private double oldVal = 50;
                private double newVal;

                public void stateChanged(ChangeEvent e) {
                    TitledBorder bb = BorderFactory.createTitledBorder(FrontendConstants.ZOOM + ": " + zoomSlider.getValue() + "%");
                    bb.setTitleFont(BackendConstants.COMPONENT_LABEL_FONT);
                    zoomSlider.setBorder(bb);
                    if ((this.newVal = zoomSlider.getValue()) == 0.0) {
                        this.newVal = 0.001;
                    }
                    graphPanel.setZoom(this.newVal / this.oldVal);
                    this.oldVal = this.newVal;
                }
            };
        }
    },

    // When the edge threshold is changed
    threshold {
        @Override
        ChangeListener getChangeListenerAction(final Object... parameters) {

            // Pull some of the GUI components from the parameters
            final JSlider threshholdSlider = (JSlider) parameters[0];
            final GraphPanel graphPanel = (GraphPanel) parameters[1];

            return new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    TitledBorder bb = BorderFactory.createTitledBorder(FrontendConstants.MINIMUM_EDGE_WEIGHT + ": " + threshholdSlider.getValue());
                    bb.setTitleFont(BackendConstants.COMPONENT_LABEL_FONT);
                    threshholdSlider.setBorder(bb);
                }
            };
        }
    };

    abstract ChangeListener getChangeListenerAction(Object... parameters);
}
