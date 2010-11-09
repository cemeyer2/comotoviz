package edu.uiuc.cs.visualmoss.graph;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLReader;

import java.io.File;
import java.io.InputStream;
import java.util.*;

import static edu.uiuc.cs.visualmoss.VisualMossConstants.*;

public class VisualMossGraph {

    /**
     * The abstract graph structure for this assignment
     */
	private Graph graph;

    /**
     * Hash table that hashes netid's to student objects
     */
	private Hashtable<String, VisualMossGraphStudent> students;

    /**
     * The matches in this assignment
     */
	private ArrayList<VisualMossGraphMatch> matches;

    /**
     * The name of the course corresponding to this graph
     */
	private String courseName;

    /**
     * The name of the assignment that this graph represents
     */
	private String assignmentName;

    /**
     * Creates a new VisualMossGraph with the given course name and assignment name
     *
     * @param courseName The name of the course
     * @param assignmentName The name of this assignment
     */
	private VisualMossGraph(String courseName, String assignmentName)
	{
		students = new Hashtable<String, VisualMossGraphStudent>();
		matches = new ArrayList<VisualMossGraphMatch>();
		this.courseName = courseName;
		this.assignmentName = assignmentName;
	}

    /**
     * Creates a new VisualMossGraph from some arbitrary input stream and course and assignment names
     *
     * @param stream The input stream from which to read, containing a graph in GraphML format
     * @param courseName The name of this course
     * @param assignmentName The name of the assignment corresponding to this graph
     * @throws DataIOException On any errors reading the GraphML format
     */
	public VisualMossGraph(InputStream stream, String courseName, String assignmentName) throws DataIOException {
		this(courseName, assignmentName);
		graph = new GraphMLReader().readGraph(stream);
		initializeSubmissionsAndMatches();
	}

    /**
     * Creates a new VisualMossGraph from some arbitrary input stream and course and assignment names
     *
     * @param inputFile The input file from which to read, containing a graph in GraphML format
     * @param courseName The name of this course
     * @param assignmentName The name of the assignment corresponding to this graph
     * @throws DataIOException On any errors reading the GraphML format
     */
	public VisualMossGraph(File inputFile, String courseName, String assignmentName) throws DataIOException {
		this(courseName, assignmentName);
		graph = new GraphMLReader().readGraph(inputFile);
		initializeSubmissionsAndMatches();
	}

    /**
     * Creates a new VisualMossGraph from some arbitrary input stream and course and assignment names
     *
     * @param inputGraph The input graph from which to read
     * @param courseName The name of this course
     * @param assignmentName The name of the assignment corresponding to this graph
     * @throws DataIOException On any errors reading the GraphML format
     */
	public VisualMossGraph(Graph inputGraph, String courseName, String assignmentName) throws DataIOException {
		this(courseName, assignmentName);
		graph = inputGraph;
		initializeSubmissionsAndMatches();
	}

    /**
     * Initializes the submission and match objects from the graph, once the graph is created
     */
	private void initializeSubmissionsAndMatches() {

		//Build student objects from the graph
		Iterator<Node> nodeIterator = graph.nodes();
		while(nodeIterator.hasNext()) {

            //Grab a node from the graph
			Node node = nodeIterator.next();

            //Extract its data
			String netid = node.getString(NETID);
			String pseudonym = node.getString(PSEUDONYM);

            //Build a new VisualMossGraphStudent object to old the basic information about this student
			VisualMossGraphStudent student = new VisualMossGraphStudent(netid, pseudonym);
			students.put(netid, student);
		}

        //Build the match objects from the graph
		nodeIterator = graph.edges();
		while(nodeIterator.hasNext()) {

            //Grab an edge from the graph
			Edge edge = (Edge)nodeIterator.next();

            // Extract its data
			VisualMossGraphStudent student1 = students.get(edge.getSourceNode().getString(NETID));
			VisualMossGraphStudent student2 = students.get(edge.getTargetNode().getString(NETID));
			double score1 = edge.getDouble(SCORE1);
			double score2 = edge.getDouble(SCORE1);

            //Build a match from this data and add it to our records
			VisualMossGraphMatch match = new VisualMossGraphMatch(student1, student2, score1, score2);
			matches.add(match);
			student1.addMatch(match);
			student2.addMatch(match);
		}
	}

    /**
     * Gets a list of the simple student objects represented in this graph
     *
     * @return A list of VisualMossGraphStudent objects containing basic student data
     */
	public List<VisualMossGraphStudent> getStudents() {
		ArrayList<VisualMossGraphStudent> list = new ArrayList<VisualMossGraphStudent>(students.values());
		Collections.sort(list);
		return list;
	}
	
	public List<VisualMossGraphMatch> getMatches() {
		return matches;
	}
	
	public Graph getPrefuseGraph() {
		return graph;
	}
	
	
	public final String getCourseName() {
		return courseName;
	}

	public final void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public final String getAssignmentName() {
		return assignmentName;
	}

	public final void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
	}
}
