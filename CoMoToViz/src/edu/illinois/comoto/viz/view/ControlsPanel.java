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

package edu.illinois.comoto.viz.view;

import edu.illinois.comoto.viz.controller.ActionListenerFactory;
import edu.illinois.comoto.viz.controller.EventListenerFactory;
import edu.illinois.comoto.viz.controller.MouseListenerFactory;
import edu.illinois.comoto.viz.view.graph.GraphPanel;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;

public class ControlsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ControlsPanel.class);

    public void initialize(final GraphPanel graphPanel) {

        // Initialize the layout
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        final JCheckBox pastStudentButton, singletonsButton, partnersButton, solutionButton, anonymousButton;
        final JSlider threshholdSlider, zoomSlider;
        final JButton textReportButton;

        // Get EventListener factories
        EventListenerFactory actionListenerFactory = new ActionListenerFactory();
        MouseListenerFactory mouseListenerFactory = new MouseListenerFactory();

        // Initialize the threshold Slider
        threshholdSlider = new JSlider();
        threshholdSlider.setValue(FrontendConstants.DEFAULT_MINIMUM_EDGE_WEIGHT);//show all edges by default on load
        threshholdSlider.setFont(BackendConstants.COMPONENT_LABEL_FONT);
        final TitledBorder b0 = BorderFactory.createTitledBorder(FrontendConstants.MINIMUM_EDGE_WEIGHT + ": "
                + threshholdSlider.getValue());
        b0.setTitleFont(BackendConstants.COMPONENT_LABEL_FONT);
        threshholdSlider.setBorder(b0);
        threshholdSlider.setMajorTickSpacing(20);
        threshholdSlider.setMinorTickSpacing(10);
        threshholdSlider.setPaintTicks(true);
        threshholdSlider.setPaintLabels(true);
        threshholdSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                TitledBorder bb = BorderFactory.createTitledBorder(FrontendConstants.MINIMUM_EDGE_WEIGHT + ": " + threshholdSlider.getValue());
                bb.setTitleFont(BackendConstants.COMPONENT_LABEL_FONT);
                threshholdSlider.setBorder(bb);
            }
        });
        threshholdSlider.addMouseListener(mouseListenerFactory.getEventListener(BackendConstants.THRESHOLD, threshholdSlider, graphPanel));
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(threshholdSlider, c);

        //Zoom Slider
        zoomSlider = new JSlider(0, 100);
        zoomSlider.setFont(BackendConstants.COMPONENT_LABEL_FONT);
        final TitledBorder b1 = BorderFactory.createTitledBorder(FrontendConstants.ZOOM + ": "
                + zoomSlider.getValue() + "%");
        b1.setTitleFont(BackendConstants.COMPONENT_LABEL_FONT);
        zoomSlider.setBorder(b1);
        zoomSlider.setMajorTickSpacing(25);
        zoomSlider.setMinorTickSpacing(5);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setPaintLabels(true);
        zoomSlider.addChangeListener(new ChangeListener() {
            private double oldVal = 50;
            private double newVal;

            public void stateChanged(ChangeEvent e) {
                TitledBorder bb = BorderFactory.createTitledBorder(FrontendConstants.ZOOM + ": " + zoomSlider.getValue() + "%");
                bb.setTitleFont(BackendConstants.COMPONENT_LABEL_FONT);
                zoomSlider.setBorder(bb);
                if ((newVal = zoomSlider.getValue()) == 0.0) {
                    newVal = 0.001;
                }
                graphPanel.setZoom(newVal / oldVal);
                oldVal = newVal;
            }
        });

        // Initialize the past students checkbox
        c.gridy = 1;
        this.add(zoomSlider, c);
        pastStudentButton = new JCheckBox(FrontendConstants.INCLUDE_PAST_STUDENTS);
        pastStudentButton.setFont(BackendConstants.COMPONENT_LABEL_FONT);
        pastStudentButton.setSelected(FrontendConstants.DEFAULT_INCLUDE_PAST_STUDENTS);
        pastStudentButton.addActionListener((ActionListener) actionListenerFactory.getEventListener(
                BackendConstants.PAST_STUDENTS, pastStudentButton, graphPanel));

        // Initialize the singletons checkbox
        c.gridy = 2;
        this.add(pastStudentButton, c);
        singletonsButton = new JCheckBox(FrontendConstants.INCLUDE_SINGLETONS);
        singletonsButton.setFont(BackendConstants.COMPONENT_LABEL_FONT);
        singletonsButton.setSelected(FrontendConstants.DEFAULT_SHOW_SINGLETONS);
        singletonsButton.addActionListener((ActionListener) actionListenerFactory.getEventListener(
                BackendConstants.SINGLETONS, singletonsButton, graphPanel));

        // Initialize the show partner edges checkbox
        c.gridy = 3;
        this.add(singletonsButton, c);
        partnersButton = new JCheckBox(FrontendConstants.INCLUDE_PARTNER_EDGES);
        partnersButton.setFont(BackendConstants.COMPONENT_LABEL_FONT);
        partnersButton.setSelected(FrontendConstants.DEFAULT_INCLUDE_PARTNERS);
        partnersButton.addActionListener((ActionListener) actionListenerFactory.getEventListener(
                BackendConstants.PARTNER_EDGES, partnersButton, graphPanel));

        // Initialize the show solution checkbox
        c.gridy = 4;
        this.add(partnersButton, c);
        solutionButton = new JCheckBox(FrontendConstants.INCLUDE_SOLUTION);
        solutionButton.setFont(BackendConstants.COMPONENT_LABEL_FONT);
        solutionButton.setSelected(FrontendConstants.DEFAULT_SHOW_SOLUTION);
        solutionButton.addActionListener((ActionListener) actionListenerFactory.getEventListener(
                BackendConstants.SHOW_SOLUTION, solutionButton, graphPanel));


        // Initialize the anonymous graph checkbox
        c.gridy = 5;
        this.add(solutionButton, c);
        anonymousButton = new JCheckBox(FrontendConstants.ANONYMOUS_GRAPH);
        anonymousButton.setFont(BackendConstants.COMPONENT_LABEL_FONT);
        anonymousButton.setSelected(FrontendConstants.DEFAULT_ANONYMOUS);
        solutionButton.addActionListener((ActionListener) actionListenerFactory.getEventListener(
                BackendConstants.ANONYMOUS_GRAPH, anonymousButton, graphPanel));

        // Initialize the 'launch text report' button
        c.gridy = 6;
        this.add(anonymousButton, c);
        textReportButton = new JButton(FrontendConstants.LAUNCH_TEXT_REPORT);
        textReportButton.setFont(BackendConstants.COMPONENT_LABEL_FONT);
        c.weightx = 0.1;
        c.weighty = 0.1;
        c.ipady = 32;
        c.gridy = 7;
        solutionButton.addActionListener((ActionListener) actionListenerFactory.getEventListener(
                BackendConstants.LAUNCH_TEXT_REPORT));
        this.add(textReportButton, c);
    }
}
