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

import org.lobobrowser.clientlet.ClientletContext;
import org.xamjwg.dom.*;
import org.xamjwg.ext.gui.*;



/**
 * @author J. H. S.
 */
public class RootElement extends BaseElement {
	private BaseWidgetElement componentElement;
	private HeadElement headElement;
	
	/**
     * @param name
     */
    public RootElement(String name) {
        super(name);
    }


	public void appendChild(XNode node) {
	    if(node instanceof BaseWidgetElement) {
			if(this.componentElement != null) {
				throw new IllegalArgumentException("Root element already has a widget child");
			}
			this.componentElement = (BaseWidgetElement) node;
		}
	    else if(node instanceof HeadElement) {
	    	if(this.headElement != null) {
	    		throw new IllegalArgumentException("Root element already has a head child");
	    	}
	    	this.headElement = (HeadElement) node;
	    }
//	    else if(node instanceof XAction || node instanceof XEvent) {
//	        throw new IllegalArgumentException("Root element cannot have children of type event or action");
//	    }
		super.appendChild(node);
	}

	public java.awt.Component getComponent() {
		if(this.componentElement == null) {
			throw new IllegalStateException("Root element does not have a component child");
		}
		return this.componentElement.getWidget();
	}
	
	public XHeadElement getHeadElement() {
		return this.headElement;
	}
	
	public XWidgetElement getWidgetElement() {
		return this.componentElement;
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.BaseNode#setParent(org.xamjwg.dom.XElement)
	 */
	public void setParent(XElement parent) {
		if(parent != null && !(parent instanceof ClientletContext)) {
			throw new IllegalArgumentException("Element named '" + this.getNodeName() + "' may not be a child of '" + parent.getNodeName() + "'");
		}
		super.setParent(parent);
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#getElementValue()
	 */
	public Object getElementValue() {
		return this;
	}
}
