package edu.uiuc.cs.visualmoss.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Nov 8, 2010
 *
 * <p> <p> A simple representation of a student object for use with the VisualMossGraph class
 */
public class VisualMossGraphStudent implements Comparable<VisualMossGraphStudent>{

    /**
     * The basic information of a student
     */
    private final String netid, pseudonym;

    /**
     * The matches for this student
     */
    private final ArrayList<VisualMossGraphMatch> matches;

    /**
     * Creates a new, basic student object from the given data
     *
     * @param netid The netid of this student
     * @param pseudonym The pseudonym of this student
     */
    protected VisualMossGraphStudent(String netid, String pseudonym) {
        this.netid = netid;
        this.pseudonym = pseudonym;
        this.matches = new ArrayList<VisualMossGraphMatch>();
    }

    public String getNetid() {
        return netid;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    protected void addMatch(VisualMossGraphMatch match) {
        matches.add(match);
    }

    public List<VisualMossGraphMatch> getMatches() {
        return matches;
    }

    public int compareTo(VisualMossGraphStudent student) {
        return getNetid().compareTo(student.getNetid());
    }

    public String toString() {
        return getNetid();
    }
}
