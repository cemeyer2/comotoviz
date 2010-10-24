package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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

    public MossMatch() {
    }

    public MossMatch(Map<String, Object> abstractMossMatch) {
        CoMoToAPIReflector<MossMatch> reflector = new CoMoToAPIReflector<MossMatch>();
        reflector.populate(this, abstractMossMatch);
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

    public URL getLink() {
        return link;
    }

    public void setLink(URL link) {
        this.link = link;
    }

    public int getMossAnalysisId() {
        return mossAnalysisId;
    }

    public void setMossAnalysisId(int mossAnalysisId) {
        this.mossAnalysisId = mossAnalysisId;
    }

    public int getScore1() {
        return score1;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public int getSubmission1Id() {
        return submission1Id;
    }

    public void setSubmission1Id(int submission1Id) {
        this.submission1Id = submission1Id;
    }

    public int getSubmission2Id() {
        return submission2Id;
    }

    public void setSubmission2Id(int submission2Id) {
        this.submission2Id = submission2Id;
    }
}
