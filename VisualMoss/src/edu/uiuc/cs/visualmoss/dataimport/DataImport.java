package edu.uiuc.cs.visualmoss.dataimport;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFrame;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.io.DataIOException;
import edu.uiuc.cs.visualmoss.VisualMossConstants;
import edu.uiuc.cs.visualmoss.graph.VisualMossGraph;
import edu.uiuc.cs.visualmoss.gui.graph.predicates.VisualMossNodeFillCurrentSemesterPredicate;
import edu.uiuc.cs.visualmoss.gui.utility.LoadingProgressDialog;
import edu.uiuc.cs.visualmoss.utility.CampusIPCheck;

public class DataImport 
{
	private VisualMossGraph graph;
	private Connection conn;
	private Statement st;
	private ArrayList<Course> courses;
	private Assignment assignment;

	public DataImport(File f) throws DataIOException
	{
		graph = new VisualMossGraph(f, "CS225", "MP3");
	}

	public DataImport() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, DataIOException
	{
		CampusIPCheck.checkCampusIP();
		Class.forName(VisualMossConstants.DB_DRIVER).newInstance();
		conn = DriverManager.getConnection(VisualMossConstants.DB_URL, VisualMossConstants.DB_USERNAME, VisualMossConstants.DB_PASSWORD);
		st = conn.createStatement();
		courses = new ArrayList<Course>();

		//populate courses
		String q0 = "SELECT name FROM conrad_model_course";
		ResultSet rs0 = st.executeQuery(q0);
		while(rs0.next())
		{
			courses.add(new Course(rs0.getString("name")));
		}
		rs0.close();

		//populate assignments that have analysis for each course
		for(Course course : courses)
		{
			String q1 = "SELECT name, language, workDirectory, webDirectory, complete FROM conrad_model_assignment, conrad_model_analysis WHERE course_name = assignment_course_name AND name = assignment_name AND complete = 1 AND course_name = '"+course.getName()+"'";
			ResultSet rs1 = st.executeQuery(q1);
			while(rs1.next())
			{
				Assignment assignment = new Assignment(course, rs1.getString("name"), rs1.getString("language"), rs1.getString("workDirectory"), rs1.getString("webDirectory"), true);
				course.addAssignment(assignment);
			}
		}

		st.close();
		conn.close();

		//Make the default assignment the first one
		//assignment = courses.get(0).getAssignments().get(0);
		//CM: we should leave the first assignment null since what would happen if there were no assignments in the system?
		//index out of  bounds exceptions would happen and make the entire app crash

		//buildGraph(assignment, true);
	}

	public VisualMossGraph getVisualMossGraph()
	{
		return graph;
	}

	public VisualMossGraph buildGraph(Assignment assignment, final boolean showProgress, final JFrame owner) throws SQLException, DataIOException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		this.assignment = assignment;
		HashMap<String, String> partnersDotTexts = new HashMap<String, String>();
		Class.forName(VisualMossConstants.DB_DRIVER).newInstance();
		conn = DriverManager.getConnection(VisualMossConstants.DB_URL, VisualMossConstants.DB_USERNAME, VisualMossConstants.DB_PASSWORD);
		st = conn.createStatement();
		
		Connection conn2 = DriverManager.getConnection(VisualMossConstants.DB_URL, VisualMossConstants.DB_USERNAME, VisualMossConstants.DB_PASSWORD);
		Statement st2 = conn2.createStatement();
		
		final LoadingProgressDialog dialog = new LoadingProgressDialog(owner, "Loading", "Loading Graph...");

		dialog.init();
		if(showProgress)
			dialog.setVisible(true);

		int i = 0;
		Graph graph = new Graph();
		graph.getNodeTable().addColumn("netid", String.class);
		graph.getNodeTable().addColumn("pseudonym", String.class);
		graph.getNodeTable().addColumn("isSolution", String.class);
		graph.getNodeTable().addColumn("season", String.class);
		graph.getNodeTable().addColumn("year", String.class);
		graph.getNodeTable().addColumn("submission_id", String.class);
		graph.getEdgeTable().addColumn("weight", double.class);
		graph.getEdgeTable().addColumn("score1", double.class);
		graph.getEdgeTable().addColumn("score2", double.class);
		graph.getEdgeTable().addColumn("link", String.class);
		graph.getEdgeTable().addColumn("isPartner", boolean.class);

		//count up the number of nodes and edges to add
		if(showProgress == true)
		{
			int count = 0;
			String c0 = "SELECT COUNT(*) AS count FROM conrad_model_submission WHERE n='"+assignment.getName()+"'";
			ResultSet rsc0 = st.executeQuery(c0);
			rsc0.next();
			count += rsc0.getInt("count");
			rsc0.close();
			String c1 = "SELECT COUNT(*) AS count FROM conrad_model_submission, conrad_model_mossmatch WHERE conrad_model_submission.id=submission1_id AND n='"+assignment.getName()+"'";
			ResultSet rsc1 = st.executeQuery(c1);
			rsc1.next();
			count += rsc1.getInt("count");
			rsc1.close();
			dialog.setTaskLength(count);
			dialog.setIndeterminate(false);
		}

