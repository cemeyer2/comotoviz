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
 * Created on Apr 23, 2005
 */
package org.xamjwg.ext.markup;

import org.lobobrowser.util.*;
import org.xamjwg.dom.*;
import org.xamjwg.event.*;

/**
 * @author J. H. S.
 */
public class AnchorElement extends BaseMarkupElement implements XAnchorElement {
	private String href;
	
	/**
	 * @param name
	 */
	public AnchorElement(String name) {
		super(name);
		this.style.setColor("blue");
		this.style.setTextDecoration("underline");
		this.addEventListener("on-click", new XamjListener() {
			public void execute(AbstractXamjEvent event) throws Exception {
				String href = AnchorElement.this.href;
				if(href == null) {
					throw new IllegalArgumentException("href not set");
				}
				document.getClientletContext().navigate(href);
			}
		});
	}
	
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XAnchorElement#getHref()
	 */
	public String getHref() {
		return this.href;
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XAnchorElement#setHref(java.lang.String)
	 */
	public void setHref(String value) {
		Object oldValue = this.href;
		if(!Objects.equals(value, oldValue)) {
			this.href = value;
			this.firePropertyChange("href", oldValue, value);
		}
	}
}
