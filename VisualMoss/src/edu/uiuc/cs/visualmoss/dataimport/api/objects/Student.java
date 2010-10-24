package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;

import java.net.URL;
import java.util.*;

import static edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConstants.MATCHES;
import static edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConstants.SUBMISSION_IDS;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Holds the data for a student
 */
public class Student {

    private int id;
    private URL historyLink;
    private String netid;
    private List<Integer> submissionIds;
    private List<MossMatch> matches;

    public Student() {
    }

    public Student(Map abstractStudent) {

        //Explicitly add non-primitive types
        Integer[] submissionIdsArray = (Integer[]) abstractStudent.get(SUBMISSION_IDS);
        Map[] matchesArray = (Map[]) abstractStudent.get(MATCHES);
        submissionIds = Arrays.asList(submissionIdsArray);
        matches = new ArrayList<MossMatch>();
        for(Map matchMap : matchesArray){
            matches.add(new MossMatch(matchMap));
        }

        //Remove them from the map
        abstractStudent.remove(SUBMISSION_IDS);
        abstractStudent.remove(MATCHES);

        CoMoToAPIReflector<Student> reflector = new CoMoToAPIReflector<Student>();
        reflector.populate(this, abstractStudent);
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

    public URL getHistoryLink() {
        return historyLink;
    }

    public void setHistoryLink(URL historyLink) {
        this.historyLink = historyLink;
    }

    public String getNetid() {
        return netid;
    }

    public void setNetid(String netid) {
        this.netid = netid;
    }

    public List<Integer> getSubmissionIds() {
        return submissionIds;
    }

    public void setSubmissionIds(List<Integer> submissionIds) {
        this.submissionIds = submissionIds;
    }

    public List<MossMatch> getMatches() {
        return matches;
    }

    public void setMatches(List<MossMatch> matches) {
        this.matches = matches;
    }
}
