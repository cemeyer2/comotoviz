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
 * Created on Apr 15, 2005
 */
package org.xamjwg.ext.gui;

import javax.swing.JSplitPane;

import org.xamjwg.dom.XNode;
import org.xamjwg.dom.XSplitBoxElement;

/**
 * @author J. H. S.
 */
public class SplitBoxElement extends BaseWidgetElement implements
		XSplitBoxElement {
	/**
	 * @param name
	 */
	public SplitBoxElement(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.BaseWidgetElement#createWidget()
	 */
	protected Widget createWidget() {
		return new WSplitBox();
	}
	
	private BaseWidgetElement leftChild = null;
	private BaseWidgetElement rightChild = null;
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#appendChild(org.xamjwg.dom.XNode)
	 */
	public void appendChild(XNode node) {
		if(node instanceof BaseWidgetElement) {
			BaseWidgetElement we = (BaseWidgetElement) node;
			if(this.leftChild == null) {
				this.leftChild = we;
				((WSplitBox) this.widget).setLeftComponent(we.widget);
			}
			else if(this.rightChild == null) {
				this.rightChild = we;
				((WSplitBox) this.widget).setRightComponent(we.widget);				
			}
			else {
				throw new IllegalArgumentException("Widget element named '" + this.getNodeName() + "' already has two widget children");
			}
		}
		// Note: Don't add children as widgets.
		this.appendChildImpl(node);
	}
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.AbstractElement#removingNode(org.xamjwg.dom.XNode)
	 */
	protected void removingNode(XNode node) {
		super.removingNode(node);
		if(node == this.leftChild) {
			this.leftChild = null;
			((WSplitBox) this.widget).setLeftComponent(null);
		}
		else if(node == this.rightChild) {
			this.rightChild = null;
			((WSplitBox) this.widget).setRightComponent(null);			
		}
	}
	
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#getElementValue()
	 */
	public Object getElementValue() {
		return this;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XSplitBoxElement#getDividerLocation()
	 */
	public int getDividerLocation() {
		return ((JSplitPane) ((WSplitBox) this.widget).component).getDividerLocation();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XSplitBoxElement#setDividerLocation(int)
	 */
	public void setDividerLocation(int value) {
		int oldValue = this.getDividerLocation();
		if(value != oldValue) {
			((JSplitPane) ((WSplitBox) this.widget).component).setDividerLocation(value);
			this.firePropertyChange("dividerLocation", Integer.valueOf(oldValue), Integer.valueOf(value));
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XSplitBoxElement#setProportionalDividerLocation(double)
	 */
	public void setProportionalDividerLocation(double plocation) {
		int oldValue = this.getDividerLocation();
		((JSplitPane) ((WSplitBox) this.widget).component).setDividerLocation(plocation);
		int newValue = this.getDividerLocation();
		if(newValue != oldValue) {
			this.firePropertyChange("dividerLocation", Integer.valueOf(oldValue), Integer.valueOf(newValue));
		}
	}
}
