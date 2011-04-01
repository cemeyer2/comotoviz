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
 * Created on May 27, 2005
 */
package org.xamjwg.ext.domimpl;

import org.lobobrowser.clientlet.*;
import org.lobobrowser.ua.ParameterInfo;
import org.lobobrowser.ua.TargetType;
import org.xamjwg.dom.*;

import java.net.*;

/**
 * @author J. H. S.
 */
public class DoNavigateElement extends BaseGoToUrlActionElement implements
		XDoNavigateElement {
	/**
	 * @param name
	 */
	public DoNavigateElement(String name) {
		super(name);
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.BaseGoToUrlActionElement#goTo(java.net.URL, java.lang.String, org.xamjwg.clientlet.ParameterInfo)
	 */
	protected void goTo(URL url, String method, ParameterInfo pinfo) {
		this.document.getClientletContext().getClientletFrame().navigate(url, method, pinfo, TargetType.SELF);
	}
}
