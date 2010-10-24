package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.objects.enums.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Submission() {
    }

    public Submission(Map abstractSubmission) {
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

    public int getFileSetId() {
        return fileSetId;
    }

    public void setFileSetId(int fileSetId) {
        this.fileSetId = fileSetId;
    }

    public int getOfferingId() {
        return offeringId;
    }

    public void setOfferingId(int offeringId) {
        this.offeringId = offeringId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public AnalysisPseudonym getAnalysisPseudonym() {
        return analysisPseudonym;
    }

    public void setAnalysisPseudonym(AnalysisPseudonym analysisPseudonym) {
        this.analysisPseudonym = analysisPseudonym;
    }

    public List<Integer> getPartnerIds() {
        return partnerIds;
    }

    public void setPartnerIds(List<Integer> partnerIds) {
        this.partnerIds = partnerIds;
    }

    public List<Integer> getSubmissionFileIds() {
        return submissionFileIds;
    }

    public void setSubmissionFileIds(List<Integer> submissionFileIds) {
        this.submissionFileIds = submissionFileIds;
    }
}
