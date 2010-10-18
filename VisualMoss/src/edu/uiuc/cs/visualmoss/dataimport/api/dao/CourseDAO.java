package edu.uiuc.cs.visualmoss.dataimport.api.dao;

import edu.uiuc.cs.visualmoss.dataimport.api.Course;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Encapsulates accessing a <code>Course</code> object from the CoMoTo API through the XMLRPC singleton  
 */
public class CourseDAO extends CoMoToDAO<Course>{

    @Override
    protected Course getById(int id) {
        return null;
    }
}
