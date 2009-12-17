package edu.uiuc.cs.visualmoss.gui.dataimport;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SpringLayout;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import prefuse.data.io.DataIOException;
import edu.uiuc.cs.visualmoss.VisualMossConstants;
import edu.uiuc.cs.visualmoss.dataimport.Assignment;
import edu.uiuc.cs.visualmoss.dataimport.Course;
import edu.uiuc.cs.visualmoss.dataimport.DataImport;
import edu.uiuc.cs.visualmoss.graph.VisualMossGraph;
import edu.uiuc.cs.visualmoss.graph.VisualMossGraph.Student;
import edu.uiuc.cs.visualmoss.gui.graph.VisualMossGraphDisplayContainer;
import edu.uiuc.cs.visualmoss.gui.layout.VisualMossLayout;
import edu.uiuc.cs.visualmoss.gui.worker.AssignmentLoadingWorker;

public class AssignmentChooser extends JPanel implements ActionListener, MouseListener
{
	private final ArrayList<Course> courses;
	private Assignment selectedAssignment;
	
	private JButton select;
	private JTree tree;
	private JScrollPane pane;
	private DataImport importer;
	private VisualMossGraphDisplayContainer display;
	private VisualMossLayout frame;
	private ArrayList<DefaultMutableTreeNode> assignmentNodes;
	
	public AssignmentChooser(VisualMossLayout frame, VisualMossGraphDisplayContainer display) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, DataIOException
	{
		super();
		this.importer = new DataImport();
		this.courses = importer.getCourses();
		this.display = display;
		this.frame = frame;
		this.assignmentNodes = new ArrayList<DefaultMutableTreeNode>();
		init();
	}
	
	private void init()
	{
		this.setLayout(new BorderLayout());
		
		DefaultMutableTreeNode coursesNode = new DefaultMutableTreeNode("Courses");
		for(Course course : courses)
		{
			DefaultMutableTreeNode courseNode = new DefaultMutableTreeNode(course.getName());
			coursesNode.add(courseNode);
			for(Assignment assignment : course.getAssignments())
			{
				DefaultMutableTreeNode assignmentNode = new DefaultMutableTreeNode(assignment);
				courseNode.add(assignmentNode);
				assignmentNodes.add(assignmentNode);
			}
		}
		
		tree = new JTree(coursesNode);
		tree.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
		tree.addMouseListener(this);
		pane = new JScrollPane(tree);
		this.add(pane, BorderLayout.CENTER);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		changeAssignment();
	}
	
	public void changeAssignment()
	{
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
		Object obj = node.getUserObject();
		
		if(obj instanceof Assignment)
		{
			for(DefaultMutableTreeNode assignmentNode : assignmentNodes)
			{
				assignmentNode.removeAllChildren();
			}
			selectedAssignment = (Assignment)obj;
			AssignmentLoadingWorker worker = new AssignmentLoadingWorker(selectedAssignment, display, frame, importer, tree, node);
			worker.execute();
		}
		else if(obj instanceof Student)
		{
			Student stu = (Student)obj;
			display.getVisualMossGraphDisplay().panToNode(stu.getNetid(), 5000);
		}
	}
	
	public Assignment getSelectedAssignment()
	{
		return this.selectedAssignment;
	}
		
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() > 1)
			changeAssignment();
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
