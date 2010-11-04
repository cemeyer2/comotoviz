package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPI;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConnection;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;

import java.util.*;

import static edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConstants.FILE_SET_IDS;
import static edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConstants.SEMESTER;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 18, 2010
 *
 * <p> <p> Holds the data of an offering
 */
public class Offering implements Refreshable{

    /**
     * The unique id for this offering
     */
    private int id;

    /**
     * The unique id of the associated course
     */
    private int courseId;

    /**
     * The associated course object
     */
    private Course course;

    /**
     * The list of ids for the associated file sets
     */
    private List<Integer> fileSetIds;

    /**
     * The list of the associated file sets
     */
    private List<FileSet> fileSets;

    /**
     * The semester of this offering
     */
    private Semester semester;

    /**
     * A connection to the API
     */
    private CoMoToAPIConnection connection;

    /**
     * Creates an offering object
     *
     * @param abstractOffering A map holding the data for this object
     * @param connection A connection to the API for lazily loading and refreshing data
     */
    public Offering(Map abstractOffering, CoMoToAPIConnection connection) {

        //Save the connections
        this.connection = connection;

        //Explicitly add non-primitive types
        Integer[] fileSetIdsArray = (Integer[]) abstractOffering.get(FILE_SET_IDS);
        Map semesterMap = (Map) abstractOffering.get(SEMESTER);
        fileSetIds = Arrays.asList(fileSetIdsArray);
        semester = new Semester(semesterMap, connection);

        //Remove these from the map
        abstractOffering.remove(FILE_SET_IDS);
        abstractOffering.remove(SEMESTER);

        CoMoToAPIReflector<Offering> reflector = new CoMoToAPIReflector<Offering>();
        reflector.populate(this, abstractOffering);
    }

    /**
     * {@inheritDoc}
     */
    public void refresh() {

        //First, grab the new offering object from the api
        Offering newOffering = CoMoToAPI.getOffering(connection, id);

        //Copy the primitive types from the API
        courseId = newOffering.getCourseId();
        fileSetIds = newOffering.getFileSetIds();
        semester = newOffering.getSemester();

        //Clear cached data
        fileSets = null;
        course = null;
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
     * Grabs the list of file sets lazily
     *
     * @return The list of file sets
     */
    public List<FileSet> getFileSets() {

        //Grab the list from the API if not cached
        if(fileSetIds == null) {
            fileSets = new ArrayList<FileSet>();
            for(int fileSetId : fileSetIds){
                fileSets.add(CoMoToAPI.getFileSet(connection, fileSetId));
            }
        }
        return fileSets;
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

    public List<Integer> getFileSetIds() {
        return fileSetIds;
    }

    public void setFileSetIds(List<Integer> fileSetIds) {
        this.fileSetIds = fileSetIds;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }
}
