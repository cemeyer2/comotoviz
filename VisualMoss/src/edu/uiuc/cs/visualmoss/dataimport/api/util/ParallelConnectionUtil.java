package edu.uiuc.cs.visualmoss.dataimport.api.util;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPI;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConnection;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIException;
import edu.uiuc.cs.visualmoss.dataimport.api.objects.FileSet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * Author:  Charlie Meyer <cemeyer2@illinois.edu>
 * Date:    2/8/11
 * Time:    12:27 PM
 * Package: edu.uiuc.cs.visualmoss.dataimport.api.util
 * Created by IntelliJ IDEA.
 */
public class ParallelConnectionUtil<T> {

    public ParallelConnectionUtil() {
    }

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
        CoMoToAPIConnection conn = new CoMoToAPIConnection("cemeyer2", "password"); //damn, had to change my password, at least its all centralized
        ParallelConnectionUtil<FileSet> parallelConnection = new ParallelConnectionUtil<FileSet>();
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
