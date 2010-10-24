package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConstants.MOSS_REPORT_FILE_IDS;

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

    public MossReport(Map<String, Object> abstractMossReport) {


        //Explicitly add the non-primitive types
        Integer[] abstractMatches = (Integer[]) abstractMossReport.get(MOSS_REPORT_FILE_IDS);
        mossReportFileIds = Arrays.asList(abstractMatches);

        //Remove it from the map
        abstractMossReport.remove(MOSS_REPORT_FILE_IDS);

        CoMoToAPIReflector<MossReport> reflector = new CoMoToAPIReflector<MossReport>();
        reflector.populate(this, abstractMossReport);
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
