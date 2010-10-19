package edu.uiuc.cs.visualmoss.dataimport.api;

import edu.uiuc.cs.visualmoss.dataimport.Assignment;
import edu.uiuc.cs.visualmoss.dataimport.Course;
import edu.uiuc.cs.visualmoss.dataimport.api.objects.*;

import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> The primary class for interacting with the CoMoTo API
 */

public class CoMoToAPI {

    public static List<Assignment> getAssignments(CoMoToAPIConnection connection, Course course) {
        return null;
    }

    public static List<Course> getCourses() {
        return null;
    }

    public static FileSet getFileSet(CoMoToAPIConnection connection, int id) {
        return null;
    }

    public static MossAnalysis getMossAnalysis(CoMoToAPIConnection connection, int id) {
        return null;
    }

    public static MossMatch getMossMatch(CoMoToAPIConnection connection, int id){
        return null;
    }

    public static Report getReport(CoMoToAPIConnection connection, int id){
        return null;
    }

    public static Student getStudent(CoMoToAPIConnection connection, int id){
        return null;
    }

    public static Submission getSubmission(CoMoToAPIConnection connection, int id){
        return null;
    }

    public static SubmissionFile getSubmissionFile(CoMoToAPIConnection connection, int id){
        return null;
    }

    public static SubmissionFile getSyntaxHighlightedSubmissionFile(CoMoToAPIConnection connection, int id){
        return null;
    }
}
