package edu.uiuc.cs.visualmoss.dataimport.api;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConstants.DEFAULT_HOST;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 18, 2010
 * <p/>
 * <p> <p> Holds the data corresponding to a connection with the CoMoTo API
 */
public class CoMoToAPIConnection {

    private final String userName;
    private final String password;
    private final String host;

    private XmlRpcClient client;
    private XmlRpcClientConfigImpl configuration;

    /**
     * Builds a connections using the given user name and password to the default host
     *
     * @param userName The user name for the connection
     * @param password The password for the connection
     */
    public CoMoToAPIConnection(String userName, String password) {
        this(userName, password, DEFAULT_HOST);
    }

    /**
     * Builds the connection using the given host, user name, and password
     *
     * @param userName The user name for the connection
     * @param password The password
     * @param host     The host to connect to
     */
    public CoMoToAPIConnection(String userName, String password, String host) {
        this.userName = userName;
        this.password = password;
        this.host = host;

        try {
            initializeConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error initializing connection to: " + userName + " at " + host);
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
        System.out.println("Executing " + method + " with params: " + Arrays.toString(parameters));
        return client.execute(method, parameters);
    }
}
