package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import java.net.URL;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Holds the data of a MOSS match
 */
public class MossMatch {

    private int id;
    private URL link;
    private int mossAnalysisId;
    private int score1;
    private int score2;
    private int submission1Id;
    private int submission2Id;

    public MossMatch(int id, URL link, int mossAnalysisId, int score1, int score2, int submission1Id, int submission2Id) {
        this.id = id;
        this.link = link;
        this.mossAnalysisId = mossAnalysisId;
        this.score1 = score1;
        this.score2 = score2;
        this.submission1Id = submission1Id;
        this.submission2Id = submission2Id;
    }

    public int getId() {
        return id;
    }

    public URL getLink() {
        return link;
    }

    public int getMossAnalysisId() {
        return mossAnalysisId;
    }

    public int getScore1() {
        return score1;
    }

    public int getScore2() {
        return score2;
    }

    public int getSubmission1Id() {
        return submission1Id;
    }

    public int getSubmission2Id() {
        return submission2Id;
    }
}
