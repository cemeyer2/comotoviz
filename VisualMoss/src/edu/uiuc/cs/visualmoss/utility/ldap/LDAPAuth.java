package edu.uiuc.cs.visualmoss.utility.ldap;
import java.util.Hashtable;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;


public class LDAPAuth {

	public static boolean authenticate(String netid, String password)
	{
		String userDN = findUserDN(netid);
		try
		{
			LdapContext ctx = createLDAPContext(userDN, password);
			ctx.close();
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	private static String findUserDN(String netid)
	{
		return "CN="+netid+",OU=Campus Accounts,DC=ad,DC=uiuc,DC=edu";
	}

	private static LdapContext createLDAPContext(String bindDN, String bindPassword) throws NamingException
	{
		Hashtable env = new Hashtable();
		env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		env.put("java.naming.provider.url", "ldaps://ad-dc-p1.ad.uiuc.edu/");
		env.put("java.naming.ldap.factory.socket", "edu.uiuc.cs.visualmoss.utility.ldap.TrustAllSSLSocketFactory");
		env.put("java.naming.security.protocol", "ssl");
		env.put("java.naming.security.principal", bindDN);
		env.put("java.naming.security.credentials", bindPassword);
		env.put("java.naming.security.authentication", "simple");
		env.put("com.sun.jndi.ldap.connect.pool", "true");
		LdapContext context = new InitialLdapContext(env, null);
		return context;
	}
}
