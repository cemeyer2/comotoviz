package edu.uiuc.cs.visualmoss.dataimport.api.objects;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Holds the data for a submission file
 */
public class SubmissionFile {

    private int id;
    private int submissionId;
    private boolean meta;
    private String content;
    private String name;

    public SubmissionFile(int id, int submissionId, boolean meta, String content, String name) {
        this.id = id;
        this.submissionId = submissionId;
        this.meta = meta;
        this.content = content;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public boolean isMeta() {
        return meta;
    }

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }
}
