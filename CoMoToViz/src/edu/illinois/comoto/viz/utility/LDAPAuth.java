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

package edu.illinois.comoto.viz.utility;

import org.apache.log4j.Logger;

import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;


public class LDAPAuth {

    private static final Logger LOGGER = Logger.getLogger(LDAPAuth.class);

    private LDAPAuth() {
    }

    public static boolean authenticate(String netid, String password) {
        LOGGER.info("LDAP authenticating user: " + netid);
        String userDN = findUserDN(netid);
        LOGGER.info("DN: " + userDN);
        try {
            LdapContext context = createLDAPContext(userDN, password);
            LOGGER.info("Successfully authenticated");
            context.close();
            return true;
        } catch (Exception exception) {
            LOGGER.info("Authentication failure", exception);
            return false;
        }
    }

    private static String findUserDN(String netid) {
        return "CN=" + netid + ",OU=Campus Accounts,DC=ad,DC=uiuc,DC=edu";
    }

    private static LdapContext createLDAPContext(String bindDN, String bindPassword) throws NamingException {
        Hashtable environment = new Hashtable();
        environment.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        environment.put("java.naming.provider.url", "ldaps://ad-dc-p1.ad.uiuc.edu/");
        environment.put("java.naming.ldap.factory.socket", "edu.illinois.comoto.viz.utility.TrustAllSSLSocketFactory");
        environment.put("java.naming.security.protocol", "ssl");
        environment.put("java.naming.security.principal", bindDN);
        environment.put("java.naming.security.credentials", bindPassword);
        environment.put("java.naming.security.authentication", "simple");
        environment.put("com.sun.jndi.ldap.connect.pool", "true");
        return new InitialLdapContext(environment, null);
    }
}
