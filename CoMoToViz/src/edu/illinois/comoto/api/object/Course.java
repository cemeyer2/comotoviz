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
import java.util.List;
import java.util.Map;

import static edu.illinois.comoto.api.CoMoToAPIConstants.OFFERINGS;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 * <p/>
 * <p> <p> Holds the data of a particular course
 */
public class Course implements Refreshable, Cacheable {

    /**
     * The list of associated assignment ids
     */
    private List<Integer> assignmentIds;

    /**
     * The list of associated file sets
     */
    private List<Integer> filesetIds;

    /**
     * The unique id for this course
     */
    private int id;

    /**
     * LDAP information for the course
     */
    private String ldapDn;

    /**
     * The name of this course
     */
    private String name;

    /**
     * The list of offerings associated with this course
     */
    private List<Offering> offerings;

    /**
     * The list of ids for the associated users
     */
    private List<Integer> userIds;

    /**
     * The list of associated assignments
     */
    private List<Assignment> assignments = null;

    /**
     * The list of file sets
     */
    private List<FileSet> filesets = null;

    /**
     * A connection to the CoMoTo API for lazily loading refreshing
     */
    private Connection connection;

    /**
     * Constructs a course object
     *
     * @param abstractCourse A map containing the data for this course
     * @param connection     A connection for lazily loading and refreshing objects
     */
    public Course(Map<String, Object> abstractCourse, Connection connection) {

        //Store the connection
        this.connection = connection;

        //Populate the list of offerings explicitly
        Object[] abstractOfferings = (Object[]) abstractCourse.get(OFFERINGS);
        if (abstractOfferings != null) {
            offerings = new ArrayList<Offering>();
            for (Object abstractOffering : abstractOfferings) {
                Offering o = new Offering((Map<String, Object>) abstractOffering, connection);
                Cache.put(o);
                offerings.add(o);
            }
            abstractCourse.remove(OFFERINGS);
        }

        //Populate this object using reflection
        Reflector<Course> reflector = new Reflector<Course>();
        reflector.populate(this, abstractCourse);
    }

    /**
     * {@inheritDoc}
     */
    public void refresh() {
        Cache.remove(this);
        //Grab the new object from the API
        Course newCourse = CoMoToAPI.getCourse(connection, id);

        //Copy the data into this object
        assignmentIds = newCourse.getAssignmentIds();
        filesetIds = newCourse.getFilesetIds();
        ldapDn = newCourse.getLdapDn();
        name = newCourse.getName();
        offerings = newCourse.getOfferings();
        userIds = newCourse.getUserIds();

        //Clear any cached data
        assignments = null;
        filesets = null;
    }

    /**
     * Grabs the list of assignments from the api if not cached in this object
     *
     * @return A list of the assignments associated with this course
     */
    public List<Assignment> getAssignments() {

        //Load the assignments from the API if not cached
        if (assignments == null) {
            assignments = CoMoToAPI.getAssignments(connection, id);
        }
        return assignments;
    }

    /**
     * Grabs the list of file sets from the API if not cached in this object
     *
     * @return A list of the file sets associated with this course
     */
    public List<FileSet> getFilesets() {

        //Load the assignments from the API if not cached
        if (filesets == null) {
            filesets = new ArrayList<FileSet>();
            for (int fileSetId : filesetIds) {
                filesets.add(CoMoToAPI.getFileSet(connection, fileSetId));
            }
        }
        return filesets;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    public List<Integer> getAssignmentIds() {
        return assignmentIds;
    }

    public void setAssignmentIds(List<Integer> assignmentIds) {
        this.assignmentIds = assignmentIds;
    }

    public List<Integer> getFilesetIds() {
        return filesetIds;
    }

    public void setFilesetIds(List<Integer> filesetIds) {
        this.filesetIds = filesetIds;
    }

    public String getLdapDn() {
        return ldapDn;
    }

    public void setLdapDn(String ldapDn) {
        this.ldapDn = ldapDn;
    }

    public List<Offering> getOfferings() {
        return offerings;
    }

    public void setOfferings(List<Offering> offerings) {
        this.offerings = offerings;
    }


}
