package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    public Course() {
    }

    public Course(Map<String, Object> abstractCourse) {

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
