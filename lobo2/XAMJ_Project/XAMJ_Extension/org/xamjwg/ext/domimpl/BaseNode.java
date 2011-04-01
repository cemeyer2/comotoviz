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
 * Created on Mar 13, 2005
 */
package org.xamjwg.ext.domimpl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.xamjwg.dom.*;

/**
 * @author J. H. S.
 */
public abstract class BaseNode implements XNode {
    protected XElement parent;
    protected XDocument document;
    
    public XElement getParent() {
        return this.parent;
    }
    
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.context.BaseDocument#setItems(java.util.Map)
	 */
	public void setItems(Map aItems) {
		if(aItems != null) {
			Iterator i = aItems.entrySet().iterator();
			while(i.hasNext()) {
				Map.Entry entry = (Map.Entry) i.next();
				this.setItem((String) entry.getKey(), entry.getValue());
			}
		}
	}

	
    public void setParent(XElement parent) {
        this.parent = parent;
    }
    
    public void setOwnerDocument(XDocument document) {
    	this.document = document;
    }
    
    public XDocument getOwnerDocument() {
    	return this.document;
    }
    
	private Map items = null;
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.ClientletContext#getAttribute(java.lang.String)
	 */
	public Object getItem(String name) {
		synchronized(this) {
			if(this.items == null) {
				return null;
			}
			return this.items.get(name);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.ClientletContext#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setItem(String name, Object value) {
		synchronized(this) {
			if(this.items == null) {
				this.items = new HashMap();
			}
			this.items.put(name, value);
		}
	}	
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.ClientletContext#setAttributes(org.xamjwg.dom.ClientletContext)
	 */
	public void copyItemsTo(XNode target) {
		Map.Entry[] entries;
		synchronized(this) {
			if(this.items == null) {
				entries = null;
			}
			else {
				entries = (Map.Entry[]) this.items.entrySet().toArray(new Map.Entry[0]);
			}
		}
		if(entries != null) {
			for(int i = 0; i < entries.length; i++) {
				Map.Entry entry = entries[i];
				target.setItem((String) entry.getKey(), entry.getValue());
			}
		}
	}

}
