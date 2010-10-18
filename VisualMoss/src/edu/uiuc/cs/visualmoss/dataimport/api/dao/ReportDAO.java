package edu.uiuc.cs.visualmoss.dataimport.api.dao;

import edu.uiuc.cs.visualmoss.dataimport.api.Report;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Encapsulates accessing a <code>Report</code> object from the CoMoTo API through the XMLRPC singleton  
 */
public class ReportDAO extends CoMoToDAO<Report>{

    @Override
    protected Report getById(int id) {
        return null;
    }
}
