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
 * Created on Mar 5, 2005
 */
package org.xamjwg.ext.gui;

import org.lobobrowser.util.*;
import org.xamjwg.dom.*;


import java.awt.event.KeyEvent;
import java.beans.*;
import org.xamjwg.event.*;
import org.xamjwg.ext.domimpl.*;

/**
 * @author J. H. S.
 */
public abstract class BaseWidgetElement extends BaseElement implements XWidgetElement {
    protected final Widget widget;
    
    public BaseWidgetElement(String name) {
        super(name);
        this.widget = this.createWidget();
        this.init(true);
    }
    
    protected BaseWidgetElement(String name, Widget widget, boolean isWidgetElement) {
        super(name);
        this.widget = widget;
        this.init(isWidgetElement);
    }
    
    private void init(boolean isWidgetElement) {
    	if(isWidgetElement) {
    		this.widget.setWidgetElement(this);
            this.widget.addPropertyChangeListener("visible", new PropertyChangeListener() {
            	public void propertyChange(PropertyChangeEvent evt) {
            		Object newValue = evt.getNewValue();
            		//if(logger.isLoggable(Level.INFO))logger.info("BaseWidgetElement(): propertyChange(): neverShown=" + neverShown + ",newValue=" + newValue + ",event=" + evt);
            		firePropertyChange("visible", evt.getOldValue(), newValue);
            	}
            });    	
    	}
    }
    
    void onFirstShown() {
		this.fireXamjEvent(AbstractXamjEvent.create(BaseWidgetElement.this, "on-first-shown"));
    }
    
    protected void onPaint(java.awt.Graphics g, java.awt.Component c, java.awt.Insets insets) {
    	// nop - override
    }
    
