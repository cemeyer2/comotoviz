package edu.uiuc.cs.visualmoss.dataimport;

import edu.uiuc.cs.visualmoss.VisualMossConstants;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPI;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConnection;
import edu.uiuc.cs.visualmoss.dataimport.api.objects.*;
import edu.uiuc.cs.visualmoss.graph.VisualMossGraph;
import edu.uiuc.cs.visualmoss.gui.graph.predicates.VisualMossNodeFillCurrentSemesterPredicate;
import edu.uiuc.cs.visualmoss.gui.utility.LoadingProgressDialog;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.io.DataIOException;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import static edu.uiuc.cs.visualmoss.VisualMossConstants.*;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Nov 3, 2010
 *
 * <p> <p> This class drives the import of data into the visual moss graph structure using the CoMoTo API
 */
public class DataImport {

    private VisualMossGraph graph;
    private CoMoToAPIConnection connection;
    private ArrayList<Submission> submissions;
    private Assignment assignment;
    private Course course;

    /**
     * The default constructor for the data import, which queries the CoMoTo API to populate the courses and assignments
     *  for each course
     */
    public DataImport(String courseName, String assignmentName) {

        //Create a connection to the CoMoTo API
        connection = new CoMoToAPIConnection(VisualMossConstants.API_USER_NAME, VisualMossConstants.API_PASSWORD);

        //Populate the courses from the API
        List<Course> courses = CoMoToAPI.getCourses(connection);

        //Find the course corresponding to this course name
        for(Course course : courses){
            if(course.getName().equals(courseName)){
                this.course = course;
            }
        }

        //Find the assignment corresponding to the given name
        List<Assignment> assignments = course.getAssignments();
        for(Assignment assignment : assignments){
            if(assignment.getName().equals(assignmentName)){
                this.assignment = assignment;
            }
        }
    }

    /**
     * Imports the data from a given file, intended to be a file written in GraphML format
     *
     * @param inputFile  The file from which to import the data
     * @throws prefuse.data.io.DataIOException  On an error accessing the file data
     */
	public DataImport(File inputFile) throws DataIOException {

        //Builds a graph with the default class and assignment from the given input file data
		graph = new VisualMossGraph(inputFile, CS225, MP3);
	}

    /**
     * Imports the data from a given file, intended to be a file written in GraphML format to be imported into the graph
     *  structure, using the given course and assignment titles
     *
     * @param inputFile         The input file in GraphML format
     * @param courseName        The name of the course's data to import
     * @param assignmentName    The name of the particular assignment we are importing
     */
    public DataImport(File inputFile, String courseName, String assignmentName) throws DataIOException {

        //Builds a graph with the given class and assignment from the given input file
		graph = new VisualMossGraph(inputFile, courseName, assignmentName);
    }

