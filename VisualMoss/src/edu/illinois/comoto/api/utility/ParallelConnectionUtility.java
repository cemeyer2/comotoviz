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
import edu.illinois.comoto.api.object.FileSet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * Author:  Charlie Meyer <cemeyer2@illinois.edu>
 * Date:    2/8/11
 * Time:    12:27 PM
 * Package: edu.uiuc.cs.visualmoss.dataimport.api.util
 */
public class ParallelConnectionUtility<T> {

    /**
     * Fetch API objects in parallel
     *
     * @param methodName              The API method to call
     * @param params                  The parameters to the method
     * @param parallelConnectionCount The number of parallel connections
     * @return A typed list of API objects
     * @throws edu.illinois.comoto.api.CoMoToAPIException
     *          If we fail to fetch the data in parallel
     */
    public List<T> parallelFetch(String methodName, Object[][] params, int parallelConnectionCount) throws CoMoToAPIException {
        List<T> retval = new LinkedList<T>();
        List<Thread> threads = new LinkedList<Thread>();
        for (int i = 0; i < params.length; i++) {
            Object[] targetParams = params[i];
            Method targetMethod = getTargetMethod(methodName, targetParams);
            Thread th = new Thread(new ParallelConnectionRunner(targetMethod, targetParams, retval));
            threads.add(th);
            th.start();
            if (threads.size() == parallelConnectionCount || i == params.length - 1) { //if we have the # of threads specified already running or if we have created threads for all requested API calls
                for (Thread th2 : threads) {
                    try {
                        th2.join();
                    } catch (InterruptedException e) {
                        throw new CoMoToAPIException("Error waiting for thread", e);
                    }
                }
                threads.clear();
            }

        }
        return retval;
    }

    private Method getTargetMethod(String methodName, Object[] params) throws CoMoToAPIException {
        Method targetMethod = null;
        for (Method m : CoMoToAPI.class.getMethods()) {
            if (m.getName().equals(methodName) && m.getParameterTypes().length == params.length) {
                targetMethod = m;
                break;
            }
        }
        if (targetMethod == null) {
            throw new CoMoToAPIException("Requested API method " + methodName + " does not exist in CoMoToAPI.java");
        }
        return targetMethod;
    }

    private class ParallelConnectionRunner implements Runnable {

        private Method targetMethod;
        private Object[] params;
        private List<T> retval;

        public ParallelConnectionRunner(Method targetMethod, Object[] params, List<T> retval) {
            this.targetMethod = targetMethod;
            this.params = params;
            this.retval = retval;
        }

        @SuppressWarnings("unchecked")
        public void run() {
            try {
                Object APIReturned = targetMethod.invoke(null, this.params);
                retval.add((T) APIReturned);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InvocationTargetException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public static void main(String[] args) {
        Connection conn = new Connection("cemeyer2", "password"); //damn, had to change my password, at least its all centralized
        ParallelConnectionUtility<FileSet> parallelConnection = new ParallelConnectionUtility<FileSet>();
        Object[][] params = new Object[3][];
        params[0] = new Object[]{conn, 1, true};
        params[1] = new Object[]{conn, 2, true};
        params[2] = new Object[]{conn, 3, true};
        try {
            List<FileSet> returned = parallelConnection.parallelFetch("getFileSet", params, 3);
        } catch (CoMoToAPIException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
