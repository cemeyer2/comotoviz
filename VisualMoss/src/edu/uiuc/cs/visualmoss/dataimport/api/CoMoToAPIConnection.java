package edu.uiuc.cs.visualmoss.dataimport.api;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;

import static edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConstants.DEFAULT_HOST;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 18, 2010
 *
 * <p> <p> Holds the data corresponding to a connection with the CoMoTo API
 */
public class CoMoToAPIConnection {

    private final String userName;
    private final String password;
    private final String host;

    private XmlRpcClient client;
    private XmlRpcClientConfigImpl configuration;

    public CoMoToAPIConnection(String userName, String password) {
        this(userName, password, DEFAULT_HOST);
    }

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

    private void initializeConnection() throws MalformedURLException {
        configuration.setServerURL(new URL(host));
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
}
