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

package edu.uiuc.cs.visualmoss.dataimport;

import edu.illinois.comoto.api.CoMoToAPI;
import edu.illinois.comoto.api.object.*;
import edu.illinois.comoto.api.utility.Connection;
import edu.uiuc.cs.visualmoss.VisualMossConstants;
import edu.uiuc.cs.visualmoss.graph.VisualMossGraph;
import edu.uiuc.cs.visualmoss.gui.utility.LoadingProgressDialog;
import edu.uiuc.cs.visualmoss.utility.Pair;
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
 * <p/>
 * <p> <p> This class drives the import of data into the visual moss graph structure using the CoMoTo API
 */
public class DataImport {

    private VisualMossGraph graph;
    private Connection connection;
    private ArrayList<Submission> submissions;
    private Assignment assignment;
    private Course course;
    private List<Course> courses;
    private Season assignmentSeason;
    private int assignmentYear;


    /**
     * The default constructor for the data import, which queries the CoMoTo API to populate the courses and assignments
     * for each course
     */
    public DataImport(String courseName, String assignmentName, Pair<String, String> activeDirectoryCredentials) {

        //Create a connection to the CoMoTo API
        connection = new Connection(activeDirectoryCredentials.getFirst(), activeDirectoryCredentials.getSecond());

        //Populate the courses from the API
        List<Course> courses = CoMoToAPI.getCourses(connection);

        //Find the course corresponding to this course name
        for (Course course : courses) {
            if (course.getName().equals(courseName)) {
                this.course = course;
            }
        }

        //Find the assignment corresponding to the given name
        List<Assignment> assignments = course.getAssignments();
        for (Assignment assignment : assignments) {
            if (assignment.getName().equals(assignmentName)) {
                this.assignment = assignment;
            }
        }
    }

    public DataImport(Pair<String, String> activeDirectoryCredentials) {

        //Create a connection to the CoMoTo API
        connection = new Connection(activeDirectoryCredentials.getFirst(), activeDirectoryCredentials.getSecond());

        //Populate the courses from the API
        courses = CoMoToAPI.getCourses(connection);


    }

    /**
     * Imports the data from a given file, intended to be a file written in GraphML format
     *
     * @param inputFile The file from which to import the data
     * @throws prefuse.data.io.DataIOException
     *          On an error accessing the file data
     */
    public DataImport(File inputFile) throws DataIOException {

        //Builds a graph with the default class and assignment from the given input file data
        graph = new VisualMossGraph(inputFile, CS225, MP3);
    }

    /**
     * Imports the data from a given file, intended to be a file written in GraphML format to be imported into the graph
     * structure, using the given course and assignment titles
     *
     * @param inputFile      The input file in GraphML format
     * @param courseName     The name of the course's data to import
     * @param assignmentName The name of the particular assignment we are importing
     */
    public DataImport(File inputFile, String courseName, String assignmentName) throws DataIOException {

        //Builds a graph with the given class and assignment from the given input file
        graph = new VisualMossGraph(inputFile, courseName, assignmentName);
    }

    public VisualMossGraph buildGraph(Assignment assignment, boolean showProgress, JFrame parent) throws DataIOException {

        //Create the progress bar
        LoadingProgressDialog dialog = new LoadingProgressDialog(parent, "Loading Graph of \"" + assignment.getName() + "\"", "");
        this.assignment = assignment;

        //Figure out the year and season of this assignment
        String assignmentTitle = assignment.toString();
        if (assignmentTitle.toLowerCase().contains("spring")) {
            assignmentSeason = Season.Spring;
        } else if (assignmentTitle.toLowerCase().contains("summer")) {
            assignmentSeason = Season.Summer;
        } else if (assignmentTitle.toLowerCase().contains("fall")) {
            assignmentSeason = Season.Fall;
        } else if (assignmentTitle.toLowerCase().contains("winter")) {
            assignmentSeason = Season.Winter;
        }
        assignmentYear = Integer.parseInt(assignmentTitle.substring(assignmentTitle.length() - 5, assignmentTitle.length()).trim());

        //Initialize and display the progress bar if we're supposed to
        initializeProgressBar(showProgress, dialog);

        //Create an empty graph structure
        Graph graph = new Graph();

        //Declare the fields we want in the nodes and edges
        declareGraphFields(graph);

        //Let the user know that we're beginning to load file sets from the API
        dialog.setMessage("Loading File Sets");

        //Get the file sets associated with this assignment from the API
        List<FileSet> fileSets = assignment.getFilesets(true);

        //Let the user know that we're generating matches
        dialog.setMessage("Loading Analysis Data");

        //Get the set of moss matches for this assignment
        List<MossMatch> matches = assignment.getAnalysis().getMossAnalysis(true).getMatches();

        //Get ready to pull out all of the submissions from the file sets
        submissions = new ArrayList<Submission>();

        //Let the user know that we're adding submissions
        dialog.setMessage("Building Submission Data");

        //Get a representation of the submissions as id, object pairs
        Hashtable<Integer, Submission> submissionsTable = new Hashtable<Integer, Submission>();
        Hashtable<Integer, Node> nodeTable = new Hashtable<Integer, Node>();

        //Add submission data to the graph
        addSubmissionNodes(graph, fileSets, submissionsTable, nodeTable);

        //Let the user know that we're adding edges
        dialog.setMessage("Building Similarity Data");

        //Add analysis data to the graph
        addMatchNodes(graph, matches, submissionsTable, nodeTable);

        //Hide the dialog -- we're done now
        dialog.setVisible(false);

        //Keep a copy of this graph we built, and return it
        this.graph = new VisualMossGraph(copyGraph(graph), assignment.getCourse().getName(), assignment.getName());
        return this.graph;
    }

