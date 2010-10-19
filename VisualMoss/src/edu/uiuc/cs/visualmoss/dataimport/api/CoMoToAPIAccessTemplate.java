package edu.uiuc.cs.visualmoss.dataimport.api;

import java.awt.*;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 18, 2010
 *
 * <p> <p> Each method in here outlines how to actually grab the data from the XML RPC client
 */
public class CoMoToAPIAccessTemplate {

    public static Map getMap(CoMoToAPIConnection connection, String method, Object... parameters) {
        try {
			return (Map) connection.getClient().execute(method, parameters);
		} catch (XmlRpcException e) {
			throw new RuntimeException("Error getting the map from the requested method:\n" + e.getMessage());
		}
    }

    public static List getArray(CoMoToAPIConnection connection, String method, Object... parameters){
        try {
			return (List) connection.getClient().execute(method, parameters);
		} catch (XmlRpcException e) {
			throw new RuntimeException("Error getting the list from the requested method:\n" + e.getMessage());
		}
    }

}
