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

package edu.illinois.comoto.api.object;

import edu.illinois.comoto.api.CoMoToAPI;
import edu.illinois.comoto.api.CoMoToAPIException;
import edu.illinois.comoto.api.utility.Cache;
import edu.illinois.comoto.api.utility.Connection;
import edu.illinois.comoto.api.utility.Reflector;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 18, 2010
 * <p/>
 * <p> <p> Holds the data for an analysis pseudonym
 */
public class AnalysisPseudonym implements Refreshable, Cacheable, Verifiable {

    /**
     * The id that uniquely identifies this analysis pseudonym
     */
    private int id = -1;

    /**
     * The id of the analysis associated with this pseudonym
     */
    private int analysisId = -1;

    /**
     * The numerical pseudonym
     */
    private String pseudonym = null;

    /**
     * The unique id of the associated submission
     */
    private int submissionId = -1;

    /**
     * The analysis associated with this pseudonym, either initialized with this object or lazily loaded
     */
    private Analysis analysis = null;

    /**
     * The submission object associated with this pseudonym, either initialized with this object or lazily loaded
     */
    private Submission submission = null;

    /**
     * A connection to the CoMoTo API for use when lazily loading attributes
     */
    private Connection connection;

    /**
     * Constructor for the AnalysisPseudonym class
     *
     * @param abstractAnalysisPseudonym The map of attributes for the pseudonym
     * @param connection                The connection to the API to use when lazily loading attributes
     */
    public AnalysisPseudonym(Map<String, Object> abstractAnalysisPseudonym, Connection connection) {

        // Check for generic bad inputs into the constructor
        if (abstractAnalysisPseudonym != null && connection != null) {

            //Grab the connection
            this.connection = connection;

            //Use reflection to fill the rest
            Reflector<AnalysisPseudonym> reflector = new Reflector<AnalysisPseudonym>();
            reflector.populate(this, abstractAnalysisPseudonym);
        } else {
            throw new CoMoToAPIException("Cannot create AnalysisPseudonym object given NULL inputs to constructor!");
        }

        // Verify that all fields were initialized
        verify();
    }

    /**
     * {@inheritDoc}
     * <p/>
     * This implementation verifies that no fields, other than <code>submission</code> and <code>analysis</code> are
     * uninitialized after we try to build this object.
     */
    @Override
    public void verify() throws CoMoToAPIException {
        if (id == -1 || analysisId == -1 || submissionId == -1 || pseudonym == null) {
            throw new CoMoToAPIException("Cannot create AnalysisPseudonym object given incomplete Map!");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void refresh() {
        Cache.remove(this);
        //Grab this object again from the API
        AnalysisPseudonym newAnalysisPseudonym = CoMoToAPI.getAnalysisPseudonym(connection, id);

        //Copy the data from this new analysis into our own
        analysisId = newAnalysisPseudonym.getAnalysisId();
        pseudonym = newAnalysisPseudonym.getPseudonym();
        submissionId = newAnalysisPseudonym.getSubmissionId();

        //Invalidate the cached data in this object
        analysis = null;
        submission = null;
    }

    /**
     * Get the analysis object associated with this pseudonym
     *
     * @return The analysis object associated with this pseudonym
     */
    public Analysis getAnalysis() {

        //Load the analysis if not initialized by the analysis
        if (analysis == null) {
            analysis = CoMoToAPI.getAnalysis(connection, analysisId);
        }
        return analysis;
    }

    /**
     * Get the submission object associated with this pseudonym
     *
     * @return The submission object associated with this pseudonym
     */
    public Submission getSubmission() {

        //Load the submission if not initialized by the analysis
        if (submission == null) {
            submission = CoMoToAPI.getSubmission(connection, submissionId);
        }
        return submission;
    }

    public Map getMap() {
        return new HashMap();
    }

    public int getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(int analysisId) {
        this.analysisId = analysisId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }
}
