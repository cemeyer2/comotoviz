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
package org.xamjwg.ext.gui;

//import java.util.logging.*;
import org.xamjwg.dom.XMenuBarElement;
import org.xamjwg.dom.XNode;

import javax.swing.*;

/**
 * @author J. H. S.
 */
public class MenuBarElement extends BaseWidgetElement implements
		XMenuBarElement {
	//private static final Logger logger = Logger.getLogger(MenuBarElement.class);

	/**
	 * @param name
	 */
	public MenuBarElement(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.BaseWidgetElement#createWidget()
	 */
	protected Widget createWidget() {
		return new WMenuBar();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#appendChild(org.xamjwg.dom.XNode)
	 */
	public void appendChild(XNode node) {
		if(node instanceof MenuElement) {
			JMenu jmenu = ((MenuElement) node).getJMenu();
			((WMenuBar) this.widget).add(jmenu);
		}
		this.appendChildImpl(node);
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.AbstractElement#removingNode(org.xamjwg.dom.XNode)
	 */
	protected void removingNode(XNode node) {
		if(node instanceof MenuElement) {
			JMenu jmenu = ((MenuElement) node).getJMenu();
			((WMenuBar) this.widget).remove(jmenu);
		}
		else {
			super.removingNode(node);			
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#getElementValue()
	 */
	public Object getElementValue() {
		return this;
	}
}
