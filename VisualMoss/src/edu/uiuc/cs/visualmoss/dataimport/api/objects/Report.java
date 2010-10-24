package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import java.util.HashMap;
import java.util.Map;

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

    public Report() {
    }

    public Report(Map abstractReport) {
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

    public JplagReport getJplagReport() {
        return jplagReport;
    }

    public void setJplagReport(JplagReport jplagReport) {
        this.jplagReport = jplagReport;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public MossReport getMossReport() {
        return mossReport;
    }

    public void setMossReport(MossReport mossReport) {
        this.mossReport = mossReport;
    }
}
