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
import edu.illinois.comoto.api.utility.Cache;
import edu.illinois.comoto.api.utility.Connection;
import edu.illinois.comoto.api.utility.Reflector;

import java.util.Map;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 * <p/>
 * <p> <p> Holds the data for a submission file
 */
public class SubmissionFile implements Refreshable, Cacheable {

    /**
     * The content of this file
     */
    private String content;

    /**
     * The unique id for this submission file
     */
    private int id;

    /**
     * Whether this submission file is 'meta'
     */
    private boolean meta;

    /**
     * The name of this file
     */
    private String name;

    /**
     * The id for the associated submission
     */
    private int submissionId;

    /**
     * The associated submission object
     */
    private Submission submission = null;

    /**
     * The API connections
     */
    private Connection connection;


    public SubmissionFile(Map abstractSubmissionFile, Connection connection) {

        //Save the connection
        this.connection = connection;

        //Populate the rest using reflection
        Reflector<SubmissionFile> reflector = new Reflector<SubmissionFile>();
        reflector.populate(this, abstractSubmissionFile);
    }

    /**
     * {@inheritDoc}
     */
    public void refresh() {

        Cache.remove(this);

        //First, grab the new data from the API
        SubmissionFile newSubmissionFile = CoMoToAPI.getSubmissionFile(connection, id);

        //Copy the primitive data over
        content = newSubmissionFile.getContent();
        meta = newSubmissionFile.isMeta();
        name = newSubmissionFile.getName();
        submissionId = newSubmissionFile.getSubmissionId();

        //Clear cached data
        submission = null;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public boolean isMeta() {
        return meta;
    }

    public void setMeta(boolean meta) {
        this.meta = meta;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
