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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.illinois.comoto.api.CoMoToAPIConstants.OFFERING_INFO;
import static edu.illinois.comoto.api.CoMoToAPIConstants.SEMESTER;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 18, 2010
 * <p/>
 * <p> <p> Holds the data of an offering
 */
public class Offering implements Refreshable, Cacheable {

    /**
     * The unique id for this offering
     */
    private int id;

    /**
     * The unique id of the associated course
     */
    private int courseId;

    /**
     * The list of ids for the associated file sets
     */
    private List<Integer> filesetIds;

    /**
     * The student id's for the students enlisted in this offering
     */
    private List<Integer> rosterStudentIds;

    /**
     * The extra offering info for this offering
     */
    private List<OfferingInfo> offeringInfo = null;

    /**
     * The LDAP dns for this offering
     */
    private List<String> ldapDns;

    /**
     * The semester of this offering
     */
    private Semester semester;

    /**
     * The list of the associated file sets
     */
    private List<FileSet> filesets = null;

    /**
     * The associated course object
     */
    private Course course = null;

    /**
     * A connection to the API
     */
    private Connection connection;

    /**
     * Whether this object should eagerly load extra offering info
     */
    private boolean extraOfferingInfo = false;

    /**
     * Creates an offering object
     *
     * @param abstractOffering A map holding the data for this object
     * @param connection       A connection to the API for lazily loading and refreshing data
     */
    public Offering(Map abstractOffering, Connection connection) {

        //Save the connections
        this.connection = connection;

        //Explicitly add semester object if it exists
        Map semesterMap = (Map) abstractOffering.get(SEMESTER);
        if (semesterMap != null) {
            semester = new Semester(semesterMap, connection);
            Cache.put(semester);
            //Remove these from the map
            abstractOffering.remove(SEMESTER);
        }

        //Explicitly add extra offering info if it existed
        Object[] offeringInfoMapList = (Object[]) abstractOffering.get(OFFERING_INFO);
        if (offeringInfoMapList != null) {
            extraOfferingInfo = true;
            offeringInfo = new ArrayList<OfferingInfo>();
            for (Object abstractOfferingInfo : offeringInfoMapList) {
                offeringInfo.add(new OfferingInfo((Map) abstractOfferingInfo, connection));
            }

            //Remove these from the map
            abstractOffering.remove(OFFERING_INFO);
        }


        Reflector<Offering> reflector = new Reflector<Offering>();
        reflector.populate(this, abstractOffering);
    }

    /**
     * {@inheritDoc}
     */
    public void refresh() {
        Cache.remove(this);
        //First, grab the new offering object from the api
        Offering newOffering;
        if (extraOfferingInfo) {
            newOffering = CoMoToAPI.getOffering(connection, id, true);
        } else {
            newOffering = CoMoToAPI.getOffering(connection, id);
        }

        //Copy the primitive types from the API
        courseId = newOffering.getCourseId();
        filesetIds = newOffering.getFilesetIds();
        rosterStudentIds = newOffering.getRosterStudentIds();
        ldapDns = newOffering.getLdapDns();
        semester = newOffering.getSemester();
        if (extraOfferingInfo) {
            offeringInfo = newOffering.getOfferingInfo();
        } else {
            offeringInfo = null;
        }

        //Clear cached data
        filesets = null;
        course = null;
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

        //Grab the list from the API if not cached
        if (filesetIds == null) {
            filesets = new ArrayList<FileSet>();
            for (int fileSetId : filesetIds) {
                filesets.add(CoMoToAPI.getFileSet(connection, fileSetId));
            }
        }
        return filesets;
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

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public List<Integer> getFilesetIds() {
        return filesetIds;
    }

    public void setFilesetIds(List<Integer> filesetIds) {
        this.filesetIds = filesetIds;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public List<Integer> getRosterStudentIds() {
        return rosterStudentIds;
    }

    public void setRosterStudentIds(List<Integer> rosterStudentIds) {
        this.rosterStudentIds = rosterStudentIds;
    }

    public List<String> getLdapDns() {
        return ldapDns;
    }

    public void setLdapDns(List<String> ldapDns) {
        this.ldapDns = ldapDns;
    }

    public List<OfferingInfo> getOfferingInfo() {
        return offeringInfo;
    }

    public void setOfferingInfo(List<OfferingInfo> offeringInfo) {
        this.offeringInfo = offeringInfo;
    }
}
