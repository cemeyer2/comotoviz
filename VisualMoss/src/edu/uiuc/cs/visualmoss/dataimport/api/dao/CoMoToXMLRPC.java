package edu.uiuc.cs.visualmoss.dataimport.api.dao;

import java.awt.*;
import java.util.Map;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 * 
 * <p> <p>
 */
public class CoMoToXMLRPC {
    private static CoMoToXMLRPC singleton = null;

    private CoMoToXMLRPC(String username, String password, String host) {

    }

    public synchronized Map getStruct(String method, Object... params) {
        return null;
    }

    public synchronized List getArray(String method, Object... params) {
        return null;
    }

    public static void initialize(String username, String password, String host) {
        if(singleton != null){
            singleton = new CoMoToXMLRPC(username, password, host);
        }
    }

    public static CoMoToXMLRPC getInstance(){

        if(singleton == null){
            throw new RuntimeException("Must initialize CoMoToXMLRPC before use");
        }
        
        return singleton;
    }
}
