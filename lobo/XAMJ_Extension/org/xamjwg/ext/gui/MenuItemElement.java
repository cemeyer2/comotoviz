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
import org.xamjwg.dom.*;

import javax.swing.*;

/**
 * @author J. H. S.
 */
public class MenuItemElement extends BaseButtonElement implements XMenuItemElement {
	/**
	 * @param name
	 */
	public MenuItemElement(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.BaseButtonElement#createAbstractButton()
	 */
	protected WAbstractButton createAbstractButton() {
		return new WMenuItem();
	}

	public JMenuItem getJMenuItem() {
		return (JMenuItem) ((WMenuItem) this.widget).component;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMenuItemElement#getAccelerator()
	 */
	public String getAccelerator() {
		KeyStroke accel = ((JMenuItem) ((WMenuItem) this.widget).component).getAccelerator();
		return accel == null ? null : accel.toString();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMenuItemElement#getMnemonic()
	 */
	public Character getMnemonic() {
		int mint = ((JMenuItem) ((WMenuItem) this.widget).component).getMnemonic();
		return mint > 0 ? Character.valueOf((char) mint) : null;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMenuItemElement#setAccelerator(java.lang.String)
	 */
	public void setAccelerator(String value) {
		String oldValue = this.getAccelerator();
		if(!Objects.equals(value, oldValue)) {
			((JMenuItem) ((WMenuItem) this.widget).component).setAccelerator(KeyStroke.getKeyStroke(value));
			this.firePropertyChange("accelerator", oldValue, value);
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMenuItemElement#setMnemonic(java.lang.Character)
	 */
	public void setMnemonic(Character value) {
		Character oldValue = this.getMnemonic();
		if(!Objects.equals(value, oldValue)) {
			int mint = value == null ? 0 : value.charValue();
			((JMenuItem) ((WMenuItem) this.widget).component).setMnemonic(mint);
		}		
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMenuItemElement#isArmed()
	 */
	public boolean isArmed() {
		return ((JMenuItem) ((WMenuItem) this.widget).component).isArmed();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMenuItemElement#setArmed(boolean)
	 */
	public void setArmed(boolean value) {
		boolean oldValue = this.isArmed();
		if(value != oldValue) {
			((JMenuItem) ((WMenuItem) this.widget).component).setArmed(value);
			this.firePropertyChange("armed", Boolean.valueOf(oldValue), Boolean.valueOf(value));
		}
	}
}
