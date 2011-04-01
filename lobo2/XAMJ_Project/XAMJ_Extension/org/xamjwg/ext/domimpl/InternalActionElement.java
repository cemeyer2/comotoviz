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
 * Created on Apr 2, 2005
 */
package org.xamjwg.ext.domimpl;

import org.xamjwg.dom.*;
import org.xamjwg.event.*;

/**
 * @author J. H. S.
 */
public class InternalActionElement extends BaseActionElement {
	private final XamjListener delegate;
	
	/**
	 * @param name
	 */
	public InternalActionElement(XamjListener delegate) {
		super("$internal-action");
		this.delegate = delegate;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XActionElement#execute(org.xamjwg.dom.XamjEvent)
	 */
	public void execute(AbstractXamjEvent event) throws Exception {
		this.delegate.execute(event);
	}
	
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XNode#cloneNode()
	 */
	public XNode cloneNode() {
		return new InternalActionElement(this.delegate);
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#appendChild(org.xamjwg.dom.XNode)
	 */
	public void appendChild(XNode node) {
		throw new IllegalArgumentException("Fatal: No nodes expected to be children of internal action element");
	}

}
