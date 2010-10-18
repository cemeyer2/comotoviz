package edu.uiuc.cs.visualmoss.dataimport.api.dao;

import edu.uiuc.cs.visualmoss.dataimport.api.Assignment;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Encapsulates accessing an <code>Assignment</code> object from the CoMoTo API through the XMLRPC singleton  
 */
public class AssignmentDAO extends CoMoToDAO<Assignment>{

    @Override
    protected Assignment getById(int id) {
        return null;
    }
}
