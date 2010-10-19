package edu.uiuc.cs.visualmoss.dataimport.api;

import java.awt.*;
import java.util.Map;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 18, 2010
 *
 * <p> <p> Each method in here outlines how to actually grab the data from the XML RPC client
 */
public class CoMoToAPIAccessTemplate {

    public static Map getMap(CoMoToAPIConnection connection, String method, Object... parameters) {
        return (Map) connection.getClient().execute(method, parameters);
    }

    public static List getArray(CoMoToAPIConnection connection, String method, Object... parameters) {
        return (List) connection.getClient().execute(method, parameters);
    }

}
