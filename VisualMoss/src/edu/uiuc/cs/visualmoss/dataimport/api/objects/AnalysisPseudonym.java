package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPI;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConnection;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 18, 2010
 *
 * <p> <p> Holds the data for an analysis pseudonym
 */
public class AnalysisPseudonym implements Refreshable{

    /**
     * The id that uniquely identifies this analysis pseudonym
     */
    private int id;

    /**
     * The id of the analysis associated with this pseudonym
     */
    private int analysisId;

    /**
     * The analysis associated with this pseudonym, either initialized with this object or lazily loaded
     */
    private Analysis analysis = null;

    /**
     * The numerical pseudonym
     */
    private int pseudonym;

    /**
     * The unique id of the associated submission
     */
    private int submissionId;

    /**
     * The submission object associated with this pseudonym, either initialized with this object or lazily loaded
     */
    private Submission submission = null;

    /**
     * A connection to the CoMoTo API for use when lazily loading attributes
     */
    CoMoToAPIConnection connection;

    /**
     * Constructor for the AnalysisPseudonym class
     *
     * @param abstractAnalysisPseudonym The map of attributes for the pseudonym
     * @param connection The connection to the API to use when lazily loading attributes
     */
    public AnalysisPseudonym(Map<String, Object> abstractAnalysisPseudonym, CoMoToAPIConnection connection) {

        //Grab the connection
        this.connection = connection;

        //Use reflection to fill the rest
        CoMoToAPIReflector<AnalysisPseudonym> reflector = new CoMoToAPIReflector<AnalysisPseudonym>();
        reflector.populate(this, abstractAnalysisPseudonym);
    }

    /**
     * {@inheritDoc}
     */
    public void refresh() {

        //Grab this object again from the API
        AnalysisPseudonym newAnalysisPseudonym = CoMoToAPI.getAnalysisPseudonym(connection, id);

        //Copy the data from this new analysis into our own
        analysisId = newAnalysisPseudonym.getAnalysisId();
        pseudonym = newAnalysisPseudonym.getPseudonym();
        submissionId = newAnalysisPseudonym.getSubmissionId();

        //Invalidate the cached data in this object
        analysis = null;
        submission = null;
    }

    /**
     * Get the analysis object associated with this pseudonym
     *
     * @return The analysis object associated with this pseudonym
     */
    public Analysis getAnalysis() {

        //Load the analysis if not initialized by the analysis
        if(analysis==null){
            analysis = CoMoToAPI.getAnalysis(connection, analysisId);
        }
        return analysis;
    }

    /**
     * Get the submission object associated with this pseudonym
     *
     * @return The submission object associated with this pseudonym
     */
    public Submission getSubmission() {

        //Load the submission if not initialized by the analysis
        if(submission==null){
            submission = CoMoToAPI.getSubmission(connection, submissionId);
        }
        return submission;
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
