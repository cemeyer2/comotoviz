/*
 * University of Illinois/NCSA
 * Open Source License
 *
 * Copyright (c) 2011 University of Illinois at Urbana-Champaign.
 * All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal with the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimers.
 *
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimers in the documentation and/or other materials provided
 *       with the distribution.
 *
 *     * Neither the names of the CoMoTo Project team, the University of
 *       Illinois at Urbana-Champaign, nor the names of its contributors
 *       may be used to endorse or promote products derived from this
 *       Software without specific prior written permission.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS WITH THE SOFTWARE.
 */

package edu.illinois.comoto.api.utility;

import edu.illinois.comoto.api.CoMoToAPIException;
import org.apache.xmlrpc.XmlRpcException;

import java.util.Map;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 18, 2010
 * <p/>
 * <p> <p> Each method in here outlines how to actually grab the data from the XML RPC client
 */
public class AccessTemplate {

    private AccessTemplate() {
    }

    /**
     * Grabs a Map of data from the API, an object of some arbitrary type
     *
     * @param connection The connection from which to grab the object
     * @param method     The name of the method to call
     * @param parameters The parameters to pass to the API call
     * @return A map, essentially an object represented as key-value pairs
     */
    public static Map getMap(Connection connection, String method, Object... parameters) {
        try {
            return (Map) connection.execute(method, parameters);
        } catch (XmlRpcException e) {
            throw new CoMoToAPIException("Error getting the map from the requested method:\t" + e.getMessage(), e);
        }
    }

    /**
     * Grabs an array of data from the API, an object of some arbitrary type
     *
     * @param connection The connection from which to grab the object
     * @param method     The name of the method to call
     * @param parameters The parameters to pass to the API call
     * @return An array of objects of some arbitrary type
     */
    public static Object[] getArray(Connection connection, String method, Object... parameters) {
        try {
            return (Object[]) connection.execute(method, parameters);
        } catch (XmlRpcException e) {
            throw new CoMoToAPIException("Error getting the list from the requested method:\t" + e.getMessage(), e);
        }
    }
}