package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import java.util.List;

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

    public MossReport(int id, int reportId, List<Integer> mossReportFileIds) {
        this.id = id;
        this.reportId = reportId;
        this.mossReportFileIds = mossReportFileIds;
    }

    public int getId() {
        return id;
    }

    public int getReportId() {
        return reportId;
    }

    public List<Integer> getMossReportFileIds() {
        return mossReportFileIds;
    }
}
