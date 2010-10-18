package edu.uiuc.cs.visualmoss.dataimport.api.dao;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Abstract class representing the access of data from the CoMoTo API
 */
public abstract class CoMoToDAO<T> {

    /**
     * A singleton for the XMLRPC connection, a single point of access to the CoMoTo API.
     */
    protected CoMoToXMLRPC xmlrpc = CoMoToXMLRPC.getInstance();

    /**
     * Gets an object by its id number from the CoMoTo API
     *
     * @param id The unique ID for this object in the CoMoTo database
     * @return The object
     */
    protected abstract T getById(int id);
}
