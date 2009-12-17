package edu.uiuc.cs.visualmoss.net;

import java.net.*;
import java.util.Hashtable;

import org.lobobrowser.request.AuthenticatorImpl;

public class VisualMossAuthenticator extends AuthenticatorImpl {

	private static final String DEFAULT = "*";
	
	private Hashtable<String, PasswordAuthentication> myAuthentications;
	
	public VisualMossAuthenticator()
	{
		myAuthentications = new Hashtable<String, PasswordAuthentication>();
	}
	
	public void addAuthentication(String hostname, String username, String password)
	{
		PasswordAuthentication authentication = new PasswordAuthentication(username, password.toCharArray());
		myAuthentications.put(hostname, authentication);
	}
	
	public void setDefaultAuthentication(String username, String password)
	{
		PasswordAuthentication authentication = new PasswordAuthentication(username, password.toCharArray());
		myAuthentications.put(DEFAULT, authentication);
	}
	
	@Override
	public PasswordAuthentication getPasswordAuthentication()
	{
		String hostname = this.getRequestingHost();
		PasswordAuthentication authentication = myAuthentications.get(hostname);
		PasswordAuthentication defaultAuthentication = myAuthentications.get(DEFAULT);
		
		if (authentication != null)
			return authentication;
		else if (defaultAuthentication != null)
			return defaultAuthentication;
		else return super.getPasswordAuthentication();
	}
	
}
