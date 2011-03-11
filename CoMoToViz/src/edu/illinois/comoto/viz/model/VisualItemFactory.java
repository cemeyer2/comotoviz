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

import edu.illinois.comoto.api.object.MossMatch;
import edu.illinois.comoto.api.object.Student;
import edu.illinois.comoto.api.object.Submission;
import edu.illinois.comoto.viz.view.BackendConstants;
import edu.illinois.comoto.viz.view.FrontendConstants;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;

import java.net.URL;
import java.util.List;

/**
 * Author:  Charlie Meyer <cemeyer2@illinois.edu>
 * Date:    3/10/11
 * Time:    12:16 PM
 * Package: edu.illinois.comoto.viz.model
 * Created by IntelliJ IDEA.
 */
public class VisualItemFactory {

    public static Node createNode(Graph graph, Student student, boolean currentSemester, int submissionId) {

        boolean isSolution = (student == null);

        //Add this submission's data to the graph
        Node node = graph.addNode();
        if (!isSolution) {
            node.setString(BackendConstants.NETID, student.getNetid());
            node.setString(BackendConstants.PSEUDONYM, Integer.toString(student.getId()));
            //i dont think we need the following 2 table entries
            //node.setString(BackendConstants.SEASON, semester.getSeason().name());
            //node.setString(BackendConstants.YEAR, Integer.toString(semester.getYear()));
            node.setBoolean(BackendConstants.CURRENT_SEMESTER, currentSemester);
            node.setInt(BackendConstants.SUBMISSION_ID, submissionId);
            node.setInt(BackendConstants.STUDENT_ID, student.getId());
        } else {
            node.setString(BackendConstants.NETID, FrontendConstants.SOLUTION);
            node.setString(BackendConstants.PSEUDONYM, FrontendConstants.SOLUTION);
            node.setString(BackendConstants.SEASON, FrontendConstants.SOLUTION);
            node.setString(BackendConstants.YEAR, FrontendConstants.SOLUTION);
            node.setBoolean(BackendConstants.CURRENT_SEMESTER, currentSemester);
            node.setInt(BackendConstants.SUBMISSION_ID, submissionId);
        }
        node.setString(BackendConstants.IS_SOLUTION, Boolean.toString(isSolution));
        return node;
    }

    public static Edge createEdge(Graph graph, MossMatch match, Node node1, Node node2) {

        Edge edge = graph.addEdge(node1, node2);

        //Build the data for this edge
        double score1 = match.getScore1();
        double score2 = match.getScore2();
        double maxScore = Math.max(score1, score2);
        URL matchLink = match.getLink();

        //Figure out if these two were partners or not
        boolean arePartners = arePartnered(match.getSubmission1(), match.getSubmission2());

        //Set this data on the edge
        edge.setDouble(BackendConstants.SCORE1, score1);
        edge.setDouble(BackendConstants.SCORE2, score2);
        edge.setDouble(BackendConstants.WEIGHT, maxScore);
        edge.setString(BackendConstants.LINK, matchLink.toString());
        edge.setBoolean(BackendConstants.IS_PARTNER, arePartners);
        edge.setInt(BackendConstants.MOSSMATCH_ID, match.getId());
        return edge;
    }

    /**
     * Checks to see if two submissions share partners
     *
     * @param submissionOne The first submisison
     * @param submissionTwo The second submisison
     * @return Whether or not these submissions are partnered
     */
    private static boolean arePartnered(Submission submissionOne, Submission submissionTwo) {

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
}
