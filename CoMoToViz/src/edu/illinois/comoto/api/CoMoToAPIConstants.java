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

package edu.illinois.comoto.api;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 18, 2010
 * <p/>
 * <p> <p> Holds constants for interacting with the CoMoTo server
 */
public class CoMoToAPIConstants {

    // API URL
    public static final String DEFAULT_HOST = "https://comoto.cs.illinois.edu/comoto/api";

    // API methods
    public static final String GET_ANALYSIS = "getAnalysis";
    public static final String GET_ANALYSIS_PSEUDONYM = "getAnalysisPseudonym";
    public static final String GET_ASSIGNMENTS = "getAssignments";
    public static final String GET_ASSIGNMENT = "getAssignment";
    public static final String GET_COURSE = "getCourse";
    public static final String GET_COURSES = "getCourses";
    public static final String GET_FILE_SET = "getFileSet";
    public static final String GET_JPLAG_ANALYSIS = "getJplagAnalysis";
    public static final String GET_MOSS_ANALYSIS = "getMossAnalysis";
    public static final String GET_MOSS_MATCH = "getMossMatch";
    public static final String GET_MOSS_REPORT = "getMossReport";
    public static final String GET_OFFERING = "getOffering";
    public static final String GET_REPORT = "getReport";
    public static final String GET_SEMESTER = "getSemester";
    public static final String GET_STUDENT = "getStudent";
    public static final String GET_STUDENTS = "getStudents";
    public static final String GET_STUDENT_BY_NETID = "getStudentByNetid";
    public static final String GET_SUBMISSION = "getSubmission";
    public static final String GET_SUBMISSION_FILE = "getSubmissionFile";

    // API objects
    public static final String ANALYSIS_PSEUDONYM = "analysis_pseudonym";
    public static final String ANALYSIS_PSEUDONYMS = "analysis_pseudonyms";
    public static final String CROSS_SEMESTER_MATCHES = "cross_semester_matches";
    public static final String FILESET_IDS = "fileset_ids";
    public static final String JPLAG_REPORT = "jplag_report";
    public static final String MATCHES = "matches";
    public static final String MOSS_REPORT = "moss_report";
    public static final String MOSS_REPORT_FILE_IDS = "moss_report_file_ids";
    public static final String OFFERING = "offering";
    public static final String OFFERINGS = "offerings";
    public static final String OFFERING_INFO = "offering_info";
    public static final String PARTNER_IDS = "partner_ids";
    public static final String SAME_SEMESTER_MATCHES = "same_semester_matches";
    public static final String SEMESTER = "semester";
    public static final String SOLUTION_MATCHES = "solution_matches";
    public static final String STUDENT = "student";
    public static final String SUBMISSIONS = "submissions";
    public static final String SUBMISSION_FILE_IDS = "submission_file_ids";
    public static final String SUBMISSION_IDS = "submission_ids";
    public static final String TIMESTAMP = "timestamp";
}
