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

import java.awt.event.*;
import java.util.logging.*;

import org.lobobrowser.util.*;
import org.xamjwg.dom.XComboBoxElement;
import org.xamjwg.dom.XEventElement;
import org.xamjwg.event.*;

import javax.swing.JComboBox;
import javax.swing.event.*;

/**
 * @author J. H. S.
 */
public class ComboBoxElement extends BaseWidgetWithTextElement implements XComboBoxElement {
	private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ComboBoxElement.class.getName());

	/**
     * @param name
     */
    public ComboBoxElement(String name) {
        super(name);
        ((WComboBox) this.widget).addPopupMenuListener(new PopupMenuListener() {
        	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        		ComboBoxElement.this.fireXamjEvent(AbstractXamjEvent.create(this, "on-before-popup-visible"));
        	}
        	
        	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        		ComboBoxElement.this.fireXamjEvent(AbstractXamjEvent.create(this, "on-before-popup-invisible"));        		
        	}
        	
        	public void popupMenuCanceled(PopupMenuEvent e) {
        		ComboBoxElement.this.fireXamjEvent(AbstractXamjEvent.create(this, "on-popup-canceled"));        		        		
        	}
        });
    }

    public void setText(String value) {
        String oldValue = ((WComboBox) this.widget).getText();
        if(!Objects.equals(value, oldValue)) {
            ((WComboBox) this.widget).setText(value, false);
            this.firePropertyChange("text", oldValue, value);
        }   
    }

    public void setText(String value, boolean forceSelect) {
        String oldValue = ((WComboBox) this.widget).getText();
        if(!Objects.equals(value, oldValue)) {
            ((WComboBox) this.widget).setText(value, forceSelect);
            this.firePropertyChange("text", oldValue, value);
        }   
    }
    
    public String getText() {
        return ((WComboBox) this.widget).getText();
    }

    /* (non-Javadoc)
     * @see net.sourceforge.xamj.domimpl.gui.BaseWidgetElement#createWidget()
     */
    protected Widget createWidget() {
        return new WComboBox();
    }
    
    /* (non-Javadoc)
     * @see org.xamjwg.dom.XComboBoxElement#getEditable()
     */
    public boolean isEditable() {
        return ((WComboBox) this.widget).isEditable();
    }

    /* (non-Javadoc)
     * @see org.xamjwg.dom.XComboBoxElement#isEditable(boolean)
     */
    public void setEditable(boolean value) {
        boolean oldValue = ((WComboBox) this.widget).isEditable();
        if(oldValue != value) {
            ((WComboBox) this.widget).setEditable(value);
            this.firePropertyChange("editable", Boolean.valueOf(oldValue), Boolean.valueOf(value));
        }
    }
    
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XComboBoxElement#getMaximumRowCount()
	 */
	public int getMaximumRowCount() {
		return ((javax.swing.JComboBox) ((WComboBox) this.widget).component).getMaximumRowCount();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XComboBoxElement#setMaximumRowCount(int)
	 */
	public void setMaximumRowCount(int value) {
		int oldValue = this.getMaximumRowCount();
		if(oldValue != value) {
			((javax.swing.JComboBox) ((WComboBox) this.widget).component).setMaximumRowCount(value);
			this.firePropertyChange("maximumRowCount", Integer.valueOf(oldValue), Integer.valueOf(value));
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XComboBoxElement#isPopupVisible()
	 */
	public boolean isPopupVisible() {
		return ((WComboBox) this.widget).isPopupVisible();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XComboBoxElement#showPopup()
	 */
	public void showPopup() {
		((WComboBox) this.widget).showPopup();
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XComboBoxElement#hidePopup()
	 */
	public void hidePopup() {
		((WComboBox) this.widget).hidePopup();
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XComboBoxElement#addItem(java.lang.String)
	 */
	public void addItem(String item) {
		((WComboBox) this.widget).addItem(item);
	}
	
    public void setSelectedItem(Object item) {
    	((WComboBox) this.widget).setSelectedItem(item);
    }

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XComboBoxElement#removeAllItems()
	 */
	public void removeAllItems() {
		((WComboBox) this.widget).removeAllItems();
	}
	
    private boolean hasActionListener = false;

    private void ensureActionListener() {
    	synchronized(this) {
    		if(!this.hasActionListener) {
    			((WComboBox) this.widget).addActionListener(new ActionListener() {
    				public void actionPerformed(ActionEvent event) {
    					String cmd = event.getActionCommand();
    					if(logger.isLoggable(Level.INFO))logger.info("ensureActionListener(): event=" + event);
    					if("comboBoxEdited".equals(cmd)) {
   							ComboBoxElement.this.fireXamjEvent(AbstractXamjEvent.create(ComboBoxElement.this, "on-edited"));
    					}
    					else if("comboBoxChanged".equals(cmd)) {
    						ComboBoxElement.this.fireXamjEvent(AbstractXamjEvent.create(ComboBoxElement.this, "on-change"));
    					}
    					else {
    						logger.warning("ensureActionListener(): Unknown command=" + cmd);
    					}
    				}
    			});
    			this.hasActionListener = true;
    		}    	
    	}
    }
    
    private boolean hasChangeListener = false;
    
    private void ensureChangeListener() {
    	synchronized(this) {
    		if(!this.hasChangeListener) {
    			((WComboBox) this.widget).addChangeListener(new ChangeListener() {
    				public void stateChanged(ChangeEvent event) {
   						ComboBoxElement.this.fireXamjEvent(AbstractXamjEvent.create(ComboBoxElement.this, "on-text-change"));
    				}
    			});
    			this.hasChangeListener = true;
    		}    	
    	}
    }
    
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.AbstractElement#checkEventElement(org.xamjwg.dom.XEventElement)
	 */
    protected void checkEventElement(XEventElement eventElement) {
    	String eventName = eventElement.getNodeName();
    	if("on-edited".equals(eventName)) {
    		this.ensureActionListener();
    		return;
    	}
    	if("on-change".equals(eventName)) {
    		this.ensureActionListener();
    		return;
    	}
    	if("on-text-change".equals(eventName)) {
    		this.ensureChangeListener();
    		return;				
    	}
    	if("on-before-popup-visible".equals(eventName)) {
    		// Passes
    		return;
    	}
    	if("on-before-popup-invisible".equals(eventName)) {
    		// Passes
    		return;
    	}
    	if("on-popup-canceled".equals(eventName)) {
    		// Passes
    		return;
    	}
    	super.checkEventElement(eventElement);
    }
    
    
}
