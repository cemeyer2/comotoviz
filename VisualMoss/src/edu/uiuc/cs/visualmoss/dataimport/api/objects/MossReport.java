/*
 * University of Illinois/NCSA
 * Open Source License
 *
 * Copyright (c) 2011 University of Illinois at Urbana-Champaign.
 * All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal with the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimers.
 *
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimers in the documentation and/or other materials provided
 *       with the distribution.
 *
 *     * Neither the names of the CoMoTo Project team, the University of
 *       Illinois at Urbana-Champaign, nor the names of its contributors
 *       may be used to endorse or promote products derived from this
 *       Software without specific prior written permission.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS WITH THE SOFTWARE.
 */

package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPI;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConnection;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 22, 2010
 * <p/>
 * <p> <p> Holds the data of a moss report
 */
public class MossReport implements Refreshable {

    /**
     * A unique id for the associated report
     */
    private int reportId;

    /**
     * A unique id for this MossReport
     */
    private int id;

    /**
     * The list of the ids of the associated moss report files
     */
    private List<Integer> mossReportFileIds;

    /**
     * The associated report object
     */
    private Report report = null;

    /**
     * A connection to the API for lazily loading and refreshing data
     */
    private CoMoToAPIConnection connection;

    /**
     * Constructs this moss report object
     *
     * @param abstractMossReport A map holding the data for this object
     * @param connection         A connection to the API for lazily loading and refreshing data
     */
    public MossReport(Map<String, Object> abstractMossReport, CoMoToAPIConnection connection) {

        //Save the connection
        this.connection = connection;

        // Populate this object using reflection
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
        if (report == null) {
            report = CoMoToAPI.getReport(connection, reportId);
        }
        return report;
    }

    public Map getMap() {
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
