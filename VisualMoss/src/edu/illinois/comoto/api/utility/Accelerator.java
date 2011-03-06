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

import edu.illinois.comoto.api.CoMoToAPI;
import edu.illinois.comoto.api.CoMoToAPIException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Jon
 * Date: 2/8/11
 * <p/>
 * This class wraps the parallel connection utility
 */
public class Accelerator<T> {
    /**
     * This function will call an API method multiple times, in parallel if possible, using the ParallelConnectionUtil
     *
     * @param connection The connection on which to grab the data
     * @param methodName The name of the API method we are calling
     * @param parameters A 2D array of parameters, an array of arrays, where each array represents the parameters for one call
     * @return A list of objects of type requested
     */
    public List<T> getAPIObjects(Connection connection, String methodName, Object[][] parameters) {
        return getAPIObjects(connection, methodName, 4, parameters);    // Default to four parallel connections
    }


    /**
     * This function will call an API method multiple times, in parallel if possible, using the ParallelConnectionUtil
     *
     * @param connection          The connection on which to grab the data
     * @param methodName          The name of the API method we are calling
     * @param parallelConnections The number of parallel connections we want to attempt
     * @param parameters          A 2D array of parameters, an array of arrays, where each array represents the parameters for one call
     * @return A list of objects of type requested
     */
    public List<T> getAPIObjects(Connection connection, String methodName, int parallelConnections, Object[][] parameters) {

        //Delegate the work to the parallel utility
        ParallelConnectionUtility<T> parallelConnectionUtil = new ParallelConnectionUtility<T>();
        List<T> data = null;

        // Try to fetch in parallel, but if that fails, fetch one by one
        try {

            data = parallelConnectionUtil.parallelFetch(methodName, parameters, parallelConnections);

        } catch (CoMoToAPIException exception) {

            // Error!
            System.err.println("Failed to fetch API objects in parallel, reverting to serial fetch");

            // Find the API method we wanted to call
            Method targetMethod = null;
            for (Method method : CoMoToAPI.class.getMethods()) {
                if (method.getName().equals(methodName) && method.getParameterTypes().length == parameters[0].length) {
                    targetMethod = method;
                    break;
                }
            }

            // Allocate some space for the data
            data = new ArrayList<T>();

            // Call this method a bunch of times
            for (Object[] oneSetOfParameters : parameters) {
                //If this fails, print out why
                try {
                    data.add((T) targetMethod.invoke(null, oneSetOfParameters));
                } catch (IllegalAccessException e) {
                    System.err.println("Failed to call API method: " + e.getMessage());
                } catch (InvocationTargetException e) {
                    System.err.println("Failed to call API method: " + e.getMessage());
                }
            }
        }

        //Return the data
        return data;
    }
}
