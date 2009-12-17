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
 * Created on Jun 11, 2005
 */
package org.xamjwg.ext.gui;

import java.awt.*;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

//import java.util.logging.*;

/**
 * @author J. H. S.
 */
public class LocalTreeCellRenderer implements TreeCellRenderer {
	//private static Logger logger = Logger.getLogger(LocalTreeCellRenderer.class);
	
	/**
	 * 
	 */
	public LocalTreeCellRenderer() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		if(value instanceof TreeNodeElement) {
			WLabel widget = (WLabel) ((TreeNodeElement) value).getWidget();
			if(selected) {
				widget.setBackground(Color.black);
				widget.setForeground(Color.white);
			}
			else {
				widget.setBackground(Color.white);
				widget.setForeground(Color.black);
			}
			//if(logger.isLoggable(Level.INFO))logger.info("getTreeCellRendererComponent(): selected=" + selected + ",row=" + row + ",node=" + value + ",widget.text=" + widget.getText() + ",widget=" + widget);
			return widget;
		} 
		else {
			return new javax.swing.JLabel("");
		}
	}
}