		//add student submission nodes
		String q0 = "SELECT id, c AS course_name, n AS assignment_name, ana AS analysis_id, pseu AS pseudonym, seas AS season, yr AS year, student_netid AS netid FROM conrad_model_submission, conrad_model_studentsubmission WHERE id=submission_id AND row_type='studentsubmission' AND n='"+assignment.getName()+"'";
		ResultSet rs0 = st.executeQuery(q0);
		while(rs0.next())
		{
			Node node = graph.addNode();
			node.setString("netid", rs0.getString("netid"));
			node.setString("pseudonym", rs0.getString("pseudonym"));
			node.setString("isSolution", "false");
			node.setString("season", rs0.getString("season"));
			node.setString("year", rs0.getString("year"));
			node.setString("submission_id", rs0.getString("id"));
			i++;
			dialog.setValue(i);
		}
		rs0.close();

		//add solution nodes
		String q1 = "SELECT id, c AS course_name, n AS assignment_name, ana AS analysis_id, pseu AS pseudonum FROM conrad_model_submission WHERE row_type='solutionsubmission' AND n='"+assignment.getName()+"'";
		ResultSet rs1 = st.executeQuery(q1);
		while(rs1.next())
		{
			Node node = graph.addNode();
			node.setString("netid", VisualMossConstants.SOLUTION_NODE_LABEL);
			node.setString("pseudonym", VisualMossConstants.SOLUTION_NODE_LABEL);
			node.setString("isSolution", "true");
			node.setString("season", VisualMossConstants.SOLUTION_NODE_LABEL);
			node.setString("year", VisualMossConstants.SOLUTION_NODE_LABEL);
			node.setString("submission_id", rs1.getString("id"));
			i++;
			dialog.setValue(i);
		}
		rs1.close();

		//add edges
		Iterator<Node> iter = graph.nodes();
		while(iter.hasNext())
		{
			Node source = iter.next();
			String source_submission_id = source.getString("submission_id");
			String q3 = "SELECT submission1_id, submission2_id, score1, score2, link FROM conrad_model_mossmatch WHERE submission1_id="+source_submission_id;
			ResultSet rs3 = st.executeQuery(q3);
			while(rs3.next())
			{
				String target_submission_id = rs3.getString("submission2_id");
				//loop to find node to link to
				Node target = null;
				Iterator<Node> iter2 = graph.nodes();
				while(iter2.hasNext())
				{
					Node temp = iter2.next();
					if(temp.getString("submission_id").equals(target_submission_id))
					{
						target = temp;
						break;
					}
				}
				Edge edge = graph.addEdge(source, target);
				double weight = Math.max(rs3.getDouble("score1"), rs3.getDouble("score2"));
				String link = rs3.getString("link");
				String[] split = assignment.getWebDirectory().split("/");
				String url = VisualMossConstants.URL_BASE+split[split.length-1]+"/"+link;
				edge.setDouble("score1", rs3.getDouble("score1"));
				edge.setDouble("score2", rs3.getDouble("score2"));
				edge.setDouble("weight", new Double(weight));
				edge.setString("link", url);

				if(partnersDotTexts.get(source_submission_id) == null)
				{
					partnersDotTexts.put(source_submission_id, getPartnersDotText(source_submission_id, st2));
				}
				if(partnersDotTexts.get(target_submission_id) == null)
				{
					partnersDotTexts.put(target_submission_id, getPartnersDotText(target_submission_id, st2));
				}
				
				boolean isPartner = isPartnerTo(partnersDotTexts.get(source_submission_id), target.getString("netid"));
				isPartner = isPartner || isPartnerTo(partnersDotTexts.get(target_submission_id), source.getString("netid"));
				
				edge.setBoolean("isPartner", isPartner);
				
//				System.out.println(source.getString("netid")+"--"+target.getString("netid")+": isPartner: "+isPartner);
				
				i++;
				dialog.setValue(i);
			}
			rs3.close();
		}
		dialog.setIndeterminate(true);

		//clean up the solution
		Iterator iterr = graph.nodes();
		while(iterr.hasNext())
		{
			Node node = (Node)iterr.next();
			if(node.getString("netid").equals(VisualMossConstants.SOLUTION_NODE_LABEL))
				continue;
			Iterator iter2 = node.edges();
			boolean toSolution = false;
			while(iter2.hasNext())
			{
				Edge edge = (Edge)iter2.next();
				Node src = edge.getSourceNode();
				Node tgt = edge.getTargetNode();
				if(src.get("netid").equals(VisualMossConstants.SOLUTION_NODE_LABEL) || tgt.get("netid").equals(VisualMossConstants.SOLUTION_NODE_LABEL))
				{
					toSolution = true;
				}
			}
			if(toSolution)
			{
				Iterator edgeIter = node.edges();
				ArrayList<Edge> toRemove = new ArrayList<Edge>();
				while(edgeIter.hasNext())
				{
					Edge edge = (Edge)edgeIter.next();
					Node src = edge.getSourceNode();
					Node tgt = edge.getTargetNode();
					if(!src.get("netid").equals(VisualMossConstants.SOLUTION_NODE_LABEL) && !tgt.get("netid").equals(VisualMossConstants.SOLUTION_NODE_LABEL))
					{
						if(!toRemove.contains(edge))
							toRemove.add(edge);
					}
				}
				for(Edge e : toRemove)
				{
					graph.removeEdge(e);
				}
			}
		}
		
