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
 * Created on Jun 2, 2005
 */
package org.xamjwg.ext.gui;

import org.xamjwg.dom.XRadioButtonElement;

/**
 * @author J. H. S.
 */
public class RadioButtonElement extends BaseToggleButtonElement implements XRadioButtonElement {
	/**
	 * @param name
	 */
	public RadioButtonElement(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.BaseButtonElement#createAbstractButton()
	 */
	protected WAbstractButton createAbstractButton() {
		return new WRadioButton();
	}

}
