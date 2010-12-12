package edu.uiuc.cs.visualmoss.dataimport.api;

import edu.uiuc.cs.visualmoss.dataimport.api.objects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIAccessTemplate.getArray;
import static edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIAccessTemplate.getMap;
import static edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConstants.*;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> The primary class for interacting with the CoMoTo API
 */

public class CoMoToAPI {

    /**
     * Gets an analysis by its id
     *
     * @param connection The connection to use to grab the analysis
     * @param analysisId The id of the analysis
     * @return An analysis identified by its id
     */
    public static Analysis getAnalysis(CoMoToAPIConnection connection, int analysisId){

        //Get the object from the API
        Map abstractAnalysis = getMap(connection, GET_ANALYSIS, analysisId);

        //Build the resulting data structure
        return new Analysis(abstractAnalysis, connection);
    }

    /**
     * Gets an analysis pseudonym by its id
     *
     * @param connection The connection to use to grab the analysis
     * @param analysisPseudonymId The id of the analysis pseudonym
     * @return An analysis pseudonym identified by its id
     */
    public static AnalysisPseudonym getAnalysisPseudonym(CoMoToAPIConnection connection, int analysisPseudonymId){

        //Get the object from the API
        Map abstractAnalysisPseudonym = getMap(connection, GET_ANALYSIS_PSEUDONYM, analysisPseudonymId);

        //Build the resulting data structure
        return new AnalysisPseudonym(abstractAnalysisPseudonym, connection);
    }

    /**
     * Gets an assignment by its id from the API.
     *
     * @param connection The connection to use to grab the list of assignments
     * @param assignmentId The id of the assignment to grab
     * @return The assignment
     */
    public static Assignment getAssignment(CoMoToAPIConnection connection, int assignmentId) {

        //Get the object from the API
        Map abstractAssignment = getMap(connection, GET_ASSIGNMENT, assignmentId);

        //Build the resulting list of assignments
        return new Assignment(abstractAssignment, connection);
    }

    /**
     * Gets the list of all assignments from a specific course
     *
     * @param connection The connection to use to grab the list of assignments
     * @param courseId The id of the class whose assignments to grab
     * @return The list of assignments
     */
    public static List<Assignment> getAssignments(CoMoToAPIConnection connection, int courseId) {

        //Get the object from the API
        Object[] abstractAssignments = getArray(connection, GET_ASSIGNMENTS, courseId);

        //Build the resulting list of assignments
        List<Assignment> assignments = new ArrayList<Assignment>();
        for(Object abstractAssignment : abstractAssignments){

            Assignment assignment = new Assignment((Map) abstractAssignment, connection);
            assignments.add(assignment);
        }

        return assignments;
    }

    /**
     * Gets a course by its id from the API
     *
     * @param connection The connection to use to grab the data
     * @param courseId The id of the course to grab
     * @return The course
     */
    public static Course getCourse(CoMoToAPIConnection connection, int courseId) {

        //Get the object from the API
        Map abstractCourse = getMap(connection, GET_COURSE, courseId);

        //Build the resulting list of assignments
        return new Course(abstractCourse, connection);
    }

    /**
     * Gets a course by its id from the API
     *
     * @param connection The connection to use to grab the data
     * @param courseId The id of the course to grab
     * @param extraOfferingInfo Indicates whether we should return extra information about this offering
     * @return The course
     */
    public static Course getCourse(CoMoToAPIConnection connection, int courseId, boolean extraOfferingInfo) {

        //Get the object from the API
        Map abstractCourse = getMap(connection, GET_COURSE, courseId, extraOfferingInfo);

        //Build the resulting list of assignments
        return new Course(abstractCourse, connection);
    }

    /**
     * Gets the list of all courses from the CoMoTo API
     *
     * @param connection The connection from which to grab this data
     * @return The list of courses
     */
    public static List<Course> getCourses(CoMoToAPIConnection connection) {

        //Get the courses from the API
        Object[] abstractCourses = getArray(connection, GET_COURSES);

        //Build the list of courses
        List<Course> courses = new ArrayList<Course>();
        for(Object abstractCourse : abstractCourses){

            Course course = new Course((Map) abstractCourse, connection);
            courses.add(course);
        }

        return courses;
    }

    /**
     * Gets the list of all courses from the CoMoTo API
     *
     * @param connection The connection from which to grab this data
     * @param extraOfferingInfo Whether we should get extra offering information from the API as well
     * @return The list of courses
     */
    public static List<Course> getCourses(CoMoToAPIConnection connection, boolean extraOfferingInfo) {

        //Get the courses from the API
        Object[] abstractCourses = getArray(connection, GET_COURSES, extraOfferingInfo);

        //Build the list of courses
        List<Course> courses = new ArrayList<Course>();
        for(Object abstractCourse : abstractCourses){

            Course course = new Course((Map) abstractCourse, connection);
            courses.add(course);
        }

        return courses;
    }

    /**
     * Get a file set from the CoMoTo API
     *
     * @param connection The connection from which to grab this data
     * @param fileSetId The id of the file set to grab
     * @return The file set
     */
    public static FileSet getFileSet(CoMoToAPIConnection connection, int fileSetId) {

        //Get the file set from the api
        Map abstractFileSet = getMap(connection, GET_FILE_SET, fileSetId);

        //Build the file set
        return new FileSet(abstractFileSet, connection);
    }

    /**
     * Get a file set from the CoMoTo API
     *
     * @param connection The connection from which to grab this data
     * @param fileSetId The id of the file set to grab
     * @param fullSubmissionInfo Whether to grab the full submission info for this file set
     * @return The file set
     */
    public static FileSet getFileSet(CoMoToAPIConnection connection, int fileSetId, boolean fullSubmissionInfo) {

        //Get the file set from the api
        Map abstractFileSet = getMap(connection, GET_FILE_SET, fileSetId, fullSubmissionInfo);

        //Build the file set
        return new FileSet(abstractFileSet, connection);
    }

