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

import edu.illinois.comoto.api.CoMoToAPIConstants;
import edu.illinois.comoto.api.CoMoToAPIException;
import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 18, 2010
 * <p/>
 * <p> <p> Holds the data corresponding to a connection with the CoMoTo API
 */
public class Connection {

    private final String userName;
    private final String password;
    private final String host;

    private XmlRpcClient client;
    private XmlRpcClientConfigImpl configuration;
    private static final Logger logger = Logger.getLogger(Connection.class);

    /**
     * Builds a connections using the given user name and password to the default host
     *
     * @param userName The user name for the connection
     * @param password The password for the connection
     */
    public Connection(String userName, String password) {
        this(userName, password, CoMoToAPIConstants.DEFAULT_HOST);
    }

    /**
     * Builds the connection using the given host, user name, and password
     *
     * @param userName The user name for the connection
     * @param password The password
     * @param host     The host to connect to
     */
    public Connection(String userName, String password, String host) {
        this.userName = userName;
        this.password = password;
        this.host = host;

        try {
            initializeConnection();
        } catch (MalformedURLException e) {
            throw new CoMoToAPIException("Error initializing connection to: " + userName + " at " + host, e);
        }
    }

    /**
     * Initializes the 'connection' to the CoMoTo API. Essentially, just creates a client configured for this
     * host, user, and password
     *
     * @throws MalformedURLException if the host is invalid
     */
    private void initializeConnection() throws MalformedURLException {

        //Create and initialize the configuration for the connection
        configuration = new XmlRpcClientConfigImpl();
        configuration.setServerURL(new URL(host));
        configuration.setBasicUserName(userName);
        configuration.setBasicPassword(password);

        //Create the client with this configuration
        client = new XmlRpcClient();
        client.setConfig(configuration);
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public XmlRpcClient getClient() {
        return client;
    }

    public XmlRpcClientConfigImpl getConfiguration() {
        return configuration;
    }

    /**
     * Simple wrapper to execute a given method on the API given a function name and parameter list
     *
     * @param method     The method to call on the API
     * @param parameters The parameters to pass to the API function
     * @return An object of some kind, a standard object type from the XML-RPC client
     * @throws XmlRpcException On errors accessing the API
     */
    public Object execute(String method, Object... parameters) throws XmlRpcException {
        logger.info("Executing " + method + " with params: " + Arrays.toString(parameters));
        return client.execute(method, parameters);
    }
}
