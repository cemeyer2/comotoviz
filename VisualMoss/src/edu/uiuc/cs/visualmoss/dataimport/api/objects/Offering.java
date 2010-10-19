package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 18, 2010
 *
 * <p> <p> Holds the data of an offering
 */
public class Offering {

    int id;
    private int courseId;
    private List<Integer> fileSetIds;
    private Semester semester;

    public Offering(int id, int courseId, List<Integer> fileSetIds, Semester semester) {
        this.id = id;
        this.courseId = courseId;
        this.fileSetIds = fileSetIds;
        this.semester = semester;
    }

    public int getId() {
        return id;
    }

    public int getCourseId() {
        return courseId;
    }

    public List<Integer> getFileSetIds() {
        return fileSetIds;
    }

    public Semester getSemester() {
        return semester;
    }
}
