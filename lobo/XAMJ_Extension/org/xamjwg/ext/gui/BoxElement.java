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
 * Created on Mar 14, 2005
 */
package org.xamjwg.ext.gui;

import java.awt.Insets;

import org.xamjwg.dom.XBoxElement;
import org.xamjwg.dom.XEventElement;
import org.xamjwg.event.*;

/**
 * @author J. H. S.
 */
public class BoxElement extends BaseWidgetElement implements XBoxElement {
    public BoxElement(String name) {
        super(name);
    }
    
    /* (non-Javadoc)
     * @see net.sourceforge.xamj.domimpl.BaseWidgetElement#createComponent()
     */
    protected Widget createWidget() {
        return new WBox();
    }

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#getElementValue()
	 */
	public Object getElementValue() {
		return this;
	}
	
    protected void onPaint(java.awt.Graphics g, java.awt.Component c, Insets insets) {
    	if(this.hasListeners("on-paint")) {
    		java.awt.Rectangle rect = c.getBounds();
    		java.awt.Rectangle paintBounds = new java.awt.Rectangle(insets.left, insets.top, rect.width - insets.left - insets.right, rect.height - insets.top - insets.bottom);
    		this.fireXamjEvent(AbstractXamjEvent.createOnPaint(this, g, paintBounds));
    	}
    }
    
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.BaseElement#checkEventElement(org.xamjwg.dom.XEventElement)
	 */
	protected void checkEventElement(XEventElement eventElement) {
		String eventName = eventElement.getNodeName();
		if("on-paint".equals(eventName)) {
			// passes
		}
		else {
			super.checkEventElement(eventElement);
		}
	}
}
