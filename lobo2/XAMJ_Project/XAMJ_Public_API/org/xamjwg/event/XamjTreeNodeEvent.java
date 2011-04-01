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
 * Created on Jul 3, 2005
 */
package org.xamjwg.event;

import org.xamjwg.dom.XTreeNodeElement;

/**
 * A tree selection event.
 * @author J. H. S.
 */
public class XamjTreeNodeEvent extends AbstractXamjEvent {
	private final XTreeNodeElement[] treeNodes;

	/**
	 * Constructs a tree selection event.
	 * @param source The object firing the event.
	 * @param eventName The name of the event.
	 * @param nodes The tree nodes involved.
	 */
	public XamjTreeNodeEvent(Object source, String eventName, XTreeNodeElement[] nodes) {
		super(source, eventName);
		this.treeNodes = nodes;
	}

	/**
	 * Constructs a tree selection event.
	 * @param source The object firing the event.
	 * @param eventName The name of the event.
	 * @param node The tree node involved.
	 */
	public XamjTreeNodeEvent(Object source, String eventName, XTreeNodeElement node) {
		super(source, eventName);
		this.treeNodes = new XTreeNodeElement[] { node };
	}

	/**
	 * Gets the tree nodes involved in the event.
	 */
	public XTreeNodeElement[] getTreeNodes() {
		return this.treeNodes;
	}
	
	/**
	 * Gets the first tree node involved in the event.
	 */
	public XTreeNodeElement getTreeNode() {
		XTreeNodeElement[] treeNodes = this.treeNodes;
		if(treeNodes != null && treeNodes.length > 0) {
			return treeNodes[0];
		}
		else {
			return null;
		}
	}
}
