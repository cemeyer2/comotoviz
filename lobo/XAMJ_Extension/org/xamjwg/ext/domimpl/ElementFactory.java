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
 * Created on Mar 5, 2005
 */
package org.xamjwg.ext.domimpl;

import java.security.AccessController;
import java.util.*;
import java.beans.*;
import java.io.*;
import java.lang.reflect.*;
import org.w3c.dom.*;
import org.xamjwg.dom.XElement;

public class ElementFactory
{
	private static ElementFactory instance;
	private boolean loaded = false;
	private final Map elementMap = new Hashtable();

	private ElementFactory()
	{
	}

	public static ElementFactory getInstance() 
	{
		if(instance == null) 
		{
			synchronized(ElementFactory.class) 
			{
				if(instance == null) 
				{
					instance = new ElementFactory();
				}
			}
		}
		return instance;
	}

	private void ensureLoaded() throws Exception
	{
		if(!this.loaded) 
		{
			this.Load();
			this.loaded = true;
		}
	}

	private static final String ELEMENTS_XML = "/net/sourceforge/xamj/elements.xml";
		
	private void Load() throws Exception
	{		
		InputStream stream = (InputStream) AccessController.doPrivileged(new java.security.PrivilegedAction() {
			public Object run() {
				return this.getClass().getResourceAsStream(ELEMENTS_XML);
			}
		});
		if(stream == null) 
		{
			throw new IllegalStateException("Did not find resource: " + ELEMENTS_XML);
		}
		try 
		{
			javax.xml.parsers.DocumentBuilder documentBuilder = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder();
			org.w3c.dom.Document document = documentBuilder.parse(stream);
			Element documentElement = document.getDocumentElement();
			NodeList nodeList = documentElement.getChildNodes();
		    int length = nodeList.getLength();
		    for(int i = 0; i < length; i++)
			{
		    	Node node = nodeList.item(i);
		    	if(node instanceof Element) {
		    		Element child = (Element) node;
		    		String elementName = child.getAttribute("name");
		    		ElementDescriptor ElementDescriptor = new ElementDescriptor(elementName, child);
	    			this.elementMap.put(elementName, ElementDescriptor);
		    	}
			}
		}
		finally 
		{
			stream.close();		
		}
	}

	public XElement createElement(String name) throws IllegalElementException 
	{
		try {
			Constructor constructor;
			synchronized (this) {
				this.ensureLoaded();
				ElementDescriptor einfo = (ElementDescriptor) this.elementMap.get(name);
				if (einfo == null) {
					throw new IllegalElementException("Element named '" + name
							+ "' is not known");
				}
				constructor = einfo.getElementConstructor();
			}
			try {
				return (XElement) constructor.newInstance(new Object[] { name });
			} catch (Exception err) {
				throw new IllegalElementException("Element named '" + name
						+ "' could not be constructed", err);
			}
		} catch (Exception err) {
		    throw new IllegalElementException("Unable to create element '" + name + "' because of engine setup error", err);
		}
	}

	public final ElementDescriptor getElementDescriptor(String elementName) throws IllegalElementException {
		return this.getElementDescriptor(elementName, true);
	}
	
	public final ElementDescriptor getElementDescriptor(String elementName, boolean throwException) throws IllegalElementException {
		try {
		    ElementDescriptor einfo;
			synchronized (this) {
				this.ensureLoaded();
				einfo = (ElementDescriptor) this.elementMap.get(elementName);
				if (einfo == null && throwException) {
					throw new IllegalElementException("Element named '" + elementName
							+ "' is not known");
				}
			}
			return einfo;
		} catch(IllegalElementException ie) {
			throw ie;
		} catch (Exception err) {
		    throw new IllegalElementException("Unable to get element information for '" + elementName + "' because of engine setup error", err);
		}	    	    
	}
	
	public PropertyDescriptor getPropertyDescriptor(String elementName, String propertyName) throws IllegalElementException {
		try {
		    ElementDescriptor einfo;
			synchronized (this) {
				this.ensureLoaded();
				einfo = (ElementDescriptor) this.elementMap.get(elementName);
				if (einfo == null) {
					throw new IllegalStateException("Element named '" + elementName
							+ "' is not known");
				}
			}
			return einfo.getPropertyDescriptor(propertyName);
		} catch (Exception err) {
		    throw new IllegalElementException("Unable to retrieve property '" + propertyName + "' because of engine setup error", err);
		}	    
	}
	
	
	
}
