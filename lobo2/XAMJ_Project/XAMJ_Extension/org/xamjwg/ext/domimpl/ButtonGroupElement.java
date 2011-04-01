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
 * Created on Jun 4, 2005
 */
package org.xamjwg.ext.domimpl;

import org.xamjwg.dom.XButtonElement;
import org.xamjwg.dom.XButtonGroupElement;
import org.xamjwg.ext.gui.*;

import javax.swing.*;

/**
 * @author J. H. S.
 */
public class ButtonGroupElement extends BaseElement implements
		XButtonGroupElement {
	private final ButtonGroup buttonGroup;
	
	/**
	 * @param name
	 */
	public ButtonGroupElement(String name) {
		super(name);
		this.buttonGroup = new ButtonGroup();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XButtonGroupElement#getSelectedButton()
	 */
	public XButtonElement getSelectedButton() {
		//TODO: Probably should be XToggleButtonElement getSelectedToggleButton().
		ButtonModel model = this.buttonGroup.getSelection();
		if(model instanceof LocalToggleButtonModel) {
			return ((LocalToggleButtonModel) model).getToggleButtonElement();
		}
		else {
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#getElementValue()
	 */
	public Object getElementValue() {
		XButtonElement be = this.getSelectedButton();
		return be == null ? null : be.getId();
	}
	
	public void add(AbstractButton button) {
		this.buttonGroup.add(button);
	}
}
