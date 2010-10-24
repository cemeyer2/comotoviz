package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
