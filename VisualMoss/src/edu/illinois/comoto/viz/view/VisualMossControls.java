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

import edu.illinois.comoto.viz.model.VisualMossGraphDisplay;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;

public class VisualMossControls extends JPanel {
    private static final long serialVersionUID = 1L;

    public void addVisualMossControls(final VisualMossGraphDisplay graphDisplay) {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        final JCheckBox pastStudentButton, singletonsButton, partnersButton, solutionButton, anonymousButton;
        final JSlider threshholdSlider, zoomSlider;
        final JButton textReportButton;

        //Threshold Slider
        threshholdSlider = new JSlider();
        threshholdSlider.setValue(0);//show all edges by default on load
        threshholdSlider.setFont(BackendConstants.COMPONENT_LABEL_FONT);
        final TitledBorder b0 = BorderFactory.createTitledBorder("Minimum Edge Weight: "
                + threshholdSlider.getValue());
        b0.setTitleFont(BackendConstants.COMPONENT_LABEL_FONT);
        threshholdSlider.setBorder(b0);
        threshholdSlider.setMajorTickSpacing(20);
        threshholdSlider.setMinorTickSpacing(10);
        threshholdSlider.setPaintTicks(true);
        threshholdSlider.setPaintLabels(true);
        threshholdSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                //b0.setTitle("Minimum Edge Weight: "+ threshholdSlider.getValue());
                TitledBorder bb = BorderFactory.createTitledBorder("Minimum Edge Weight: " + threshholdSlider.getValue());
                bb.setTitleFont(BackendConstants.COMPONENT_LABEL_FONT);
                threshholdSlider.setBorder(bb);
                graphDisplay.setMinimumEdgeWeightToDisplay(threshholdSlider.getValue());
            }
        });
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(threshholdSlider, c);

        //Zoom Slider
        zoomSlider = new JSlider(0, 100);
        zoomSlider.setFont(BackendConstants.COMPONENT_LABEL_FONT);
        final TitledBorder b1 = BorderFactory.createTitledBorder("Zoom: "
                + zoomSlider.getValue() + "%");
        b1.setTitleFont(BackendConstants.COMPONENT_LABEL_FONT);
        zoomSlider.setBorder(b1);
        zoomSlider.setMajorTickSpacing(25);
        zoomSlider.setMinorTickSpacing(5);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setPaintLabels(true);
        zoomSlider.addChangeListener(new ChangeListener() {
            double oldVal = 50;
            double newVal;

            public void stateChanged(ChangeEvent e) {
                TitledBorder bb = BorderFactory.createTitledBorder("Zoom: " + zoomSlider.getValue() + "%");
                bb.setTitleFont(BackendConstants.COMPONENT_LABEL_FONT);
                zoomSlider.setBorder(bb);
                if ((newVal = zoomSlider.getValue()) == 0.0)
                    newVal = 0.001;
                graphDisplay.setZoom(newVal / oldVal);
                oldVal = newVal;
            }
        });
        c.gridy = 1;
        this.add(zoomSlider, c);

        pastStudentButton = new JCheckBox("Include past students");
        pastStudentButton.setFont(BackendConstants.COMPONENT_LABEL_FONT);
        pastStudentButton.setSelected(false);
        pastStudentButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                graphDisplay.setIncludePast(pastStudentButton.isSelected());
            }
        });
        c.gridy = 2;
        this.add(pastStudentButton, c);

        singletonsButton = new JCheckBox("Include Singletons");
        singletonsButton.setFont(BackendConstants.COMPONENT_LABEL_FONT);
        singletonsButton.setSelected(true);
        singletonsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (singletonsButton.isSelected())
                    graphDisplay.setShowSingletons(true);
                else
                    graphDisplay.setShowSingletons(false);
            }
        });
        c.gridy = 3;
        this.add(singletonsButton, c);

        partnersButton = new JCheckBox("Include Partner Edges");
        partnersButton.setFont(BackendConstants.COMPONENT_LABEL_FONT);
        partnersButton.setSelected(false);
        partnersButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                graphDisplay.setIncludePartners(partnersButton.isSelected());
            }
        });
        c.gridy = 4;
        this.add(partnersButton, c);

        solutionButton = new JCheckBox("Include Solution");
        solutionButton.setFont(BackendConstants.COMPONENT_LABEL_FONT);
        solutionButton.setSelected(true);
        solutionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (solutionButton.isSelected())
                    graphDisplay.setShowSolution(true);
                else
                    graphDisplay.setShowSolution(false);
            }
        });
        c.gridy = 5;
        this.add(solutionButton, c);

        anonymousButton = new JCheckBox("Anonymous Graph");
        anonymousButton.setFont(BackendConstants.COMPONENT_LABEL_FONT);
        anonymousButton.setSelected(true);
        graphDisplay.setAnonymous(true);
        anonymousButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (anonymousButton.isSelected())
                    graphDisplay.setAnonymous(true);
                else
                    graphDisplay.setAnonymous(false);
            }
        });
        c.gridy = 6;
        this.add(anonymousButton, c);

        textReportButton = new JButton("Launch Text Report");
        textReportButton.setFont(BackendConstants.COMPONENT_LABEL_FONT);
        c.weightx = 0.1;
        c.weighty = 0.1;
        c.ipady = 32;
        c.gridy = 7;
        textReportButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    new WebpageDialog(graphDisplay.getReportURL(), true);
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        this.add(textReportButton, c);

    }
}
