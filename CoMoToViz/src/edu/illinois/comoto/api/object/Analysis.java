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
import edu.illinois.comoto.api.utility.Cache;
import edu.illinois.comoto.api.utility.Connection;
import edu.illinois.comoto.api.utility.Reflector;

import java.util.*;

import static edu.illinois.comoto.api.CoMoToAPIConstants.ANALYSIS_PSEUDONYMS;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 * <p/>
 * <p> <p> Holds the data of an analysis
 */
public class Analysis implements Refreshable, Cacheable, Verifiable {

    /**
     * The unique id that identifies this analysis in the API
     */
    private int id = -1;

    /**
     * The timestamp of this analysis
     */
    private Date timestamp = null;

    /**
     * Whether this analysis is complete
     */
    private boolean complete;

    /**
     * The id to uniquely identify the associated moss analysis
     */
    private int assignmentId = -1;

    /**
     * The id to uniquely identify the associated moss analysis with this aggregate analysis
     */
    private int mossAnalysisId = -1;

    /**
     * The id to uniquely identify the associated jplag analysis with this aggregate analysis
     */
    private int jplagAnalysisId = -1;

    /**
     * The pseudonyms for the this analysis
     */
    private List<AnalysisPseudonym> analysisPseudonyms = null;

    /**
     * The actual moss analysis object associated with this analysis, loaded lazily
     */
    private Assignment assignment = null;

    /**
     * The actual jplag analysis object associated with this analysis, loaded lazily
     */
    private JplagAnalysis jplagAnalysis = null;

    /**
     * The actual moss analysis object associated with this analysis, loaded lazily
     */
    private MossAnalysis mossAnalysis = null;

    /**
     * The connection to the CoMoToAPI. Having a local copy of this connection allows this object to refresh itself with
     * the API without needed to reuse the API class directly
     */
    private Connection connection;

    /**
     * Constructor that initializes this object from an abstract map, pulled directly from the API. Using this map, this
     * object will populate itself via reflection.
     *
     * @param abstractAnalysis A map of attribute names and data to which to initialize this object
     * @param connection       A connection to the API, to be used for lazy loading
     * @throws CoMoToAPIException If invalid inputs to the constructor prevent successfully creating the object
     */
    public Analysis(Map<String, Object> abstractAnalysis, Connection connection) throws CoMoToAPIException {

        // Check for null inputs, and just fail in this case (neither may be null!)
        if (connection != null && abstractAnalysis != null) {

            //Grab the connection to the API so we can load attributes lazily
            this.connection = connection;

            //Add analysis pseudonyms explicitly
            Object[] abstractAnalysisPseudonymsArray = (Object[]) abstractAnalysis.get(ANALYSIS_PSEUDONYMS);
            if (abstractAnalysisPseudonymsArray != null) {
                analysisPseudonyms = new ArrayList<AnalysisPseudonym>();
                for (Object abstractAnalysisPseudonym : abstractAnalysisPseudonymsArray) {
                    Map analysisPseudonymMap = (Map) abstractAnalysisPseudonym;
                    AnalysisPseudonym ap = new AnalysisPseudonym(analysisPseudonymMap, connection);
                    Cache.put(ap);
                    analysisPseudonyms.add(ap);
                }
                abstractAnalysis.remove(ANALYSIS_PSEUDONYMS);
            }

            // Populate the rest of this object using reflection
            Reflector<Analysis> reflector = new Reflector<Analysis>();
            reflector.populate(this, abstractAnalysis);

        } else {
            throw new CoMoToAPIException(CoMoToAPIConstants.getNullParamsMessage("Analysis"));
        }

        // Verify that all necessary fields have been assigned
        verify(abstractAnalysis);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * This object fails if any fields other than <code>assignment</code>, <code>jplagAnalysis</code>, and
     * <code>mossAnalysis</code> were not initialized.
     */
    @Override
    public void verify(Map<String, Object> objectMap) throws CoMoToAPIException {
        if (analysisPseudonyms == null || assignmentId == -1 || id == -1 || timestamp == null ||
                mossAnalysisId == -1 || jplagAnalysisId == -1) {

            System.err.println("Invalid Analysis:");
            for (String key : objectMap.keySet()) {
                System.err.println("\t" + key + " : " + objectMap.get(key));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void refresh() {

        Cache.remove(this);

        //Grab this object again from the API
        Analysis newAnalysis = CoMoToAPI.getAnalysis(connection, id);

        //Copy the data from this new analysis into our own
        timestamp = newAnalysis.getTimestamp();
        complete = newAnalysis.isComplete();
        assignmentId = newAnalysis.getAssignmentId();
        mossAnalysisId = newAnalysis.getMossAnalysisId();
        jplagAnalysisId = newAnalysis.getJplagAnalysisId();
        analysisPseudonyms = newAnalysis.getAnalysisPseudonyms();

        //Void the cached objects
        assignment = null;
        jplagAnalysis = null;
        mossAnalysis = null;
    }

    /**
     * Gets the moss analysis object associated with this analysis
     *
     * @return The moss analysis object associated with this analysis
     */
    public MossAnalysis getMossAnalysis() {
        return getMossAnalysis(false);
    }

    /**
     * Gets the moss analysis object associated with this analysis
     *
     * @param categorizeMatches Whether to categorize the matches from the API
     * @return The moss analysis object associated with this analysis
     */
    public MossAnalysis getMossAnalysis(boolean categorizeMatches) {

        //Grab the moss analysis associated with this analysis in the API
        if (mossAnalysis == null) {
            mossAnalysis = CoMoToAPI.getMossAnalysis(connection, mossAnalysisId, categorizeMatches);
        }
        return mossAnalysis;
    }

    /**
     * Gets the assignment object associated with this object
     *
     * @return The assignment object associated with this analysis
     */
    public Assignment getAssignment() {

        //Grab the assignment associated with this analysis in the API
        if (assignment == null) {
            assignment = CoMoToAPI.getAssignment(connection, id);
        }
        return assignment;
    }

    /**
     * Gets the jplag analysis object associated with this analysis
     *
     * @return The jplag analysis object associated with this analysis
     */
    public JplagAnalysis getJplagAnalysis() {

        //Grab the analysis associated with this analysis in the API
        if (jplagAnalysis == null) {
            jplagAnalysis = CoMoToAPI.getJplagAnalysis(connection, id);
        }
        return jplagAnalysis;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public int getMossAnalysisId() {
        return mossAnalysisId;
    }

    public void setMossAnalysisId(int mossAnalysisId) {
        this.mossAnalysisId = mossAnalysisId;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public int getJplagAnalysisId() {
        return jplagAnalysisId;
    }

    public void setJplagAnalysisId(int jplagAnalysisId) {
        this.jplagAnalysisId = jplagAnalysisId;
    }

    public List<AnalysisPseudonym> getAnalysisPseudonyms() {
        return analysisPseudonyms;
    }

    public void setAnalysisPseudonyms(List<AnalysisPseudonym> analysisPseudonyms) {
        this.analysisPseudonyms = analysisPseudonyms;
    }
}
