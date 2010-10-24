package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;
import edu.uiuc.cs.visualmoss.dataimport.api.objects.enums.Language;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Holds the data of an assignment
 */
public class Assignment {

    private int id;
    private int analysisId;
    private int courseId;
    private Language language;
    private String name;
    private List<Integer> fileSetIds;

    public Assignment() {
    }

    public Assignment(Map<String, Object> abstractAssignment) {

        //Explicitly add non-primitive types
        Integer[] fileSetIdsArray = (Integer[]) abstractAssignment.get("file_set_ids");
        fileSetIds = Arrays.asList(fileSetIdsArray);

        //Remove them from the map
        abstractAssignment.remove("file_set_ids");

        CoMoToAPIReflector<Assignment> reflector = new CoMoToAPIReflector<Assignment>();
        reflector.populate(this, abstractAssignment);
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

    public List<Integer> getFileSetIds() {
        return fileSetIds;
    }

    public void setFileSetIds(List<Integer> fileSetIds) {
        this.fileSetIds = fileSetIds;
    }
}
