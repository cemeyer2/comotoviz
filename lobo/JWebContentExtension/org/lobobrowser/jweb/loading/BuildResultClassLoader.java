/*
    GNU GENERAL PUBLIC LICENSE
    Copyright (C) 2006 The Lobo Project

    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public
    License as published by the Free Software Foundation; either
    verion 2 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General Public License for more details.

    You should have received a copy of the GNU General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

    Contact info: lobochief@users.sourceforge.net
*/
package org.lobobrowser.jweb.loading;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.security.*;
import org.lobobrowser.jweb.compilation.*;

import java.util.Enumeration;
//import java.util.logging.*;

public class BuildResultClassLoader extends SecureClassLoader {
	//TODO: Check for sealing violations?
	//private static final Logger logger = Logger.getLogger(BuildResultClassLoader.class.getName());
	private final BuildResult buildResult;
	private final java.net.URL codeLocation;
		
	public BuildResultClassLoader(ClassLoader parent, URL codeLocation, BuildResult buildResult) {
		super(parent);
		this.buildResult = buildResult;
		this.codeLocation = codeLocation;
	}

	@Override
	protected Class<?> findClass(String className) throws ClassNotFoundException {
		OutputFileInfo ofi = this.buildResult.outputFiles.get(className);
		if(ofi == null) {
			throw new ClassNotFoundException(className);
		}
		byte[] buffer = ofi.bytes;
		CodeSource codeSource = new CodeSource(this.codeLocation, (java.security.cert.Certificate[]) null);
		return this.defineClass(className, buffer, 0, buffer.length, codeSource);
	}

	@Override
	protected URL findResource(String resourceName) {
		// No resources in build result
		return null;
	}

	@Override
	protected Enumeration<URL> findResources(String name) throws IOException {
		return java.util.Collections.enumeration(java.util.Collections.EMPTY_LIST);
	}

	@Override
	public InputStream getResourceAsStream(String resourceName) {
		// No resources in build result
		return null;
	}	
}
