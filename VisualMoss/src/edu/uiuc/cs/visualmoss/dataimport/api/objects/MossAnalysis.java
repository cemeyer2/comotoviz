package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Holds the data of a MOSS analysis
 */
public class MossAnalysis {

    private int id;
    private int analysisId;
    private List<MossMatch> matches;

    public MossAnalysis(int id, int analysisId, List<MossMatch> matches) {
        this.id = id;
        this.analysisId = analysisId;
        this.matches = matches;
    }

    public int getId() {
        return id;
    }

    public int getAnalysisId() {
        return analysisId;
    }

    public List<MossMatch> getMatches() {
        return matches;
    }
}
