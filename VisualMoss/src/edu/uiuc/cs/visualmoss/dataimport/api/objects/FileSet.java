package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Holds the data of a file set
 */
public class FileSet {

    private int id;
    private int courseId;
    private boolean complete;
    private Offering offering;
    private DateFormat timestamp;
    private List<Integer> assignmentIds;
    private List<Integer> submissionIds;
    private Type type = Type.fileset;

    public FileSet() {
    }

    public FileSet(Map<String, Object> abstractFileSet) {

        //Explicitly add non primitive types
        Map offeringMap = (Map) abstractFileSet.get("offering");
        String timestampString = (String) abstractFileSet.get("timestamp");
        Integer[] assignmentIdsArray = (Integer[]) abstractFileSet.get("assignment_ids");
        Integer[] submissionIdsArray = (Integer[]) abstractFileSet.get("submission_ids");
        offering = new Offering(offeringMap);
        timestamp = new SimpleDateFormat(timestampString);
        assignmentIds = Arrays.asList(assignmentIdsArray);
        submissionIds = Arrays.asList(submissionIdsArray);

        //Remove these entries from the map
        abstractFileSet.remove("offering");
        abstractFileSet.remove("timestamp");
        abstractFileSet.remove("assignment_ids");
        abstractFileSet.remove("submission_ids");

        CoMoToAPIReflector<FileSet> reflector = new CoMoToAPIReflector<FileSet>();
        reflector.populate(this, abstractFileSet);
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

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
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
}
