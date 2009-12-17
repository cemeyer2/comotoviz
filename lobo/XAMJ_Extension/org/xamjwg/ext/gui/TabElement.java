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

import org.lobobrowser.util.*;
import org.xamjwg.dom.XTabElement;

/**
 * @author J. H. S.
 */
public class TabElement extends BoxElement implements XTabElement {
	private String title;
	private String icon;
	private String disabledIcon;
	
	/**
	 * @param name
	 */
	public TabElement(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTabElement#getTitle()
	 */
	public String getTitle() {
		return this.getText();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTabElement#setTitle(java.lang.String)
	 */
	public void setTitle(String value) {
		this.setText(value);
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTabElement#getTitle()
	 */
	public String getText() {
		return this.title;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTabElement#setTitle(java.lang.String)
	 */
	public void setText(String value) {
		Object oldValue = this.title;
		if(!Objects.equals(value, oldValue)) {
			this.title = value;
			this.firePropertyChange("title", oldValue, value);
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTabElement#getIcon()
	 */
	public String getDisabledIcon() {
		return this.disabledIcon;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTabElement#setIcon(java.lang.String)
	 */
	public void setDisabledIcon(String value) {
		Object oldValue = this.disabledIcon;
		if(!Objects.equals(value, oldValue)) {
			this.disabledIcon = value;
			this.firePropertyChange("disabledIcon", oldValue, value);
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTabElement#getIcon()
	 */
	public String getIcon() {
		return this.icon;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTabElement#setIcon(java.lang.String)
	 */
	public void setIcon(String value) {
		Object oldValue = this.icon;
		if(!Objects.equals(value, oldValue)) {
			this.icon = value;
			this.firePropertyChange("icon", oldValue, value);
		}
	}

}