    /**
     * Get a file set from the CoMoTo API
     *
     * @param connection The connection from which to grab this data
     * @param fileSetId The id of the file set to grab
     * @param fullSubmissionInfo Whether to grab the full submission data from the API
     * @param extraOfferingInfo Whether to grab the extra offering information from the API as well
     * @return The file set
     */
    public static FileSet getFileSet(CoMoToAPIConnection connection, int fileSetId, boolean fullSubmissionInfo, boolean extraOfferingInfo) {

        //Get the file set from the api
        Map abstractFileSet = getMap(connection, GET_FILE_SET, fileSetId, fullSubmissionInfo, extraOfferingInfo);

        //Build the file set
        return new FileSet(abstractFileSet, connection);
    }

    /**
     * Get a jplag analysis from the CoMoTo API
     *
     * @param connection The connection from which to grab this data
     * @param jplagAnalysisId The id of the jplag analysis set to grab
     * @return The jplag analysis
     */
    public static JplagAnalysis getJplagAnalysis(CoMoToAPIConnection connection, int jplagAnalysisId) {

        //Get the file set from the api
        Map abstractJplagAnalysis = getMap(connection, GET_JPLAG_ANALYSIS, jplagAnalysisId);

        //Build the file set
        return new JplagAnalysis(abstractJplagAnalysis, connection);
    }

    /**
     * Gets a moss analysis from the CoMoTo API
     *
     * @param connection The connection from which to grab this data
     * @param mossAnalysisId The id of the moss analysis to grab
     * @return The moss analysis from the API
     */
    public static MossAnalysis getMossAnalysis(CoMoToAPIConnection connection, int mossAnalysisId) {

        //Get the file set from the api
        Map abstractMossAnalysis = getMap(connection, GET_MOSS_ANALYSIS, mossAnalysisId);

        //Build the file set
        return new MossAnalysis(abstractMossAnalysis, connection);
    }

    /**
     * Gets a moss match object by its id
     *
     * @param connection The connection from which to grab the data
     * @param mossMatchId The id of the moss match
     * @return The moss match
     */
    public static MossMatch getMossMatch(CoMoToAPIConnection connection, int mossMatchId) {

        //Get the moss match from the API
        Map abstractMossMatch = getMap(connection, GET_MOSS_MATCH, mossMatchId);

        //Build the file set
        return new MossMatch(abstractMossMatch, connection);
    }

    /**
     * Gets a moss report from the CoMoTo API
     *
     * @param connection The connection from which to grab the data
     * @param mossReportId The id of the moss report
     * @return The moss report object
     */
    public static MossReport getMossReport(CoMoToAPIConnection connection, int mossReportId) {

        //Get the file set from the api
        Map abstractMossReport = getMap(connection, GET_MOSS_REPORT, mossReportId);

        //Build the file set
        return new MossReport(abstractMossReport, connection);
    }

    public static Offering getOffering(CoMoToAPIConnection connection, int offeringId) {

        //Get the offering from the api
        Map abstractOffering = getMap(connection, GET_OFFERING, offeringId);

        //Build the offering
        return new Offering(abstractOffering, connection);
    }

    /**
     * Gets a report from the CoMoTo API
     *
     * @param connection The connection from which to grab this data
     * @param reportId The id of the report to grab
     * @return The report from the API
     */
    public static Report getReport(CoMoToAPIConnection connection, int reportId){

        //Get the file set from the api
        Map abstractReport = getMap(connection, GET_REPORT, reportId);

        //Build the file set
        return new Report(abstractReport, connection);
    }

    /**
     * Gets a student from the CoMoTo API
     *
     * @param connection The connection from which to grab this data
     * @param studentId The id of the student to grab
     * @return The student from the API
     */
    public static Student getStudent(CoMoToAPIConnection connection, int studentId, boolean showHistory){

        //Get the file set from the api
        Map abstractStudent = getMap(connection, GET_STUDENT, studentId, showHistory);

        //Build the file set
        return new Student(abstractStudent, connection);
    }

    /**
     * Gets a submission from the CoMoTo API
     *
     * @param connection The connection from which to grab this data
     * @param submissionId The id of the submission to grab
     * @return The submission from the API
     */
    public static Submission getSubmission(CoMoToAPIConnection connection, int submissionId){

        //Get the file set from the api
        Map abstractSubmission = getMap(connection, GET_SUBMISSION, submissionId);

        //Build the file set
        return new Submission(abstractSubmission, connection);
    }

    /**
     * Gets a submission file from the CoMoTo API
     *
     * @param connection The connection from which to grab this data
     * @param submissionFileId The id of the submission file to grab
     * @return The submission file from the API
     */
    public static SubmissionFile getSubmissionFile(CoMoToAPIConnection connection, int submissionFileId){

        //Get the file set from the api
        Map abstractSubmissionFile = getMap(connection, GET_SUBMISSION_FILE, submissionFileId);

        //Build the file set
        return new SubmissionFile(abstractSubmissionFile, connection);
    }

    /**
     * Gets a semester by its id
     *
     * @param connection The connection from which to grab the data
     * @param semesterId The id of the semester
     * @return The semester
     */
    public static Semester getSemester(CoMoToAPIConnection connection, int semesterId) {

        //Get the file set from the api
        Map abstractSemester = getMap(connection, GET_SEMESTER, semesterId);

        //Build the file set
        return new Semester(abstractSemester, connection);
    }
}
