package edu.uiuc.cs.visualmoss.dataimport;

import edu.uiuc.cs.visualmoss.VisualMossConstants;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConnection;
import edu.uiuc.cs.visualmoss.dataimport.api.objects.*;
import edu.uiuc.cs.visualmoss.graph.VisualMossGraph;
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

import static edu.uiuc.cs.visualmoss.graph.VisualMossGraphConstants.*;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Nov 3, 2010
 *
 * <p> <p> This class drives the import of data into the visual moss graph structure using the CoMoTo API
 */
public class NewDataImport {

    private VisualMossGraph graph;
    private CoMoToAPIConnection connection;
    private List<Course> courses;
    private ArrayList<Submission> submissions;

    /**
     * Imports the data from a given file, intended to be a file written in GraphML format
     *
     * @param inputFile  The file from which to import the data
     * @throws prefuse.data.io.DataIOException  On an error accessing the file data
     */
	public NewDataImport(File inputFile) throws DataIOException {

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
    public NewDataImport(File inputFile, String courseName, String assignmentName) throws DataIOException {

        //Builds a graph with the given class and assignment from the given input file
		graph = new VisualMossGraph(inputFile, courseName, assignmentName);
    }

    public VisualMossGraph buildGraph(Assignment assignment, boolean showProgress, JFrame parent) {

        //Create the progress bar
        LoadingProgressDialog dialog = new LoadingProgressDialog(parent, "Loading", "Loading Graph...");

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

        //Estimate the proportion of data grabbed by counting the number of file sets
        estimateTotalWorkForProgressBar(showProgress, dialog, fileSets, matches);

        //Get a representation of the submissions as id, object pairs
        Hashtable<Integer, Submission> submissionsTable = new Hashtable<Integer, Submission>();
        Hashtable<Integer, Node> nodeTable = new Hashtable<Integer, Node>();

        //Add submission data to the graph
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

        //Add analysis data to the graph
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
            //TODO: Add this functionality!

            //Set this data on the edge
            edge.setDouble(SCORE1, score1);
            edge.setDouble(SCORE2, score2);
            edge.setDouble(WEIGHT, maxScore);
            edge.setString(LINK, matchLink.toString());

            //Update the progress bar
            progress = updateProgressBar(showProgress, dialog, progress);
        }
        dialog.setIndeterminate(true);

        //Clean up the solution

        //Remove old semester nodes that aren't connected to the rest of the graph
        dialog.setVisible(false);
        this.graph = new VisualMossGraph(copyGraph(graph), assignment.getCourse().getName(), assignment.getName());
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

    private int updateProgressBar(boolean showProgress, LoadingProgressDialog dialog, int progress) {
        if(showProgress){
            progress++;
            dialog.setValue(progress);
        }
        return progress;
    }

    private void initializeProgressBar(boolean showProgress, LoadingProgressDialog dialog) {
        dialog.init();
        if(showProgress)
            dialog.setVisible(true);
    }

    public VisualMossGraph getGraph() {
        return graph;
    }

    public CoMoToAPIConnection getConnection() {
        return connection;
    }

    public List<Course> getCourses() {
        return courses;
    }
}
