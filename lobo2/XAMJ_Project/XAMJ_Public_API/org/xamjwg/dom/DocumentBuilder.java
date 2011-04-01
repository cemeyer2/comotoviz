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
 * Created on Sep 17, 2005
 */
package org.xamjwg.dom;

import java.security.AccessController;
import java.security.PrivilegedAction;

import org.lobobrowser.clientlet.*;

/**
 * Factory of XDocument instances.
 * @author J. H. S.
 */
public abstract class DocumentBuilder {
	public DocumentBuilder() {
		super();
	}

	public abstract XDocument createDocument(ClientletContext context);
	
	private static Class builderClass;
	
	/**
	 * Instantiates a class whose name is obtained
	 * from system property <code>xamj.document.builder.class</code>.
	 */
	public static DocumentBuilder newInstance() throws ClassNotFoundException,InstantiationException,IllegalAccessException {
		if(builderClass == null) {
			synchronized(DocumentBuilder.class) {
				if(builderClass == null) {
					final String property = "xamj.document.builder.class";
					String builderClassName = AccessController.doPrivileged(new PrivilegedAction<String>() {
						public String run() {
							return System.getProperty(property);
						}
					});
					if(builderClassName == null) {
						throw new IllegalStateException("Property " + property + " not provided");
					}
					Class clazz = Class.forName(builderClassName);
					builderClass = clazz;
				}
			}
		}
		return (DocumentBuilder) builderClass.newInstance();
	}
}
