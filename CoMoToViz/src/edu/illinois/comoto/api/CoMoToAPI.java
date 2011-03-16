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

import edu.illinois.comoto.api.object.*;
import edu.illinois.comoto.api.utility.AccessTemplate;
import edu.illinois.comoto.api.utility.Cache;
import edu.illinois.comoto.api.utility.Connection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 * <p/>
 * <p> <p> The primary class for interacting with the CoMoTo API
 */

public class CoMoToAPI {

    private CoMoToAPI() {
    }

    /**
     * Gets an analysis by its id
     *
     * @param connection The connection to use to grab the analysis
     * @param analysisId The id of the analysis
     * @return An analysis identified by its id
     */
    public static Analysis getAnalysis(Connection connection, int analysisId) {
        Cacheable c = Cache.get(Analysis.class, analysisId);
        if (c != null) {
            return (Analysis) c;
        }
        //Get the object from the API
        Map abstractAnalysis = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_ANALYSIS, analysisId);

        //Build the resulting data structure
        Analysis a = new Analysis(abstractAnalysis, connection);
        Cache.put(a);
        return a;
    }

    /**
     * Gets an analysis pseudonym by its id
     *
     * @param connection          The connection to use to grab the analysis
     * @param analysisPseudonymId The id of the analysis pseudonym
     * @return An analysis pseudonym identified by its id
     */
    public static AnalysisPseudonym getAnalysisPseudonym(Connection connection, int analysisPseudonymId) {
        Cacheable c = Cache.get(AnalysisPseudonym.class, analysisPseudonymId);
        if (c != null) {
            return (AnalysisPseudonym) c;
        }
        //Get the object from the API
        Map abstractAnalysisPseudonym = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_ANALYSIS_PSEUDONYM, analysisPseudonymId);

        //Build the resulting data structure
        AnalysisPseudonym ap = new AnalysisPseudonym(abstractAnalysisPseudonym, connection);
        Cache.put(ap);
        return ap;
    }

    /**
     * Gets an assignment by its id from the API.
     *
     * @param connection   The connection to use to grab the list of assignments
     * @param assignmentId The id of the assignment to grab
     * @return The assignment
     */
    public static Assignment getAssignment(Connection connection, int assignmentId) {
        Cacheable c = Cache.get(Assignment.class, assignmentId);
        if (c != null) {
            return (Assignment) c;
        }
        //Get the object from the API
        Map abstractAssignment = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_ASSIGNMENT, assignmentId);

        //Build the resulting list of assignments
        Assignment a = new Assignment(abstractAssignment, connection);
        Cache.put(a);
        return a;
    }

    /**
     * Gets the list of all assignments from a specific course
     *
     * @param connection The connection to use to grab the list of assignments
     * @param courseId   The id of the class whose assignments to grab
     * @return The list of assignments
     */
    public static List<Assignment> getAssignments(Connection connection, int courseId) {

        //Get the object from the API
        Object[] abstractAssignments = AccessTemplate.getArray(connection, CoMoToAPIConstants.GET_ASSIGNMENTS, courseId);

        //Build the resulting list of assignments
        List<Assignment> assignments = new ArrayList<Assignment>();
        for (Object abstractAssignment : abstractAssignments) {

            Assignment assignment = new Assignment((Map) abstractAssignment, connection);
            assignments.add(assignment);
            Cache.put(assignment);
        }

        return assignments;
    }

    /**
     * Gets a course by its id from the API
     *
     * @param connection The connection to use to grab the data
     * @param courseId   The id of the course to grab
     * @return The course
     */
    public static Course getCourse(Connection connection, int courseId) {
        Cacheable c = Cache.get(Course.class, courseId);
        if (c != null) {
            return (Course) c;
        }
        //Get the object from the API
        Map abstractCourse = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_COURSE, courseId);

        //Build the resulting list of assignments
        Course course = new Course(abstractCourse, connection);
        Cache.put(course);
        return course;
    }

    /**
     * Gets a course by its id from the API
     *
     * @param connection        The connection to use to grab the data
     * @param courseId          The id of the course to grab
     * @param extraOfferingInfo Indicates whether we should return extra information about this offering
     * @return The course
     */
    public static Course getCourse(Connection connection, int courseId, boolean extraOfferingInfo) {
        Cacheable c = Cache.get(Course.class, courseId);
        if (c != null) {
            return (Course) c;
        }
        //Get the object from the API
        Map abstractCourse = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_COURSE, courseId, extraOfferingInfo);

        //Build the resulting list of assignments
        Course course = new Course(abstractCourse, connection);
        Cache.put(course);
        return course;
    }

    /**
     * Gets the list of all courses from the CoMoTo API
     *
     * @param connection The connection from which to grab this data
     * @return The list of courses
     */
    public static List<Course> getCourses(Connection connection) {

        //Get the courses from the API
        Object[] abstractCourses = AccessTemplate.getArray(connection, CoMoToAPIConstants.GET_COURSES);

        //Build the list of courses
        List<Course> courses = new ArrayList<Course>();
        for (Object abstractCourse : abstractCourses) {

            Course course = new Course((Map) abstractCourse, connection);
            courses.add(course);
            Cache.put(course);
        }

        return courses;
    }

    /**
     * Gets the list of all courses from the CoMoTo API
     *
     * @param connection        The connection from which to grab this data
     * @param extraOfferingInfo Whether we should get extra offering information from the API as well
     * @return The list of courses
     */
    public static List<Course> getCourses(Connection connection, boolean extraOfferingInfo) {

        //Get the courses from the API
        Object[] abstractCourses = AccessTemplate.getArray(connection, CoMoToAPIConstants.GET_COURSES, extraOfferingInfo);

        //Build the list of courses
        List<Course> courses = new ArrayList<Course>();
        for (Object abstractCourse : abstractCourses) {

            Course course = new Course((Map) abstractCourse, connection);
            courses.add(course);
            Cache.put(course);
        }

        return courses;
    }

    /**
     * Get a file set from the CoMoTo API
     *
     * @param connection The connection from which to grab this data
     * @param fileSetId  The id of the file set to grab
     * @return The file set
     */
    public static FileSet getFileSet(Connection connection, int fileSetId) {
        Cacheable c = Cache.get(FileSet.class, fileSetId);
        if (c != null) {
            return (FileSet) c;
        }
        //Get the file set from the api
        Map abstractFileSet = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_FILE_SET, fileSetId);

        //Build the file set
        FileSet fs = new FileSet(abstractFileSet, connection);
        Cache.put(fs);
        return fs;
    }

    /**
     * Get a file set from the CoMoTo API
     *
     * @param connection         The connection from which to grab this data
     * @param fileSetId          The id of the file set to grab
     * @param fullSubmissionInfo Whether to grab the full submission info for this file set
     * @return The file set
     */
    public static FileSet getFileSet(Connection connection, int fileSetId, boolean fullSubmissionInfo) {
        Cacheable c = Cache.get(FileSet.class, fileSetId);
        if (c != null) {
            return (FileSet) c;
        }
        //Get the file set from the api
        Map abstractFileSet = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_FILE_SET, fileSetId, fullSubmissionInfo);

        //Build the file set
        FileSet fs = new FileSet(abstractFileSet, connection);
        Cache.put(fs);
        return fs;
    }

    /**
     * Get a file set from the CoMoTo API
     *
     * @param connection         The connection from which to grab this data
     * @param fileSetId          The id of the file set to grab
     * @param fullSubmissionInfo Whether to grab the full submission data from the API
     * @param extraOfferingInfo  Whether to grab the extra offering information from the API as well
     * @return The file set
     */
    public static FileSet getFileSet(Connection connection, int fileSetId, boolean fullSubmissionInfo, boolean extraOfferingInfo) {
        Cacheable c = Cache.get(FileSet.class, fileSetId);
        if (c != null) {
            return (FileSet) c;
        }
        //Get the file set from the api
        Map abstractFileSet = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_FILE_SET, fileSetId, fullSubmissionInfo, extraOfferingInfo);

        //Build the file set
        FileSet fs = new FileSet(abstractFileSet, connection);
        Cache.put(fs);
        return fs;
    }

    /**
     * Get a jplag analysis from the CoMoTo API
     *
     * @param connection      The connection from which to grab this data
     * @param jplagAnalysisId The id of the jplag analysis set to grab
     * @return The jplag analysis
     */
    public static JplagAnalysis getJplagAnalysis(Connection connection, int jplagAnalysisId) {
        Cacheable c = Cache.get(JplagAnalysis.class, jplagAnalysisId);
        if (c != null) {
            return (JplagAnalysis) c;
        }
        //Get the file set from the api
        Map abstractJplagAnalysis = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_JPLAG_ANALYSIS, jplagAnalysisId);

        //Build the file set
        JplagAnalysis jpa = new JplagAnalysis(abstractJplagAnalysis, connection);
        Cache.put(jpa);
        return jpa;
    }

    /**
     * Gets a moss analysis from the CoMoTo API
     *
     * @param connection     The connection from which to grab this data
     * @param mossAnalysisId The id of the moss analysis to grab
     * @return The moss analysis from the API
     */
    public static MossAnalysis getMossAnalysis(Connection connection, int mossAnalysisId) {
        Cacheable c = Cache.get(MossAnalysis.class, mossAnalysisId);
        if (c != null) {
            return (MossAnalysis) c;
        }
        //Get the file set from the api
        Map abstractMossAnalysis = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_MOSS_ANALYSIS, mossAnalysisId);

        //Build the file set
        MossAnalysis ma = new MossAnalysis(abstractMossAnalysis, connection);
        Cache.put(ma);
        return ma;
    }

    /**
     * Gets a moss analysis from the CoMoTo API
     *
     * @param connection        The connection from which to grab this data
     * @param mossAnalysisId    The id of the moss analysis to grab
     * @param categorizeMatches Whether we should get back categorized matches rather than one pile of matches
     * @return The moss analysis from the API
     */
    public static MossAnalysis getMossAnalysis(Connection connection, int mossAnalysisId, boolean categorizeMatches) {
        Cacheable c = Cache.get(MossAnalysis.class, mossAnalysisId);
        if (c != null) {
            return (MossAnalysis) c;
        }
        //Get the file set from the api
        Map abstractMossAnalysis = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_MOSS_ANALYSIS, mossAnalysisId, categorizeMatches);

        //Build the file set
        MossAnalysis ma = new MossAnalysis(abstractMossAnalysis, connection);
        Cache.put(ma);
        return ma;
    }

    /**
     * Gets a moss analysis from the CoMoTo API
     *
     * @param connection        The connection from which to grab this data
     * @param mossAnalysisId    The id of the moss analysis to grab
     * @param categorizeMatches Whether we should get back categorized matches rather than one pile of matches
     * @param minimumMatchScore The minimum threshold for match scores
     * @return The moss analysis from the API
     */
    public static MossAnalysis getMossAnalysis(Connection connection, int mossAnalysisId, boolean categorizeMatches,
                                               int minimumMatchScore) {
        Cacheable c = Cache.get(MossAnalysis.class, mossAnalysisId);
        if (c != null) {
            return (MossAnalysis) c;
        }
        //Get the file set from the api
        Map abstractMossAnalysis = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_MOSS_ANALYSIS, mossAnalysisId, categorizeMatches, minimumMatchScore);

        //Build the file set
        MossAnalysis ma = new MossAnalysis(abstractMossAnalysis, connection);
        Cache.put(ma);
        return ma;
    }

    /**
     * Gets a moss analysis from the CoMoTo API
     *
     * @param connection        The connection from which to grab this data
     * @param mossAnalysisId    The id of the moss analysis to grab
     * @param categorizeMatches Whether we should get back categorized matches rather than one pile of matches
     * @param minimumMatchScore The minimum threshold for match scores
     * @param singleStudentMaxMatchesLowerBound
     *                          The lower bound for the maximum number of matches
     * @return The moss analysis from the API
     */
    public static MossAnalysis getMossAnalysis(Connection connection, int mossAnalysisId, boolean categorizeMatches,
                                               int minimumMatchScore, int singleStudentMaxMatchesLowerBound) {
        Cacheable c = Cache.get(MossAnalysis.class, mossAnalysisId);
        if (c != null) {
            return (MossAnalysis) c;
        }
        //Get the file set from the api
        Map abstractMossAnalysis = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_MOSS_ANALYSIS, mossAnalysisId, categorizeMatches, minimumMatchScore,
                singleStudentMaxMatchesLowerBound);

        //Build the file set
        MossAnalysis ma = new MossAnalysis(abstractMossAnalysis, connection);
        Cache.put(ma);
        return ma;
    }

    /**
     * Gets a moss analysis from the CoMoTo API
     *
     * @param connection        The connection from which to grab this data
     * @param mossAnalysisId    The id of the moss analysis to grab
     * @param categorizeMatches Whether we should get back categorized matches rather than one pile of matches
     * @param minimumMatchScore The minimum threshold for match scores
     * @param singleStudentMaxMatchesLowerBound
     *                          The lower bound for the maximum number of matches
     * @param singleStudentMaxMatchesUpperBound
     *                          The upper bound for the maximum number of matches
     * @return The moss analysis from the API
     */
    public static MossAnalysis getMossAnalysis(Connection connection, int mossAnalysisId, boolean categorizeMatches,
                                               int minimumMatchScore, int singleStudentMaxMatchesLowerBound,
                                               int singleStudentMaxMatchesUpperBound) {
        Cacheable c = Cache.get(MossAnalysis.class, mossAnalysisId);
        if (c != null) {
            return (MossAnalysis) c;
        }
        //Get the file set from the api
        Map abstractMossAnalysis = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_MOSS_ANALYSIS, mossAnalysisId, categorizeMatches, minimumMatchScore,
                singleStudentMaxMatchesLowerBound, singleStudentMaxMatchesUpperBound);

        //Build the file set
        MossAnalysis ma = new MossAnalysis(abstractMossAnalysis, connection);
        Cache.put(ma);
        return ma;
    }

    /**
     * Gets a moss match object by its id
     *
     * @param connection  The connection from which to grab the data
     * @param mossMatchId The id of the moss match
     * @return The moss match
     */
    public static MossMatch getMossMatch(Connection connection, int mossMatchId) {
        Cacheable c = Cache.get(MossMatch.class, mossMatchId);
        if (c != null) {
            return (MossMatch) c;
        }
        //Get the moss match from the API
        Map abstractMossMatch = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_MOSS_MATCH, mossMatchId);

        //Build the file set
        MossMatch mm = new MossMatch(abstractMossMatch, connection);
        Cache.put(mm);
        return mm;
    }

    /**
     * Gets a moss report from the CoMoTo API
     *
     * @param connection   The connection from which to grab the data
     * @param mossReportId The id of the moss report
     * @return The moss report object
     */
    public static MossReport getMossReport(Connection connection, int mossReportId) {
        Cacheable c = Cache.get(MossReport.class, mossReportId);
        if (c != null) {
            return (MossReport) c;
        }
        //Get the file set from the api
        Map abstractMossReport = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_MOSS_REPORT, mossReportId);

        //Build the file set
        MossReport mr = new MossReport(abstractMossReport, connection);
        Cache.put(mr);
        return mr;
    }

    /**
     * Gets an offering object from the API
     *
     * @param connection The connection rom which to grab the data
     * @param offeringId The id of the offering to grab
     * @return The offering object from the API
     */
    public static Offering getOffering(Connection connection, int offeringId) {
        Cacheable c = Cache.get(Offering.class, offeringId);
        if (c != null) {
            return (Offering) c;
        }
        //Get the offering from the api
        Map abstractOffering = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_OFFERING, offeringId);

        //Build the offering
        Offering o = new Offering(abstractOffering, connection);
        Cache.put(o);
        return o;
    }

    /**
     * Gets an offering object from the API
     *
     * @param connection The connection rom which to grab the data
     * @param offeringId The id of the offering to grab
     * @param extraInfo  Whether to grab the extra information for this offering
     * @return The offering object from the API
     */
    public static Offering getOffering(Connection connection, int offeringId, boolean extraInfo) {
        Cacheable c = Cache.get(Offering.class, offeringId);
        if (c != null) {
            return (Offering) c;
        }
        //Get the offering from the api
        Map abstractOffering = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_OFFERING, offeringId, extraInfo);

        //Build the offering
        Offering o = new Offering(abstractOffering, connection);
        Cache.put(o);
        return o;
    }

    /**
     * Gets a report from the CoMoTo API
     *
     * @param connection The connection from which to grab this data
     * @param reportId   The id of the report to grab
     * @return The report from the API
     */
    public static Report getReport(Connection connection, int reportId) {
        Cacheable c = Cache.get(Report.class, reportId);
        if (c != null) {
            return (Report) c;
        }
        //Get the file set from the api
        Map abstractReport = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_REPORT, reportId);

        //Build the file set
        Report r = new Report(abstractReport, connection);
        Cache.put(r);
        return r;
    }

    /**
     * Gets a semester by its id
     *
     * @param connection The connection from which to grab the data
     * @param semesterId The id of the semester
     * @return The semester
     */
    public static Semester getSemester(Connection connection, int semesterId) {
        Cacheable c = Cache.get(Semester.class, semesterId);
        if (c != null) {
            return (Semester) c;
        }
        //Get the file set from the api
        Map abstractSemester = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_SEMESTER, semesterId);

        //Build the file set
        Semester s = new Semester(abstractSemester, connection);
        Cache.put(s);
        return s;
    }

    /**
     * Gets a student from the CoMoTo API
     *
     * @param connection The connection from which to grab this data
     * @param studentId  The id of the student to grab
     * @return The student from the API
     */
    public static Student getStudent(Connection connection, int studentId) {
        Cacheable cached = Cache.get(Student.class, studentId);
        if (cached != null) {
            return (Student) cached;
        }
        //Get the file set from the api
        Map abstractStudent = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_STUDENT, studentId);

        //Build the file set
        Student s = new Student(abstractStudent, connection);
        Cache.put(s);
        return s;
    }

    /**
     * Gets a student from the CoMoTo API
     *
     * @param connection  The connection from which to grab this data
     * @param studentId   The id of the student to grab
     * @param showHistory Whether to get the student's history as well
     * @return The student from the API
     */
    public static Student getStudent(Connection connection, int studentId, boolean showHistory) {
        Cacheable cached = Cache.get(Student.class, studentId);
        if (cached != null) {
            return (Student) cached;
        }
        //Get the file set from the api
        Map abstractStudent = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_STUDENT, studentId, showHistory);

        //Build the file set
        Student s = new Student(abstractStudent, connection);
        Cache.put(s);
        return s;
    }

    /**
     * Gets a student from the CoMoTo API
     *
     * @param connection        The connection from which to grab this data
     * @param studentId         The id of the student to grab
     * @param showHistory       Whether to get the student's history as well
     * @param minimumMatchScore The threshold below which to ignore matches in the students history
     * @return The student from the API
     */
    public static Student getStudent(Connection connection, int studentId, boolean showHistory, int minimumMatchScore) {
        Cacheable cached = Cache.get(Student.class, studentId);
        if (cached != null) {
            return (Student) cached;
        }
        //Get the file set from the api
        Map abstractStudent = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_STUDENT, studentId, showHistory, minimumMatchScore);

        //Build the file set
        Student s = new Student(abstractStudent, connection);
        Cache.put(s);
        return s;
    }


    /**
     * Gets a list of students from the CoMoTo API
     *
     * @param connection  The connection from which to grab this data
     * @param studentIds  The ids of the student to grab
     * @param showHistory W hether to get the student's history as well
     * @return The students from the API
     */
    public static List<Student> getStudents(Connection connection, int[] studentIds, boolean showHistory) {

        //Get the file set from the api
        Object[] abstractStudents = AccessTemplate.getArray(connection, CoMoToAPIConstants.GET_STUDENTS, studentIds, showHistory);

        //Build the resulting list of students
        List<Student> students = new ArrayList<Student>();
        for (Object abstractStudent : abstractStudents) {
            Student student = new Student((Map) abstractStudent, connection);
            students.add(student);
            Cache.put(student);
        }
        return students;
    }

    /**
     * Gets a student from the CoMoTo API
     *
     * @param connection The connection from which to grab this data
     * @param netid      The netid of the student to grab
     * @return The student from the API
     */
    public static Student getStudentByNetid(Connection connection, String netid) {

        //Get the file set from the api
        Map abstractStudent = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_STUDENT_BY_NETID, netid);

        //Build the file set
        Student s = new Student(abstractStudent, connection);
        Cache.put(s);
        return s;
    }

    /**
     * Gets a student from the CoMoTo API
     *
     * @param connection  The connection from which to grab this data
     * @param netid       The netid of the student to grab
     * @param showHistory Whether to get the student's history as well
     * @return The student from the API
     */
    public static Student getStudentByNetid(Connection connection, String netid, boolean showHistory) {

        //Get the file set from the api
        Map abstractStudent = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_STUDENT_BY_NETID, netid, showHistory);

        //Build the file set
        Student s = new Student(abstractStudent, connection);
        Cache.put(s);
        return s;
    }

    /**
     * Gets a student from the CoMoTo API
     *
     * @param connection        The connection from which to grab this data
     * @param netid             The id of the student to grab
     * @param showHistory       Whether to get the student's history as well
     * @param minimumMatchScore The threshold below which to ignore matches in the students history
     * @return The student from the API
     */
    public static Student getStudentByNetid(Connection connection, String netid, boolean showHistory, int minimumMatchScore) {

        //Get the file set from the api
        Map abstractStudent = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_STUDENT_BY_NETID, netid, showHistory, minimumMatchScore);

        //Build the file set
        Student s = new Student(abstractStudent, connection);
        Cache.put(s);
        return s;
    }

    /**
     * Gets a submission from the CoMoTo API
     *
     * @param connection   The connection from which to grab this data
     * @param submissionId The id of the submission to grab
     * @return The submission from the API
     */
    public static Submission getSubmission(Connection connection, int submissionId) {
        Cacheable cached = Cache.get(Submission.class, submissionId);
        if (cached != null) {
            return (Submission) cached;
        }
        //Get the file set from the api
        Map abstractSubmission = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_SUBMISSION, submissionId);

        //Build the file set
        Submission s = new Submission(abstractSubmission, connection);
        Cache.put(s);
        return s;
    }

    /**
     * Gets a submission from the CoMoTo API
     *
     * @param connection      The connection from which to grab this data
     * @param submissionId    The id of the submission to grab
     * @param fullStudentData Whether to grab the full student data from the API with the submission data
     * @return The submission from the API
     */
    public static Submission getSubmission(Connection connection, int submissionId, boolean fullStudentData) {
        Cacheable cached = Cache.get(Submission.class, submissionId);
        if (cached != null) {
            return (Submission) cached;
        }
        //Get the file set from the api
        Map abstractSubmission = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_SUBMISSION, submissionId, fullStudentData);

        //Build the file set
        Submission s = new Submission(abstractSubmission, connection);
        Cache.put(s);
        return s;
    }

    /**
     * Gets a submission file from the CoMoTo API
     *
     * @param connection       The connection from which to grab this data
     * @param submissionFileId The id of the submission file to grab
     * @return The submission file from the API
     */
    public static SubmissionFile getSubmissionFile(Connection connection, int submissionFileId) {
        Cacheable cached = Cache.get(SubmissionFile.class, submissionFileId);
        if (cached != null) {
            return (SubmissionFile) cached;
        }
        //Get the file set from the api
        Map abstractSubmissionFile = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_SUBMISSION_FILE, submissionFileId);

        //Build the file set
        SubmissionFile sf = new SubmissionFile(abstractSubmissionFile, connection);
        Cache.put(sf);
        return sf;
    }

    /**
     * Gets a submission file from the CoMoTo API
     *
     * @param connection       The connection from which to grab this data
     * @param submissionFileId The id of the submission file to grab
     * @param highlighted      Whether to return the submission file as a highlighted file
     * @return The submission file from the API
     */
    public static SubmissionFile getSubmissionFile(Connection connection, int submissionFileId, boolean highlighted) {
        Cacheable cached = Cache.get(SubmissionFile.class, submissionFileId);
        if (cached != null) {
            return (SubmissionFile) cached;
        }
        //Get the file set from the api
        Map abstractSubmissionFile = AccessTemplate.getMap(connection, CoMoToAPIConstants.GET_SUBMISSION_FILE, submissionFileId, highlighted);

        //Build the file set
        SubmissionFile sf = new SubmissionFile(abstractSubmissionFile, connection);
        Cache.put(sf);
        return sf;
    }
}
