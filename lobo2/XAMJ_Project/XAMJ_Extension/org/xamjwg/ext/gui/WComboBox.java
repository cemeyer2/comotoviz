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
 * Created on Mar 29, 2005
 */
package org.xamjwg.ext.gui;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.*;
//import java.util.logging.*;
import javax.swing.event.*;

/**
 * @author J. H. S.
 */
public class WComboBox extends WWrapper {
	//private static final Logger logger = Logger.getLogger(WComboBox.class);
	private final LocalComboBoxEditor editor = new LocalComboBoxEditor();

	/**
     * 
     */
    public WComboBox() {
        super();
        JComboBox comboBox = (JComboBox) this.component;
        comboBox.setEditor(editor);
    }

    public void addPopupMenuListener(PopupMenuListener listener) {
    	((JComboBox) this.component).addPopupMenuListener(listener);
    }
    
    public void addKeyListener(java.awt.event.KeyListener keyListener) {
    	this.editor.addKeyListener(keyListener);
    }
    
    public void removeKeyListener(java.awt.event.KeyListener keyListener) {
    	this.editor.removeKeyListener(keyListener);
    }

    /* (non-Javadoc)
     * @see net.sourceforge.xamj.domimpl.gui.WWrapper#createComponent()
     */
    protected Component createComponent() {
        return new JComboBox();
    }
    
    public void setText(String text, boolean forceSelect) {
    	JComboBox combo = (JComboBox) this.component;
    	boolean editable = this.isEditable();
    	if(editable) {
    		combo.getEditor().setItem(text);
    	}
    	if(forceSelect || !editable) {
    		combo.addItem(text);
    		combo.setSelectedItem(text);
    	}
    }
    
    public void setSelectedItem(Object item) {
    	((JComboBox) this.component).setSelectedItem(item);
    }

    public String getText() {
    	if(this.isEditable()) {
    		return (String) ((JComboBox) this.component).getEditor().getItem();
    	}
    	else {
    		return String.valueOf(((JComboBox) this.component).getSelectedItem());
    	}
    }    

    public void setEditable(boolean value) {
        ((JComboBox) this.component).setEditable(value);
    }
    
    public boolean isEditable() {
        return ((JComboBox) this.component).isEditable();
    }
    
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XComboBoxElement#isPopupVisible()
	 */
	public boolean isPopupVisible() {
		return ((JComboBox) this.component).isPopupVisible();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XComboBoxElement#showPopup()
	 */
	public void showPopup() {
		((JComboBox) this.component).showPopup();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XComboBoxElement#showPopup()
	 */
	public void hidePopup() {
		((JComboBox) this.component).hidePopup();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XComboBoxElement#addItem(java.lang.String)
	 */
	public void addItem(String item) {
		((JComboBox) this.component).addItem(item);
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XComboBoxElement#removeAllItems()
	 */
	public void removeAllItems() {
		((JComboBox) this.component).removeAllItems();
	}
	
    public void addActionListener(ActionListener listener) {
        ((JComboBox) this.component).addActionListener(listener);
    }
    
    public void addChangeListener(ChangeListener listener) {
        this.editor.addChangeListener(listener);
    }
    
//    public Dimension getPreferredSize() {
//    	Dimension ps = super.getPreferredSize();
//    	if(logger.isLoggable(Level.INFO))logger.info("getPreferredSize(): " + ps);
//    	return ps;
//    }
//    
//    public int getPreferredWidth() {
//    	int pw = super.getPreferredWidth();
//    	if(logger.isLoggable(Level.INFO))logger.info("getPreferredWidth(): " + pw);
//    	return pw;
//    }
//    
//    public int getPreferredHeight() {
//    	int ph = super.getPreferredHeight();
//    	if(logger.isLoggable(Level.INFO))logger.info("getPreferredHeight(): " + ph);
//    	return ph;
//    }
}
