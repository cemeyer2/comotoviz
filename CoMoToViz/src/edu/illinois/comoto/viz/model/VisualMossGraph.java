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

import edu.illinois.comoto.viz.view.BackendConstants;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLReader;

import java.io.File;
import java.io.InputStream;
import java.util.*;


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
     * @param courseName     The name of the course
     * @param assignmentName The name of this assignment
     */
    public VisualMossGraph(String courseName, String assignmentName) {
        students = new Hashtable<String, VisualMossGraphStudent>();
        matches = new ArrayList<VisualMossGraphMatch>();
        this.courseName = courseName;
        this.assignmentName = assignmentName;
    }

    /**
     * Creates a new VisualMossGraph from some arbitrary input stream and course and assignment names
     *
     * @param stream         The input stream from which to read, containing a graph in GraphML format
     * @param courseName     The name of this course
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
     * @param inputFile      The input file from which to read, containing a graph in GraphML format
     * @param courseName     The name of this course
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
     * @param inputGraph     The input graph from which to read
     * @param courseName     The name of this course
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
        while (nodeIterator.hasNext()) {

            //Grab a node from the graph
            Node node = nodeIterator.next();

            //Extract its data
            String netid = node.getString(BackendConstants.NETID);
            String pseudonym = node.getString(BackendConstants.PSEUDONYM);

            //Build a new VisualMossGraphStudent object to old the basic information about this student
            VisualMossGraphStudent student = new VisualMossGraphStudent(netid, pseudonym);
            students.put(netid, student);
        }

        //Build the match objects from the graph
        nodeIterator = graph.edges();
        while (nodeIterator.hasNext()) {

            //Grab an edge from the graph
            Edge edge = (Edge) nodeIterator.next();

            // Extract its data
            VisualMossGraphStudent student1 = students.get(edge.getSourceNode().getString(BackendConstants.NETID));
            VisualMossGraphStudent student2 = students.get(edge.getTargetNode().getString(BackendConstants.NETID));
            double score1 = edge.getDouble(BackendConstants.SCORE1);
            double score2 = edge.getDouble(BackendConstants.SCORE1);

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