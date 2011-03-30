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

package edu.illinois.comoto.viz.model;

import edu.illinois.comoto.api.CoMoToAPI;
import edu.illinois.comoto.api.object.Assignment;
import edu.illinois.comoto.api.object.FileSet;
import edu.illinois.comoto.api.object.MossMatch;
import edu.illinois.comoto.api.object.Offering;
import edu.illinois.comoto.api.object.Student;
import edu.illinois.comoto.api.object.Submission;
import edu.illinois.comoto.viz.model.predicates.VisibilityPredicate;
import edu.illinois.comoto.viz.utility.CoMoToVizException;
import edu.illinois.comoto.viz.view.BackendConstants;
import edu.illinois.comoto.viz.view.FrontendConstants;
import edu.illinois.comoto.viz.view.LoadingProgressDialog;
import org.apache.log4j.Logger;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Tuple;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Author:  Charlie Meyer <cemeyer2@illinois.edu>
 * Date:    3/9/11
 * Time:    11:25 PM
 * Package: edu.illinois.comoto.viz.model
 * Created by IntelliJ IDEA.
 */
public class PrefuseGraphBuilder {

    //singleton pattern stuff
    private static PrefuseGraphBuilder instance = new PrefuseGraphBuilder();
    ;
    static final Logger LOGGER = Logger.getLogger(PrefuseGraphBuilder.class);

    public static PrefuseGraphBuilder getBuilder() {
        return instance;
    }

    private Assignment assignment;
    private VisibilityPredicate predicate;
    private boolean showBuildProgress;
    private LoadingProgressDialog dialog;
    private Map<Integer, Edge> edges;
    private Map<Integer, Node> nodes;
    private List<Student> students;

    private PrefuseGraphBuilder() {
        this.predicate = new VisibilityPredicate(BackendConstants.DEFAULT_MINIMUM_EDGE_WEIGHT,
                BackendConstants.DEFAULT_SHOW_SINGLETONS,
                BackendConstants.DEFAULT_SHOW_SOLUTION,
                BackendConstants.DEFAULT_INCLUDE_PAST_STUDENTS,
                BackendConstants.DEFAULT_INCLUDE_PARTNERS);
        showBuildProgress = BackendConstants.DEFAULT_SHOW_BUILD_PROGRESS;
        nodes = new HashMap<Integer, Node>();
        edges = new HashMap<Integer, Edge>();
        students = new LinkedList<Student>();
    }

    public PrefuseGraphBuilder setAssignment(Assignment assignment) {
        this.assignment = assignment;
        this.setShowBuildProgress(BackendConstants.DEFAULT_SHOW_BUILD_PROGRESS);
        return this;
    }

    public Assignment getAssignment() {
        return this.assignment;
    }

    public PrefuseGraphBuilder setMinimumEdgeWeight(double weight) {
        this.predicate.setMinWeight(weight);
        return this;
    }

    public PrefuseGraphBuilder setIncludePartners(boolean includePartners) {
        this.predicate.setIncludePartners(includePartners);
        return this;
    }

    public PrefuseGraphBuilder setShowSolution(boolean showSolution) {
        this.predicate.setShowSolution(showSolution);
        return this;
    }

    public PrefuseGraphBuilder setShowSingletons(boolean showSingletons) {
        this.predicate.setShowSingletons(showSingletons);
        return this;
    }

    public PrefuseGraphBuilder setIncludePastStudents(boolean includePast) {
        this.predicate.setIncludePast(includePast);
        return this;
    }

    public PrefuseGraphBuilder setShowBuildProgress(boolean showBuildProgress) {
        if (this.dialog != null && this.dialog.isVisible()) {
            throw new CoMoToVizException("Cannot switch build progress visibility setting while building graph");
        }
        this.showBuildProgress = showBuildProgress;
        return this;
    }

    public PrefuseGraphBuilder setVisibilityPredicate(VisibilityPredicate predicate) {
        this.predicate = predicate;
        return this;
    }

    public double getMinimumEdgeWeight() {
        return this.predicate.getMinWeight();
    }

    public boolean getIncludePartners() {
        return this.predicate.getIncludePartners();
    }

    public boolean getShowSolution() {
        return this.predicate.getShowSolution();
    }

    public boolean getShowSingletons() {
        return this.predicate.getShowSingletons();
    }

    public boolean getIncludePastStudents() {
        return this.predicate.getIncludePast();
    }

    public VisibilityPredicate getPredicate() {
        return this.predicate;
    }

    public boolean getShowBuildProgress() {
        return this.showBuildProgress;
    }

