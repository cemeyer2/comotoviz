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

import edu.illinois.comoto.api.object.*;
import edu.illinois.comoto.viz.controller.EventListenerFactory;
import edu.illinois.comoto.viz.controller.MouseListenerFactory;
import edu.illinois.comoto.viz.model.DataImport;
import edu.illinois.comoto.viz.model.VisualMossGraphDisplayContainer;
import edu.illinois.comoto.viz.utility.AssignmentLoadingWorker;
import edu.illinois.comoto.viz.utility.Pair;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;


/**
 * The tree-based structure for choosing classes and assignments
 */
public class AssignmentChooser extends JPanel {

    // The list of courses to choose from
    private final List<Course> courses;

    // GUI components
    private JTree tree;
    private DataImport importer;
    private VisualMossGraphDisplayContainer display;
    private VisualMossLayout frame;
    private ArrayList<DefaultMutableTreeNode> assignmentNodes;

    // Load the tree node icons
    private Icon openIcon = new ImageIcon(BackendConstants.OPEN_NODE_ICON_PATH);
    private Icon closedIcon = new ImageIcon(BackendConstants.CLOSED_NODE_ICON_PATH);
    private Icon leafIcon = new ImageIcon(BackendConstants.LEAF_NODE_ICON_PATH);

    /**
     * Builds this tree structure given the credentials, and parent GUI elements
     *
     * @param parentFrame                Parent GUI frame
     * @param parentContainer            Parent GUI container
     * @param activeDirectoryCredentials AD credentials from login dialog
     */
    public AssignmentChooser(VisualMossLayout parentFrame, VisualMossGraphDisplayContainer parentContainer,
                             Pair<String, String> activeDirectoryCredentials) {
        super();
        this.importer = new DataImport(activeDirectoryCredentials);
        this.courses = importer.getCourses();
        this.display = parentContainer;
        this.frame = parentFrame;
        this.assignmentNodes = new ArrayList<DefaultMutableTreeNode>();
        initialize();
    }

    /**
     * Initialize the tree structure
     */
    private void initialize() {
        this.setLayout(new BorderLayout());
        DefaultMutableTreeNode coursesNode = new DefaultMutableTreeNode(FrontendConstants.COURSES);
        for (Course course : courses) {
            DefaultMutableTreeNode courseNode = new DefaultMutableTreeNode(course.getName());
            coursesNode.add(courseNode);

            // Add nodes for each class, and each offering of each class
            for (Offering offering : course.getOfferings()) {

                // Add this semester node
                Semester semester = offering.getSemester();
                DefaultMutableTreeNode semesterNode = new DefaultMutableTreeNode(semester);

                // For each assignment in this course during this semester, add it to the semester node
                for (Assignment assignment : course.getAssignments()) {
                    if (assignment.getYear() > 0) {
                        if (assignment.getYear() == semester.getYear() && assignment.getSeason() == semester.getSeason()) {
                            DefaultMutableTreeNode assignmentNode = new DefaultMutableTreeNode(assignment);
                            semesterNode.add(assignmentNode);
                            assignmentNodes.add(assignmentNode);
                        }
                    }
                }

                // If there are some assignments under this semester, add it
                if (semesterNode.getChildCount() > 0) {
                    courseNode.add(semesterNode);
                }
            }
        }

        // Add the root node and set GUI properties
        tree = new JTree(coursesNode);
        tree.setFont(BackendConstants.COMPONENT_LABEL_FONT);


        // Setup the listener
        EventListenerFactory actionFactory = new MouseListenerFactory();
        tree.addMouseListener((MouseListener) actionFactory.getEventListener(BackendConstants.ASSIGNMENT_CHOOSER, this));

        // Set custom icons for the tree
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setOpenIcon(openIcon);
        renderer.setClosedIcon(closedIcon);
        renderer.setLeafIcon(leafIcon);
        tree.setCellRenderer(renderer);

        // Add scrollbars to this panel
        JScrollPane pane = new JScrollPane(tree);
        this.add(pane, BorderLayout.CENTER);
    }

    /**
     * On some user input event to the tree
     *
     * @param arg0 the event that occurred
     */
    public void actionPerformed(ActionEvent arg0) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
        changeAssignment(node);
    }

    /**
     * Find the node of the tree given the id
     *
     * @param id The id of the assignment to find
     * @return The tree node corresponding to this assignment
     */
    public DefaultMutableTreeNode getAssignmentTreeNodeById(int id) {
        DefaultMutableTreeNode retval = null;
        for (DefaultMutableTreeNode node : assignmentNodes) {
            if (((Assignment) node.getUserObject()).getId() == id) {
                retval = node;
                break;
            }
        }
        return retval;
    }

    /**
     * Changes the assignment of the graph, given the node clicked
     *
     * @param node The node on which the user clicked
     */
    public void changeAssignment(DefaultMutableTreeNode node) {

        Object object = node.getUserObject();
        if (object instanceof Assignment) {
            for (DefaultMutableTreeNode assignmentNode : assignmentNodes) {
                assignmentNode.removeAllChildren();
            }
            Assignment selectedAssignment = (Assignment) object;
            AssignmentLoadingWorker worker = new AssignmentLoadingWorker(selectedAssignment, display, frame, importer, tree, node);
            worker.execute();
        } else if (object instanceof Student) {
            Student student = (Student) object;
            display.getVisualMossGraphDisplay().panToNode(student.getNetid(), 5000);
        }
    }

    public JTree getTree() {
        return tree;
    }
}
