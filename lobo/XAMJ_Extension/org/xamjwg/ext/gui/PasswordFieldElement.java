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
 * Created on May 28, 2005
 */
package org.xamjwg.ext.gui;

import org.xamjwg.dom.XPasswordFieldElement;

/**
 * @author J. H. S.
 */
public class PasswordFieldElement extends TextFieldElement implements
		XPasswordFieldElement {
	/**
	 * @param name
	 */
	public PasswordFieldElement(String name) {
		super(name);
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.BaseWidgetElement#createWidget()
	 */
	protected Widget createWidget() {
		return new WPasswordField();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XPasswordFieldElement#getEchoChar()
	 */
	public char getEchoChar() {
		return ((javax.swing.JPasswordField) ((WPasswordField) this.widget).component).getEchoChar();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XPasswordFieldElement#setEchoChar(char)
	 */
	public void setEchoChar(char value) {
		char oldValue = this.getEchoChar();
		if(oldValue != value) {
			((javax.swing.JPasswordField) ((WPasswordField) this.widget).component).setEchoChar(value);
			this.firePropertyChange("echoChar", Character.valueOf(oldValue), Character.valueOf(value));
		}
	}
	
	
}
