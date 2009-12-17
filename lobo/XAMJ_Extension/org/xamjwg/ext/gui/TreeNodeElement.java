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

import java.beans.*;
import java.util.Enumeration;
import javax.swing.tree.*;
import javax.swing.event.*;

import org.lobobrowser.util.CollectionUtilities;
import org.xamjwg.dom.*;
import java.util.logging.*;

/**
 * @author J. H. S.
 */
public class TreeNodeElement extends LabelElement implements LocalTreeNodeElement {
	private static final Logger logger = Logger.getLogger(TreeNodeElement.class.getName());
	
	/**
	 * @param name
	 */
	public TreeNodeElement(String name) {
		super(name);
		this.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				TreeBoxElement tbe = getTreeBox();
				if(tbe != null) {
					tbe.fireNodeChange(TreeNodeElement.this);
				}
			}
		});
	}

	protected TreeBoxElement getTreeBox() {
		//TODO: What if part of a model?
		XNode parent = this;
		while(parent != null && !(parent instanceof TreeBoxElement)) {
			parent = parent.getParent();
			if(logger.isLoggable(Level.INFO))logger.info("getTreeBox(): parent=" + parent);
		}
		return (TreeBoxElement) parent;
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.LabelElement#didChange()
	 */
	protected void didChange() {
		TreeBoxElement tbe = this.getTreeBox();
		if(tbe != null) {
			//TODO: Horrible workaround for Swing bug that prevents it
			//from adjusting label size as icon is added.
			((WTreeBox) tbe.widget).setCellRenderer(new LocalTreeCellRenderer());
		}
	}
	
	public TreeModelEvent getTreeModelEvent(Object source) {
		//TODO: What if part of a model?
		
		XNode parent = this.getParent();
		if(!(parent instanceof LocalTreeNodeElement)) {
			throw new IllegalStateException("Invalid parent for change/insert/delete event: " + parent);
		} 
		LocalTreeNodeElement parentNode = (LocalTreeNodeElement) parent;
		return new TreeModelEvent(source, this.getParentTreePath(), new int[] { parentNode.getIndexOfChild(this) }, new Object[] { this } );
	}
	
	public TreePath getParentTreePath() {
		XNode parent = this.getParent();
		if(parent != null && !(parent instanceof LocalTreeNodeElement)) {
			throw new IllegalStateException("Invalid parent for path: " + parent);
		}
		if(parent == null) {
			return null;
		}
		else {
			return ((LocalTreeNodeElement) parent).getTreePath();
		}
	}
	
	public TreePath getTreePath() {
		TreePath parentTreePath = this.getParentTreePath();
		if(parentTreePath == null) {
			Object[] path = new Object[1];
			path[0] = this;
			return new TreePath(path);			
		}
		else {
			Object[] parentPath = parentTreePath.getPath();
			Object[] path = new Object[parentPath.length + 1];
			System.arraycopy(parentPath, 0, path, 0, parentPath.length);
			path[parentPath.length] = this;
			return new TreePath(path);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#children()
	 */
	public Enumeration children() {
		return CollectionUtilities.getIteratorEnumeration(this.getChildNodes(new TreeNodeFilter()));
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getAllowsChildren()
	 */
	public boolean getAllowsChildren() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getChildAt(int)
	 */
	public LocalTreeNodeElement getChild(int childIndex) {
		java.util.Iterator i = this.getChildNodes(new TreeNodeFilter());
		int count = 0;
		while(i.hasNext()) {
			LocalTreeNodeElement node = (LocalTreeNodeElement) i.next();
			if(count == childIndex) {
				return node;
			}
			count++;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getChildCount()
	 */
	public int getChildCount() {
		java.util.Iterator i = this.getChildNodes(new TreeNodeFilter());
		int count = 0;
		while(i.hasNext()) {
			i.next();
			count++;
		}
		return count;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getIndex(javax.swing.tree.TreeNode)
	 */
	public int getIndexOfChild(LocalTreeNodeElement node) {
		java.util.Iterator i = this.getChildNodes(new TreeNodeFilter());
		int count = 0;
		while(i.hasNext()) {
			LocalTreeNodeElement childNode = (LocalTreeNodeElement) i.next();
			if(node == childNode) {
				return count;
			}
			count++;
		}
		return -1;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#isLeaf()
	 */
	public boolean isLeaf() {
		return this.getChildCount() == 0;
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTreeNodeElement#collapse()
	 */
	public void collapse() {
		((WTreeBox) this.getTreeBox().widget).collapsePath(this.getTreePath());
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTreeNodeElement#expand()
	 */
	public void expand() {
		((WTreeBox) this.getTreeBox().widget).expandPath(this.getTreePath());
	}
	
	public boolean isExpanded() {
		return ((WTreeBox) this.getTreeBox().widget).isExpanded(this.getTreePath());		
	}
	
	public boolean isCollapsed() {
		return ((WTreeBox) this.getTreeBox().widget).isCollapsed(this.getTreePath());		
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTreeNodeElement#select(boolean)
	 */
	public void select(boolean unique) {
		TreeBoxElement treeBoxElement = this.getTreeBox();
		if(treeBoxElement == null) {
			throw new IllegalStateException("tree-node appears to be detached");
		}
		WTreeBox treeBox = (WTreeBox) treeBoxElement.widget;
		if(unique) {
			treeBox.setSelectionPath(this.getTreePath());
		}
		else {
			treeBox.addSelectionPath(this.getTreePath());
		}
	}	
}
