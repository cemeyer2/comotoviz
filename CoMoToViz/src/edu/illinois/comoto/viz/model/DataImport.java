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

package edu.illinois.comoto.viz.model;

import edu.illinois.comoto.api.CoMoToAPI;
import edu.illinois.comoto.api.object.Assignment;
import edu.illinois.comoto.api.object.Course;
import edu.illinois.comoto.api.utility.Connection;
import edu.illinois.comoto.viz.utility.Pair;

import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Nov 3, 2010
 * <p/>
 * <p> <p> This class drives the import of data into the visual moss graph structure using the CoMoTo API
 */
public class DataImport {

    private static Connection connection; //kind of a hack to allow me to not have to pass this object around everywhere else i need it
    private Assignment assignment;
    private Course course;
    private List<Course> courses;


    /**
     * The default constructor for the data import, which queries the CoMoTo API to populate the courses and assignments
     * for each course
     */
    public DataImport(String courseName, String assignmentName, Pair<String, String> activeDirectoryCredentials) {
        //Create a connection to the CoMoTo API
        connection = new Connection(activeDirectoryCredentials.getFirst(), activeDirectoryCredentials.getSecond());

        //Populate the courses from the API
        List<Course> courses = CoMoToAPI.getCourses(connection);

        //Find the course corresponding to this course name
        for (Course course : courses) {
            if (course.getName().equals(courseName)) {
                this.course = course;
            }
        }

        //Find the assignment corresponding to the given name
        List<Assignment> assignments = course.getAssignments();
        for (Assignment assignment : assignments) {
            if (assignment.getName().equals(assignmentName)) {
                this.assignment = assignment;
            }
        }
    }

    public DataImport(Pair<String, String> activeDirectoryCredentials) {
        //Create a connection to the CoMoTo API
        connection = new Connection(activeDirectoryCredentials.getFirst(), activeDirectoryCredentials.getSecond());

        //Populate the courses from the API
        courses = CoMoToAPI.getCourses(connection);
    }

    /**
     * Gets the connection to the API
     *
     * @return The connection to the API
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * Get the assignment associated with this import
     *
     * @return The assignment that this graph represents
     */
    public Assignment getAssignment() {
        return assignment;
    }

    /**
     * Gets all courses available from the API
     *
     * @return The list of courses
     */
    public List<Course> getCourses() {
        return courses;
    }
}
