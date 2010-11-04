package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPI;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConnection;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;

import java.util.Map;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Holds the data for a submission file
 */
public class SubmissionFile implements Refreshable{

    /**
     * The unique id for this submission file
     */
    private int id;

    /**
     * The id for the associated submission
     */
    private int submissionId;

    /**
     * The associated submission object
     */
    private Submission submission = null;

    /**
     * Whether this submission file is 'meta'
     */
    private boolean meta;

    /**
     * The content of this file
     */
    private String content;

    /**
     * The name of this file
     */
    private String name;

    /**
     * The API connections
     */
    private CoMoToAPIConnection connection;

    public SubmissionFile(Map abstractSubmissionFile, CoMoToAPIConnection connection) {

        //Save the connection
        this.connection = connection;

        //Populate the rest using reflection
        CoMoToAPIReflector<SubmissionFile> reflector = new CoMoToAPIReflector<SubmissionFile>();
        reflector.populate(this, abstractSubmissionFile);
    }

    /**
     * {@inheritDoc}
     */
    public void refresh() {

        //First, grab the new data from the API
        SubmissionFile newSubmissionFile = CoMoToAPI.getSubmissionFile(connection, id);

        //Copy the primitive data over
        submissionId = newSubmissionFile.getSubmissionId();
        meta = newSubmissionFile.isMeta();
        content = newSubmissionFile.getContent();
        name = newSubmissionFile.getName();

        //Clear cached data
        submission = null;
    }

    /**
     * Get the submission object associated with this pseudonym
     *
     * @return The submission object associated with this pseudonym
     */
    public Submission getSubmission() {

        //Load the submission if not initialized by the analysis
        if(submission==null){
            submission = CoMoToAPI.getSubmission(connection, submissionId);
        }
        return submission;
    }
 
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public boolean isMeta() {
        return meta;
    }

    public void setMeta(boolean meta) {
        this.meta = meta;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
