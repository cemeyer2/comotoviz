package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPI;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConnection;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConstants.OFFERINGS;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Holds the data of a particular course
 */
public class Course implements Refreshable{

    /**
     * The list of associated assignment ids
     */
    private List<Integer> assignmentIds;

    /**
     * The list of associated file sets
     */
    private List<Integer> filesetIds;

    /**
     * The unique id for this course
     */
    private int id;

    /**
     * LDAP information for the course
     */
    private String ldapDn;

    /**
     * The name of this course
     */
    private String name;

    /**
     * The list of offerings associated with this course
     */
    private List<Offering> offerings;

    /**
     * The list of ids for the associated users
     */
    private List<Integer> userIds;

    /**
     * The list of associated assignments
     */
    private List<Assignment> assignments = null;

    /**
     * The list of file sets
     */
    private List<FileSet> filesets = null;

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

        //Populate the list of offerings explicitly
        Object[] abstractOfferings = (Object[]) abstractCourse.get(OFFERINGS);
        if(abstractOfferings != null){
            offerings = new ArrayList<Offering>();
            for(Object abstractOffering : abstractOfferings){
                offerings.add(new Offering((Map<String, Object>) abstractOffering, connection));
            }
            abstractCourse.remove(OFFERINGS);
        }

        //Populate this object using reflection
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
        assignmentIds = newCourse.getAssignmentIds();
        filesetIds = newCourse.getFilesetIds();
        ldapDn = newCourse.getLdapDn();
        name = newCourse.getName();
        offerings = newCourse.getOfferings();
        userIds = newCourse.getUserIds();

        //Clear any cached data
        assignments = null;
        filesets = null;
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
    public List<FileSet> getFilesets() {

        //Load the assignments from the API if not cached
        if (filesets == null) {
            filesets = new ArrayList<FileSet>();
            for(int fileSetId : filesetIds){
                filesets.add(CoMoToAPI.getFileSet(connection, fileSetId));
            }
        }
        return filesets;
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

    public List<Integer> getFilesetIds() {
        return filesetIds;
    }

    public void setFilesetIds(List<Integer> filesetIds) {
        this.filesetIds = filesetIds;
    }

    public String getLdapDn() {
        return ldapDn;
    }

    public void setLdapDn(String ldapDn) {
        this.ldapDn = ldapDn;
    }

    public List<Offering> getOfferings() {
        return offerings;
    }

    public void setOfferings(List<Offering> offerings) {
        this.offerings = offerings;
    }


}
