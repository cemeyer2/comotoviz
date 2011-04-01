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
 * Created on Mar 15, 2005
 */
package org.xamjwg.ext.domimpl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.util.Map;



import org.lobobrowser.util.*;
import org.w3c.dom.Element;
import org.xamjwg.dom.XActionContainerElement;

/**
 * @author J. H. S.
 */
public class ElementDescriptor {
    private final String name;
    private final Element elementDef;
    
    private Constructor constructor; 
    private Class clazz;
    private Class inter;
    private Bean bean;
    
    public ElementDescriptor(String name, Element elementDef) {
        this.name = name;
        this.elementDef = elementDef;
    }
    
    public synchronized Class getElementClass() throws Exception {
        if(this.clazz == null) {
            String className = this.elementDef.getAttribute("class");
            this.clazz = Class.forName(className);
        }
        return this.clazz;
    }

    private String eventType;
    
    public synchronized String getEventTypeName() {
    	if(this.eventType == null) {
    		String elementType = this.elementDef.getAttribute("eventType");
    		if(Strings.isBlank(elementType)) {
    			elementType = "AbstractXamjEvent";
    		}
    		this.eventType = elementType;
    	}
    	return this.eventType;
    }
    
    public synchronized Class getElementInterface() throws Exception {
        if(this.inter == null) {
            String interfaceName = this.getElementInterfaceName();
            this.inter = Class.forName("org.xamjwg.dom." + interfaceName);
        }
        return this.inter;
    }
    
    public boolean isActionContainer() throws Exception {
        return XActionContainerElement.class.isAssignableFrom(this.getElementInterface());
    }
    
    private String interfaceName = null;
    public synchronized String getElementInterfaceName() {
        if(this.interfaceName == null) {
            String interName = this.elementDef.getAttribute("interface");
            if("".equals(interName)) {
            	throw new IllegalStateException("No interface attribute for element named '" + this.name + "' due to user agent setup error");
            }
            this.interfaceName = interName;
        }
        return this.interfaceName;
    }
    
    public synchronized Constructor getElementConstructor() throws Exception {
        if(this.constructor == null) {
            Class clazz = this.getElementClass();
            this.constructor = clazz.getConstructor(new Class[] { String.class });
            if(this.constructor == null) {
                throw new IllegalStateException("Did not find propert constructor for class " + clazz.getName());
            }
        }
        return this.constructor;
    }
    
    public synchronized Map getPropertyDescriptorsMap() throws Exception {
        if(this.bean == null) {
        	this.bean = new Bean(this.getElementInterface());
        }
        return this.bean.getPropertyDescriptorsMap();    	
    }

    public synchronized PropertyDescriptor[] getPropertyDescriptors() throws Exception {
        if(this.bean == null) {
        	this.bean = new Bean(this.getElementInterface());
        }
        return this.bean.getPropertyDescriptors();    	
    }
    
    public synchronized PropertyDescriptor getPropertyDescriptor(String name) throws Exception {
    	return (PropertyDescriptor) this.getPropertyDescriptorsMap().get(name);
    }
}