    public VisualMossGraph buildGraph(Assignment assignment, boolean showProgress, JFrame parent) throws DataIOException {

        //Create the progress bar
        LoadingProgressDialog dialog = new LoadingProgressDialog(parent, "Loading", "Loading Graph...");
        this.assignment = assignment;

        //Initialize and display the progress bar if we're supposed to
        initializeProgressBar(showProgress, dialog);

        //Create an empty graph structure
        Graph graph = new Graph();

        //Declare the fields we want in the nodes and edges
        declareGraphFields(graph);

        //Get the file sets associated with this assignment from the API
        List<FileSet> fileSets = assignment.getFileSets();

        //Get the set of moss matches for this assignment
        List<MossMatch> matches = assignment.getAnalysis().getMossAnalysis().getMatches();
        
        //Get ready to pull out all of the submissions from the file sets
        submissions = new ArrayList<Submission>();

        //Estimate the proportion of data to be done by counting the number of file sets and matches
        estimateTotalWorkForProgressBar(showProgress, dialog, fileSets, matches);

        //Get a representation of the submissions as id, object pairs
        Hashtable<Integer, Submission> submissionsTable = new Hashtable<Integer, Submission>();
        Hashtable<Integer, Node> nodeTable = new Hashtable<Integer, Node>();

        //Add submission data to the graph
        int progress = addSubmissionNodes(showProgress, dialog, graph, fileSets, submissionsTable, nodeTable);

        //Add analysis data to the graph
        progress = addMatchNodes(showProgress, dialog, graph, matches, submissionsTable, nodeTable, progress);

        //Clean up the solution
        Iterator<Node> nodeIterator = graph.nodes();
        while(nodeIterator.hasNext())
        {
            Node node = nodeIterator.next();
            if(node.getString(NETID).equals(VisualMossConstants.SOLUTION_NODE_LABEL))
                continue;
            Iterator iter2 = node.edges();
            boolean toSolution = false;
            while(iter2.hasNext())
            {
                Edge edge = (Edge)iter2.next();
                Node src = edge.getSourceNode();
                Node tgt = edge.getTargetNode();
                if(src.get(NETID).equals(VisualMossConstants.SOLUTION_NODE_LABEL) || tgt.get(NETID).equals(VisualMossConstants.SOLUTION_NODE_LABEL))
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
                    if(!src.get(NETID).equals(VisualMossConstants.SOLUTION_NODE_LABEL) && !tgt.get(NETID).equals(VisualMossConstants.SOLUTION_NODE_LABEL))
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

        //Clean up the solution more
        ArrayList<Node> toRemove = new ArrayList<Node>();
        VisualMossNodeFillCurrentSemesterPredicate pred = new VisualMossNodeFillCurrentSemesterPredicate();
        nodeIterator = graph.nodes();
        while(nodeIterator.hasNext())
        {
            Node curNode = nodeIterator.next();
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
                System.out.println("keeping "+curNode.getString(NETID));
            }
        }
        System.out.println("selected nodes for deletion: "+graph.getNodeCount());
        for(Node node : toRemove)
        {
            graph.removeNode(node);
        }
        System.out.println("deleted nodes: "+graph.getNodeCount());


        //Hide the dialog -- we're done now
        dialog.setVisible(false);

        //Keep a copy of this graph we built, and return it
        this.graph = new VisualMossGraph(copyGraph(graph), assignment.getCourse().getName(), assignment.getName());
        return this.graph;

    }

    private int addMatchNodes(boolean showProgress, LoadingProgressDialog dialog, Graph graph, List<MossMatch> matches, Hashtable<Integer, Submission> submissionsTable, Hashtable<Integer, Node> nodeTable, int progress) {
        for(MossMatch match : matches){

            //Find the two submissions associated with this match
            Submission submissionOne = submissionsTable.get(match.getSubmission1Id());
            Submission submissionTwo = submissionsTable.get(match.getSubmission2Id());

            //Find the nodes for these submissions
            Node submissionOneNode = nodeTable.get(submissionOne.getId());
            Node submissionTwoNode = nodeTable.get(submissionTwo.getId());

            //Create a new edge for this match
            Edge edge = graph.addEdge(submissionOneNode, submissionTwoNode);

            //Build the data for this edge
            double score1 = match.getScore1();
            double score2 = match.getScore2();
            double maxScore = Math.max(score1, score2);
            URL matchLink = match.getLink();

            //Figure out if these two were partners or not
            boolean arePartners = arePartnered(submissionOne, submissionTwo);

            //Set this data on the edge
            edge.setDouble(SCORE1, score1);
            edge.setDouble(SCORE2, score2);
            edge.setDouble(WEIGHT, maxScore);
            edge.setString(LINK, matchLink.toString());
            edge.setBoolean(IS_PARTNER, arePartners);

            //Update the progress bar
            progress = updateProgressBar(showProgress, dialog, progress);
        }
        dialog.setIndeterminate(true);
        return progress;
    }

    /**
     * Checks to see if two submissions share partners
     *
     * @param submissionOne The first submisison
     * @param submissionTwo The second submisison
     * @return Whether or not these submissions are partnered
     */
    private boolean arePartnered(Submission submissionOne, Submission submissionTwo) {

        //Grab the partners from the two submissions
        List<Student> submissionOnePartners = submissionOne.getPartners();
        List<Student> submissionTwoPartners = submissionTwo.getPartners();
        boolean arePartners = false;

        //Check each student from the second submission's partners with the first submission's author
        for(Student student: submissionTwoPartners){
            if(student.getId() == submissionOne.getStudentId()){
                arePartners = true;
            }
        }

        //Check each student from the first submission's partners with the second submission's author
        for(Student student : submissionOnePartners){
            if(student.getId() == submissionTwo.getStudentId()){
                arePartners = true;
            }
        }
        return arePartners;
    }

    /**
     * Adds the nodes to the graph, each representing a submission for the given assignment
     *
     * @param showProgress Whether or not to show the progress bar
     * @param dialog A handle on the dialog to show progress
     * @param graph The graph object that we're creating
     * @param fileSets The list of file sets to load and add to the graph
     * @param submissionsTable A table for referencing submissions quickly
     * @param nodeTable A table for referencing the nodes quickly
     *
     * @return The progress after we've added all of the submission nodes
     */
    private int addSubmissionNodes(boolean showProgress, LoadingProgressDialog dialog, Graph graph, List<FileSet> fileSets,
                                   Hashtable<Integer, Submission> submissionsTable, Hashtable<Integer, Node> nodeTable) {
        int progress = 0;
        for(FileSet fileSet : fileSets){

            //Grab all submissions associated with this file set
            List<Submission> setOfSubmissions = fileSet.getSubmissions();
            submissions.addAll(setOfSubmissions);

            //Get the semester data from the file set
            Semester semester = fileSet.getOffering().getSemester();

            //Add the submissions to the graph
            for (Submission submission : setOfSubmissions) {

                //Add this submission to the hash table
                int submissionId = submission.getId();
                submissionsTable.put(submissionId, submission);

                //Get the student and pseudonym associated with this submission
                Student student = submission.getStudent();
                int pseudonym = submission.getAnalysisPseudonym().getPseudonym();

                //Figure out if this submission is a solution
                Type submissionType = submission.getType();
                boolean isSolution = (submissionType == Type.solutionsubmission);

                //Add this submission's data to the graph
                Node node = graph.addNode();
                if(!isSolution){
                    node.setString(NETID, student.getNetid());
                    node.setString(PSEUDONYM, Integer.toString(pseudonym));
                    node.setString(SEASON, semester.getSeason().name());
                    node.setString(YEAR, Integer.toString(semester.getYear()));
                    node.setString(SUBMISSION_ID, Integer.toString(submissionId));
                } else {
                    node.setString(NETID, VisualMossConstants.SOLUTION_NODE_LABEL);
                    node.setString(PSEUDONYM, VisualMossConstants.SOLUTION_NODE_LABEL);
                    node.setString(SEASON, VisualMossConstants.SOLUTION_NODE_LABEL);
                    node.setString(YEAR, VisualMossConstants.SOLUTION_NODE_LABEL);
                    node.setString(SUBMISSION_ID, Integer.toString(submissionId));
                }
                node.setString(IS_SOLUTION, Boolean.toString(isSolution));

                //Add this new node to the hash table
                nodeTable.put(submissionId, node);
            }

            //Update the progress bar
            progress = updateProgressBar(showProgress, dialog, progress);
        }
        return progress;
    }

    /**
     * Makes a copy of an input graph, and returns the copy
     *
     * @param graph The graph object to copy
     * @return A new graph, identical to the input graph
     */
    private Graph copyGraph(Graph graph)
    {

        //Create the new graph and declare the fields of each node
        Graph graph2 = new Graph();
        graph2.getNodeTable().addColumn(NETID, String.class);
        graph2.getNodeTable().addColumn(PSEUDONYM, String.class);
        graph2.getNodeTable().addColumn(IS_SOLUTION, String.class);
        graph2.getNodeTable().addColumn(SEASON, String.class);
        graph2.getNodeTable().addColumn(YEAR, String.class);
        graph2.getNodeTable().addColumn(SUBMISSION_ID, String.class);
        graph2.getEdgeTable().addColumn(WEIGHT, double.class);
        graph2.getEdgeTable().addColumn(SCORE1, double.class);
        graph2.getEdgeTable().addColumn(SCORE2, double.class);
        graph2.getEdgeTable().addColumn(LINK, String.class);
        graph2.getEdgeTable().addColumn(IS_PARTNER, boolean.class);

        //Copy all of the graph nodes
        Iterator<Node> nodeIterator = graph.nodes();
        while(nodeIterator.hasNext())
        {
            Node oldNode = nodeIterator.next();
            Node newNode = graph2.addNode();
            newNode.setString(NETID, oldNode.getString(NETID));
            newNode.setString(PSEUDONYM, oldNode.getString(PSEUDONYM));
            newNode.setString(IS_SOLUTION, oldNode.getString(IS_SOLUTION));
            newNode.setString(SEASON, oldNode.getString(SEASON));
            newNode.setString(YEAR, oldNode.getString(YEAR));
            newNode.setString(SUBMISSION_ID, oldNode.getString(SUBMISSION_ID));
        }

        //Copy all of the graph edges
        Iterator<Edge> edgeIterator = graph.edges();
        while(edgeIterator.hasNext())
        {
            Edge oldEdge = edgeIterator.next();
            Edge newEdge = graph2.addEdge(getNode(oldEdge.getSourceNode().getString(NETID),graph2), getNode(oldEdge.getTargetNode().getString(NETID),graph2));
            newEdge.setDouble(SCORE1, oldEdge.getDouble(SCORE1));
            newEdge.setDouble(SCORE2, oldEdge.getDouble(SCORE2));
            newEdge.setDouble(WEIGHT, oldEdge.getDouble(WEIGHT));
            newEdge.setString(LINK, oldEdge.getString(LINK));
            newEdge.setBoolean(IS_PARTNER, oldEdge.getBoolean(IS_PARTNER));
        }
        return graph2;
    }

    /**
     * Finds a node by the netId associated with it
     *
     * @param netId The netId identifying the node to get
     * @param graph The graph in which to find the node
     * @return The node corresponding to the given netId
     */
    private Node getNode(String netId, Graph graph) {

        Iterator<Node> nodeIterator = graph.nodes();
        while(nodeIterator.hasNext()) {
            Node node = nodeIterator.next();
            if(node.getString(NETID).equals(netId)) {
                return node;
            }
        }
        return null;
    }

    private void estimateTotalWorkForProgressBar(boolean showProgress, LoadingProgressDialog dialog, List<FileSet> fileSets, List<MossMatch> matches) {
        if(showProgress) {
            dialog.setTaskLength(fileSets.size() + matches.size());
            dialog.setIndeterminate(false);
        }
    }

    private void declareGraphFields(Graph graph) {

        //Declare all the properties of a submission (e.g. a node in the graph)
        graph.getNodeTable().addColumn(NETID, String.class);
        graph.getNodeTable().addColumn(PSEUDONYM, String.class);
        graph.getNodeTable().addColumn(IS_SOLUTION, boolean.class);
        graph.getNodeTable().addColumn(SEASON, String.class);
        graph.getNodeTable().addColumn(YEAR, String.class);
        graph.getNodeTable().addColumn(SUBMISSION_ID, String.class);

        //Declare all the properties of an analysis
        graph.getEdgeTable().addColumn(WEIGHT, double.class);   //Weight = max(score1,score2)
        graph.getEdgeTable().addColumn(SCORE1, double.class);
        graph.getEdgeTable().addColumn(SCORE2, double.class);
        graph.getEdgeTable().addColumn(LINK, String.class);
        graph.getEdgeTable().addColumn(IS_PARTNER, boolean.class);
    }

    /**
     * Updates the progress bar if necessary
     *
     * @param showProgress Whether or not we're supposed to show the progress bar
     * @param dialog A handle on the progress bar dialog
     * @param progress The progress of the job, ranging from 0 to the number of items loaded from the API (edges + nodes)
     * @return The updated progress value
     */
    private int updateProgressBar(boolean showProgress, LoadingProgressDialog dialog, int progress) {
        if(showProgress){
            progress++;
            dialog.setValue(progress);
        }
        return progress;
    }

    /**
     * Intializes the progress bar
     *
     * @param showProgress Whether or not to show the progress bar
     * @param dialog A handle on the progress bar dialog
     */
    private void initializeProgressBar(boolean showProgress, LoadingProgressDialog dialog) {
        dialog.init();
        if(showProgress)
            dialog.setVisible(true);
    }

    /**
     * Gets the graph
     *
     * @return the graph
     */
    public VisualMossGraph getGraph() {
        return graph;
    }

    /**
     * Gets the connection to the API
     *
     * @return The connection to the API
     */
    public CoMoToAPIConnection getConnection() {
        return connection;
    }

    /**
     * Get the assignment associated with this import
     *
     * @return The assignment that this graph represents
     */
    public Assignment getAssignment() {
        return assignment;
    }
}
