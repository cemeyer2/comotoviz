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
 * Created on Jun 9, 2005
 */
package org.xamjwg.ext.gui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.table.*;

/**
 * @author J. H. S.
 */
public class WGrid extends WWrapper {
	/**
	 * 
	 */
	public WGrid() {
		super(true);
	}

	public java.awt.Dimension getPreferredSize() {
		// TODO: Needed?
		return new Dimension(150, 400);
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.Widget#setWidgetElement(net.sourceforge.xamj.domimpl.gui.BaseWidgetElement)
	 */
	public void setWidgetElement(BaseWidgetElement element) {
		super.setWidgetElement(element);
	}

	public void setTableModel(TableModel model) {
		((JTable) this.component).setModel(model);
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.WWrapper#createComponent()
	 */
	protected Component createComponent() {
		JTable table = new JTable();
		return table;
	}	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.Widget#getDefaultHeight()
	 */
	protected int getDefaultHeight() {
		return -1;
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.Widget#getDefaultWidth()
	 */
	protected int getDefaultWidth() {
		return -1;
	}
}
