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
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPICache;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConnection;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;

import java.util.HashMap;
import java.util.Map;

import static edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConstants.JPLAG_REPORT;
import static edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConstants.MOSS_REPORT;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 * <p/>
 * <p> <p> Holds the data of a report
 */
public class Report implements Refreshable, Cacheable {

    /**
     * The id of the associated assignment object
     */
    private int assignmentId;

    /**
     * Whether or not this report is complete
     */
    private boolean complete;

    /**
     * The unique id for this report
     */
    private int id;

    /**
     * The associated jplag report object
     */
    private JplagReport jplagReport;

    /**
     * The associated moss report object
     */
    private MossReport mossReport;

    /**
     * The associated assignment
     */
    private Assignment assignment = null;

    /**
     * The connection to the API
     */
    private CoMoToAPIConnection connection;

    /**
     * Creates a report object
     *
     * @param abstractReport A map containing the data of the report
     * @param connection     A connection to the API for lazily loading attributes and refreshing
     */
    public Report(Map<String, Object> abstractReport, CoMoToAPIConnection connection) {

        //Save the connection
        this.connection = connection;

        //Explicitly add non-primitive types
        Map jplagReportMap = (Map) abstractReport.get(JPLAG_REPORT);
        Map mossReportMap = (Map) abstractReport.get(MOSS_REPORT);
        jplagReport = new JplagReport(jplagReportMap, connection);
        mossReport = new MossReport(mossReportMap, connection);
        CoMoToAPICache.put(jplagReport);
        CoMoToAPICache.put(mossReport);

        //Remove them from the map
        abstractReport.remove(JPLAG_REPORT);
        abstractReport.remove(MOSS_REPORT);

        //Populate this object using reflection
        CoMoToAPIReflector<Report> reflector = new CoMoToAPIReflector<Report>();
        reflector.populate(this, abstractReport);
    }

    /**
     * {@inheritDoc}
     */
    public void refresh() {

        //First, grab the new report object
        Report newReport = CoMoToAPI.getReport(connection, id);

        //Copy the primitive values first
        complete = newReport.isComplete();
        assignmentId = newReport.getAssignmentId();
        jplagReport = newReport.getJplagReport();
        mossReport = newReport.getMossReport();

        //Clear cached data
        assignment = null;
    }

    /**
     * Gets the associated assignment from the API if not cached in this object
     *
     * @return The assignment
     */
    public Assignment getAssignment() {

        //Only call API if not cached
        if (assignment == null) {
            assignment = CoMoToAPI.getAssignment(connection, assignmentId);
        }
        return assignment;
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