		//get rid of past semester nodes that dont link to current semester nodes
		ArrayList<Node> toRemove = new ArrayList<Node>();
		VisualMossNodeFillCurrentSemesterPredicate pred = new VisualMossNodeFillCurrentSemesterPredicate();
		iter = graph.nodes();
		while(iter.hasNext())
		{
			Node curNode = iter.next();
			boolean shouldKeep = false;
			if(pred.getBoolean(curNode))
			{
				//node in question is already this semester
				shouldKeep = true;
			}
			
			Iterator<Node> adjIter = curNode.neighbors();
			while(adjIter.hasNext())
			{
				Node adj = adjIter.next();
				if(pred.getBoolean(adj))
				{
					//node in question has a link to a current semester node
					shouldKeep = true;
				}
			}
			if(shouldKeep == false)
			{
				toRemove.add(curNode);
			}
			else
			{
				System.out.println("keeping "+curNode.getString("netid"));
			}
		}
		System.out.println("selected nodes for deletion: "+graph.getNodeCount());
		for(Node node : toRemove)
		{
			graph.removeNode(node);
		}
		System.out.println("deleted nodes: "+graph.getNodeCount());
		
		
		
		dialog.setVisible(false);
		this.graph = new VisualMossGraph(copyGraph(graph), assignment.getCourse().getName(), assignment.getName());
		st.close();
		conn.close();
		st2.close();
		conn2.close();
		return this.graph;
	}

	private String getPartnersDotText(String submission_id, Statement st) throws SQLException
	{
		String q4 = "SELECT content FROM conrad_model_submissionfile WHERE submission_id="+submission_id+" AND name='partners.txt'";
		ResultSet rs4 = st.executeQuery(q4);

		Clob fileClob = null;
		if(rs4.next())
		{
			fileClob = rs4.getClob("content");
			String str = "";
			String retval = "";

			BufferedReader in = new BufferedReader(fileClob.getCharacterStream());
			try {
				while((str = in.readLine()) != null)
				{
					retval += str;
				}
				return retval;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		rs4.close();
		return "";
	}
	
	private Graph copyGraph(Graph graph)
	{
		Graph graph2 = new Graph();
		graph2.getNodeTable().addColumn("netid", String.class);
		graph2.getNodeTable().addColumn("pseudonym", String.class);
		graph2.getNodeTable().addColumn("isSolution", String.class);
		graph2.getNodeTable().addColumn("season", String.class);
		graph2.getNodeTable().addColumn("year", String.class);
		graph2.getNodeTable().addColumn("submission_id", String.class);
		graph2.getEdgeTable().addColumn("weight", double.class);
		graph2.getEdgeTable().addColumn("score1", double.class);
		graph2.getEdgeTable().addColumn("score2", double.class);
		graph2.getEdgeTable().addColumn("link", String.class);
		graph2.getEdgeTable().addColumn("isPartner", boolean.class);

		Iterator<Node> nodeIter = graph.nodes();
		while(nodeIter.hasNext())
		{
			Node oldNode = nodeIter.next();
			Node newNode = graph2.addNode();
			newNode.setString("netid", oldNode.getString("netid"));
			newNode.setString("pseudonym", oldNode.getString("pseudonym"));
			newNode.setString("isSolution", oldNode.getString("isSolution"));
			newNode.setString("season", oldNode.getString("season"));
			newNode.setString("year", oldNode.getString("year"));
			newNode.setString("submission_id", oldNode.getString("submission_id"));
		}
		Iterator<Edge> edgeIter = graph.edges();
		while(edgeIter.hasNext())
		{
			Edge oldEdge = edgeIter.next();
			Edge newEdge = graph2.addEdge(getNode(oldEdge.getSourceNode().getString("netid"),graph2), getNode(oldEdge.getTargetNode().getString("netid"),graph2));
			newEdge.setDouble("score1", oldEdge.getDouble("score1"));
			newEdge.setDouble("score2", oldEdge.getDouble("score2"));
			newEdge.setDouble("weight", oldEdge.getDouble("weight"));
			newEdge.setString("link", oldEdge.getString("link"));
			newEdge.setBoolean("isPartner", oldEdge.getBoolean("isPartner"));
		}
		return graph2;
	}
	
	private Node getNode(String netid, Graph graph)
	{
		Iterator<Node> iter = graph.nodes();
		while(iter.hasNext())
		{
			Node node = iter.next();
			if(node.getString("netid").equals(netid))
			{
				return node;
			}
		}
		return null;
	}
	
	public boolean isPartnerTo(String partnersDotText, String netid) throws SQLException
	{
		return partnersDotText.contains(netid);
	}

	public Assignment getAssignment()
	{
		return assignment;
	}

	public ArrayList<Course> getCourses()
	{
		return courses;
	}

}