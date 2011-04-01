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
 * Created on Apr 1, 2005
 */
package org.xamjwg.ext.domimpl;

import org.lobobrowser.clientlet.*;
import org.lobobrowser.ua.Parameter;
import org.xamjwg.dom.*;
import java.util.*;

/**
 * @author J. H. S.
 */
public abstract class BaseActionElement extends BaseElement implements XActionElement {
    /**
     * 
     */
    public BaseActionElement(String name) {
        super(name);
    }

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.BaseNode#setParent(org.xamjwg.dom.XElement)
	 */
	public void setParent(XElement parent) {
		if(parent != null &&
		   !(parent instanceof XActionContainerElement)) {
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
	
	protected Map getParameters() throws Exception {
		Map params = null;
		Iterator i = this.getChildNodes();
		while(i.hasNext()) {
			Object child = i.next();
			if(child instanceof XParamElement) {
				XParamElement param = (XParamElement) child;
				String name = param.getName();
				XamjEvaluatable ev = param.getValue();
				Object value = ev == null ? null : ev.evaluate();
				if(params == null) {
					params = new HashMap();
				}
				params.put(name, value);
			}
		}
		return params;
	}
	
	protected static Parameter getUrlParameter(String name, Object value) throws Exception {
		if(value instanceof XElement) {
			value = ((XElement) value).getElementValue();			
		}
		if(value == null) {
			return null;
		}
		return new SimpleParameter(name, String.valueOf(value));
	}
}
