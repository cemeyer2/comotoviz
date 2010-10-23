package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import java.sql.Timestamp;
import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Holds the data of an analysis
 */
public class Analysis {

    private int id;
    private Timestamp timestamp;
    private boolean complete;
    private int mossAnalysisId;
    private int assignmentId;
    private int jplagAnalysisId;
    private List<AnalysisPseudonym> analysisPseudonyms;

    public Analysis(int id, Timestamp timestamp, boolean complete, int mossAnalysisId, int assignmentId, int jplagAnalysisId, List<AnalysisPseudonym> analysisPseudonyms) {
        this.id = id;
        this.timestamp = timestamp;
        this.complete = complete;
        this.mossAnalysisId = mossAnalysisId;
        this.assignmentId = assignmentId;
        this.jplagAnalysisId = jplagAnalysisId;
        this.analysisPseudonyms = analysisPseudonyms;
    }

    public int getId() {
        return id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public boolean isComplete() {
        return complete;
    }

    public int getMossAnalysisId() {
        return mossAnalysisId;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public int getJplagAnalysisId() {
        return jplagAnalysisId;
    }

    public List<AnalysisPseudonym> getAnalysisPseudonyms() {
        return analysisPseudonyms;
    }
}
