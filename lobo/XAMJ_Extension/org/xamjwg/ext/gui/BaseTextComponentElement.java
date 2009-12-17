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
package org.xamjwg.ext.gui;

import java.io.Writer;

import org.lobobrowser.util.*;
import org.xamjwg.dom.*;

/**
 * @author J. H. S.
 */
public abstract class BaseTextComponentElement extends BaseWidgetWithTextElement implements XTextWidgetElement {
	/**
	 * @param name
	 */
	public BaseTextComponentElement(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.BaseWidgetWithTextElement#getText()
	 */
	public String getText() {
		return ((WTextComponent) this.widget).getText();
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.BaseWidgetWithTextElement#setText(java.lang.String)
	 */
	public void setText(String value) {
		Object oldValue = this.getText();
		if(!Objects.equals(oldValue, value)) {
			((WTextComponent) this.widget).setText(value);
			this.firePropertyChange("text", oldValue, value);
		}
	}
	
	public boolean isEditable() {
		return ((WTextComponent) this.widget).isEditable();
	}	

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.BaseWidgetWithTextElement#setText(java.lang.String)
	 */
	public void setEditable(boolean value) {
		boolean oldValue = this.isEditable();
		if(oldValue != value) {
			((WTextComponent) this.widget).setEditable(value);
			this.firePropertyChange("text", new Boolean(oldValue), new Boolean(value));
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#getElementValue()
	 */
	public Object getElementValue() {
		return this.getText();
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTextWidgetElement#copy()
	 */
	public void copy() {
		((javax.swing.text.JTextComponent) ((WTextComponent) this.widget).component).copy();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTextWidgetElement#cut()
	 */
	public void cut() {
		((javax.swing.text.JTextComponent) ((WTextComponent) this.widget).component).cut();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTextWidgetElement#paste()
	 */
	public void paste() {
		((javax.swing.text.JTextComponent) ((WTextComponent) this.widget).component).paste();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTextWidgetElement#getSelectedText()
	 */
	public String getSelectedText() {
		// TODO Auto-generated method stub
		return ((javax.swing.text.JTextComponent) ((WTextComponent) this.widget).component).getSelectedText();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTextWidgetElement#replaceSelection(java.lang.String)
	 */
	public void replaceSelection(String replacement) {
		((javax.swing.text.JTextComponent) ((WTextComponent) this.widget).component).replaceSelection(replacement);
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTextWidgetElement#save(java.io.Writer)
	 */
	public void save(Writer writer) throws java.io.IOException {
		((javax.swing.text.JTextComponent) ((WTextComponent) this.widget).component).write(writer);		
	}
}
