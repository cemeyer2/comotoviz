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

import java.awt.*;
import javax.swing.*;
import java.util.logging.*;

/**
 * @author J. H. S.
 */
public class WSplitBox extends WWrapper {
	private static final Logger logger = Logger.getLogger(WSplitBox.class.getName());

	/**
	 * 
	 */
	public WSplitBox() {
		this(false);
	}

	/**
	 * @param scrollable
	 */
	public WSplitBox(boolean scrollable) {
		super(scrollable);
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.Widget#onStyleChange(java.lang.String)
	 */
	protected void onStyleChange(String propertyName) {
		if("orientation".equals(propertyName)) {
			String orientation = this.style.getOrientation();
			if("vertical".equals(orientation)) {
				setOrientation(JSplitPane.VERTICAL_SPLIT);
			}
			else if("horizontal".equals(orientation)) {
				setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			}
			else {
				logger.log(Level.SEVERE, "propertyChange(): Unknown orientation value: " + orientation);						
			}
		}
		else {
			super.onStyleChange(propertyName);
		}
	}
	
	public void setLayout(LayoutManager lm) {
		if(lm instanceof BoxStyleLayout) {
			throw new UnsupportedOperationException("unexpected");
		}
		super.setLayout(lm);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.WWrapper#createComponent()
	 */
	protected Component createComponent() {
		JSplitPane splitPane = new JSplitPane();
		return splitPane;
	}
	
	public void setLeftComponent(Component c) {
		((JSplitPane) this.component).setLeftComponent(c);
	}

	public void setRightComponent(Component c) {
		((JSplitPane) this.component).setRightComponent(c);
	}
	
	public void setOrientation(int orientation) {
		((JSplitPane) this.component).setOrientation(orientation);
	}
	
	public int getDefaultWidth() {
		return -1;
	}
	
	public int getDefaultHeight() {
		return -1;
	}	
}
