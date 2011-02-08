package edu.uiuc.cs.visualmoss.gui.worker;

import edu.uiuc.cs.visualmoss.dataimport.DataImport;
import edu.uiuc.cs.visualmoss.dataimport.api.objects.Assignment;
import edu.uiuc.cs.visualmoss.graph.VisualMossGraph;
import edu.uiuc.cs.visualmoss.graph.VisualMossGraphStudent;
import edu.uiuc.cs.visualmoss.gui.graph.VisualMossGraphDisplayContainer;
import edu.uiuc.cs.visualmoss.gui.layout.VisualMossLayout;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class AssignmentLoadingWorker extends SwingWorker<Void, Void> {

    private Assignment assignment;
    private VisualMossGraphDisplayContainer container;
    private VisualMossLayout frame;
    private DataImport importer;
    private JTree tree;
    private DefaultMutableTreeNode node;

    public AssignmentLoadingWorker(Assignment assignment, VisualMossGraphDisplayContainer container, VisualMossLayout frame, DataImport importer, JTree tree, DefaultMutableTreeNode node) {
        this.assignment = assignment;
        this.container = container;
        this.frame = frame;
        this.importer = importer;
        this.tree = tree;
        this.node = node;
    }

    @Override
    protected Void doInBackground() throws Exception {

        VisualMossGraph graph = importer.buildGraph(assignment, true, frame);

        //add students as children
        for (VisualMossGraphStudent student : graph.getStudents()) {
            node.add(new DefaultMutableTreeNode(student));
        }
        ((DefaultTreeModel) tree.getModel()).reload();

        container.changeGraph(graph);
        frame.updateTitle(assignment);
        frame.searchStudents();

        return null;
    }

}
