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
 * A menu-item element, which is a special kind of button. 
 * Elements of this kind may be placed
 * below menu elements.
 * @author J. H. S.
 */
public interface XMenuItemElement extends XButtonElement {
	public Character getMnemonic();
	public String getAccelerator();
	
	/**
	 * Sets a mnemonic character for the menu item.
	 * This is usually set to be the first character of the
	 * menu item text. 
	 */
	public void setMnemonic(Character value);
	
	/**
	 * Sets a menu accelerator key combination using a Swing
	 * KeyStroke format. It may use modifiers
	 * shift, control, ctrl, meta, alt, altGraph.
	 * Examples of accelerators are "ctrl X" or
	 * "alt shift INSERT". 
	 * @param value
	 */
	public void setAccelerator(String value);
	
	public boolean isArmed();
	public void setArmed(boolean value);
}
