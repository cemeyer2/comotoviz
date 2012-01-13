/*
 * University of Illinois/NCSA
 * Open Source License
 *
 * Copyright (c) 2012 University of Illinois at Urbana-Champaign.
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

package edu.illinois.comoto.api.object;

import edu.illinois.comoto.api.CoMoToAPI;
import edu.illinois.comoto.api.CoMoToAPIConstants;
import edu.illinois.comoto.api.CoMoToAPIException;
import edu.illinois.comoto.api.utility.Accelerator;
import edu.illinois.comoto.api.utility.Cache;
import edu.illinois.comoto.api.utility.Connection;
import edu.illinois.comoto.api.utility.Reflector;
import org.apache.commons.lang.WordUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.illinois.comoto.api.CoMoToAPIConstants.GET_FILE_SET;
import static edu.illinois.comoto.api.CoMoToAPIConstants.MOSS_ANALYSIS_PRUNED_OFFERING;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 * <p/>
 * <p> <p> Holds the data of an assignment
 */
public class Assignment implements Refreshable, Cacheable, Verifiable {

    /**
     * Integer uniquely identifying this assignment
     */
    private int id = -1;

    /**
     * Unique id for the analysis associated with this assignment
     */
    private int analysisId = -1;

    /**
     * Unique id for the course associated with this assignment
     */
    private int courseId = -1;

    /**
     * Unique id for the report associated with this assignment
     */
    private int reportId = -1;

    /**
     * The language of this assignment
     */
    private Language language = null;

    /**
     * The name of this assignment
     */
    private String name = null;

    /**
     * The 'pruned offering' object that contains information about the semester & year of this class
     */
    private Offering mossAnalysisPrunedOffering = null;

    /**
     * The list of unique ids for the file sets associated with this assignment
     */
    private List<Integer> filesetIds = null;

    /**
     * The analysis object associated with this assignment
     */
    private Analysis analysis = null;

    /**
     * The course associated with this assignment
     */
    private Course course = null;

    /**
     * The list of file sets associated with this assignment
     */
    private List<FileSet> filesets = null;

    /**
     * A connection to the API for loading data lazily and refreshing this object
     */
    private Connection connection;

    /**
     * Constructor for an assignment object
     *
     * @param abstractAssignment A map holding the data of an assignment
     * @param connection         A connection to the API for refreshing this object and loading data lazily
     */
    public Assignment(Map<String, Object> abstractAssignment, Connection connection) {

        // Check for bad inputs
        if (abstractAssignment != null && connection != null) {

            //Store the connection
            this.connection = connection;

            // Extract the associated offering data out explicitly, if it exists
            Map abstractMossAnalysisPrunedOffering = (Map) abstractAssignment.get(MOSS_ANALYSIS_PRUNED_OFFERING);
            if (abstractMossAnalysisPrunedOffering != null) {
                mossAnalysisPrunedOffering = new Offering(abstractMossAnalysisPrunedOffering, connection);
                abstractAssignment.remove(MOSS_ANALYSIS_PRUNED_OFFERING);
            }

            //Populate this object using reflection
            Reflector<Assignment> reflector = new Reflector<Assignment>();
            reflector.populate(this, abstractAssignment);

        } else {
            throw new CoMoToAPIException(CoMoToAPIConstants.getNullParamsMessage("Assignment"));
        }

        // Verify this object was created successfully
        //CM: fix this like other verify methods
        //verify();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void verify() throws CoMoToAPIException {
        if (analysisId == -1 || courseId == -1 || filesetIds == null || id == -1 || language == null ||
                mossAnalysisPrunedOffering == null || name == null || reportId == -1) {
            throw new CoMoToAPIException(CoMoToAPIConstants.getInvalidParamsMessage("Assignment"));
        }
    }

    /**
     * {@inheritDoc}
     */
    public void refresh() {
        Cache.remove(this);
        //Grab the new assignment object
        Assignment newAssignment = CoMoToAPI.getAssignment(connection, id);

        //Refresh this data
        analysisId = newAssignment.getAnalysisId();
        courseId = newAssignment.getCourseId();
        language = newAssignment.getLanguage();
        name = newAssignment.getName();
        filesetIds = newAssignment.getFilesetIds();

        //Invalidate the cached data
        analysis = null;
        course = null;
        filesets = null;
    }

    /**
     * Gets the associated analysis lazily
     *
     * @return The analysis object
     */
    public Analysis getAnalysis() {

        //If it's not cached, grab it from the API
        if (analysis == null && analysisId >= 0) {
            analysis = CoMoToAPI.getAnalysis(connection, analysisId);
        }
        return analysis;
    }

    /**
     * Grabs the associated course lazily
     *
     * @return The course
     */
    public Course getCourse() {

        //Grab the course from the API if not cached
        if (course == null) {
            course = CoMoToAPI.getCourse(connection, courseId);
        }
        return course;
    }

    /**
     * Grabs the list of file sets lazily
     *
     * @return The list of file sets
     */
    public List<FileSet> getFilesets() {
        return getFilesets(false);
    }

    /**
     * Grabs the list of file sets lazily
     *
     * @param getSubmissionInfo Whether to load the submissions eagerly from the API
     * @return The list of file sets
     */
    public List<FileSet> getFilesets(boolean getSubmissionInfo) {

        //Grab the list from the API if not cached
        if (filesets == null) {

            // Build a 2D parameter list for the API accelerator
            Object[][] params = new Object[filesetIds.size()][3];
            int i = 0;
            for (int fileSetId : filesetIds) {
                params[i] = new Object[]{connection, fileSetId, getSubmissionInfo};
                i++;
            }

            // Get an accelerator and fetch these filesets using the accelerator
            Accelerator<FileSet> accelerator = new Accelerator<FileSet>();
            filesets = accelerator.getAPIObjects(connection, GET_FILE_SET, params);
        }
        return filesets;
    }

    /**
     * Shows the display name of the assignment and to represent this assignment as a string
     *
     * @return The string representing this assignment
     */
    @Override
    public String toString() {

        if (getName() != null) {
            //Make the names uniform and call them 'MP# ...'
            String unformattedName = getName().toLowerCase().trim();
            if (unformattedName.indexOf("mp") == 0) {
                unformattedName = "MP" + unformattedName.substring(2);
            }

            //Return the display name of this assignment
            return WordUtils.capitalize(unformattedName);
        } else {
            return "";
        }
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

    public int getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(int analysisId) {
        this.analysisId = analysisId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        // Take the full title as the name
        this.name = name;
    }

    public List<Integer> getFilesetIds() {
        return filesetIds;
    }

    public void setFilesetIds(List<Integer> filesetIds) {
        this.filesetIds = filesetIds;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public Offering getMossAnalysisPrunedOffering() {
        return mossAnalysisPrunedOffering;
    }

    public void setMossAnalysisPrunedOffering(Offering mossAnalysisPrunedOffering) {
        this.mossAnalysisPrunedOffering = mossAnalysisPrunedOffering;
    }
}
