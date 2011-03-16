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

import edu.illinois.comoto.viz.view.BackendConstants;
import org.apache.log4j.Logger;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class TrustAllSSLSocketFactory extends SSLSocketFactory {

    private static final Logger LOGGER = Logger.getLogger(TrustAllSSLSocketFactory.class);

    private static class TrustAllTrustManager
            implements X509TrustManager {

        public void checkClientTrusted(X509Certificate ax509certificate[], String s1)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate ax509certificate[], String s1)
                throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        private TrustAllTrustManager() {
        }

    }


    public TrustAllSSLSocketFactory() {
        SSLContext sslcontent = null;
        try {
            sslcontent = SSLContext.getInstance(BackendConstants.TLS);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.fatal(e.getMessage(), e);
        }
        try {
            sslcontent.init(null, new TrustManager[]{
                    new TrustAllTrustManager()
            }, new SecureRandom());
        } catch (KeyManagementException e) {
            LOGGER.fatal(e.getMessage(), e);
        }
        _factory = sslcontent.getSocketFactory();
    }

    public static SocketFactory getDefault() {
        return new TrustAllSSLSocketFactory();
    }

    public Socket createSocket(Socket socket, String s, int i, boolean flag)
            throws IOException {
        return _factory.createSocket(socket, s, i, flag);
    }

    public Socket createSocket(InetAddress inaddr, int i, InetAddress inaddr2, int j)
            throws IOException {
        return _factory.createSocket(inaddr, i, inaddr2, j);
    }

    public Socket createSocket(InetAddress inaddr, int i)
            throws IOException {
        return _factory.createSocket(inaddr, i);
    }

    public Socket createSocket(String s, int i, InetAddress inaddr, int j)
            throws IOException {
        return _factory.createSocket(s, i, inaddr, j);
    }

    public Socket createSocket(String s, int i)
            throws IOException {
        return _factory.createSocket(s, i);
    }

    public String[] getDefaultCipherSuites() {
        return _factory.getSupportedCipherSuites();
    }

    public String[] getSupportedCipherSuites() {
        return _factory.getSupportedCipherSuites();
    }

    private SSLSocketFactory _factory;

}