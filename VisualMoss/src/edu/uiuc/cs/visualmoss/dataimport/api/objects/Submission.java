package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.objects.enums.Type;

import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Holds the data for a submission
 */
public class Submission {

    private int id;
    private int fileSetId;
    private int offeringId;
    private int studentId;
    private Type type = Type.studentsubmission;
    private AnalysisPseudonym analysisPseudonym;
    private List<Integer> partnerIds;
    private List<Integer> submissionFileIds;

    public Submission(int id, int fileSetId, int offeringId, int studentId, AnalysisPseudonym analysisPseudonym, List<Integer> partnerIds, List<Integer> submissionFileIds) {
        this.id = id;
        this.fileSetId = fileSetId;
        this.offeringId = offeringId;
        this.studentId = studentId;
        this.analysisPseudonym = analysisPseudonym;
        this.partnerIds = partnerIds;
        this.submissionFileIds = submissionFileIds;
    }

    public Submission(int id, int fileSetId, int offeringId, int studentId, Type type, AnalysisPseudonym analysisPseudonym, List<Integer> partnerIds, List<Integer> submissionFileIds) {
        this.id = id;
        this.fileSetId = fileSetId;
        this.offeringId = offeringId;
        this.studentId = studentId;
        this.type = type;
        this.analysisPseudonym = analysisPseudonym;
        this.partnerIds = partnerIds;
        this.submissionFileIds = submissionFileIds;
    }

    public int getId() {
        return id;
    }

    public int getFileSetId() {
        return fileSetId;
    }

    public int getOfferingId() {
        return offeringId;
    }

    public int getStudentId() {
        return studentId;
    }

    public Type getType() {
        return type;
    }

    public AnalysisPseudonym getAnalysisPseudonym() {
        return analysisPseudonym;
    }

    public List<Integer> getPartnerIds() {
        return partnerIds;
    }

    public List<Integer> getSubmissionFileIds() {
        return submissionFileIds;
    }
}
