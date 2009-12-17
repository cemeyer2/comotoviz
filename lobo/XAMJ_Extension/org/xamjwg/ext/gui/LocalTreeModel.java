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

import javax.swing.event.*;
import javax.swing.tree.*;
import java.util.*;
//import java.util.logging.*;

/**
 * @author J. H. S.
 */
public class LocalTreeModel implements TreeModel {
	//private final Logger logger = Logger.getLogger(LocalTreeModel.class);
	private final LocalTreeNodeElement rootElement;
	private final Collection listeners = new LinkedList();
	
	public LocalTreeModel(LocalTreeNodeElement rootElement) {
		this.rootElement = rootElement;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	public void addTreeModelListener(TreeModelListener l) {
		synchronized(this.listeners) {
			this.listeners.add(l);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 */
	public Object getChild(Object parent, int index) {
		//if(logger.isLoggable(Level.INFO))logger.info("getChild(): parent=" + parent + ",index=" + index);
		return ((LocalTreeNodeElement) parent).getChild(index);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
	 */
	public int getChildCount(Object parent) {
		//if(logger.isLoggable(Level.INFO))logger.info("getChildCount(): parent=" + parent);
		return ((LocalTreeNodeElement) parent).getChildCount();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
	 */
	public int getIndexOfChild(Object parent, Object child) {
		//if(logger.isLoggable(Level.INFO))logger.info("getIndexOfChild(): parent=" + parent + ",child=" + child);
		return ((LocalTreeNodeElement) parent).getIndexOfChild((TreeNodeElement) child);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getRoot()
	 */
	public Object getRoot() {
		return this.rootElement;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
	 */
	public boolean isLeaf(Object node) {
		return ((LocalTreeNodeElement) node).isLeaf();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	public void removeTreeModelListener(TreeModelListener l) {
		synchronized(this.listeners) {
			this.listeners.remove(l);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
	 */
	public void valueForPathChanged(TreePath path, Object newValue) {
	}
	
	private TreeModelListener[] getListeners() {
		synchronized(this.listeners) {
			return (TreeModelListener[]) this.listeners.toArray(new TreeModelListener[0]);
		}
	}
	
	void fireInserted(LocalTreeNodeElement element) {
		TreeModelEvent event = element.getTreeModelEvent(this);
		TreeModelListener[] listener = this.getListeners();
		for(int i = 0; i < listener.length; i++) {
			listener[i].treeNodesInserted(event);
		}
	}

	void fireRemoved(LocalTreeNodeElement element) {
		TreeModelEvent event = element.getTreeModelEvent(this);
		TreeModelListener[] listener = this.getListeners();
		for(int i = 0; i < listener.length; i++) {
			listener[i].treeNodesRemoved(event);
		}
	}
	
	void fireStructureChange(LocalTreeNodeElement element) {
		TreePath path = element.getParentTreePath();
		TreeModelListener[] listener = this.getListeners();
		for(int i = 0; i < listener.length; i++) {
			listener[i].treeStructureChanged(new TreeModelEvent(this, path));
		}		
	}
	
	void fireChange(LocalTreeNodeElement element) {
		TreeModelEvent event = element.getTreeModelEvent(this);
		TreeModelListener[] listener = this.getListeners();
		for(int i = 0; i < listener.length; i++) {
			listener[i].treeNodesChanged(event);
		}				
	}
}
