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
 * Created on Jun 11, 2005
 */
package org.xamjwg.ext.domimpl;

import org.lobobrowser.util.*;
import org.xamjwg.dom.*;

/**
 * @author J. H. S.
 */
public class ParamElement extends BaseElement implements XParamElement {
	/**
	 * @param name
	 */
	public ParamElement(String name) {
		super(name);
	}

	private String name;
	private XamjEvaluatable value;
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XParamElement#setName(java.lang.String)
	 */
	public void setName(String value) {
		String oldValue = this.name;
		if(!Objects.equals(oldValue, value)) {
			this.name = value;
			this.firePropertyChange("name", oldValue, value);
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XParamElement#getName()
	 */
	public String getName() {
		return this.name;
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XParamElement#getValue()
	 */
    public XamjEvaluatable getValue() {
    	return this.value;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XParamElement#setValue(java.lang.Object)
	 */
	public void setValue(XamjEvaluatable v) {
		Object oldValue = this.value;
		if(!Objects.equals(oldValue, v)) {
			this.value = v;
			this.firePropertyChange("value", oldValue, v);
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#getElementValue()
	 */
	public Object getElementValue() {
		return this.value;
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.BaseNode#setParent(org.xamjwg.dom.XElement)
	 */
	public void setParent(XElement parent) {
		if(parent != null && !(parent instanceof XActionElement)) {
			throw new IllegalArgumentException("Parent of element named '" + this.getNodeName() + "' must be an action element");
		}
		super.setParent(parent);
	}
}
