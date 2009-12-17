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
 * Created on Mar 29, 2005
 */
package org.xamjwg.ext.gui;

import org.xamjwg.dom.XNode;

/**
 * @author J. H. S.
 */
public abstract class BaseWidgetWithTextElement extends BaseWidgetElement {
    /**
     * @param name
     */
    public BaseWidgetWithTextElement(String name) {
        super(name);
    }

    /* (non-Javadoc)
     * @see org.xamjwg.dom.XElement#appendChild(org.xamjwg.dom.XNode)
     */
    public void appendChild(XNode node) {
        //TODO Problematic to have text node that changes "text" property
        //TODO because of cloning, DOM modifications.
    	//TODO Perhaps add mechanism to set complex properties.
       	super.appendChild(node);
    }    
    
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#getElementValue()
	 */
	public Object getElementValue() {
		return this.getText();
	}
	
    public abstract void setText(String text);
    public abstract String getText();
}
