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
 * Created on Mar 20, 2005
 */
package org.xamjwg.ext.markup;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.*;

import org.xamjwg.dom.*;
import org.xamjwg.event.*;
import org.xamjwg.ext.domimpl.BaseElement;
import org.xamjwg.ext.gui.Widget;

import java.util.logging.*;

/**
 * @author J. H. S.
 */
public abstract class BaseMarkupElement extends BaseElement implements XMarkupElement {
	private static final Logger logger = Logger.getLogger(BaseMarkupElement.class.getName());
	private Component containerWidget;
	protected final MarkupStyleElement style;
	
    public BaseMarkupElement(String name) {
        super(name);
        this.style = new MarkupStyleElement(this.getNodeName() + "$style");
        this.style.addPropertyChangeListener(new StylePropertyChangeListener());
    }

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMarkupElement#getStyle()
	 */
	public XMarkupStyleElement getStyle() {
		return this.style;
	}
	
	public void setContainerWidget(Component component) {
		this.containerWidget = component;
	}
	
	protected void refresh() {
		Component c = this.containerWidget;
		if(c instanceof Widget) {
			((Widget) c).refresh();
		}
		else {
			logger.warning("refresh(): Ignored for " + c);
		}
	}
	
	protected void repaint() {
		Component c = this.containerWidget;
		if(c instanceof Widget) {
			((Widget) c).repaint();
		}
		else {
			logger.warning("repaint(): Ignored for " + c);
		}
	}

	public XamjRenderState createRenderState(XamjRenderState prev) {
		return this.style.createRenderState(prev);
	}
	
	public void onMouseClick(java.awt.event.MouseEvent event, int x, int y) {
		if(this.hasListeners("on-click")) {
			this.fireXamjEvent(AbstractXamjEvent.create(this, "on-click"));
		}
		XElement parent = this.getParent();
		if(parent instanceof BaseMarkupElement) {
			((BaseMarkupElement) parent).onMouseClick(event, x, y);
		}
	}
	
	public void onMousePressed(java.awt.event.MouseEvent event, int x, int y) {
		if(this.hasListeners("on-click")) {
			this.style.setHighlight(true);
		}
		XElement parent = this.getParent();
		if(parent instanceof BaseMarkupElement) {
			((BaseMarkupElement) parent).onMousePressed(event, x, y);
		}
	}

	public void onMouseReleased(java.awt.event.MouseEvent event, int x, int y) {
		this.style.setHighlight(false);
		XElement parent = this.getParent();
		if(parent instanceof BaseMarkupElement) {
			((BaseMarkupElement) parent).onMousePressed(event, x, y);
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#getElementValue()
	 */
	public Object getElementValue() {
		return this;
	}
	
    private class StylePropertyChangeListener implements PropertyChangeListener {
        /* (non-Javadoc)
         * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
         */
        public void propertyChange(PropertyChangeEvent arg0) {
            // TODO Should not revalidate while rebuilding DOM
        	// TODO Should only repaint on some property changes
        	if(EventQueue.isDispatchThread()) {
        		String propertyName = arg0.getPropertyName();
        		if("highlight".equals(propertyName) || "color".equals(propertyName) || "backgroundColor".equals(propertyName)) {
        			BaseMarkupElement.this.repaint();
        		}
        		else {
        			BaseMarkupElement.this.refresh();        			
        		}
        	}
        }
	}
}
