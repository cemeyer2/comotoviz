package edu.uiuc.cs.visualmoss.dataimport.api.dao;

import edu.uiuc.cs.visualmoss.dataimport.api.Analysis;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Encapsulates accessing an <code>Analysis</code> object from the CoMoTo API through the XMLRPC singleton  
 */
public class AnalysisDAO extends CoMoToDAO<Analysis>{

    @Override
    protected Analysis getById(int id) {
        return null;
    }
}
