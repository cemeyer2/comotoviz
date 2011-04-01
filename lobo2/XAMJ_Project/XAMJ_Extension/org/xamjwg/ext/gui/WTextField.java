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
 * Created on Jun 7, 2005
 */
package org.xamjwg.ext.gui;

import java.awt.Component;
import javax.swing.*;

/**
 * @author J. H. S.
 */
public class WTextField extends WTextComponent {
	/**
	 * 
	 */
	public WTextField() {
		super();
	}

	/**
	 * @param scrollable
	 */
	public WTextField(boolean scrollable) {
		super(scrollable);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.WWrapper#createComponent()
	 */
	protected Component createComponent() {
		return new JTextField();
	}

	public int getDefaultWidth() {
		return -1;
	}

	public int getDefaultHeight() {
		return super.getDefaultHeight();
	}	
}
