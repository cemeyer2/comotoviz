package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConstants.*;


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

        //Explicitly add non-primitive types
        Map analysisPseudonymMap = (Map) abstractSubmission.get(ANALYSIS_PSEUDONYM);
        Integer[] partnerIdsArray = (Integer[]) abstractSubmission.get(PARTNER_IDS);
        Integer[] submissionFileIdsArray = (Integer[]) abstractSubmission.get(SUBMISSION_FILE_IDS);
        analysisPseudonym = new AnalysisPseudonym(analysisPseudonymMap);
        partnerIds = Arrays.asList(partnerIdsArray);
        submissionFileIds = Arrays.asList(submissionFileIdsArray);

        //Remove them from the map
        abstractSubmission.remove(ANALYSIS_PSEUDONYM);
        abstractSubmission.remove(PARTNER_IDS);
        abstractSubmission.remove(SUBMISSION_FILE_IDS);

        CoMoToAPIReflector<Submission> reflector = new CoMoToAPIReflector<Submission>();
        reflector.populate(this, abstractSubmission);
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

    public void setType(String type) {
        this.type = Type.valueOf(type);
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
