package edu.uiuc.cs.visualmoss.gui.worker;

import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.jdesktop.swingworker.SwingWorker;

import prefuse.data.io.DataIOException;
import edu.uiuc.cs.visualmoss.dataimport.Assignment;
import edu.uiuc.cs.visualmoss.dataimport.DataImport;
import edu.uiuc.cs.visualmoss.graph.VisualMossGraph;
import edu.uiuc.cs.visualmoss.graph.VisualMossGraph.Student;
import edu.uiuc.cs.visualmoss.gui.graph.VisualMossGraphDisplayContainer;
import edu.uiuc.cs.visualmoss.gui.layout.VisualMossLayout;

public class AssignmentLoadingWorker extends SwingWorker<Void, Void> {

	private Assignment assignment;
	private VisualMossGraphDisplayContainer container;
	private VisualMossLayout frame;
	private DataImport importer;
	private JTree tree;
	private DefaultMutableTreeNode node;
	
	public AssignmentLoadingWorker(Assignment assignment, VisualMossGraphDisplayContainer container, VisualMossLayout frame, DataImport importer, JTree tree, DefaultMutableTreeNode node)
	{
		this.assignment = assignment;
		this.container = container;
		this.frame = frame;
		this.importer = importer;
		this.tree = tree;
		this.node = node;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		try
		{
			VisualMossGraph graph = importer.buildGraph(assignment, true, frame);
			
			//add students as children
			for(Student s : graph.getStudents())
			{
				node.add(new DefaultMutableTreeNode(s));
			}
			((DefaultTreeModel)tree.getModel()).reload();
			
			container.changeGraph(graph);
			frame.updateTitle(assignment);
			frame.searchStudents();
		}
		//TODO: do this right:
		catch (InstantiationException e)
		{
			JOptionPane.showMessageDialog(null, "Can't instantiate graph", "Error", JOptionPane.ERROR_MESSAGE);
		}
		catch (IllegalAccessException e){JOptionPane.showMessageDialog(null, "Can't access graph data", "Error", JOptionPane.ERROR_MESSAGE);}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Error in the database", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		catch (ClassNotFoundException e){JOptionPane.showMessageDialog(null, "Can't find class", "Error", JOptionPane.ERROR_MESSAGE);}
		catch (DataIOException e){JOptionPane.showMessageDialog(null, "Can't output data", "Error", JOptionPane.ERROR_MESSAGE);}
	
		return null;
	}

}
