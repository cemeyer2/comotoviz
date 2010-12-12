package edu.uiuc.cs.visualmoss.gui.dataimport;

import edu.uiuc.cs.visualmoss.VisualMossConstants;
import edu.uiuc.cs.visualmoss.dataimport.DataImport;
import edu.uiuc.cs.visualmoss.dataimport.api.objects.Assignment;
import edu.uiuc.cs.visualmoss.dataimport.api.objects.Course;
import edu.uiuc.cs.visualmoss.dataimport.api.objects.Student;
import edu.uiuc.cs.visualmoss.graph.VisualMossGraph;
import edu.uiuc.cs.visualmoss.graph.VisualMossGraphStudent;
import edu.uiuc.cs.visualmoss.gui.graph.VisualMossGraphDisplayContainer;
import edu.uiuc.cs.visualmoss.gui.layout.VisualMossLayout;
import edu.uiuc.cs.visualmoss.utility.Pair;
import prefuse.data.io.DataIOException;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AssignmentChooser extends JPanel implements ActionListener, MouseListener
{
	private final List<Course> courses;
	private Assignment selectedAssignment;
	
	private JButton select;
	private JTree tree;
	private JScrollPane pane;
	private DataImport importer;
	private VisualMossGraphDisplayContainer display;
	private VisualMossLayout frame;
	private ArrayList<DefaultMutableTreeNode> assignmentNodes;
	
	public AssignmentChooser(VisualMossLayout frame, VisualMossGraphDisplayContainer display, Pair<String, String> activeDirectoryCredentials) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, DataIOException
	{
		super();
		this.importer = new DataImport(activeDirectoryCredentials);
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
//			AssignmentLoadingWorker worker = new AssignmentLoadingWorker(selectedAssignment, display, frame, importer, tree, node);
//			worker.execute();

            VisualMossGraph graph = null;
            try {
                graph = importer.buildGraph(selectedAssignment, true, frame);
            } catch (DataIOException e) {
                e.printStackTrace();
            }

            //add students as children
            for(VisualMossGraphStudent student: graph.getStudents()) {
                node.add(new DefaultMutableTreeNode(student));
            }
            ((DefaultTreeModel)tree.getModel()).reload();

            display.changeGraph(graph);
            frame.updateTitle(selectedAssignment);
            frame.searchStudents();


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
