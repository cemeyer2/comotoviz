package edu.uiuc.cs.visualmoss.dataimport.api.objects;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Holds the data of a report
 */
public class Report {

    private int id;
    private JplagReport jplagReport; 
    private boolean complete;
    private int assignmentId;
    private MossReport mossReport;

    public Report(int id, JplagReport jplagReport, boolean complete, int assignmentId, MossReport mossReport) {
        this.id = id;
        this.jplagReport = jplagReport;
        this.complete = complete;
        this.assignmentId = assignmentId;
        this.mossReport = mossReport;
    }

    public int getId() {
        return id;
    }

    public JplagReport getJplagReport() {
        return jplagReport;
    }

    public boolean isComplete() {
        return complete;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public MossReport getMossReport() {
        return mossReport;
    }
}
