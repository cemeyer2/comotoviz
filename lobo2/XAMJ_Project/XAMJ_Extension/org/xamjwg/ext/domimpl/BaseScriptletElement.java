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

import org.xamjwg.dom.XNode;
import org.xamjwg.dom.XText;

/**
 * @author J. H. S.
 */
public class BaseScriptletElement extends BaseElement {
    private final StringBuffer text = new StringBuffer();
    
    public BaseScriptletElement(String name) {
        super(name);        
    }
    
    /* (non-Javadoc)
     * @see org.xamjwg.dom.XElement#appendChild(org.xamjwg.dom.XNode)
     */
    public void appendChild(XNode node) {
        if(node instanceof XText) {
            text.append(((XText) node).getValue());
            super.appendChild(node);
        }
        else {
            throw new IllegalArgumentException("Non-text node cannot be added to element named '" + this.getNodeName() + "'");
        }
    }
    
    public String getInnerText() {
        return this.text.toString();
    }
    
    
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#getElementValue()
	 */
	public Object getElementValue() {
		return this.getInnerText();
	}
}
