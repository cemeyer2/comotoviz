package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPI;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConnection;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConstants;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConstants.OFFERING;
import static edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConstants.SUBMISSIONS;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Holds the data of a file set
 */
public class FileSet implements Refreshable{

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
    private CoMoToAPIConnection connection;

    /**
     * Constructs this file set
     *
     * @param abstractFileSet A map holding the data of this file set
     * @param connection A connection to the API to use for lazily loading and refreshing this object
     */
    public FileSet(Map<String, Object> abstractFileSet, CoMoToAPIConnection connection) {

        //Save the connection
        this.connection = connection;

        //Explicitly add the 'offering' object
        Map offeringMap = (Map) abstractFileSet.get(CoMoToAPIConstants.OFFERING);
        if(offeringMap != null){
            offering = new Offering(offeringMap, connection);
            abstractFileSet.remove(OFFERING);
        }

        //Explicitly add the submission objects
        Object[] abstractSubmissions = (Object[]) abstractFileSet.get(SUBMISSIONS);
        if(abstractSubmissions != null){
            submissions = new ArrayList<Submission>();
            for(Object abstractSubmission : abstractSubmissions){
                submissions.add(new Submission((Map<String, Object>) abstractSubmission, connection));
            }
            abstractFileSet.remove(SUBMISSIONS);
        }

        //Populate the rest of this object using reflection
        CoMoToAPIReflector<FileSet> reflector = new CoMoToAPIReflector<FileSet>();
        reflector.populate(this, abstractFileSet);
    }

    /**
     * {@inheritDoc}
     */
    public void refresh() {

        //Grab the new file set from the API
        FileSet newFileSet = CoMoToAPI.getFileSet(connection, id);

        //Copy the primitive data over
        courseId = newFileSet.getCourseId();
        offering = newFileSet.getOffering();
        timestamp = newFileSet.getTimestamp();
        assignmentIds = newFileSet.getAssignmentIds();
        submissionIds = newFileSet.getSubmissionIds();
        type = newFileSet.getType();

        //Clear the cached data
        course = null;
        assignments = null;
        submissions = null;
    }

    /**
      * Grabs the associated course lazily
      *
      * @return The course
      */
     public Course getCourse() {

         //Grab the course from the API if not cached
         if(course == null){
             course = CoMoToAPI.getCourse(connection, courseId);
         }
         return course;
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
     * Grabs the list of submissions from the api if not cached in this object
     * 
     * @return A list of the submissions associated with this file set
     */
    public List<Submission> getSubmissions() {

        //Load the assignments from the API if not cached
        if (submissions == null) {
            submissions = new ArrayList<Submission>();
            for(int submissionId : submissionIds){
                submissions.add(CoMoToAPI.getSubmission(connection, submissionId));
            }
        }
        return submissions;
    }

    public Map getMap(){
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

    public void setType(String type) {
        this.type = Type.valueOf(type);
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
