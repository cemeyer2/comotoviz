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
 * Created on Mar 31, 2005
 */
package org.xamjwg.ext.domimpl;

import org.lobobrowser.util.*;
import org.xamjwg.dom.XActionContainerElement;
import org.xamjwg.dom.XActionElement;
import org.xamjwg.event.*;

import java.awt.*;
import java.util.*;
/**
 * @author J. H. S.
 */
public class BaseActionContainerElement<TEvent extends AbstractXamjEvent> extends BaseElement implements XActionContainerElement<TEvent> {
    /* (non-Javadoc)
     * @see org.xamjwg.dom.XActionElement#execute()
     */
    public void execute(final TEvent event) throws Exception {
        Diagnostics.Assert(EventQueue.isDispatchThread(), "Must execute in event dispatch thread"); 
        Iterator i = this.getChildNodes();
        while(i.hasNext()) {
            Object node = i.next();
            if(node instanceof XActionElement) {
                ((XActionElement) node).execute(event);
            }
        }
    }
    
    /**
     * @param name
     */
    public BaseActionContainerElement(String name) {
        super(name);
    }
    
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#getElementValue()
	 */
	public Object getElementValue() {
		return this;
	}
}