    private void addMatchNodes(Graph graph, List<MossMatch> matches, Hashtable<Integer, Submission> submissionsTable,
                               Hashtable<Integer, Node> nodeTable) {
        for (MossMatch match : matches) {

            //Find the two submissions associated with this match
            Submission submissionOne = submissionsTable.get(match.getSubmission1Id());
            Submission submissionTwo = submissionsTable.get(match.getSubmission2Id());

            //Catch situations where we can't find the submission nodes for the edge
            if (submissionOne != null && submissionTwo != null) {

                //Find the nodes for these submissions
                Node submissionOneNode = nodeTable.get(submissionOne.getId());
                Node submissionTwoNode = nodeTable.get(submissionTwo.getId());

                if (submissionOneNode != null && submissionTwoNode != null) {

                    //Create a new edge for this match
                    Edge edge = null;
                    edge = graph.addEdge(submissionOneNode, submissionTwoNode);

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
                }
            }
        }
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
        for (Student student : submissionTwoPartners) {
            if (student.getId() == submissionOne.getStudentId()) {
                arePartners = true;
            }
        }

        //Check each student from the first submission's partners with the second submission's author
        for (Student student : submissionOnePartners) {
            if (student.getId() == submissionTwo.getStudentId()) {
                arePartners = true;
            }
        }
        return arePartners;
    }

    /**
     * Adds the nodes to the graph, each representing a submission for the given assignment
     *
     * @param graph            The graph object that we're creating
     * @param fileSets         The list of file sets to load and add to the graph
     * @param submissionsTable A table for referencing submissions quickly
     * @param nodeTable        A table for referencing the nodes quickly
     * @return The progress after we've added all of the submission nodes
     */
    private void addSubmissionNodes(Graph graph, List<FileSet> fileSets, Hashtable<Integer, Submission> submissionsTable,
                                    Hashtable<Integer, Node> nodeTable) {
        for (FileSet fileSet : fileSets) {

            //Grab all submissions associated with this file set
            List<Submission> setOfSubmissions = fileSet.getSubmissions(true);
            submissions.addAll(setOfSubmissions);

            //Get the semester data from the file set
            Semester semester = fileSet.getOffering().getSemester();

            //Add the submissions to the graph
            for (Submission submission : setOfSubmissions) {

                //Add this submission to the hash table
                int submissionId = submission.getId();
                submissionsTable.put(submissionId, submission);

                //Get the student and pseudonym associated with this submission
                try {

                    // Since we just grabbed all student data, this shouldn't touch the API
                    Student student = submission.getStudent();

                    //Figure out if this submission is a solution
                    Type submissionType = submission.getType();
                    boolean isSolution = (submissionType == Type.solutionsubmission);

                    //Determine whether this node is from this semester or not
                    boolean currentSemester = (semester.getYear() == assignmentYear) && (semester.getSeason().equals(assignmentSeason));

                    //Add this submission's data to the graph
                    Node node = graph.addNode();
                    if (!isSolution) {
                        node.setString(NETID, student.getNetid());
                        node.setString(PSEUDONYM, Integer.toString(student.getId()));
                        node.setString(SEASON, semester.getSeason().name());
                        node.setString(YEAR, Integer.toString(semester.getYear()));
                        node.setString(CURRENT_SEMESTER, Boolean.toString(currentSemester));
                        node.setString(SUBMISSION_ID, Integer.toString(submissionId));
                    } else {
                        node.setString(NETID, VisualMossConstants.SOLUTION_NODE_LABEL);
                        node.setString(PSEUDONYM, VisualMossConstants.SOLUTION_NODE_LABEL);
                        node.setString(SEASON, VisualMossConstants.SOLUTION_NODE_LABEL);
                        node.setString(YEAR, VisualMossConstants.SOLUTION_NODE_LABEL);
                        node.setString(CURRENT_SEMESTER, Boolean.toString(currentSemester));
                        node.setString(SUBMISSION_ID, Integer.toString(submissionId));
                    }
                    node.setString(IS_SOLUTION, Boolean.toString(isSolution));

                    //Add this new node to the hash table
                    nodeTable.put(submissionId, node);

                } catch (RuntimeException e) {
                    // Something here didn't exist in the API, skip this one
                }
            }
        }
    }

