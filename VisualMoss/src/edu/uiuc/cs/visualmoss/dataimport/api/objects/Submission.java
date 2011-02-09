package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPI;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConnection;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConstants.STUDENT;


/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Holds the data for a submission
 */
public class Submission implements Refreshable{

    /**
     * The pseudonym ids for this submission
     */
    private List<Integer> analysisPseudonymIds;

    /**
     * The unique id of the associated file set
     */
    private int fileSetId;

    /**
     * The unique id for this submission
     */
    private int id;

    /**
     * The id of the associated offering
     */
    private int offeringId;

    /**
     * A list of student ids representing the partners on this submission
     */
    private List<Integer> partnerIds;

    /**
     * The associated student object
     */
    private Student student;

    /**
     * The associated student id
     */
    private int studentId;

    /**
     * A list of the associated submission file ids
     */
    private List<Integer> submissionFileIds;

    /**
     * The type of this submission (Defaults to a student's submission)
     */
    private Type type = Type.studentsubmission;

    /**
     * The associated file set object
     */
    private FileSet fileSet = null;

    /**
     * The associated offering object
     */
    private Offering offering = null;

    /**
     * A list of associated students objects, representing the partners on this submission
     */
    private List<Student> partners = null;

    /**
     * A list of the associated submission files
     */
    private List<SubmissionFile> submissionFiles = null;

    /**
     * A connection to the API
     */
    private CoMoToAPIConnection connection;

    /**
     * Whether this object should load student data eagerly
     */
    private boolean fullStudentData = false;

    /**
     * Creates a new submission object
     *
     * @param abstractSubmission A map holding the submission data
     * @param connection A connection to the API
     */
    public Submission(Map abstractSubmission, CoMoToAPIConnection connection) {

        //Save the connection
        this.connection = connection;

        //Explicitly add the student object if it exists
        Map studentMap = (Map) abstractSubmission.get(STUDENT);
        if(studentMap != null){
            fullStudentData = true;
            student = new Student(studentMap, connection);
            abstractSubmission.remove(STUDENT);
        }

        CoMoToAPIReflector<Submission> reflector = new CoMoToAPIReflector<Submission>();
        reflector.populate(this, abstractSubmission);
    }

    /**
     * {@inheritDoc}
     */
    public void refresh() {

        //First, grab the new submission from the api
        Submission newSubmission;
        if(fullStudentData){
            newSubmission = CoMoToAPI.getSubmission(connection, id, true);
        } else {
            newSubmission = CoMoToAPI.getSubmission(connection, id);
        }

        //Copy over the primitive values
        analysisPseudonymIds = newSubmission.getAnalysisPseudonymIds();
        fileSetId = newSubmission.getFileSetId();
        offeringId = newSubmission.getOfferingId();
        partnerIds = newSubmission.getPartnerIds();
        studentId = newSubmission.getStudentId();
        submissionFileIds = newSubmission.getSubmissionFileIds();
        type = newSubmission.getType();
        if(fullStudentData){
            student = newSubmission.getStudent();
        } else {
            student = null;
        }

        //Clear cached data
        fileSet = null;
        offering = null;
        partners = null;
        submissionFiles = null;
    }

    /**
     * Get the file set from the API lazily using caching
     *
     * @return The associated file set
     */
    public FileSet getFileSet() {

        //Only call the API if it's not cached
        if(fileSet == null) {
            fileSet = CoMoToAPI.getFileSet(connection, fileSetId);
        }
        return fileSet;
    }

    /**
     * Get the offering from the API lazily
     *
     * @return The associated offering
     */
    public Offering getOffering() {

        //Only call the API if it's not cached
        if(offering == null) {
            offering = CoMoToAPI.getOffering(connection, offeringId);
        }
        return offering;
    }

    /**
     * Get the student from the API lazily
     *
     * @return The associated student
     */
    public Student getStudent() {

        //Only call the API if it's not cached
        if(student == null) {
            student = CoMoToAPI.getStudent(connection, studentId, true);
        }
        return student;
    }

    /**
     * Get the parters, or list of students, lazily from the API
     *
     * @return The list of students representing the partners for this submission
     */
    public List<Student> getPartners() {

        //Only call the API if it's not cached
        if(partners == null) {
            partners = new ArrayList<Student>();
            for(int partnerId : partnerIds){
                partners.add(CoMoToAPI.getStudent(connection, partnerId, true));
            }
        }
        return partners;
    }

    /**
     * Get the list of submission file lazily from the API
     *
     * @return The list of submission files
     */
    public List<SubmissionFile> getSubmissionFiles() {

        //Only call the API if it's not cached
        if(submissionFiles == null) {
            submissionFiles = new ArrayList<SubmissionFile>();
            for(int submissionFileId : submissionFileIds){
                submissionFiles.add(CoMoToAPI.getSubmissionFile(connection, submissionFileId));
            }
        }

        return submissionFiles;
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

    public int getFileSetId() {
        return fileSetId;
    }

    public void setFileSetId(int fileSetId) {
        this.fileSetId = fileSetId;
    }

    public int getOfferingId() {
        return offeringId;
    }

    public void setOfferingId(int offeringId) {
        this.offeringId = offeringId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public Type getType() {
        return type;
    }

    public void setType(String type) {
        this.type = Type.valueOf(type);
    }

    public List<Integer> getAnalysisPseudonymIds() {
        return analysisPseudonymIds;
    }

    public void setAnalysisPseudonymIds(List<Integer> analysisPseudonymIds) {
        this.analysisPseudonymIds = analysisPseudonymIds;
    }

    public List<Integer> getPartnerIds() {
        return partnerIds;
    }

    public void setPartnerIds(List<Integer> partnerIds) {
        this.partnerIds = partnerIds;
    }

    public List<Integer> getSubmissionFileIds() {
        return submissionFileIds;
    }

    public void setSubmissionFileIds(List<Integer> submissionFileIds) {
        this.submissionFileIds = submissionFileIds;
    }
}
