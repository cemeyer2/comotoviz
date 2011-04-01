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
 * Created on Jul 30, 2005
 */
package org.xamjwg.ext.gui;

import org.xamjwg.dom.*;
import org.xamjwg.ext.domimpl.RootElement;

import java.util.*;

public class RowElement extends BaseWidgetElement implements XRowElement {
	private final ArrayList items = new ArrayList();
	
	public RowElement(String name) {
		super(name);
	}

	public RowElement(String name, Widget widget, boolean isWidgetElement) {
		super(name, widget, isWidgetElement);
	}

	@Override
	protected Widget createWidget() {
		//TODO: Inefficient, but can't return null.
		return new Widget();
	}

	private GridElement getGridElement() {
		Object parent = this.getParent();
		if(parent instanceof GridElement) {
			return (GridElement) parent;
		}
		return null;
	}
	
	public void select(boolean unique) {
		GridElement ge = this.getGridElement();
		if(ge != null) {
			ge.select(this, unique);			
		}
	}

	public boolean isSelected() {
		GridElement ge = this.getGridElement();
		if(ge != null) {
			return ge.isSelected(this);			
		}
		else {
			return false;
		}
	}

	public Object getElementValue() {
		return this.getId();
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.BaseWidgetElement#setParent(org.xamjwg.dom.XElement)
	 */
	@Override
	public void setParent(XElement parent) {
		if(!(parent instanceof XGridElement) &&
		   !(parent instanceof RootElement)) {
			throw new IllegalArgumentException("Element named '" + this.getNodeName() + "' must be a child of grid or xamj");
		}
		super.setParent(parent);
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.BaseWidgetElement#removingNode(org.xamjwg.dom.XNode)
	 */
	@Override
	protected void removingNode(XNode node) {
		if(node instanceof XGridItemElement) {
			synchronized(this.items) {
				this.items.remove(node);
			}
		}
		super.removingNode(node);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.BaseElement#addedNode(org.xamjwg.dom.XNode)
	 */
	@Override
	protected void addedNode(XNode node) {
		super.addedNode(node);
		if(node instanceof XGridItemElement) {
			synchronized(this.items) {
				this.items.add(node);
			}
		}
	}

	public Object getValueAt(int columnIndex) {
		try {
			XGridItemElement item;
			synchronized(this.items) {
				item = (XGridItemElement) this.items.get(columnIndex);
			}
			return item == null ? null : item.getText();
		} catch(IndexOutOfBoundsException iob) {
			return null;
		}
	}
}
