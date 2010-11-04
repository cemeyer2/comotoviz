package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPI;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConnection;
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
public class MossReport implements Refreshable{

    /**
     * A unique id for this MossReport
     */
    private int id;

    /**
     * A unique id for the associated report
     */
    private int reportId;

    /**
     * The associated report object
     */
    private Report report = null;

    /**
     * The list of the ids of the associated moss report files
     */
    private List<Integer> mossReportFileIds;

    /**
     * A connection to the API for lazily loading and refreshing data
     */
    private CoMoToAPIConnection connection;

    /**
     * Constructs this moss report object
     *
     * @param abstractMossReport A map holding the data for this object
     * @param connection A connection to the API for lazily loading and refreshing data
     */
    public MossReport(Map<String, Object> abstractMossReport, CoMoToAPIConnection connection) {

        //Save the connection
        this.connection = connection;

        //Explicitly add the non-primitive types
        Integer[] abstractMatches = (Integer[]) abstractMossReport.get(MOSS_REPORT_FILE_IDS);
        mossReportFileIds = Arrays.asList(abstractMatches);

        //Remove it from the map
        abstractMossReport.remove(MOSS_REPORT_FILE_IDS);

        CoMoToAPIReflector<MossReport> reflector = new CoMoToAPIReflector<MossReport>();
        reflector.populate(this, abstractMossReport);
    }

    /**
     * {@inheritDoc}
     */
    public void refresh() {

        //First, load the new moss report from the api
        MossReport newMossReport = CoMoToAPI.getMossReport(connection, id);

        //Copy all the primitive data over
        reportId = newMossReport.getReportId();
        mossReportFileIds = newMossReport.getMossReportFileIds();

        //Clear cached data
        report = null;
    }

    /**
     * Gets the associated report from the API if not cached in this object
     *
     * @return The report
     */
    public Report getReport() {

        //Grab the report from the API if not cached
        if(report == null) {
            report = CoMoToAPI.getReport(connection, reportId);
        }
        return report;
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
