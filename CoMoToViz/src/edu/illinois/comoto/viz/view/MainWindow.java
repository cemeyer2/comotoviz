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

import edu.illinois.comoto.api.object.Assignment;
import edu.illinois.comoto.viz.controller.ActionListenerFactory;
import edu.illinois.comoto.viz.controller.EventListenerFactory;
import edu.illinois.comoto.viz.controller.KeyListenerFactory;
import edu.illinois.comoto.viz.controller.WindowListenerFactory;
import edu.illinois.comoto.viz.utility.Pair;
import edu.illinois.comoto.viz.view.graph.GraphPanel;
import org.apache.log4j.Logger;
import prefuse.visual.NodeItem;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;
import java.util.Iterator;


public class MainWindow extends JFrame {

    // GUI elements
    private GraphPanel graphPanel;
    private ControlsPanel rightControls;
    private AssignmentChooserPanel leftControls;
    private JTextField searchBox;

    private static Logger logger = Logger.getLogger(MainWindow.class);

    public MainWindow(Pair<String, String> activeDirectoryCredentials) {

        // Add the primary GUI panes
        Container visualMoss = getContentPane();
        visualMoss.setLayout(new BorderLayout());
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        // Get the current screen size
        Dimension screenSize = toolkit.getScreenSize();
        visualMoss.setPreferredSize(new Dimension((int) screenSize.getWidth() - 75, (int) screenSize.getHeight() - 75));

        // Change the program icon
        setIconImage(BackendConstants.PROGRAM_IMAGE);

        // Set the title
        this.setTitle(FrontendConstants.PROGRAM_TITLE);

        // Add the controls and graph
        graphPanel = new GraphPanel(768, 768);
        rightControls = new ControlsPanel();
        rightControls.initialize(graphPanel);
        leftControls = new AssignmentChooserPanel(this, graphPanel, activeDirectoryCredentials);
        leftControls.setPreferredSize(new Dimension(200, 768));
        graphPanel.setAssignmentChooserPanel(leftControls);
        visualMoss.add(graphPanel, BorderLayout.CENTER);
        visualMoss.add(leftControls, BorderLayout.WEST);
        visualMoss.add(rightControls, BorderLayout.EAST);

        // Add extra GUI elements (search box, menu bar)
        addSearchBox();
        addMenuBar();

        // Run this graph layout
//        container.getGraphDisplay().run();

        // Add action listener for the window
        EventListenerFactory windowListenerFactory = new WindowListenerFactory();
        this.addWindowListener((WindowListener) windowListenerFactory.getEventListener(BackendConstants.MAIN_WINDOW));
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    /**
     * Helper function to update the title of the window to show that we have a particular assignment open
     *
     * @param assignment The assignment currently open, whose data to add to the title
     */
    public void updateTitle(Assignment assignment) {
        this.setTitle(FrontendConstants.PROGRAM_TITLE + ": " + assignment.getCourse().getName() + ", " + assignment.getName());
    }

    /**
     * Build the search box
     */
    public void addSearchBox() {

        // Add the GUI components & panels
        Container visualMoss = getContentPane();
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        JLabel searchLabel = new JLabel(FrontendConstants.SEARCH_FOR_STUDENT + ": ");
        searchLabel.setFont(BackendConstants.COMPONENT_LABEL_FONT);
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchBox = new JTextField();
        searchPanel.add(searchBox);

        // Create a key listener for this search box
        EventListenerFactory keyListenerFactory = new KeyListenerFactory();
        searchBox.addKeyListener((KeyListener) keyListenerFactory.getEventListener(BackendConstants.SEARCH_STUDENTS, this));

        // Add the search box
        visualMoss.add(searchPanel, BorderLayout.NORTH);
    }

    /**
     * Search the graph for students via the text box
     *
     * @return Whether or not the student was found
     */
    public boolean searchStudents() {

        // Get the text that's now in the box
        graphPanel.clearMessage();
        String searchText = searchBox.getText();

        // Clear the box background if it's empty
        if (graphPanel == null || searchText.length() == 0) {
            searchBox.setBackground(Color.white);
            return false;
        }

        // If the graph is empty (i.e. no graph loaded)
        Iterator<NodeItem> iterator = graphPanel.getCurrentGraphDisplay().getVisualization().items(BackendConstants.GRAPH + "." + BackendConstants.NODES);
        if (iterator == null) {
            searchBox.setBackground(Color.white);
            return false;
        }

        // Look for the student in the graph
        while (iterator.hasNext()) {
            NodeItem node = iterator.next();
            String netid = node.getString(BackendConstants.NETID);
            if (netid.startsWith(searchText)) {

                // Pan to this node if we found it
                graphPanel.panToNode(netid, 500);
                searchBox.setBackground(Color.green);
                graphPanel.setMessage("Centered on node \"" + netid + "\".");

                // We found it
                return true;
            }
        }
        searchBox.setBackground(Color.red);
        return false;
    }

    /**
     * Build the GUI components for the menu bar and add event listeners
     */
    public void addMenuBar() {

        //Add menu bar
        JMenuBar menu = new JMenuBar();

        // Top level menus
        JMenu fileMenu = new JMenu(FrontendConstants.FILE);
        fileMenu.setFont(BackendConstants.COMPONENT_LABEL_FONT);
        JMenu helpMenu = new JMenu(FrontendConstants.HELP);
        helpMenu.setFont(BackendConstants.COMPONENT_LABEL_FONT);

        // Menu items
        JMenuItem exportGraph = new JMenuItem(FrontendConstants.EXPORT_GRAPH_ML);
        JMenuItem exportImage = new JMenuItem(FrontendConstants.EXPORT_IMAGE);
        JMenuItem quit = new JMenuItem(FrontendConstants.QUIT);
        JMenuItem help = new JMenuItem(FrontendConstants.HELP);
        JMenuItem about = new JMenuItem(FrontendConstants.ABOUT);

        exportGraph.setFont(BackendConstants.COMPONENT_LABEL_FONT);
        exportImage.setFont(BackendConstants.COMPONENT_LABEL_FONT);
        quit.setFont(BackendConstants.COMPONENT_LABEL_FONT);
        help.setFont(BackendConstants.COMPONENT_LABEL_FONT);
        about.setFont(BackendConstants.COMPONENT_LABEL_FONT);

        //File Menu
        menu.add(fileMenu);
        //fileMenu.add(add);
        fileMenu.add(exportGraph);
        fileMenu.add(exportImage);
        fileMenu.add(quit);

        //Help Menu
        menu.add(helpMenu);
        helpMenu.add(help);
        helpMenu.add(about);

        // Use the action listener factory to create the actions for each item
        EventListenerFactory actionListenerFactory = new ActionListenerFactory();
        exportGraph.addActionListener((ActionListener) actionListenerFactory.getEventListener(BackendConstants.EXPORT_GRAPH, this));
        exportImage.addActionListener((ActionListener) actionListenerFactory.getEventListener(BackendConstants.EXPORT_IMAGE, this));
        quit.addActionListener((ActionListener) actionListenerFactory.getEventListener(BackendConstants.QUIT));
        about.addActionListener((ActionListener) actionListenerFactory.getEventListener(BackendConstants.ABOUT));
        help.addActionListener((ActionListener) actionListenerFactory.getEventListener(BackendConstants.HELP));

        this.setJMenuBar(menu);
    }

    /**
     * Change the assignment of the graph
     *
     * @param id The id of the new assignment
     */
    public void changeAssignment(int id) {
        DefaultMutableTreeNode node = leftControls.getAssignmentTreeNodeById(id);
        if (node != null) {
            leftControls.changeAssignment(node);
        } else {
            JOptionPane.showInputDialog(this, FrontendConstants.ASSIGNMENT_DOES_NOT_EXIST_MESSAGE, FrontendConstants.GENERIC_ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
        }
    }

    public ControlsPanel getControlsPanel() {
        return rightControls;
    }

    public AssignmentChooserPanel getAssignmentChooserPanel() {
        return leftControls;
    }

    public GraphPanel getGraphPanel() {
        return this.graphPanel;
    }
}
