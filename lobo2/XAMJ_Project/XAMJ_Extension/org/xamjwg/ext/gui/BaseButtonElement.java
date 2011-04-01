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


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.*;
import java.util.logging.*;

import org.lobobrowser.async.AsyncResult;
import org.lobobrowser.async.AsyncResultEvent;
import org.lobobrowser.async.AsyncResultListener;
import org.lobobrowser.clientlet.*;
import org.lobobrowser.request.RequestEngine;
import org.lobobrowser.util.*;
import org.xamjwg.dom.XButtonGroupElement;
import org.xamjwg.dom.XamjEvaluatable;
import org.xamjwg.dom.XButtonElement;
import org.xamjwg.dom.XEventElement;
import org.xamjwg.event.*;
import org.xamjwg.ext.domimpl.*;


/**
 * @author J. H. S.
 */
public abstract class BaseButtonElement extends BaseWidgetWithTextElement implements XButtonElement {
	private static final Logger logger = Logger.getLogger(BaseButtonElement.class.getName());

	/**
     * @param name
     */
    public BaseButtonElement(String name) {
        super(name);
        ((WAbstractButton) this.widget).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	try {
            		BaseButtonElement.this.onClick(e);
            	} catch(Exception err) {
            		logger.log(Level.SEVERE, "actionPerformed()", err);
            	}
            }
        });
		((WAbstractButton) this.widget).addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				updateState(getState());
			}
		});
    }

    protected void onClick(ActionEvent e) throws Exception {
        BaseButtonElement.this.fireXamjEvent(AbstractXamjEvent.create(BaseButtonElement.this, "on-click"));
        XamjEvaluatable href = this.href;
        if(href != null) {
        	String relUrl = (String) href.evaluate();
        	if(relUrl != null) {
        		this.document.getClientletContext().navigate(relUrl);
        	}
        }
    }
    
    /* (non-Javadoc)
     * @see net.sourceforge.xamj.domimpl.gui.BaseWidgetElement#createWidget()
     */
    protected Widget createWidget() {
        return createAbstractButton();
    }
    
    protected abstract WAbstractButton createAbstractButton();

	public boolean getState() {
		return ((WAbstractButton) this.widget).isSelected();
	}
	
	public void setState(boolean value) {
		boolean oldValue = this.getState();
		if(oldValue != value) {
			((WAbstractButton) this.widget).setSelected(value);
			// Property change handled otherwise
		}
	}

	private boolean stateCopy = false;
	
	private void updateState(boolean value) {
		boolean oldValue = this.stateCopy;
		if(oldValue != value) {
			this.stateCopy = value;
			this.firePropertyChange("state", new Boolean(oldValue), new Boolean(value));
		}		
	}
	
    private String icon;
    
    public void setIcon(final String value) {	
        String oldValue = this.icon;
        if(!Objects.equals(value, oldValue)) {
        	try {
        		AsyncResult<byte[]> ar = RequestEngine.getInstance().loadBytesAsync(value);
        		ar.addResultListener(new AsyncResultListener<byte[]>() {
					public void exceptionReceived(AsyncResultEvent<Throwable> event) {
        				logger.log(Level.WARNING, "setIcon(): Unable to retrieve image: " + value, (Throwable) event.getResult());
					}

					public void resultReceived(AsyncResultEvent<byte[]> event) {
        				byte[] contentBytes = event.getResult();
        				Icon icon = new ImageIcon(contentBytes);
        				((WAbstractButton) BaseButtonElement.this.widget).setIcon(icon);
					}
        			
        		});
        		this.firePropertyChange("icon", oldValue, value);
        	} catch(java.net.MalformedURLException mfu) {
        		throw new IllegalArgumentException("Malformed URL: " + value);
        	}
        }   
    }

    public String getIcon() {
    	return this.icon;
    }
    
    private String disabledIcon;
    
    public void setDisabledIcon(final String value) {	
        String oldValue = this.disabledIcon;
        if(!Objects.equals(value, oldValue)) {
        	try {
        		AsyncResult<byte[]> ar = RequestEngine.getInstance().loadBytesAsync(value);
        		ar.addResultListener(new AsyncResultListener<byte[]>() {
					public void exceptionReceived(AsyncResultEvent<Throwable> event) {
        				logger.log(Level.WARNING, "setDisabledIcon(): Unable to retrieve image: " + value, (Throwable) event.getResult());
					}

					public void resultReceived(AsyncResultEvent<byte[]> event) {
        				byte[] contentBytes = event.getResult();
        				Icon icon = new ImageIcon(contentBytes);
        				((WAbstractButton) BaseButtonElement.this.widget).setDisabledIcon(icon);
					}
        			
        		});
        		this.firePropertyChange("disabledIcon", oldValue, value);
        	} catch(java.net.MalformedURLException mfu) {
        		throw new IllegalArgumentException("Malformed URL: " + value);
        	}
        }   
    }

    public String getDisabledIcon() {
    	return this.disabledIcon;
    }

    public void setText(String value) {
        String oldValue = ((WAbstractButton) this.widget).getText();
        if(!Objects.equals(value, oldValue)) {
            ((WAbstractButton) this.widget).setText(value);
            this.firePropertyChange("text", oldValue, value);
        }   
    }
    
    private XButtonGroupElement buttonGroup;
    
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XButtonElement#getButtonGroup()
	 */
	public XButtonGroupElement getGroup() {
		return this.buttonGroup;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XButtonElement#setButtonGroup(org.xamjwg.dom.XButtonGroupElement)
	 */
	public void setGroup(XButtonGroupElement value) {
		Object oldValue = this.buttonGroup;
		if(!Objects.equals(oldValue, value)) {
			if(value != null) {
				if(!(value instanceof ButtonGroupElement)) {
					throw new IllegalArgumentException("Invalid implementation of XButtonGroupElement");
				}
				ButtonGroupElement bge = (ButtonGroupElement) value;
				bge.add((AbstractButton) ((WAbstractButton) this.widget).component);
			}
			this.buttonGroup = value;
			this.firePropertyChange("buttonGroup", oldValue, value);
		}
	}
	
    public String getText() {
        return ((WAbstractButton) this.widget).getText();
    }
    
    private XamjEvaluatable href;
    
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XButtonElement#getHref()
	 */
	public XamjEvaluatable<String> getHref() {
		return this.href;
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XButtonElement#setHref(org.xamjwg.dom.XamjEvaluatable)
	 */
	public void setHref(XamjEvaluatable<String> value) {
		Object oldValue = this.href;
		if(!Objects.equals(value, oldValue)) {
			this.href = value;
			this.firePropertyChange("href", oldValue, value);
		}
	}
	
    public void doClick() {
        ((WAbstractButton) this.widget).doClick();
    }
    
	protected void checkEventElement(XEventElement eventElement) {
	    if("on-click".equals(eventElement.getNodeName())) {
	        // passes
	    }
	    else {
	        super.checkEventElement(eventElement);
	    }
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#getElementValue()
	 */
	public Object getElementValue() {
		return this.getText();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XButtonElement#doClick(int)
	 */
	public void doClick(int pressTime) {
        ((AbstractButton) ((WAbstractButton) this.widget).component).doClick(pressTime);	
	}
}
