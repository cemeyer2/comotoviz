package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;

import java.util.HashMap;
import java.util.Map;

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

    public AnalysisPseudonym() {
    }

    public AnalysisPseudonym(Map<String, Object> abstractAnalysisPseudonym) {
        CoMoToAPIReflector<AnalysisPseudonym> reflector = new CoMoToAPIReflector<AnalysisPseudonym>();
        reflector.populate(this, abstractAnalysisPseudonym);
    }

    public Map getMap(){
        return new HashMap();
    }

    public int getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(int analysisId) {
        this.analysisId = analysisId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPseudonym() {
        return pseudonym;
    }

    public void setPseudonym(int pseudonym) {
        this.pseudonym = pseudonym;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }
}
