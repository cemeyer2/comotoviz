package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConstants.ANALYSIS_PSEUDONYMS;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Holds the data of an analysis
 */
public class Analysis {

    private int id;
    private DateFormat timestamp;
    private boolean complete;
    private int mossAnalysisId;
    private int assignmentId;
    private int jplagAnalysisId;
    private List<AnalysisPseudonym> analysisPseudonyms;

    public Analysis() {
    }

    public Analysis(Map<String, Object> abstractAnalysis) {

        //Add non-primitive types explicitly
        Map[] analysisPseudonymsArray = (Map[]) abstractAnalysis.get(ANALYSIS_PSEUDONYMS);
        analysisPseudonyms = new ArrayList<AnalysisPseudonym>();
        for(Map analysisPseudonymMap : analysisPseudonymsArray){
            analysisPseudonyms.add(new AnalysisPseudonym(analysisPseudonymMap));
        }
        abstractAnalysis.remove(ANALYSIS_PSEUDONYMS);
        
        CoMoToAPIReflector<Analysis> reflector = new CoMoToAPIReflector<Analysis>();
        reflector.populate(this, abstractAnalysis);
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

    public DateFormat getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateFormat timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public int getMossAnalysisId() {
        return mossAnalysisId;
    }

    public void setMossAnalysisId(int mossAnalysisId) {
        this.mossAnalysisId = mossAnalysisId;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public int getJplagAnalysisId() {
        return jplagAnalysisId;
    }

    public void setJplagAnalysisId(int jplagAnalysisId) {
        this.jplagAnalysisId = jplagAnalysisId;
    }

    public List<AnalysisPseudonym> getAnalysisPseudonyms() {
        return analysisPseudonyms;
    }

    public void setAnalysisPseudonyms(List<AnalysisPseudonym> analysisPseudonyms) {
        this.analysisPseudonyms = analysisPseudonyms;
    }
}
