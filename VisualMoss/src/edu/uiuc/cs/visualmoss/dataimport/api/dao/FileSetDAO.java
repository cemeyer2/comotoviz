package edu.uiuc.cs.visualmoss.dataimport.api.dao;

import edu.uiuc.cs.visualmoss.dataimport.api.FileSet;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Encapsulates accessing a <code>FileSet</code> object from the CoMoTo API through the XMLRPC singleton  
 */
public class FileSetDAO extends CoMoToDAO<FileSet>{

    @Override
    protected FileSet getById(int id) {
        return null;
    }
}
