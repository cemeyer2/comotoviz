package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPI;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConnection;
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
public class MossMatch implements Refreshable{

    /**
     * The unique id for this match
     */
    private int id;

    /**
     * The link for this object
     */
    private URL link;

    /**
     * The id for the associated analysis
     */
    private int mossAnalysisId;

    /**
     * The score of the first student
     */
    private int score1;

    /**
     * The score of the second student
     */
    private int score2;

    /**
     * The id of the first submission
     */
    private int submission1Id;

    /**
     * The id of the second submission
     */
    private int submission2Id;

    /**
     * The associated analysis object
     */
    private MossAnalysis mossAnalysis = null;

    /**
     * The first submission object
     */
    private Submission submission1 = null;

    /**
     * The second associated submission object
     */
    private Submission submission2 = null;

    /**
     * A connection to the API
     */
    private CoMoToAPIConnection connection;

    public MossMatch() {
    }

    public MossMatch(Map<String, Object> abstractMossMatch, CoMoToAPIConnection connection) {
        
        //Save the connection
        this.connection = connection;

        //Fill the rest of the object with reflection
        CoMoToAPIReflector<MossMatch> reflector = new CoMoToAPIReflector<MossMatch>();
        reflector.populate(this, abstractMossMatch);
    }

    /**
     * {@inheritDoc}
     */
    public void refresh() {

        //First, grab the new match object
        MossMatch newMossMatch = CoMoToAPI.getMossMatch(connection, id);

        //Refresh primitive data
        link = newMossMatch.getLink();
        mossAnalysisId = newMossMatch.getMossAnalysisId();
        score1 = newMossMatch.getScore1();
        score2 = newMossMatch.getScore2();
        submission1Id = newMossMatch.getSubmission1Id();
        submission2Id = newMossMatch.getSubmission2Id();

        //Clear cached data
        mossAnalysis = null;
        submission1 = null;
        submission2 = null;
    }

    /**
     * Get the moss analysis object associated with this object lazily
     *
     * @return The associated moss analysis
     */
    public MossAnalysis getMossAnalysis() {

        //Only query the API if it's not cached
        if(mossAnalysis == null) {
            mossAnalysis = CoMoToAPI.getMossAnalysis(connection, mossAnalysisId);
        }
        return mossAnalysis;
    }

    /**
     * Get the first submission object from the API lazily
     *
     * @return The first submission object
     */
    public Submission getSubmission1() {

        //Only call the API if it's not cached
        if(submission1 == null) {
            submission1 = CoMoToAPI.getSubmission(connection, submission1Id);
        }
        return submission1;
    }

    /**
     * Get the second submission object from the API lazily
     *
     * @return The second submission object
     */
    public Submission getSubmission2() {

        //Only call the API if it's not cached
        if(submission2 == null) {
            submission2 = CoMoToAPI.getSubmission(connection, submission2Id);
        }
        return submission2;
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
