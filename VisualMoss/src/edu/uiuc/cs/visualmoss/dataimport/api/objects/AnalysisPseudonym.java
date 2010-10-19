package edu.uiuc.cs.visualmoss.dataimport.api.objects;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 18, 2010
 *
 * <p> <p> Holds the data for an analysis pseudonym
 */
public class AnalysisPseudonym {

    private int analysisId;
    private int id;
    private int pseudonym;
    private int submissionId;

    public AnalysisPseudonym(int analysisId, int id, int pseudonym, int submissionId) {
        this.analysisId = analysisId;
        this.id = id;
        this.pseudonym = pseudonym;
        this.submissionId = submissionId;
    }

    public int getAnalysisId() {
        return analysisId;
    }

    public int getId() {
        return id;
    }

    public int getPseudonym() {
        return pseudonym;
    }

    public int getSubmissionId() {
        return submissionId;
    }
}
