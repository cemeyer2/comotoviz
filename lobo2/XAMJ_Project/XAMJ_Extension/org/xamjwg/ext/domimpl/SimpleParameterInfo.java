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

import org.lobobrowser.ua.Parameter;
import org.lobobrowser.ua.ParameterInfo;


class SimpleParameterInfo implements ParameterInfo {
	private final Parameter[] parameters;
	
	public SimpleParameterInfo(Parameter[] parameters) {
		this.parameters = parameters;
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.clientlet.ParameterInfo#getEncoding()
	 */
	public String getEncoding() {
		return "application/x-www-form-urlencoded";
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.clientlet.ParameterInfo#getParameters()
	 */
	public Parameter[] getParameters() {
		return this.parameters;
	}
}