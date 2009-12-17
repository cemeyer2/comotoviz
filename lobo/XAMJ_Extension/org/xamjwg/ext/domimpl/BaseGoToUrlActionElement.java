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

import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;


import org.lobobrowser.ua.Parameter;
import org.lobobrowser.ua.ParameterInfo;
import org.lobobrowser.util.Objects;
import org.xamjwg.dom.XamjEvaluatable;
import org.xamjwg.event.*;

/**
 * @author J. H. S.
 */
public abstract class BaseGoToUrlActionElement extends BaseActionElement {
	/**
	 * @param name
	 */
	public BaseGoToUrlActionElement(String name) {
		super(name);
	}

	private XamjEvaluatable href;
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XDoNavigateElement#setHref(java.lang.String)
	 */
	public void setHref(XamjEvaluatable<String> value) {
		Object oldValue = this.href;
		if(!Objects.equals(oldValue, value)) {
			this.href = value;
			this.firePropertyChange("href", oldValue, value);
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XDoNavigateElement#getHref()
	 */
	public XamjEvaluatable<String> getHref() {
		return this.href;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XActionElement#execute(org.xamjwg.dom.XamjEvent)
	 */
	public void execute(AbstractXamjEvent event) throws Exception {
		XamjEvaluatable ehref = this.href;
		if(ehref == null) {
			throw new IllegalArgumentException("href not set");
		}
		String basicUrl = (String) href.evaluate();
		URL url = new URL(this.getCurrentURL(), basicUrl);
		Map paramMap = this.getParameters();
		ParameterInfo paramInfo = null;
		if(paramMap != null) {
			Iterator i = paramMap.entrySet().iterator();
			LinkedList<Parameter> paramList = new LinkedList<Parameter>();
			while(i.hasNext()) {
				Map.Entry entry = (Map.Entry) i.next();
				String paramName = (String) entry.getKey();
				Object value = entry.getValue();
				Parameter param = getUrlParameter(paramName, value);
				if(param != null) {
					paramList.add(param);
				}
			}
			Parameter[] parameters = paramList.toArray(new Parameter[0]);
			paramInfo = new SimpleParameterInfo(parameters);
		}
		this.goTo(url, "GET", paramInfo);
	}
	
	protected abstract void goTo(URL url, String method, ParameterInfo pinfo);
}
