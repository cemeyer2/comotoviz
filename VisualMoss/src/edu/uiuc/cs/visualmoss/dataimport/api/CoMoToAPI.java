package edu.uiuc.cs.visualmoss.dataimport.api;

import edu.uiuc.cs.visualmoss.dataimport.Assignment;
import edu.uiuc.cs.visualmoss.dataimport.Course;

import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> The primary class for interacting with the CoMoTo API
 */

public class CoMoToAPI {

    private final String username, password, host;
    public static final String DEFAULT_HOST = "https://maggie.cs.illinois.edu/comoto/api/api";

    public CoMoToAPI(String username, String password) {
        this(username, password, DEFAULT_HOST);
    }

    public CoMoToAPI(String username, String password, String host) {
        this.username = username;
        this.password = password;
        this.host = host;
    }

    public List<Course> getCourses() {
        return null;
    }

    public List<Assignment> getAssignments(Course course) {
        return null;
    }

    public FileSet getFileSet(int id) {
        return null;
    }

    public MossAnalysis getMossAnalysis(int id){
        return null;
    }
}