    /**
     * Builds a Prefuse GraphToBeRemoved object based on the Assignment previously set and on
     * the other settings
     *
     * @return a Prefuse GraphToBeRemoved object representing the backing Assignment as filtered by this builder's settings
     * @see prefuse.data.Graph
     */
    public synchronized Graph buildPrefuseGraph() {

        if (this.getAssignment() == null) {
            throw new CoMoToVizException("Must set the Assignment on PrefuseGraphBuilder before building a GraphToBeRemoved");
        }

        nodes.clear();
        edges.clear();
        students.clear();

        this.initializeLoadingProgressDialog();
        Graph graph = new Graph();
        initializeGraph(graph);
        this.setLoadingProgressDialogMessage(FrontendConstants.BUILDING_STUDENT_DATA_MESSAGE);
        addNodes(graph);
        this.setLoadingProgressDialogMessage(FrontendConstants.BUILDING_MATCH_DATA_MESSAGE);
        addEdges(graph);
        this.setLoadingProgressDialogMessage(FrontendConstants.FILTERING_DATA_MESSAGE);
        filterTuples(graph);
        this.hideLoadingProgressDialog();

        LOGGER.debug("Filtering complete");
        LOGGER.debug("Nodes remaining: " + graph.getNodeCount());
        LOGGER.debug("Edges remaining: " + graph.getEdgeCount());
        this.setShowBuildProgress(!BackendConstants.DEFAULT_SHOW_BUILD_PROGRESS);
        return copyGraph(graph);
    }

    //step 1: initialize the backing prefuse tables
    private void initializeGraph(Graph graph) {
        LOGGER.debug("Initializing graph backing tables");
        //Declare all the properties of a submission (e.g. a node in the graph)
        graph.getNodeTable().addColumn(BackendConstants.NETID, String.class);
        graph.getNodeTable().addColumn(BackendConstants.PSEUDONYM, String.class);
        graph.getNodeTable().addColumn(BackendConstants.IS_SOLUTION, boolean.class);
        graph.getNodeTable().addColumn(BackendConstants.SEASON, String.class);
        graph.getNodeTable().addColumn(BackendConstants.YEAR, String.class);
        graph.getNodeTable().addColumn(BackendConstants.SUBMISSION_ID, int.class);
        graph.getNodeTable().addColumn(BackendConstants.CURRENT_SEMESTER, boolean.class);
        graph.getNodeTable().addColumn(BackendConstants.STUDENT_ID, int.class);
        graph.getNodeTable().addColumn(BackendConstants.CONNECTED_TO_PAST, boolean.class, false);

        //Declare all the properties of an analysis
        graph.getEdgeTable().addColumn(BackendConstants.WEIGHT, double.class);   //Weight = max(score1,score2)
        graph.getEdgeTable().addColumn(BackendConstants.SCORE1, double.class);
        graph.getEdgeTable().addColumn(BackendConstants.SCORE2, double.class);
        graph.getEdgeTable().addColumn(BackendConstants.LINK, String.class);
        graph.getEdgeTable().addColumn(BackendConstants.IS_PARTNER, boolean.class);
        graph.getEdgeTable().addColumn(BackendConstants.MOSSMATCH_ID, int.class);

    }

    //step 2: add the nodes from the students
    private void addNodes(Graph graph) {
        LOGGER.debug("Adding nodes");
        Offering prunedOffering = assignment.getMossAnalysisPrunedOffering();

        List<FileSet> fileSets = assignment.getFilesets(true);
        for (FileSet fileSet : fileSets) {
            Offering offering = fileSet.getOffering();
            boolean isCurrentSemester = true; //default to having all students appear in the graph if the analysis is not pruned
            if (prunedOffering != null) {
                isCurrentSemester = (offering.getId() == prunedOffering.getId());
            }
            List<Submission> submissions = fileSet.getSubmissions(true);
            for (Submission submission : submissions) {
                Student student = submission.getStudent();
                Node node = VisualItemFactory.createNode(graph, student, offering.getSemester(), isCurrentSemester, submission.getId());
                nodes.put(submission.getId(), node);
            }
        }
    }

    //step 3: add the edges from the moss matches
    private void addEdges(Graph graph) {
        LOGGER.debug("Adding edges");
        List<MossMatch> matches = assignment.getAnalysis().getMossAnalysis(true).getMatches();

        for (MossMatch match : matches) {
            Submission submission1 = match.getSubmission1();
            Submission submission2 = match.getSubmission2();
            Node node1 = nodes.get(submission1.getId());
            Node node2 = nodes.get(submission2.getId());
            if (node1 != null && node2 != null) {
                Edge edge = VisualItemFactory.createEdge(graph, match, node1, node2);
                edges.put(match.getId(), edge);
            }
        }
    }