    /* (non-Javadoc)
     * @see org.xamjwg.dom.XElement#appendChild(org.xamjwg.dom.XNode)
     */
    public void appendChild(XNode node) {
        if(node instanceof BaseWidgetElement) {
        	Widget childWidget = ((BaseWidgetElement) node).widget;  
        	//if(logger.isLoggable(Level.INFO))logger.info("appendChild(): Adding widget " + childWidget + " to " + this.widget);
            this.widget.add(childWidget);
            super.appendChild(node);
            if(java.awt.EventQueue.isDispatchThread()) {
            	this.widget.refresh();
            }
        }
        else {
         	super.appendChild(node);
        }
    }

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.AbstractElement#removingNode(org.xamjwg.dom.XNode)
	 */
	protected void removingNode(XNode node) {
		super.removingNode(node);
		if(node instanceof BaseWidgetElement) {
			this.widget.remove(((BaseWidgetElement) node).widget);
            if(java.awt.EventQueue.isDispatchThread()) {
            	this.widget.refresh();
            }
		}
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.BaseNode#setParent(org.xamjwg.dom.XElement)
	 */
	public void setParent(XElement parent) {
		if(!(parent instanceof XWidgetElement) && 
		   !(parent instanceof XMarkupElement) &&
		   !(parent instanceof XModelElement) &&
		   !(parent instanceof RootElement) &&
		   parent != null) {
			throw new IllegalArgumentException("Widget named '" + this.getNodeName() + "' may not be a child of '" + parent.getNodeName() + "'");
		}
		super.setParent(parent);
	}
	
    public XWidgetStyleElement getStyle() {
        return this.getWidget().getStyle();
    }
    
	public final Widget getWidget() {
	    return this.widget;
	}
	
	protected abstract Widget createWidget();	

    /* (non-Javadoc)
     * @see org.xamjwg.dom.XWidgetElement#getTooltip()
     */
    public String getToolTip() {
        return this.widget.getToolTipText();
    }

    /* (non-Javadoc)
     * @see org.xamjwg.dom.XWidgetElement#isEnabled()
     */
    public boolean isEnabled() {
        return this.widget.isAllEnabled();
    }

    /* (non-Javadoc)
     * @see org.xamjwg.dom.XWidgetElement#isVisible()
     */
    public boolean isVisible() {
        return this.widget.isVisible();
    }
    
    /* (non-Javadoc)
     * @see org.xamjwg.dom.XWidgetElement#setEnabled(boolean)
     */
    public void setEnabled(boolean value) {
        boolean oldValue = this.widget.isAllEnabled();
        if(oldValue != value) {
            this.widget.setAllEnabled(value);
            this.firePropertyChange("enabled", Boolean.valueOf(oldValue), Boolean.valueOf(value));
        }        
    }
    
    /* (non-Javadoc)
     * @see org.xamjwg.dom.XWidgetElement#setTooltip(java.lang.String)
     */
    public void setToolTip(String value) {
        String oldValue = this.widget.getToolTipText();
        if(!Objects.equals(value, oldValue)) {
            this.widget.setToolTipText(value);
            this.firePropertyChange("tooltip", Boolean.valueOf(oldValue), Boolean.valueOf(value));
        }        
    }
    
    /* (non-Javadoc)
     * @see org.xamjwg.dom.XWidgetElement#setVisible(boolean)
     */
    public void setVisible(boolean value) {
        boolean oldValue = this.widget.isVisible();
        if(oldValue != value) {
            this.widget.setVisible(value);
            // PropertyChange fired elsewhere
        }        
    }

    public boolean isShowing() {
    	//TODO: If exposed, need to fire property change
    	return this.widget.isShowing();
    }
    
    private boolean hasKeyListener = false;
    private void ensureKeyListener() {
    	synchronized(this) {
    		if(!this.hasKeyListener) {
    			this.widget.addKeyListener(new LocalKeyListener());
    			this.hasKeyListener = true;
    		}
    	}
    }
    
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.AbstractElement#checkEventElement(org.xamjwg.dom.XEventElement)
	 */
	protected void checkEventElement(XEventElement eventElement) {
		String eventName = eventElement.getNodeName();
		if("on-first-shown".equals(eventName)) {
			// passes
		}
		else if("on-key-pressed".equals(eventName) || "on-key-released".equals(eventName) || "on-key-typed".equals(eventName)) {
			this.ensureKeyListener();
		}
		else {
			super.checkEventElement(eventElement);
		}
	}
	
	
//	/* (non-Javadoc)
//	 * @see org.xamjwg.dom.XWidgetElement#replaceWidgetElements(java.lang.String)
//	 */
//	public void replaceWidgetElements(String url) throws MalformedURLException {
//		this.replaceChildNodes(url, new XamjWidgetElementFilter());
//	}
//		
//	/* (non-Javadoc)
//	 * @see org.xamjwg.dom.XElement#replaceChildNodes(java.lang.String, org.xamjwg.dom.NodeFilter)
//	 */
//	public void replaceChildNodes(String url, XamjNodeFilter filter)
//			throws MalformedURLException {
//		//TODO Modifications only in GUI thread?
//		//TODO Revalidate on all DOM modifications?
//		super.replaceChildNodes(url, filter);
//		this.widget.refresh();
//	}
//	
//	/* (non-Javadoc)
//	 * @see org.xamjwg.dom.XElement#replaceChildNodes(java.lang.String)
//	 */
//	public void replaceChildNodes(String url) throws MalformedURLException {
//		super.replaceChildNodes(url);
//		if(logger.isLoggable(Level.INFO))logger.info("replaceChildNodes(): Revalidating... bounds=" + this.widget.getBounds());
//		this.widget.refresh();
//	}
//
//	/* (non-Javadoc)
//	 * @see org.xamjwg.dom.XElement#replaceChildNodes(org.xamjwg.dom.XElement, org.xamjwg.dom.NodeFilter)
//	 */
//	public void replaceChildNodes(XElement source, XamjNodeFilter filter) {
//		super.replaceChildNodes(source, filter);
//		this.widget.refresh();
//	}
//	
//	/* (non-Javadoc)
//	 * @see org.xamjwg.dom.XElement#replaceChildNodes(org.xamjwg.dom.XElement)
//	 */
//	public void replaceChildNodes(XElement source) {
//		super.replaceChildNodes(source);
//		this.widget.refresh();
//	}
//	
	private class LocalKeyListener implements java.awt.event.KeyListener {
		/* (non-Javadoc)
		 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
		 */
		public void keyPressed(KeyEvent e) {
			BaseWidgetElement.this.fireXamjEvent(AbstractXamjEvent.createKeyEvent(BaseWidgetElement.this, "on-key-pressed", e.getKeyChar(), e.getKeyCode()));
		}

		/* (non-Javadoc)
		 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
		 */
		public void keyReleased(KeyEvent e) {
			BaseWidgetElement.this.fireXamjEvent(AbstractXamjEvent.createKeyEvent(BaseWidgetElement.this, "on-key-released", e.getKeyChar(), e.getKeyCode()));
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
		 */
		public void keyTyped(KeyEvent e) {
			BaseWidgetElement.this.fireXamjEvent(AbstractXamjEvent.createKeyEvent(BaseWidgetElement.this, "on-key-typed", e.getKeyChar(), e.getKeyCode()));
		}
}
}
