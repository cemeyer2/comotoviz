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

import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.*;
/*
 * Created on Jun 6, 2005
 */

/**
 * @author J. H. S.
 */
public class LocalComboBoxEditor implements ComboBoxEditor {
	private final JTextField textField;
	private boolean inNotification = false;
	
	public LocalComboBoxEditor() {
		this.textField = new JTextField();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.ComboBoxEditor#getEditorComponent()
	 */
	public Component getEditorComponent() {
		return this.textField;
	}
	
	//private Object item;
	
	/* (non-Javadoc)
	 * @see javax.swing.ComboBoxEditor#setItem(java.lang.Object)
	 */
	public void setItem(Object arg0) {
		//this.item = arg0;
		if(!this.inNotification) {
			this.textField.setText(String.valueOf(arg0));
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.ComboBoxEditor#getItem()
	 */
	public Object getItem() {
		return this.textField.getText();
	}

	/* (non-Javadoc)
	 * @see javax.swing.ComboBoxEditor#selectAll()
	 */
	public void selectAll() {
		this.textField.selectAll();
	}

	/* (non-Javadoc)
	 * @see javax.swing.ComboBoxEditor#addActionListener(java.awt.event.ActionListener)
	 */
	public void addActionListener(ActionListener arg0) {
		this.textField.addActionListener(arg0);
	}

	/* (non-Javadoc)
	 * @see javax.swing.ComboBoxEditor#removeActionListener(java.awt.event.ActionListener)
	 */
	public void removeActionListener(ActionListener arg0) {
		this.textField.removeActionListener(arg0);
	}
	
	public void addKeyListener(java.awt.event.KeyListener listener) {
		this.textField.addKeyListener(listener);
	}

	public void removeKeyListener(java.awt.event.KeyListener listener) {
		this.textField.removeKeyListener(listener);
	}
	
	public void addChangeListener(final ChangeListener listener) {
		this.textField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				LocalComboBoxEditor.this.inNotification = true;
				try {
					listener.stateChanged(new ChangeEvent(LocalComboBoxEditor.this));
				} finally {
					LocalComboBoxEditor.this.inNotification = false;					
				}
			}
			
			public void insertUpdate(DocumentEvent e) {
				LocalComboBoxEditor.this.inNotification = true;
				try {
					listener.stateChanged(new ChangeEvent(LocalComboBoxEditor.this));
				} finally {
					LocalComboBoxEditor.this.inNotification = false;					
				}
			}
			
			public void removeUpdate(DocumentEvent e) { 
				LocalComboBoxEditor.this.inNotification = true;
				try {
					listener.stateChanged(new ChangeEvent(LocalComboBoxEditor.this));
				} finally {
					LocalComboBoxEditor.this.inNotification = false;					
				}
			}
		});
	}

}
