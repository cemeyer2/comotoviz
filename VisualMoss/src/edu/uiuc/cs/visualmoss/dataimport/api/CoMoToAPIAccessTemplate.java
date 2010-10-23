package edu.uiuc.cs.visualmoss.dataimport.api;

import org.apache.xmlrpc.XmlRpcException;

import java.util.Map;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 18, 2010
 *
 * <p> <p> Each method in here outlines how to actually grab the data from the XML RPC client
 */
public class CoMoToAPIAccessTemplate {

    /**
     * Grabs a Map of data from the API, an object of some arbitrary type
     *
     * @param connection The connection from which to grab the object
     * @param method The name of the method to call
     * @param parameters The parameters to pass to the API call
     *
     * @return A map, essentially an object represented as key-value pairs
     */
    public static Map getMap(CoMoToAPIConnection connection, String method, Object... parameters) {
        try {
			return (Map) connection.execute(method, parameters);
		} catch (XmlRpcException e) {
			throw new RuntimeException("Error getting the map from the requested method:\n" + e.getMessage());
		}
    }

    /**
     * Grabs an array of data from the API, an object of some arbitrary type
     *
     * @param connection The connection from which to grab the object
     * @param method The name of the method to call
     * @param parameters The parameters to pass to the API call
     *
     * @return An array of objects of some arbitrary type
     */
    public static Object[] getArray(CoMoToAPIConnection connection, String method, Object... parameters){
        try {
			return (Object[]) connection.execute(method, parameters);
		} catch (XmlRpcException e) {
			throw new RuntimeException("Error getting the list from the requested method:\n" + e.getMessage());
		}
    }

}
