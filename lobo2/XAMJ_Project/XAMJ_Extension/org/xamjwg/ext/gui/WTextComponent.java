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
 * Created on Apr 3, 2005
 */
package org.xamjwg.ext.gui;

import javax.swing.text.*;
import javax.swing.event.*;

/**
 * @author J. H. S.
 */
public abstract class WTextComponent extends WWrapper {
	/**
	 * 
	 */
	public WTextComponent() {
		super();
	}

	public WTextComponent(boolean scrollable) {
		super(scrollable);
	}

	public void setText(String value) {
		((JTextComponent) this.component).setText(value);
	}

	public String getText() {
		return ((JTextComponent) this.component).getText();
	}
	
	public void setEditable(boolean value) {
		((JTextComponent) this.component).setEditable(value);
	}
	
	public boolean isEditable() {
		return ((JTextComponent) this.component).isEditable();
	}
	
	public void addDocumentListener(DocumentListener listener) {
		((JTextComponent) this.component).getDocument().addDocumentListener(listener);
	}
}
