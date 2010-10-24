package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 22, 2010
 *
 * <p> <p> Holds the data of a moss report
 */
public class MossReport {

    private int id;
    private int reportId;
    private List<Integer> mossReportFileIds;

    public MossReport() {
    }

    public MossReport(Map abstractMossReport) {
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

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public List<Integer> getMossReportFileIds() {
        return mossReportFileIds;
    }

    public void setMossReportFileIds(List<Integer> mossReportFileIds) {
        this.mossReportFileIds = mossReportFileIds;
    }
}
