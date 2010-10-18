package edu.uiuc.cs.visualmoss.dataimport.api.dao;

import edu.uiuc.cs.visualmoss.dataimport.api.MossMatch;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Encapsulates accessing a <code>MossMatch</code> object from the CoMoTo API through the XMLRPC singleton  
 */
public class MossMatchDAO extends CoMoToDAO<MossMatch>{

    @Override
    protected MossMatch getById(int id) {
        return null;
    }
}
