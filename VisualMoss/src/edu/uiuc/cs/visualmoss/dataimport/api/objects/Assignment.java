package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPI;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConnection;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;
import org.apache.commons.lang.WordUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Holds the data of an assignment
 */
public class Assignment implements Refreshable{

    /**
     * Integer uniquely identifying this assignment
     */
    private int id;

    /**
     * Unique id for the analysis associated with this assignment
     */
    private int analysisId;

    /**
     * The analysis object associated with this assignment
     */
    private Analysis analysis = null;

    /**
     * Unique id for the course associated with this assignment
     */
    private int courseId;

    /**
     * The course associated with this assignment
     */
    private Course course = null;

    /**
     * The language of this assignment
     */
    private Language language;

    /**
     * The name of this assignment
     */
    private String name;

    /**
     * The list of unique ids for the file sets associated with this assignment
     */
    private List<Integer> filesetIds;

    /**
     * The list of file sets associated with this assignment
     */
    private List<FileSet> filesets = null;

    /**
     * A connection to the API for loading data lazily and refreshing this object
     */
    private CoMoToAPIConnection connection;

    /**
     * Constructor for an assignment object
     *
     * @param abstractAssignment A map holding the data of an assignment
     * @param connection A connection to the API for refreshing this object and loading data lazily
     */
    public Assignment(Map<String, Object> abstractAssignment, CoMoToAPIConnection connection) {

        //Store the connection
        this.connection = connection;

        //Populate this object using reflection
        CoMoToAPIReflector<Assignment> reflector = new CoMoToAPIReflector<Assignment>();
        reflector.populate(this, abstractAssignment);
    }

    /**
     * {@inheritDoc}
     */
    public void refresh() {

        //Grab the new assignment object
        Assignment newAssignment = CoMoToAPI.getAssignment(connection, id);

        //Refresh this data
        analysisId = newAssignment.getAnalysisId();
        courseId = newAssignment.getCourseId();
        language = newAssignment.getLanguage();
        name = newAssignment.getName();
        filesetIds = newAssignment.getFilesetIds();
        
        //Invalidate the cached data
        analysis = null;
        course = null;
        filesets = null;
    }

    /**
     * Gets the associated analysis lazily
     *
     * @return The analysis object
     */
    public Analysis getAnalysis() {

        //If it's not cached, grab it from the API
        if(analysis == null){
            analysis = CoMoToAPI.getAnalysis(connection, analysisId);
        }
        return analysis;
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
    public List<FileSet> getFilesets() {

        //Grab the list from the API if not cached
        if(filesetIds == null) {
            filesets = new ArrayList<FileSet>();
            for(int fileSetId : filesetIds){
                filesets.add(CoMoToAPI.getFileSet(connection, fileSetId));
            }
        }
        return filesets;
    }

    /**
     * Shows the display name of the assignment and to represent this assignment as a string
     *
     * @return The string representing this assignment
     */
    @Override
    public String toString(){

        //Make the names uniform and call them 'MP# ...'
        String unformattedName = getName().toLowerCase().trim();
        if(unformattedName.indexOf("mp")==0){
            unformattedName = "MP" + unformattedName.substring(2);
        }

        //Return the display name of this assignment
        return WordUtils.capitalize(unformattedName);
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

    public int getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(int analysisId) {
        this.analysisId = analysisId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = Language.valueOf(language);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getFilesetIds() {
        return filesetIds;
    }

    public void setFilesetIds(List<Integer> filesetIds) {
        this.filesetIds = filesetIds;
    }
}
