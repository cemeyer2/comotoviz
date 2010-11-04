package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPI;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConnection;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Holds the data of a particular course
 */
public class Course implements Refreshable{

    /**
     * The unique id for this course
     */
    private int id;

    /**
     * The name of this course
     */
    private String name;

    /**
     * The list of ids for the associated users
     */
    private List<Integer> userIds;

    /**
     * The list of associated assignment ids
     */
    private List<Integer> assignmentIds;

    /**
     * The list of associated assignments
     */
    private List<Assignment> assignments = null;

    /**
     * The list of associated file sets
     */
    private List<Integer> fileSetIds;

    /**
     * The list of file sets
     */
    private List<FileSet> fileSets = null;

    /**
     * A connection to the CoMoTo API for lazily loading refreshing
     */
    private CoMoToAPIConnection connection;

    /**
     * Constructs a course object
     *
     * @param abstractCourse A map containing the data for this course
     * @param connection A connection for lazily loading and refreshing objects
     */
    public Course(Map<String, Object> abstractCourse, CoMoToAPIConnection connection) {

        //Store the connection
        this.connection = connection;

        //Explicitly add the non-primitive types
        Integer[] userIdsArray = (Integer[]) abstractCourse.get("user_ids");
        Integer[] assignmentIdsArray = (Integer[]) abstractCourse.get("assignment_ids");
        Integer[] fileSetIdsArray = (Integer[]) abstractCourse.get("file_set_ids");
        userIds = Arrays.asList(userIdsArray);
        assignmentIds = Arrays.asList(assignmentIdsArray);
        fileSetIds = Arrays.asList(fileSetIdsArray);
        
        //Remove it from the map
        abstractCourse.remove("user_ids");
        abstractCourse.remove("assignment_ids");
        abstractCourse.remove("file_set_ids");

        CoMoToAPIReflector<Course> reflector = new CoMoToAPIReflector<Course>();
        reflector.populate(this, abstractCourse);
    }

    /**
     * {@inheritDoc}
     */
    public void refresh() {

        //Grab the new object from the API
        Course newCourse = CoMoToAPI.getCourse(connection, id);

        //Copy the data into this object
        name = newCourse.getName();
        userIds = newCourse.getUserIds();
        assignmentIds = newCourse.getAssignmentIds();
        fileSetIds = newCourse.getFileSetIds();

        //Clear any cached data
        assignments = null;
        fileSets = null;
    }

    /**
     * Grabs the list of assignments from the api if not cached in this object
     *
     * @return A list of the assignments associated with this course
     */
    public List<Assignment> getAssignments(){

        //Load the assignments from the API if not cached
        if (assignments == null) {
            assignments = new ArrayList<Assignment>();
            for(int assignmentId : assignmentIds){
                assignments.add(CoMoToAPI.getAssignment(connection, assignmentId));
            }
        }
        return assignments;
    }

    /**
     * Grabs the list of file sets from the API if not cached in this object
     *
     * @return A list of the file sets associated with this course
     */
    public List<FileSet> getFileSets() {

        //Load the assignments from the API if not cached
        if (fileSets == null) {
            fileSets = new ArrayList<FileSet>();
            for(int fileSetId : fileSetIds){
                fileSets.add(CoMoToAPI.getFileSet(connection, fileSetId));
            }
        }
        return fileSets;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    public List<Integer> getAssignmentIds() {
        return assignmentIds;
    }

    public void setAssignmentIds(List<Integer> assignmentIds) {
        this.assignmentIds = assignmentIds;
    }

    public List<Integer> getFileSetIds() {
        return fileSetIds;
    }

    public void setFileSetIds(List<Integer> fileSetIds) {
        this.fileSetIds = fileSetIds;
    }
}
