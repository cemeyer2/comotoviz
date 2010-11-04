package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPI;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConnection;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConstants.MATCHES;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Holds the data of a MOSS analysis
 */
public class MossAnalysis implements Refreshable{

    /**
     * A unique id for this moss analysis object
     */
    private int id;

    /**
     * A unique id for the associated analysis
     */
    private int analysisId;

    /**
     * The associated analysis
     */
    private Analysis analysis = null;

    /**
     * A list of the matches for this moss analysis
     */
    private List<MossMatch> matches;

    /**
     * A connection to the API used for lazily loading components of this object and refreshing
     */
    private CoMoToAPIConnection connection;

    public MossAnalysis(Map<String, Object> abstractMossAnalysis, CoMoToAPIConnection connection) {

        //Store this connection
        this.connection = connection;

        //Explicitly add the non-primitive types
        Map[] abstractMatches = (Map[]) abstractMossAnalysis.get(MATCHES);
        matches = new ArrayList<MossMatch>();
        for(Map abstractMatch : abstractMatches){
            matches.add(new MossMatch(abstractMatch));
        }

        //Remove it from the map
        abstractMossAnalysis.remove(MATCHES);

        CoMoToAPIReflector<MossAnalysis> reflector = new CoMoToAPIReflector<MossAnalysis>();
        reflector.populate(this, abstractMossAnalysis);
    }

    /**
     * {@inheritDoc}
     */
    public void refresh() {

        //First, grab the new object from the API
        MossAnalysis newMossAnalysis = CoMoToAPI.getMossAnalysis(connection, id);

        //Copy the primitive data over
        analysisId = newMossAnalysis.getAnalysisId();
        matches = newMossAnalysis.getMatches();

        //Clear cached data
        analysis = null;
    }

    /**
     * Gets the associated analysis lazily
     *
     * @return The analysis object
     */
    public Analysis getAnalysis() {

        //If it's not cached, grab it from the API
        if(analysis == null){
            analysis = CoMoToAPI.getAnalysis(connection, analysisId);
        }
        return analysis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(int analysisId) {
        this.analysisId = analysisId;
    }

    public List<MossMatch> getMatches() {
        return matches;
    }

    public void setMatches(List<MossMatch> matches) {
        this.matches = matches;
    }
}
