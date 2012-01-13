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
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 18, 2010
 * <p/>
 * <p> <p> Holds the data of a semester
 */
public class Semester implements Refreshable, Cacheable, Comparable<Semester>, Verifiable {

    Logger logger = Logger.getLogger(Semester.class);

    /**
     * The season
     */
    private Season season = null;

    /**
     * The 'type' for this semester
     */
    private Type type = null;

    /**
     * The unique id for this semester
     */
    private int id = -1;

    /**
     * The year
     */
    private int year = -1;

    /**
     * A connection to the API for lazily loading and refreshing data
     */
    private Connection connection;


    public Semester() {
    }

    public Semester(Map abstractSemester, Connection connection) {

        // Check for null inputs
        if (abstractSemester != null && connection != null) {

            //Save the connection
            this.connection = connection;

            //Use reflection to populate this object
            Reflector<Semester> reflector = new Reflector<Semester>();
            reflector.populate(this, abstractSemester);

        } else {
            throw new CoMoToAPIException(CoMoToAPIConstants.getNullParamsMessage("Semester"));
        }

        // Verify that this object was built successfully
        // CM: this needs to be updated for special semesters
        //verify();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void verify() throws CoMoToAPIException {
        if (id == -1 || year == -1 || season == null || type == null) {
            throw new CoMoToAPIException(CoMoToAPIConstants.getInvalidParamsMessage("Semester"));
        }
    }

    /**
     * {@inheritDoc}
     */
    public void refresh() {

        Cache.remove(this);

        //First, grab the new object from the API
        Semester newSemester = CoMoToAPI.getSemester(connection, id);

        //Copy the primitive data
        type = newSemester.getType();
        season = newSemester.getSeason();
        year = newSemester.getYear();
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        if (season != null) {
            return season.name() + " " + year;
        } else {
            return "Semester.toString: not initialized yet";
        }
    }

    @Override
    public int compareTo(Semester semester) {
        if (getYear() < semester.getYear()) {
            return -1;
        } else if (getYear() > semester.getYear()) {
            return 1;
        } else {
            return getSeason().compareTo(semester.getSeason());
        }
    }
}
