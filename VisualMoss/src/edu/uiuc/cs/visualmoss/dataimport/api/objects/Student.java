package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import java.net.URL;
import java.util.List;

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

    public Student(int id, URL historyLink, String netid, List<Integer> submissionIds, List<MossMatch> matches) {
        this.id = id;
        this.historyLink = historyLink;
        this.netid = netid;
        this.submissionIds = submissionIds;
        this.matches = matches;
    }

    public int getId() {
        return id;
    }

    public URL getHistoryLink() {
        return historyLink;
    }

    public String getNetid() {
        return netid;
    }

    public List<Integer> getSubmissionIds() {
        return submissionIds;
    }

    public List<MossMatch> getMatches() {
        return matches;
    }
}