    /**
     * Makes a copy of an input graph, and returns the copy
     *
     * @param graph The graph object to copy
     * @return A new graph, identical to the input graph
     */
    private Graph copyGraph(Graph graph) {

        //Create the new graph and declare the fields of each node
        Graph graph2 = new Graph();
        graph2.getNodeTable().addColumn(NETID, String.class);
        graph2.getNodeTable().addColumn(PSEUDONYM, String.class);
        graph2.getNodeTable().addColumn(IS_SOLUTION, String.class);
        graph2.getNodeTable().addColumn(SEASON, String.class);
        graph2.getNodeTable().addColumn(YEAR, String.class);
        graph2.getNodeTable().addColumn(SUBMISSION_ID, String.class);
        graph2.getNodeTable().addColumn(CURRENT_SEMESTER, boolean.class);
        graph2.getEdgeTable().addColumn(WEIGHT, double.class);
        graph2.getEdgeTable().addColumn(SCORE1, double.class);
        graph2.getEdgeTable().addColumn(SCORE2, double.class);
        graph2.getEdgeTable().addColumn(LINK, String.class);
        graph2.getEdgeTable().addColumn(IS_PARTNER, boolean.class);

        //Copy all of the graph nodes
        Iterator<Node> nodeIterator = graph.nodes();
        while (nodeIterator.hasNext()) {
            Node oldNode = nodeIterator.next();
            Node newNode = graph2.addNode();
            newNode.setString(NETID, oldNode.getString(NETID));
            newNode.setString(PSEUDONYM, oldNode.getString(PSEUDONYM));
            newNode.setString(IS_SOLUTION, oldNode.getString(IS_SOLUTION));
            newNode.setString(SEASON, oldNode.getString(SEASON));
            newNode.setString(YEAR, oldNode.getString(YEAR));
            newNode.setString(SUBMISSION_ID, oldNode.getString(SUBMISSION_ID));
            newNode.setBoolean(CURRENT_SEMESTER, oldNode.getBoolean(CURRENT_SEMESTER));
        }

        //Copy all of the graph edges
        Iterator<Edge> edgeIterator = graph.edges();
        while (edgeIterator.hasNext()) {
            Edge oldEdge = edgeIterator.next();
            Edge newEdge = graph2.addEdge(getNode(oldEdge.getSourceNode().getString(NETID), graph2), getNode(oldEdge.getTargetNode().getString(NETID), graph2));
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
        while (nodeIterator.hasNext()) {
            Node node = nodeIterator.next();
            if (node.getString(NETID).equals(netId)) {
                return node;
            }
        }
        return null;
    }

    private void declareGraphFields(Graph graph) {

        //Declare all the properties of a submission (e.g. a node in the graph)
        graph.getNodeTable().addColumn(NETID, String.class);
        graph.getNodeTable().addColumn(PSEUDONYM, String.class);
        graph.getNodeTable().addColumn(IS_SOLUTION, boolean.class);
        graph.getNodeTable().addColumn(SEASON, String.class);
        graph.getNodeTable().addColumn(YEAR, String.class);
        graph.getNodeTable().addColumn(SUBMISSION_ID, String.class);
        graph.getNodeTable().addColumn(CURRENT_SEMESTER, boolean.class);

        //Declare all the properties of an analysis
        graph.getEdgeTable().addColumn(WEIGHT, double.class);   //Weight = max(score1,score2)
        graph.getEdgeTable().addColumn(SCORE1, double.class);
        graph.getEdgeTable().addColumn(SCORE2, double.class);
        graph.getEdgeTable().addColumn(LINK, String.class);
        graph.getEdgeTable().addColumn(IS_PARTNER, boolean.class);
    }

    /**
     * Intializes the progress bar
     *
     * @param showProgress Whether or not to show the progress bar
     * @param dialog       A handle on the progress bar dialog
     */
    private void initializeProgressBar(boolean showProgress, LoadingProgressDialog dialog) {
        dialog.init();
        if (showProgress)
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
    public Connection getConnection() {
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

    /**
     * Gets all courses available from the API
     *
     * @return The list of courses
     */
    public List<Course> getCourses() {
        return courses;
    }

    /**
     * Gets the visual moss graph associated with this project
     *
     * @return
     */
    public VisualMossGraph getVisualMossGraph() {
        return graph;
    }
}
