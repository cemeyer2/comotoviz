package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Holds the data of a particular course
 */
public class Course {

    private int id;
    private String name;
    private List<Integer> userIds;
    private List<Integer> assignmentIds;
    private List<Integer> fileSetIds;

    public Course(int id, String name, List<Integer> userIds, List<Integer> assignmentIds, List<Integer> fileSetIds) {
        this.id = id;
        this.name = name;
        this.userIds = userIds;
        this.assignmentIds = assignmentIds;
        this.fileSetIds = fileSetIds;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public List<Integer> getAssignmentIds() {
        return assignmentIds;
    }

    public List<Integer> getFileSetIds() {
        return fileSetIds;
    }
}
