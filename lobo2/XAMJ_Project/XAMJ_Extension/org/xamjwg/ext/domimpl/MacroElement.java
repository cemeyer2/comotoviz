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
 * Created on Apr 9, 2005
 */
package org.xamjwg.ext.domimpl;

import org.xamjwg.dom.*;

/**
 * @author J. H. S.
 */
public class MacroElement extends BaseElement implements XMacroElement {
	/**
	 * @param name
	 */
	public MacroElement(String name) {
		super(name);
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.AbstractNode#setParent(org.xamjwg.dom.XElement)
	 */
	public void setParent(XElement parent) {
		if(!(parent instanceof XMacrosElement)) {
			throw new IllegalArgumentException("Element named '" + this.getNodeName() + "' must be a child of 'macros'");
		}
		super.setParent(parent);
	}
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.AbstractElement#checkEventElement(org.xamjwg.dom.XEventElement)
	 */
	protected void checkEventElement(XEventElement eventElement) {
		// any allowed
	}
	
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#getElementValue()
	 */
	public Object getElementValue() {
		return this;
	}
}
