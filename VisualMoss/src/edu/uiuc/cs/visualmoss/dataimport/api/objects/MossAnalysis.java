package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPI;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConnection;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConstants.*;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Holds the data of a MOSS analysis
 */
public class MossAnalysis implements Refreshable{

    /**
     * A unique id for the associated analysis
     */
    private int analysisId;

    /**
     * A unique id for this moss analysis object
     */
    private int id;

    /**
     * A list of the matches for this moss analysis
     */
    private List<MossMatch> matches = null;

    /**
     * A list of the cross-semester matches for this moss analysis
     */
    private List<MossMatch> crossSemesterMatches = null;
    /**
     * A list of the same-semester matches for this moss analysis
     */
    private List<MossMatch> sameSemesterMatches = null;
    /**
     * A list of the solution matches for this moss analysis
     */
    private List<MossMatch> solutionMatches = null;

    /**
     * The associated analysis
     */
    private Analysis analysis = null;

    /**
     * A connection to the API used for lazily loading components of this object and refreshing
     */
    private CoMoToAPIConnection connection;

    /**
     * Whether this analysis contains categorized matches
     */
    private boolean categorizedMatches = false;

    /**
     * Build a moss analysis object
     *
     * @param abstractMossAnalysis A map holding the data for this analysis
     * @param connection The connection to the API
     */
    public MossAnalysis(Map<String, Object> abstractMossAnalysis, CoMoToAPIConnection connection) {

        //Store this connection
        this.connection = connection;

        //Explicitly build and add the general matches objects
        Object[] abstractMatches = (Object[]) abstractMossAnalysis.get(MATCHES);
        if(abstractMatches != null){
            matches = new ArrayList<MossMatch>();
            for(Object abstractMatch : abstractMatches){
                matches.add(new MossMatch((Map<String, Object>) abstractMatch, connection));
            }
            abstractMossAnalysis.remove(MATCHES);

        }

        //Explicitly build and add the cross-semester matches objects
        Object[] abstractCrossSemesterMatches = (Object[]) abstractMossAnalysis.get(CROSS_SEMESTER_MATCHES);
        if(abstractCrossSemesterMatches != null){
            categorizedMatches = true;
            crossSemesterMatches = new ArrayList<MossMatch>();
            for(Object abstractMatch : abstractCrossSemesterMatches){
                crossSemesterMatches.add(new MossMatch((Map<String, Object>) abstractMatch, connection));
            }
            abstractMossAnalysis.remove(CROSS_SEMESTER_MATCHES);

        }

        //Explicitly build and add the same-semester matches objects
        Object[] abstractSameSemesterMatches = (Object[]) abstractMossAnalysis.get(SAME_SEMESTER_MATCHES);
        if(abstractSameSemesterMatches != null){
            sameSemesterMatches = new ArrayList<MossMatch>();
            for(Object abstractMatch : abstractSameSemesterMatches){
                sameSemesterMatches.add(new MossMatch((Map<String, Object>) abstractMatch, connection));
            }
            abstractMossAnalysis.remove(SAME_SEMESTER_MATCHES);

        }

        //Explicitly build and add the solution matches objects
        Object[] abstractSolutionMatches = (Object[]) abstractMossAnalysis.get(SOLUTION_MATCHES);
        if(abstractSolutionMatches != null){
            solutionMatches = new ArrayList<MossMatch>();
            for(Object abstractMatch : abstractSolutionMatches){
                solutionMatches.add(new MossMatch((Map<String, Object>) abstractMatch, connection));
            }
            abstractMossAnalysis.remove(SOLUTION_MATCHES);

        }

        // Populate the rest of this object using reflection
        CoMoToAPIReflector<MossAnalysis> reflector = new CoMoToAPIReflector<MossAnalysis>();
        reflector.populate(this, abstractMossAnalysis);
    }

    /**
     * {@inheritDoc}
     */
    public void refresh() {

        //First, grab the new object from the API
        MossAnalysis newMossAnalysis;
        if(categorizedMatches){
            newMossAnalysis = CoMoToAPI.getMossAnalysis(connection, id, true);
        } else {
            newMossAnalysis = CoMoToAPI.getMossAnalysis(connection, id);
        }

        //Copy the primitive data over
        analysisId = newMossAnalysis.getAnalysisId();
        matches = newMossAnalysis.getMatches();
        if(categorizedMatches){
            crossSemesterMatches = newMossAnalysis.getCrossSemesterMatches();
            sameSemesterMatches = newMossAnalysis.getSameSemesterMatches();
            solutionMatches = newMossAnalysis.getSolutionMatches();
        } else {
            crossSemesterMatches = null;
            sameSemesterMatches = null;
            solutionMatches = null;
        }

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

    public List<MossMatch> getCrossSemesterMatches() {
        return crossSemesterMatches;
    }

    public void setCrossSemesterMatches(List<MossMatch> crossSemesterMatches) {
        this.crossSemesterMatches = crossSemesterMatches;
    }

    public List<MossMatch> getSameSemesterMatches() {
        return sameSemesterMatches;
    }

    public void setSameSemesterMatches(List<MossMatch> sameSemesterMatches) {
        this.sameSemesterMatches = sameSemesterMatches;
    }

    public List<MossMatch> getSolutionMatches() {
        return solutionMatches;
    }

    public void setSolutionMatches(List<MossMatch> solutionMatches) {
        this.solutionMatches = solutionMatches;
    }
}
