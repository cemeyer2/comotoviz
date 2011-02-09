package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPI;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConnection;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConstants.MATCHES;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Holds the data for a student
 */
public class Student implements Refreshable{

    /**
     * The name to display for this student
     */
    private String displayName;

    /**
     * The given name for this student
     */
    private String givenName;

    /**
     * The link to the history for this student
     */
    private URL historyLink;

    /**
     * The unique id for this student
     */
    private int id;

    /**
     * The LDAP authentication info for this student
     */
    private String ldapDn;

    /**
     * Whether this student has left UIUC
     */
    private String leftUiuc;

    /**
     * The 'level' of this student
     */
    private String levelName;

    /**
     * The student's netid
     */
    private String netid;

    /**
     * The program this student is in
     */
    private String programName;

    /**
     * The list of ids for the submissions from this student
     */
    private List<Integer> submissionIds;

    /**
     * The 'surname' for this student
     */
    private String surName;

    /**
     * The list of the student's matches
     */
    private List<MossMatch> matches;

    /**
     * The list of associated submission objects
     */
    private List<Submission> submissions = null;

    /**
     * The API connection
     */
    private CoMoToAPIConnection connection;

    /**
     * Creates a student object
     *
     * @param abstractStudent A map containing the data for this object
     * @param connection The API connections
     */
    public Student(Map abstractStudent, CoMoToAPIConnection connection) {

        //Save the connection
        this.connection = connection;

        //Explicitly add non-primitive types
        Object[] matchesArray = (Object[]) abstractStudent.get(MATCHES);
        if(matches != null){
            matches = new ArrayList<MossMatch>();
            for(Object matchMap : matchesArray){
                matches.add(new MossMatch((Map<String, Object>) matchMap, connection));
            }
            abstractStudent.remove(MATCHES);
        }

        //Populate this object using reflection
        CoMoToAPIReflector<Student> reflector = new CoMoToAPIReflector<Student>();
        reflector.populate(this, abstractStudent);
    }

    /**
     * {@inheritDoc}
     */
    public void refresh() {

        //First, grab the new student object from the API
        Student newStudent = CoMoToAPI.getStudent(connection, id, true);

        //Copy the basic data
        historyLink = newStudent.getHistoryLink();
        netid = newStudent.getNetid();
        submissionIds = newStudent.getSubmissionIds();
        matches = newStudent.getMatches();

        //Clear cached data
        submissions = null;
    }

    /**
     * Gets the associated submission objects for this student lazily
     *
     * @return The list of submissions for this student
     */
    public List<Submission> getSubmissions() {

        //Only call the API if it's not cached
        if(submissions == null) {
            submissions = new ArrayList<Submission>();
            for(int submissionId : submissionIds){
                submissions.add(CoMoToAPI.getSubmission(connection, submissionId));
            }
        }
        return submissions;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public URL getHistoryLink() {
        return historyLink;
    }

    public void setHistoryLink(URL historyLink) {
        this.historyLink = historyLink;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLdapDn() {
        return ldapDn;
    }

    public void setLdapDn(String ldapDn) {
        this.ldapDn = ldapDn;
    }

    public String getLeftUiuc() {
        return leftUiuc;
    }

    public void setLeftUiuc(String leftUiuc) {
        this.leftUiuc = leftUiuc;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getNetid() {
        return netid;
    }

    public void setNetid(String netid) {
        this.netid = netid;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public List<Integer> getSubmissionIds() {
        return submissionIds;
    }

    public void setSubmissionIds(List<Integer> submissionIds) {
        this.submissionIds = submissionIds;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public List<MossMatch> getMatches() {
        return matches;
    }

    public void setMatches(List<MossMatch> matches) {
        this.matches = matches;
    }

    public CoMoToAPIConnection getConnection() {
        return connection;
    }

    public void setConnection(CoMoToAPIConnection connection) {
        this.connection = connection;
    }
}
