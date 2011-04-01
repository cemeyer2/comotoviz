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
import org.xamjwg.event.*;

/**
 * @author J. H. S.
 */
public class DoSetElement extends BaseActionElement implements XDoSetElement {
	/**
	 * @param name
	 */
	public DoSetElement(String name) {
		super(name);
	}

	private XamjEvaluatable<XElement> element;
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XDoSetElement#setElement(org.xamjwg.dom.XamjEvaluatable)
	 */
	public void setElement(XamjEvaluatable<XElement> value) {
		this.element = value;
	}

	private String property;
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XDoSetElement#setProperty(java.lang.String)
	 */
	public void setProperty(String value) {
		this.property = value;
	}

	private XamjEvaluatable valueEval;
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XDoSetElement#setValue(org.xamjwg.dom.XamjEvaluatable)
	 */
	public void setValue(XamjEvaluatable value) {
		this.valueEval = value;
	}
	
	private boolean invert;
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XDoSetElement#setInvert(boolean)
	 */
	public void setInvert(boolean value) {
		this.invert = value;
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XActionElement#execute(org.xamjwg.dom.XamjEvent)
	 */
	public void execute(AbstractXamjEvent event) throws Exception {
		XElement elem = this.element.evaluate();
		if(elem == null) {
			throw new NullPointerException("element not set");
		}
		String pn = this.property;
		if(pn == null) {
			throw new NullPointerException("property not set");
		}
		XamjEvaluatable ve = this.valueEval;
		Object value = ve == null ? null : ve.evaluate();
		if(value instanceof Boolean && this.invert) {
			value = ((Boolean) value).booleanValue() ? Boolean.FALSE : Boolean.TRUE;
		}
		Bean bean = new Bean(elem.getClass());
		bean.setPropertyForFQN(elem, pn, value);
	}
}
