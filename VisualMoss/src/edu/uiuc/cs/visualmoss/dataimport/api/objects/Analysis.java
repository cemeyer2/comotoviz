package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPI;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConnection;
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
public class Analysis implements Refreshable {

    /**
     * The unique id that identifies this analysis in the API
     */
    private int id;

    /**
     * The timestamp of this analysis
     */
    private DateFormat timestamp;

    /**
     * Whether this analysis is complete
     */
    private boolean complete;

    /**
     * The id to uniquely identify the associated moss analysis
     */
    private int assignmentId;

    /**
     * The actual moss analysis object associated with this analysis, loaded lazily
     */
    private Assignment assignment = null;

    /**
     * The id to uniquely identify the associated moss analysis with this aggregate analysis
     */
    private int mossAnalysisId;

    /**
     * The actual moss analysis object associated with this analysis, loaded lazily
     */
    private MossAnalysis mossAnalysis = null;

    /**
     * The id to uniquely identify the associated jplag analysis with this aggregate analysis
     */
    private int jplagAnalysisId;

    /**
     * The actual jplag analysis object associated with this analysis, loaded lazily
     */
    private JplagAnalysis jplagAnalysis;

    /**
     * The pseudonyms for the this analysis
     */
    private List<AnalysisPseudonym> analysisPseudonyms;

    /**
     * The connection to the CoMoToAPI. Having a local copy of this connection allows this object to refresh itself with
     *  the API without needed to reuse the API class directly
     */
    private CoMoToAPIConnection connection;

    /**
     * Constructor that initializes this object from an abstract map, pulled directly from the API. Using this map, this
     *  object will populate itself via reflection.
     *
     * @param abstractAnalysis  A map of attribute names and data to which to initialize this object
     * @param connection        A connection to the API, to be used for lazy loading
     */
    public Analysis(Map<String, Object> abstractAnalysis, CoMoToAPIConnection connection) {

        //Grab the connection to the API so we can load attributes lazily
        this.connection = connection;

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

    /**
     * {@inheritDoc}
     */
    public void refresh() {

        //Grab this object again from the API
        Analysis newAnalysis = CoMoToAPI.getAnalysis(connection, id);

        //Copy the data from this new analysis into our own
        analysisPseudonyms = newAnalysis.getAnalysisPseudonyms();
        jplagAnalysisId = newAnalysis.getJplagAnalysisId();
        mossAnalysisId = newAnalysis.getMossAnalysisId();
        timestamp = newAnalysis.getTimestamp();
        complete = newAnalysis.isComplete();
        assignmentId = newAnalysis.getAssignmentId();
    }

    /**
     * Gets the moss analysis object associated with this analysis
     *
     * @return The moss analysis object associated with this analysis
     */
    public MossAnalysis getMossAnalysis(){

        //Grab the moss analysis associated with this analysis in the API
        if(mossAnalysis==null){
            mossAnalysis = CoMoToAPI.getMossAnalysis(connection, mossAnalysisId);
        }
        return mossAnalysis;
    }

    /**
     * Gets the assignment object associated with this object
     *
     * @return The assignment object associated with this analysis
     */
    public Assignment getAssignment() {

        //Grab the assignment associated with this analysis in the API
        if(assignment==null){
            assignment = CoMoToAPI.getAssignment(connection, id);
        }
        return assignment;
    }

    /**
     * Gets the jplag analysis object associated with this analysis
     *
     * @return The jplag analysis object associated with this analysis
     */
    public JplagAnalysis getJplagAnalysis() {

        //Grab the analysis associated with this analysis in the API
        if(jplagAnalysis==null){
            jplagAnalysis = CoMoToAPI.getJplagAnalysis(connection, id);
        }
        return jplagAnalysis;
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
