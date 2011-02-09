package edu.uiuc.cs.visualmoss.dataimport.api;

import edu.uiuc.cs.visualmoss.dataimport.api.util.ParallelConnectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Jon
 * Date: 2/8/11
 *
 * This class wraps the parallel connection utility
 */
public class CoMoToAPIAccelerator<T> {
    /**
     * This function will call an API method multiple times, in parallel if possible, using the ParallelConnectionUtil
     *
     * @param   connection      The connection on which to grab the data
     * @param   methodName      The name of the API method we are calling
     * @param   parameters      A 2D array of parameters, an array of arrays, where each array represents the parameters for one call
     * @return  A list of objects of type requested
     */
    public List<T> getAPIObjects(CoMoToAPIConnection connection, String methodName, Object[][] parameters) {
        return getAPIObjects(connection, methodName, 4, parameters);    // Default to four parallel connections
    }


    /**
     * This function will call an API method multiple times, in parallel if possible, using the ParallelConnectionUtil
     *
     * @param   connection              The connection on which to grab the data
     * @param   methodName              The name of the API method we are calling
     * @param   parallelConnections     The number of parallel connections we want to attempt
     * @param   parameters              A 2D array of parameters, an array of arrays, where each array represents the parameters for one call
     * @return  A list of objects of type requested
     */
    public List<T> getAPIObjects(CoMoToAPIConnection connection, String methodName, int parallelConnections, Object[][] parameters) {

        //Delegate the work to the parallel utility
        ParallelConnectionUtil<T> parallelConnectionUtil = new ParallelConnectionUtil<T>();
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
