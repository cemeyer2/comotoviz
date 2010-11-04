package edu.uiuc.cs.visualmoss.dataimport.api.objects;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Nov 3, 2010
 *
 * <p> <p> Outlines what we expect from elements that are refreshable via the API
 */
public interface Refreshable {

    /**
     * This function will refresh the database object with the contents of the database to ensure that the most recent
     *  information is used when performing operations on this object.
     *
     *  @see edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPI 
     */
    public void refresh();
}
