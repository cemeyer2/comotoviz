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

package edu.uiuc.cs.visualmoss.gui.graph;

import edu.uiuc.cs.visualmoss.VisualMossConstants;
import edu.uiuc.cs.visualmoss.dataimport.DataImport;
import edu.uiuc.cs.visualmoss.graph.VisualMossGraph;
import prefuse.data.io.DataIOException;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class VisualMossGraphDisplayDriver {
    public static void main(String[] args) throws DataIOException, InterruptedException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        VisualMossGraph graph = new DataImport(new File("graph.xml")).getVisualMossGraph();
        VisualMossGraphDisplayContainer container = new VisualMossGraphDisplayContainer(graph, 1000, 1000);

        JFrame frame = new JFrame("prefuse example");
        //ensure application exits when window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(container);
        frame.pack();           // layout components in window
        frame.setVisible(true); // show the window

        container.getVisualMossGraphDisplay().run();

        createSimpleControlPanel(container.getVisualMossGraphDisplay());

    }

    public static void createSimpleControlPanel(final VisualMossGraphDisplay graphDisplay) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 400));
        panel.setLayout(new GridLayout(8, 1));

        final JSlider slider = new JSlider(0, 100);
        slider.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
        TitledBorder b = BorderFactory.createTitledBorder("Minimum Edge Weight");
        b.setTitleFont(VisualMossConstants.COMPONENT_LABEL_FONT);
        slider.setBorder(b);
        slider.setMajorTickSpacing(25);
        slider.setMinorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setValue(0);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                graphDisplay.setMinimumEdgeWeightToDisplay(slider.getValue());
            }
        });
        panel.add(slider);

        final JSlider slider2 = new JSlider(0, 100);
        TitledBorder b2 = BorderFactory.createTitledBorder("Rotate");
        b2.setTitleFont(VisualMossConstants.COMPONENT_LABEL_FONT);
        slider2.setBorder(b2);
        slider2.setMajorTickSpacing(25);
        slider2.setMinorTickSpacing(10);
        slider2.setPaintTicks(false);
        slider2.setPaintLabels(false);
        slider2.setValue(50);
        slider2.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                double value = (Math.PI * 2) * ((slider2.getValue() - 50d) / 50d);
                graphDisplay.rotate(value);
            }
        });
        panel.add(slider2);

        final JSlider slider3 = new JSlider(0, 100);
        TitledBorder b3 = BorderFactory.createTitledBorder("Zoom");
        b3.setTitleFont(VisualMossConstants.COMPONENT_LABEL_FONT);
        slider3.setBorder(b3);
        slider3.setMajorTickSpacing(25);
        slider3.setMinorTickSpacing(10);
        slider3.setPaintTicks(false);
        slider3.setPaintLabels(false);
        slider3.setValue(50);
        slider3.addChangeListener(new ChangeListener() {

            int last = 50;

            public void stateChanged(ChangeEvent e) {
                if (slider3.getValue() > last) {
                    graphDisplay.setZoom(1.15);
                } else {
                    graphDisplay.setZoom(0.85);
                }
                last = slider3.getValue();
            }
        });
        panel.add(slider3);

        final JCheckBox checkbox = new JCheckBox("show singletons?");
        checkbox.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
        checkbox.setSelected(true);
        checkbox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                graphDisplay.setShowSingletons(checkbox.isSelected());
            }
        });
        panel.add(checkbox);

        final JCheckBox checkbox2 = new JCheckBox("show solution?");
        checkbox2.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
        checkbox2.setSelected(true);
        checkbox2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                graphDisplay.setShowSolution(checkbox2.isSelected());
            }
        });
        panel.add(checkbox2);

        final JCheckBox checkbox3 = new JCheckBox("anonymous?");
        checkbox3.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
        checkbox3.setSelected(false);
        checkbox3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                graphDisplay.setAnonymous(checkbox3.isSelected());
            }
        });
        panel.add(checkbox3);

        final JButton button = new JButton("write to file");
        button.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    graphDisplay.writeToImage(new File("graph.png"));
                } catch (Exception ex) {

                }
            }
        });
        panel.add(button);

        final JButton button2 = new JButton("pan to solution");
        button2.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
        button2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    graphDisplay.panToNode(VisualMossConstants.SOLUTION_NODE_LABEL, 3000);
                } catch (Exception ex) {

                }
            }
        });
        panel.add(button2);

        JFrame frame = new JFrame("controls");
        frame.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
