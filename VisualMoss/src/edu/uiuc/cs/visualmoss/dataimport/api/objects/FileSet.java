package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.objects.enums.Type;

import java.sql.Timestamp;
import java.util.List;

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
    private Timestamp timestamp;
    private List<Integer> assignmentIds;
    private List<Integer> submissionIds;
    private Type type = Type.fileset;

    public FileSet(int id, int courseId, boolean complete, Offering offering, Timestamp timestamp, List<Integer> assignmentIds, List<Integer> submissionIds) {
        this.id = id;
        this.courseId = courseId;
        this.complete = complete;
        this.offering = offering;
        this.timestamp = timestamp;
        this.assignmentIds = assignmentIds;
        this.submissionIds = submissionIds;
    }

    public FileSet(int id, int courseId, boolean complete, Offering offering, Timestamp timestamp, List<Integer> assignmentIds, List<Integer> submissionIds, Type type) {
        this.id = id;
        this.courseId = courseId;
        this.complete = complete;
        this.offering = offering;
        this.timestamp = timestamp;
        this.assignmentIds = assignmentIds;
        this.submissionIds = submissionIds;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public int getCourseId() {
        return courseId;
    }

    public boolean isComplete() {
        return complete;
    }

    public Offering getOffering() {
        return offering;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public List<Integer> getAssignmentIds() {
        return assignmentIds;
    }

    public List<Integer> getSubmissionIds() {
        return submissionIds;
    }

    public Type getType() {
        return type;
    }
}
