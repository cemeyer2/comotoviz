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
     * @param id The id of the analysis
     * @return An analysis identified by its id
     */
    public static Analysis getAnalysis(CoMoToAPIConnection connection, int id){

        //Get the object from the API
        Map abstractAnalysis = getMap(connection, GET_ANALYSIS, id);

        //Build the resulting data structure
        return (Analysis) DataPopulator.valueOf(ANALYSIS).getData(abstractAnalysis);
    }

    /**
     * Gets the list of all assignments from a specific course
     *
     * @param connection The connection to use to grab the list of assignments
     * @param id The id of the class whose assignments to grab 
     * @return The list of assignments
     */
    public static List<Assignment> getAssignments(CoMoToAPIConnection connection, int id) {

        //Get the object from the API
        Object[] abstractAssignments = getArray(connection, GET_ASSIGNMENTS, id);

        //Build the resulting list of assignments
        List<Assignment> assignments = new ArrayList<Assignment>();
        for(Object abstractAssignment : abstractAssignments){

            Assignment assignment = (Assignment) DataPopulator.valueOf(ASSIGNMENT).getData((Map) abstractAssignment);
            assignments.add(assignment);
        }

        return assignments;
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

            Course course = (Course) DataPopulator.valueOf(COURSE).getData((Map) abstractCourse);
            courses.add(course);
        }

        return courses;
    }

    /**
     * Get a file set from the CoMoTo API
     *
     * @param connection The connection from which to grab this data
     * @param id The id of the file set to grab
     * @return The file set
     */
    public static FileSet getFileSet(CoMoToAPIConnection connection, int id) {

        //Get the file set from the api
        Map abstractFileSet = getMap(connection, GET_FILE_SET, id);

        //Build the file set
        return (FileSet) DataPopulator.valueOf(FILE_SET).getData(abstractFileSet);
    }

    /**
     * Gets a moss analysis from the CoMoTo API
     *
     * @param connection The connection from which to grab this data
     * @param id The id of the moss analysis to grab
     * @return The moss analysis from the API
     */
    public static MossAnalysis getMossAnalysis(CoMoToAPIConnection connection, int id) {

        //Get the file set from the api
        Map abstractMossAnalysis = getMap(connection, GET_MOSS_ANALYSIS, id);

        //Build the file set
        return (MossAnalysis) DataPopulator.valueOf(MOSS_ANALYSIS).getData(abstractMossAnalysis);
    }

    /**
     * Gets a report from the CoMoTo API
     *
     * @param connection The connection from which to grab this data
     * @param id The id of the report to grab
     * @return The report from the API
     */
    public static Report getReport(CoMoToAPIConnection connection, int id){

        //Get the file set from the api
        Map abstractReport = getMap(connection, GET_REPORT, id);

        //Build the file set
        return (Report) DataPopulator.valueOf(REPORT).getData(abstractReport);
    }

    /**
     * Gets a student from the CoMoTo API
     *
     * @param connection The connection from which to grab this data
     * @param id The id of the student to grab
     * @return The student from the API
     */
    public static Student getStudent(CoMoToAPIConnection connection, int id){

        //Get the file set from the api
        Map abstractStudent = getMap(connection, GET_STUDENT, id);

        //Build the file set
        return (Student) DataPopulator.valueOf(STUDENT).getData(abstractStudent);
    }

    /**
     * Gets a submission from the CoMoTo API
     *
     * @param connection The connection from which to grab this data
     * @param id The id of the submission to grab
     * @return The submission from the API
     */
    public static Submission getSubmission(CoMoToAPIConnection connection, int id){

        //Get the file set from the api
        Map abstractSubmission = getMap(connection, GET_SUBMISSION, id);

        //Build the file set
        return (Submission) DataPopulator.valueOf(SUBMISSION).getData(abstractSubmission);
    }

    /**
     * Gets a submission file from the CoMoTo API
     *
     * @param connection The connection from which to grab this data
     * @param id The id of the submission file to grab
     * @return The submission file from the API
     */
    public static SubmissionFile getSubmissionFile(CoMoToAPIConnection connection, int id){

        //Get the file set from the api
        Map abstractSubmissionFile = getMap(connection, GET_SUBMISSION_FILE, id);

        //Build the file set
        return (SubmissionFile) DataPopulator.valueOf(SUBMISSION_FILE).getData(abstractSubmissionFile);
    }
}
