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

import java.awt.EventQueue;

import org.xamjwg.dom.XOnPropertyChangeElement;
import org.xamjwg.event.*;

/**
 * @author J. H. S.
 */
public class OnPropertyChangeElement extends BaseEventElement<XamjPropertyChangeEvent> implements
		XOnPropertyChangeElement {
	/**
	 * @param name
	 */
	public OnPropertyChangeElement(String name) {
		super(name);
	}

	private String propertyName;
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XOnPropertyChangeElement#setName(java.lang.String)
	 */
	public void setProperty(String value) {
		this.propertyName = value;
	}

	private void superExecute(XamjPropertyChangeEvent event) throws Exception {
		super.execute(event);
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XActionElement#execute(org.xamjwg.dom.XamjEvent)
	 */
	public void execute(final XamjPropertyChangeEvent event) throws Exception {
		String localPn = this.propertyName;
		if(localPn == null || localPn.equals(event.getPropertyName())) {
			if(EventQueue.isDispatchThread()) {
				super.execute(event);
			}
			else {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							superExecute(event);
						} catch(Exception err) {
							err.printStackTrace(System.err);
						}
					}
				});
			}
		}
	}
}
