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

import org.lobobrowser.request.AuthenticatorImpl;

import java.net.PasswordAuthentication;
import java.util.Hashtable;

public class VisualMossAuthenticator extends AuthenticatorImpl {

    private static final String DEFAULT = "*";

    private Hashtable<String, PasswordAuthentication> myAuthentications;

    public VisualMossAuthenticator() {
        myAuthentications = new Hashtable<String, PasswordAuthentication>();
    }

    public void addAuthentication(String hostname, String username, String password) {
        PasswordAuthentication authentication = new PasswordAuthentication(username, password.toCharArray());
        myAuthentications.put(hostname, authentication);
    }

    public void setDefaultAuthentication(String username, String password) {
        PasswordAuthentication authentication = new PasswordAuthentication(username, password.toCharArray());
        myAuthentications.put(DEFAULT, authentication);
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
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
