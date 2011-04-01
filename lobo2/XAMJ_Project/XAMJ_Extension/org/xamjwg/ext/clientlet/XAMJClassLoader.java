/*
    GNU LESSER GENERAL PUBLIC LICENSE
    Copyright (C) 2006 The Lobo Project

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

    Contact info: xamjadmin@users.sourceforge.net
*/
/*
 * Created on Mar 19, 2005
 */
package org.xamjwg.ext.clientlet;

import java.security.*;

import java.net.*;
import java.io.*;
import java.util.logging.*;

import org.lobobrowser.util.*;
import org.lobobrowser.util.io.IORoutines;

/**
 * @author J. H. S.
 */
public final class XAMJClassLoader extends BaseClassLoader {
    private static final Logger logger = Logger.getLogger(XAMJClassLoader.class.getName());
    private final File classDirectory;
    private final URL codeSourceURL;

    XAMJClassLoader(ClassLoader parentLoader, URL codeSourceURL, String classDirectory) {
    	// This should not be accessible by unprivileged code.
    	// The jar file is sealed and there's also a ClassLoader permission, which
    	// should never be granted to untrusted XAMJ documents.
        super(parentLoader);
        this.classDirectory = new File(classDirectory);
        this.codeSourceURL = codeSourceURL;
    }
    
    /* (non-Javadoc)
     * @see java.security.SecureClassLoader#getPermissions(java.security.CodeSource)
     */
    protected PermissionCollection getPermissions(CodeSource arg0) {
        if(logger.isLoggable(Level.INFO))logger.info("getPermissions(): CodeSource=" + arg0);
        // This should get policy permissions
        PermissionCollection pc = super.getPermissions(arg0);
        
//        Enumeration permissionElements = pc.elements();
//        while(permissionElements.hasMoreElements()) {
//        	Permission permission = (Permission) permissionElements.nextElement();
//        	if(logger.isLoggable(Level.INFO))logger.info("getPermissions(): Permission=" + permission);        	
//        }
        
        return pc;
    }

    /* (non-Javadoc)
     * @see java.lang.ClassLoader#findClass(java.lang.String)
     */
    protected Class findClass(String arg0) throws ClassNotFoundException {
    	String subPath = arg0.replace('.', '/') + ".class";
    	final File classFile = new File(this.classDirectory, subPath);
    	byte[] classBytes;
    	classBytes = (byte[]) AccessController.doPrivileged(new PrivilegedAction() {
    		public Object run() {
    			try {
    				return IORoutines.load(classFile);
    			} catch(IOException ioe) {
    				return null;
    			}
    		}
    	});
    	if(classBytes == null) {
    		throw new ClassNotFoundException("I/O error (File not found?)");
    	}
    	// TODO Signers Certificates
    	CodeSource cs = new CodeSource(this.codeSourceURL, new java.security.cert.Certificate[0]);
    	return this.defineClass(arg0, classBytes, 0, classBytes.length, cs);
    }
}
