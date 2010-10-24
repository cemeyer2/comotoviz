package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;

import java.util.HashMap;
import java.util.Map;

import static edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConstants.JPLAG_REPORT;
import static edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConstants.MOSS_REPORT;

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

    public Report(Map<String, Object> abstractReport) {

        //Explicitly add non-primitive types
        Map jplagReportMap = (Map) abstractReport.get(JPLAG_REPORT);
        Map mossReportMap = (Map) abstractReport.get(MOSS_REPORT);
        jplagReport = new JplagReport(jplagReportMap);
        mossReport = new MossReport(mossReportMap);

        //Remove them from the map
        abstractReport.remove(JPLAG_REPORT);
        abstractReport.remove(MOSS_REPORT);

        CoMoToAPIReflector<Report> reflector = new CoMoToAPIReflector<Report>();
        reflector.populate(this, abstractReport);
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
