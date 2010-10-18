package edu.uiuc.cs.visualmoss.dataimport.api.dao;

import edu.uiuc.cs.visualmoss.dataimport.api.MossAnalysis;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Encapsulates grabbing a <code>MossAnalysis</code> object from the CoMoTo API through the XMLRPC singleton
 */
public class MossAnalysisDAO extends CoMoToDAO<MossAnalysis>{

    @Override
    protected MossAnalysis getById(int id) {
        return null;
    }
}
