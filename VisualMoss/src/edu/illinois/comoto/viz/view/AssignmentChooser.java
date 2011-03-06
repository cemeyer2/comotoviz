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

import edu.illinois.comoto.api.DataImport;
import edu.illinois.comoto.api.object.Assignment;
import edu.illinois.comoto.api.object.Course;
import edu.illinois.comoto.api.object.Student;
import edu.illinois.comoto.viz.model.VisualMossGraphDisplayContainer;
import edu.illinois.comoto.viz.utility.AssignmentLoadingWorker;
import edu.illinois.comoto.viz.utility.Pair;
import prefuse.data.io.DataIOException;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AssignmentChooser extends JPanel implements ActionListener, MouseListener {
    private final List<Course> courses;
    private Assignment selectedAssignment;

    private JButton select;
    private JTree tree;
    private JScrollPane pane;
    private DataImport importer;
    private VisualMossGraphDisplayContainer display;
    private VisualMossLayout frame;
    private ArrayList<DefaultMutableTreeNode> assignmentNodes;

    private Icon openIcon = new ImageIcon("VisualMoss/blue_node.png");
    private Icon closedIcon = new ImageIcon("VisualMoss/gray_node.png");
    private Icon leafIcon = new ImageIcon("VisualMoss/small_gray_node.png");


    public AssignmentChooser(VisualMossLayout frame, VisualMossGraphDisplayContainer display, Pair<String, String> activeDirectoryCredentials) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, DataIOException {
        super();
        this.importer = new DataImport(activeDirectoryCredentials);
        this.courses = importer.getCourses();
        this.display = display;
        this.frame = frame;
        this.assignmentNodes = new ArrayList<DefaultMutableTreeNode>();
        init();
    }

    private void init() {
        this.setLayout(new BorderLayout());

        DefaultMutableTreeNode coursesNode = new DefaultMutableTreeNode("Courses");
        for (Course course : courses) {
            DefaultMutableTreeNode courseNode = new DefaultMutableTreeNode(course.getName());
            coursesNode.add(courseNode);


            for (Assignment assignment : course.getAssignments()) {
                DefaultMutableTreeNode assignmentNode = new DefaultMutableTreeNode(assignment);
                courseNode.add(assignmentNode);
                assignmentNodes.add(assignmentNode);
            }
        }

        tree = new JTree(coursesNode);
        tree.setFont(BackendConstants.COMPONENT_LABEL_FONT);
        tree.addMouseListener(this);

        // Set custom icons for the tree
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setOpenIcon(openIcon);
        renderer.setClosedIcon(closedIcon);
        renderer.setLeafIcon(leafIcon);
        tree.setCellRenderer(renderer);


        pane = new JScrollPane(tree);
        this.add(pane, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent arg0) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
        changeAssignment(node);
    }

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

    public void changeAssignment(DefaultMutableTreeNode node) {

        Object obj = node.getUserObject();
        if (obj instanceof Assignment) {
            for (DefaultMutableTreeNode assignmentNode : assignmentNodes) {
                assignmentNode.removeAllChildren();
            }
            selectedAssignment = (Assignment) obj;
            AssignmentLoadingWorker worker = new AssignmentLoadingWorker(selectedAssignment, display, frame, importer, tree, node);
            worker.execute();
//            worker.runSynchronous();
        } else if (obj instanceof Student) {
            Student stu = (Student) obj;
            display.getVisualMossGraphDisplay().panToNode(stu.getNetid(), 5000);
        }
    }

    public Assignment getSelectedAssignment() {
        return this.selectedAssignment;
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() > 1) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            changeAssignment(node);
        }
    }

    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }
}
