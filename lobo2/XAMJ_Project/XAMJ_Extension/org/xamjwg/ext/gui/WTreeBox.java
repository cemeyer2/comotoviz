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
import javax.swing.tree.*;
import javax.swing.event.*;
import java.util.*;
import org.xamjwg.dom.*;

/**
 * @author J. H. S.
 */
public class WTreeBox extends WWrapper {
	/**
	 * 
	 */
	public WTreeBox() {
		super(true);
	}
	
	public java.awt.Dimension getPreferredSize() {
		// TODO: needed for split-box
		return new Dimension(150, 400);
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.Widget#setWidgetElement(net.sourceforge.xamj.domimpl.gui.BaseWidgetElement)
	 */
	public void setWidgetElement(BaseWidgetElement element) {
		super.setWidgetElement(element);
	}

	public void setTreeModel(TreeModel model) {
		((JTree) this.component).setModel(model);
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.WWrapper#createComponent()
	 */
	protected Component createComponent() {
		JTree tree = new JTree();
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		tree.setCellRenderer(new LocalTreeCellRenderer());
		tree.setSelectionModel(new DefaultTreeSelectionModel());
		return tree;
	}
	
	public void addTreeExpansionListener(TreeExpansionListener tel) {
		((JTree) this.component).addTreeExpansionListener(tel);
	}
	 
	public void addTreeSelectionListener(TreeSelectionListener tsl) { 
		((JTree) this.component).addTreeSelectionListener(tsl);
	}
	
	public void expandPath(TreePath path) {
		((JTree) this.component).expandPath(path);
	}
	
	public void expandRow(int row) {
		((JTree) this.component).expandRow(row);
	}
	
	public void collapsePath(TreePath path) {
		((JTree) this.component).collapsePath(path);
	}

	public boolean isExpanded(TreePath path) {
		return ((JTree) this.component).isExpanded(path);
	}
	 
    public boolean isCollapsed(TreePath path) {
    	return ((JTree) this.component).isCollapsed(path);
    }	 
	
	public void addSelectionPath(TreePath path) {
		((JTree) this.component).addSelectionPath(path);	 	
	}
	 
	public void setSelectionPath(TreePath path) {
		((JTree) this.component).setSelectionPath(path);	 		 	
	}
	
	public void setCellRenderer(TreeCellRenderer treeCellRenderer) {
		((JTree) this.component).setCellRenderer(treeCellRenderer);
	}
	
	public Collection getBranchPaths() {
		TreeModel treeModel = ((JTree) this.component).getModel();
		LocalTreeNodeElement rootNode = (LocalTreeNodeElement) treeModel.getRoot();
		return this.getBranchPaths(rootNode);
	}
	
	private Collection getBranchPaths(LocalTreeNodeElement node) {
		if(node.isLeaf()) {
			return Collections.EMPTY_LIST;
		}
		Collection paths = new LinkedList();
		Iterator i = node.getChildNodes(new org.xamjwg.dom.XamjNodeFilter() {
			public boolean accept(XNode node) {
				return node instanceof TreeNodeElement;
			}
		});
		while(i.hasNext()) {
			TreeNodeElement tne = (TreeNodeElement) i.next();
			paths.addAll(this.getBranchPaths(tne));
		}
		if(paths.isEmpty()) {
			paths.add(node.getTreePath());
		}
		return paths;
	}
	
	public void expandAll() {
		Collection paths = this.getBranchPaths();
		Iterator i = paths.iterator();
		while(i.hasNext()) {
			TreePath path = (TreePath) i.next();
			this.expandPath(path);
		}
	}
	
	public void treeDidChange() {
		((JTree) this.component).treeDidChange();
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
