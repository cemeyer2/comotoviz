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

import edu.illinois.comoto.api.object.*;
import edu.illinois.comoto.viz.model.predicates.VisibilityPredicate;
import edu.illinois.comoto.viz.utility.CoMoToVizException;
import edu.illinois.comoto.viz.view.BackendConstants;
import edu.illinois.comoto.viz.view.FrontendConstants;
import edu.illinois.comoto.viz.view.LoadingProgressDialog;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;

import java.util.HashMap;
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
    private static PrefuseGraphBuilder instance;

    public static PrefuseGraphBuilder getBuilder() {
        if (instance == null) {
            instance = new PrefuseGraphBuilder();
        }
        return instance;
    }

    private final double DEFAULT_MINIMUM_EDGE_WEIGHT = 0.0d;
    private final boolean DEFAULT_SHOW_SINGLETONS = true;
    private final boolean DEFAULT_SHOW_SOLUTION = true;
    private final boolean DEFAULT_INCLUDE_PAST_STUDENTS = false;
    private final boolean DEFAULT_INCLUDE_PARTNERS = true;
    private final boolean DEFAULT_SHOW_BUILD_PROGRESS = true;

    private Assignment assignment;
    private VisibilityPredicate predicate;
    private boolean showBuildProgress;
    private LoadingProgressDialog dialog;
    private Map<Integer, Edge> edges;
    private Map<Integer, Node> nodes;
    private List<Student> students;

    private PrefuseGraphBuilder() {
        this.predicate = new VisibilityPredicate(DEFAULT_MINIMUM_EDGE_WEIGHT,
                DEFAULT_SHOW_SINGLETONS,
                DEFAULT_SHOW_SOLUTION,
                DEFAULT_INCLUDE_PAST_STUDENTS,
                DEFAULT_INCLUDE_PARTNERS);
        showBuildProgress = DEFAULT_SHOW_BUILD_PROGRESS;
        nodes = new HashMap<Integer, Node>();
        edges = new HashMap<Integer, Edge>();
        students = new LinkedList<Student>();
    }

    public PrefuseGraphBuilder setAssignment(Assignment assignment) {
        this.assignment = assignment;
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
        if (this.dialog != null) {
            if (this.dialog.isVisible()) {
                throw new CoMoToVizException("Cannot switch build progress visibility setting while building graph");
            }
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
    public Graph buildPrefuseGraph() {

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
        filterEdges(graph);
        filterNodes(graph);
        this.hideLoadingProgressDialog();

        return graph;
    }

    //step 1: initialize the backing prefuse tables
    private void initializeGraph(Graph graph) {

        //Declare all the properties of a submission (e.g. a node in the graph)
        graph.getNodeTable().addColumn(BackendConstants.NETID, String.class);
        graph.getNodeTable().addColumn(BackendConstants.PSEUDONYM, String.class);
        graph.getNodeTable().addColumn(BackendConstants.IS_SOLUTION, boolean.class);
        graph.getNodeTable().addColumn(BackendConstants.SEASON, String.class);
        graph.getNodeTable().addColumn(BackendConstants.YEAR, String.class);
        graph.getNodeTable().addColumn(BackendConstants.SUBMISSION_ID, String.class);
        graph.getNodeTable().addColumn(BackendConstants.CURRENT_SEMESTER, boolean.class);
        graph.getNodeTable().addColumn(BackendConstants.STUDENT_ID, int.class);

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
                Node node = VisualItemFactory.createNode(graph, student, isCurrentSemester, submission.getId());
                nodes.put(submission.getId(), node);
                if (student != null) {
                    students.add(student);
                }
            }
        }
    }

    //step 3: add the edges from the moss matches
    private void addEdges(Graph graph) {
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

    //step 5: remove nodes from the backing data that do not pass the predicate
    private void filterNodes(Graph graph) {


    }

    //step 4: remove edges from the backing data that do not pass the predicate
    private void filterEdges(Graph graph) {

    }

    private void initializeLoadingProgressDialog() {
        if (this.getShowBuildProgress()) {
            this.dialog = new LoadingProgressDialog(null, "Loading Graph of \"" + this.getAssignment().getName() + "\"", "");
            this.dialog.initialize();
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
        return students;
    }

}


