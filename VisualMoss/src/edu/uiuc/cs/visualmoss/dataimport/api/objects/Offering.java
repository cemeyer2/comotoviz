package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConstants.FILE_SET_IDS;
import static edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConstants.SEMESTER;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 18, 2010
 *
 * <p> <p> Holds the data of an offering
 */
public class Offering {

    private int id;
    private int courseId;
    private List<Integer> fileSetIds;
    private Semester semester;

    public Offering() {
    }

    public Offering(Map abstractOffering) {

        //Explicitly add non-primitive types
        Integer[] fileSetIdsArray = (Integer[]) abstractOffering.get(FILE_SET_IDS);
        Map semesterMap = (Map) abstractOffering.get(SEMESTER);
        fileSetIds = Arrays.asList(fileSetIdsArray);
        semester = new Semester(semesterMap);

        //Remove these from the map
        abstractOffering.remove(FILE_SET_IDS);
        abstractOffering.remove(SEMESTER);

        CoMoToAPIReflector<Offering> reflector = new CoMoToAPIReflector<Offering>();
        reflector.populate(this, abstractOffering);
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
