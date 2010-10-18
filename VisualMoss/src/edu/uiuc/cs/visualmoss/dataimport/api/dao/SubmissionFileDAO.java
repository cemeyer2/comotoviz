package edu.uiuc.cs.visualmoss.dataimport.api.dao;

import edu.uiuc.cs.visualmoss.dataimport.api.SubmissionFile;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Encapsulates accessing a <code>SubmissionFile</code> object from the CoMoTo API through the XMLRPC singleton  
 */
public class SubmissionFileDAO extends CoMoToDAO<SubmissionFile>{
    @Override
    protected SubmissionFile getById(int id) {
        return null;
    }
}
