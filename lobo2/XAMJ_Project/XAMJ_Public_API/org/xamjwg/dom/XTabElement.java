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
package org.xamjwg.dom;

/**
 * A tab element, which may only be a child of tabbed-box elements.
 * @see org.xamjwg.dom.XTabbedBoxElement
 * @author J. H. S.
 */
public interface XTabElement extends XBoxElement {
	/**
	 * @deprecated Will be removed before 1.0.
	 */
	public String getTitle();
	/**
	 * @deprecated Will be removed before 1.0.
	 */
	public void setTitle(String value);

	/**
	 * Gets the tab title.
	 */
	public String getText();
	
	/**
	 * Sets the tab title.
	 */
	public void setText(String value);

	public String getIcon();
	public void setIcon(String value);
	public String getDisabledIcon();
	public void setDisabledIcon(String value);
}
