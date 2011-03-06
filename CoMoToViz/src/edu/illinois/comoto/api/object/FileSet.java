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

package edu.illinois.comoto.api.object;

import edu.illinois.comoto.api.CoMoToAPI;
import edu.illinois.comoto.api.CoMoToAPIConstants;
import edu.illinois.comoto.api.utility.Cache;
import edu.illinois.comoto.api.utility.Connection;
import edu.illinois.comoto.api.utility.Reflector;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.illinois.comoto.api.CoMoToAPIConstants.OFFERING;
import static edu.illinois.comoto.api.CoMoToAPIConstants.SUBMISSIONS;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 * <p/>
 * <p> <p> Holds the data of a file set
 */
public class FileSet implements Refreshable, Cacheable {

    /**
     * The list of ids for the associated assignments
     */
    private List<Integer> assignmentIds;

    /**
     * The id of the associated course
     */
    private int courseId;

    /**
     * Unique id for this file set
     */
    private int id;

    /**
     * Flags whether this file set is complete
     */
    private boolean isComplete;

    /**
     * The associated offering for this file set, loaded eagerly
     */
    private Offering offering;

    /**
     * The list of ids for the associated submissions
     */
    private List<Integer> submissionIds;

    /**
     * The list of associated submission
     */
    private List<Submission> submissions;

    /**
     * The timestamp for this file set
     */
    private DateFormat timestamp;

    /**
     * Enumerates the type for this class
     */
    private Type type = Type.fileset;

    /**
     * The associated course object
     */
    private Course course = null;

    /**
     * The list of associated assignments
     */
    private List<Assignment> assignments = null;

    /**
     * Connection to the API
     */
    private Connection connection;

    /**
     * Records whether this object pulls all the submisison info eagerly from the API
     */
    private boolean fullSubmissionInfo = false;

    /**
     * Constructs this file set
     *
     * @param abstractFileSet A map holding the data of this file set
     * @param connection      A connection to the API to use for lazily loading and refreshing this object
     */
    public FileSet(Map<String, Object> abstractFileSet, Connection connection) {

        //Save the connection
        this.connection = connection;

        //Explicitly add the 'offering' object
        Map offeringMap = (Map) abstractFileSet.get(CoMoToAPIConstants.OFFERING);
        if (offeringMap != null) {
            offering = new Offering(offeringMap, connection);
            Cache.put(offering);
            abstractFileSet.remove(OFFERING);
        }

        //Explicitly add the submission objects
        Object[] abstractSubmissions = (Object[]) abstractFileSet.get(SUBMISSIONS);
        if (abstractSubmissions != null) {
            fullSubmissionInfo = true;
            submissions = new ArrayList<Submission>();
            for (Object abstractSubmission : abstractSubmissions) {
                Submission s = new Submission((Map<String, Object>) abstractSubmission, connection);
                Cache.put(s);
                submissions.add(s);
            }
            abstractFileSet.remove(SUBMISSIONS);
        }

        //Populate the rest of this object using reflection
        Reflector<FileSet> reflector = new Reflector<FileSet>();
        reflector.populate(this, abstractFileSet);
    }


    /**
     * {@inheritDoc}
     */
    public void refresh() {
        Cache.remove(this);
        //Grab the new file set from the API
        FileSet newFileSet;
        if (fullSubmissionInfo) {
            newFileSet = CoMoToAPI.getFileSet(connection, id, true);
        } else {
            newFileSet = CoMoToAPI.getFileSet(connection, id, false);
        }

        //Copy the data over
        assignmentIds = newFileSet.getAssignmentIds();
        courseId = newFileSet.getCourseId();
        isComplete = newFileSet.isComplete();
        offering = newFileSet.getOffering();
        submissionIds = newFileSet.getSubmissionIds();
        timestamp = newFileSet.getTimestamp();
        type = newFileSet.getType();

        //Clear the cached data
        if (!fullSubmissionInfo) {
            submissions = null;
        }
        course = null;
        assignments = null;
    }

    /**
     * Grabs the associated course lazily
     *
     * @return The course
     */
    public Course getCourse() {

        //Grab the course from the API if not cached
        if (course == null) {
            course = CoMoToAPI.getCourse(connection, courseId);
        }
        return course;
    }

    /**
     * Grabs the list of assignments from the api if not cached in this object
     *
     * @return A list of the assignments associated with this course
     */
    public List<Assignment> getAssignments() {

        //Load the assignments from the API if not cached
        if (assignments == null) {
            assignments = new ArrayList<Assignment>();
            for (int assignmentId : assignmentIds) {
                assignments.add(CoMoToAPI.getAssignment(connection, assignmentId));
            }
        }
        return assignments;
    }

    /**
     * Grabs the list of submissions from the api if not cached in this object
     *
     * @return A list of the submissions associated with this file set
     */
    public List<Submission> getSubmissions() {
        return getSubmissions(false);
    }

    /**
     * Grabs the list of submissions from the api if not cached in this object
     *
     * @param fullStudentData Whether to eagerly load the student data from the API
     * @return A list of the submissions associated with this file set
     */
    public List<Submission> getSubmissions(boolean fullStudentData) {

        //Load the assignments from the API if not cached
        if (submissions == null) {
            submissions = new ArrayList<Submission>();
            for (int submissionId : submissionIds) {
                submissions.add(CoMoToAPI.getSubmission(connection, submissionId, fullStudentData));
            }
        }
        return submissions;
    }

    public Map getMap() {
        return new HashMap();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Offering getOffering() {
        return offering;
    }

    public void setOffering(Offering offering) {
        this.offering = offering;
    }

    public DateFormat getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateFormat timestamp) {
        this.timestamp = timestamp;
    }

    public List<Integer> getAssignmentIds() {
        return assignmentIds;
    }

    public void setAssignmentIds(List<Integer> assignmentIds) {
        this.assignmentIds = assignmentIds;
    }

    public List<Integer> getSubmissionIds() {
        return submissionIds;
    }

    public void setSubmissionIds(List<Integer> submissionIds) {
        this.submissionIds = submissionIds;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
