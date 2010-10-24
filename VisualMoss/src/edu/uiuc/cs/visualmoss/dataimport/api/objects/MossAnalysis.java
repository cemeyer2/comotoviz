package edu.uiuc.cs.visualmoss.dataimport.api.objects;

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
public class MossAnalysis {

    private int id;
    private int analysisId;
    private List<MossMatch> matches;

    public MossAnalysis() {
    }

    public MossAnalysis(Map<String, Object> abstractMossAnalysis) {

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