    //step 4: remove edges from the backing data that do not pass the predicate
    private void filterTuples(Graph graph) {
        LOGGER.debug("Filtering tuples");
        List<Tuple> tuplesToRemove = new LinkedList<Tuple>();
        Iterator iter = graph.edges();
        while (iter.hasNext()) {
            Tuple t = (Tuple) iter.next();
            LOGGER.debug("Inspecting tuple: " + t);
            if (!this.predicate.getBoolean(t)) {
                tuplesToRemove.add(t);
            }
        }
        iter = graph.nodes();
        while (iter.hasNext()) {
            Tuple t = (Tuple) iter.next();
            LOGGER.debug("Inspecting tuple: " + t);
            if (!this.predicate.getBoolean(t)) {
                tuplesToRemove.add(t);
            }
        }
        for (Tuple t : tuplesToRemove) {
            boolean removed = graph.removeTuple(t);
            LOGGER.debug("Removing: " + removed);
        }
    }


    //this is a hack to get around a bug in prefuse
    private prefuse.data.Graph copyGraph(prefuse.data.Graph graph) {

        //Create the new graph and declare the fields of each node
        Graph graph2 = new prefuse.data.Graph();
        initializeGraph(graph2);

        //Copy all of the graph nodes
        Iterator<Node> nodeIterator = graph.nodes();
        while (nodeIterator.hasNext()) {
            Node oldNode = nodeIterator.next();
            Node newNode = graph2.addNode();
            newNode.setString(BackendConstants.NETID, oldNode.getString(BackendConstants.NETID));
            newNode.setString(BackendConstants.PSEUDONYM, oldNode.getString(BackendConstants.PSEUDONYM));
            newNode.setString(BackendConstants.IS_SOLUTION, oldNode.getString(BackendConstants.IS_SOLUTION));
            newNode.setString(BackendConstants.SUBMISSION_ID, oldNode.getString(BackendConstants.SUBMISSION_ID));
            newNode.setString(BackendConstants.SEASON, oldNode.getString(BackendConstants.SEASON));
            newNode.setString(BackendConstants.YEAR, oldNode.getString(BackendConstants.YEAR));
            newNode.setBoolean(BackendConstants.CURRENT_SEMESTER, oldNode.getBoolean(BackendConstants.CURRENT_SEMESTER));
            newNode.setBoolean(BackendConstants.CONNECTED_TO_PAST, oldNode.getBoolean(BackendConstants.CONNECTED_TO_PAST));
            if (oldNode.canGetInt(BackendConstants.STUDENT_ID)) {
                int studentId = oldNode.getInt(BackendConstants.STUDENT_ID);
                newNode.setInt(BackendConstants.STUDENT_ID, studentId);
                if (studentId >= 0) {
                    Student student = CoMoToAPI.getStudent(DataImport.getConnection(), studentId, true);
                    students.add(student);
                }
            }
        }

        //Copy all of the graph edges
        Iterator<Edge> edgeIterator = graph.edges();
        while (edgeIterator.hasNext()) {

            Edge oldEdge = edgeIterator.next();
            Edge newEdge = graph2.addEdge(getNode(oldEdge.getSourceNode().getInt(BackendConstants.SUBMISSION_ID), graph2), getNode(oldEdge.getTargetNode().getInt(BackendConstants.SUBMISSION_ID), graph2));
            newEdge.setDouble(BackendConstants.SCORE1, oldEdge.getDouble(BackendConstants.SCORE1));
            newEdge.setDouble(BackendConstants.SCORE2, oldEdge.getDouble(BackendConstants.SCORE2));
            newEdge.setDouble(BackendConstants.WEIGHT, oldEdge.getDouble(BackendConstants.WEIGHT));
            newEdge.setString(BackendConstants.LINK, oldEdge.getString(BackendConstants.LINK));
            newEdge.setBoolean(BackendConstants.IS_PARTNER, oldEdge.getBoolean(BackendConstants.IS_PARTNER));
            newEdge.setInt(BackendConstants.MOSSMATCH_ID, oldEdge.getInt(BackendConstants.MOSSMATCH_ID));
        }
        return graph2;
    }

    /**
     * Finds a node by the netId associated with it
     *
     * @param graph The graph in which to find the node
     * @return The node corresponding to the given netId
     */
    private Node getNode(int submissionId, prefuse.data.Graph graph) {

        Iterator<Node> nodeIterator = graph.nodes();
        while (nodeIterator.hasNext()) {
            Node node = nodeIterator.next();
            if (node.getInt(BackendConstants.SUBMISSION_ID) == submissionId) {
                return node;
            }
        }
        return null;
    }


    private void initializeLoadingProgressDialog() {
        if (this.getShowBuildProgress()) {
            this.dialog = new LoadingProgressDialog(null, "Loading Graph of \"" + this.getAssignment().getName() + "\"", "");
            this.dialog.initializeDialog();
            this.dialog.setVisible(true);
        }
    }

    private void setLoadingProgressDialogMessage(String message) {
        if (this.getShowBuildProgress() && this.dialog != null) {
            this.dialog.setMessage(message);
        }
    }

    private void hideLoadingProgressDialog() {
        if (this.getShowBuildProgress() && this.dialog != null) {
            this.dialog.setVisible(false);
        }
    }

    public List<Student> getStudents() {
        Collections.sort(students);
        return students;
    }

}


