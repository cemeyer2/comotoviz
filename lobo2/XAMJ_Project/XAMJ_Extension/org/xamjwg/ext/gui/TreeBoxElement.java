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
import org.xamjwg.dom.*;
import org.xamjwg.event.*;

/**
 * @author J. H. S.
 */
public class TreeBoxElement extends BaseWidgetElement implements XTreeBoxElement, LocalTreeNodeElement {
	private final LocalTreeModel model = new LocalTreeModel(this);
	
	/**
	 * @param name
	 */
	public TreeBoxElement(String name) {
		super(name);
		final WTreeBox treeBox = ((WTreeBox) this.widget);
		treeBox.setTreeModel(model);
		treeBox.addTreeSelectionListener(new LocalTreeSelectionListener());
		treeBox.addTreeExpansionListener(new LocalTreeExpansionListener());
		this.addEventListener("on-first-shown", new XamjListener() {
			public void execute(AbstractXamjEvent event) {
				treeBox.expandPath(new TreePath(TreeBoxElement.this));
			}
		});
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.BaseWidgetElement#createWidget()
	 */
	protected Widget createWidget() {
		return new WTreeBox();
	}
	
	protected LocalTreeNodeElement getRootNode() {
		return (LocalTreeNodeElement) this.model.getRoot();		
	}
	
	public void fireChange(LocalTreeNodeElement element) {
		this.model.fireChange(element);
	}

	public void fireStructureChange(LocalTreeNodeElement element) {
		this.model.fireStructureChange(element);
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTreeBoxElement#expandAll()
	 */
	public void expandAll() {
		((WTreeBox) this.widget).expandAll();
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.BaseElement#removingNode(org.xamjwg.dom.XNode)
	 */
	protected void removingNode(XNode node) {
		super.removingNode(node);
		if(node instanceof LocalTreeNodeElement) {
			this.model.fireRemoved((LocalTreeNodeElement) node);
		}
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.BaseElement#addedNode(org.xamjwg.dom.XNode)
	 */
	protected void addedNode(XNode node) {
		super.addedNode(node);
		if(node instanceof LocalTreeNodeElement) {
			this.model.fireInserted((LocalTreeNodeElement) node);
		}
	}
	
	protected void fireNodeChange(LocalTreeNodeElement node) {
		this.model.fireChange(node);
	}

	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.BaseElement#checkEventElement(org.xamjwg.dom.XEventElement)
	 */
	protected void checkEventElement(XEventElement eventElement) {
		String eventName = eventElement.getNodeName();
		if("on-selection-change".equals(eventName)) {
			// passes
		}
		else if("on-collapsed".equals(eventName)) {
			// passes
		}
		else if("on-expanded".equals(eventName)) {
			// passes
		}
		else {
			super.checkEventElement(eventElement);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTreeNodeElement#collapse()
	 */
	public void collapse() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTreeNodeElement#expand()
	 */
	public void expand() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTreeNodeElement#getDisabledIcon()
	 */
	public String getDisabledIcon() {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTreeNodeElement#getIcon()
	 */
	public String getIcon() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTreeNodeElement#getText()
	 */
	public String getText() {
		return "root";
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTreeNodeElement#select(boolean)
	 */
	public void select(boolean unique) {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTreeNodeElement#setDisabledIcon(java.lang.String)
	 */
	public void setDisabledIcon(String value) {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTreeNodeElement#setIcon(java.lang.String)
	 */
	public void setIcon(String value) {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XTreeNodeElement#setText(java.lang.String)
	 */
	public void setText(String value) {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.LocalTreeNodeElement#getIndexOfChild(net.sourceforge.xamj.domimpl.gui.LocalTreeNodeElement)
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
	 * @see net.sourceforge.xamj.domimpl.gui.LocalTreeNodeElement#getParentTreePath()
	 */
	public TreePath getParentTreePath() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.LocalTreeNodeElement#getTreeModelEvent(java.lang.Object)
	 */
	public TreeModelEvent getTreeModelEvent(Object source) {
		throw new UnsupportedOperationException("not expected to be called");
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.LocalTreeNodeElement#getTreePath()
	 */
	public TreePath getTreePath() {
		Object[] path = new Object[1];
		path[0] = this;
		return new TreePath(path);			
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.LocalTreeNodeElement#isLeaf()
	 */
	public boolean isLeaf() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.LocalTreeNodeElement#getChild(int)
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
	 * @see net.sourceforge.xamj.domimpl.gui.LocalTreeNodeElement#getChildCount()
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
	 * @see org.xamjwg.dom.XElement#getElementValue()
	 */
	public Object getElementValue() {
		return this;
	}
	
	private class LocalTreeSelectionListener implements TreeSelectionListener {
			/* (non-Javadoc)
		 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
		 */
		public void valueChanged(TreeSelectionEvent e) {
			TreePath[] paths = e.getPaths();
			XTreeNodeElement[] nodes = new XTreeNodeElement[paths.length];
			for(int i = 0; i < nodes.length; i++) {
				XTreeNodeElement node = (XTreeNodeElement) paths[i].getLastPathComponent();
				nodes[i] = node;
			}
			TreeBoxElement.this.fireXamjEvent(AbstractXamjEvent.createTreeNodeEvent(TreeBoxElement.this, "on-selection-change", nodes));
		}
	}
	
	private class LocalTreeExpansionListener implements TreeExpansionListener {
		/* (non-Javadoc)
		 * @see javax.swing.event.TreeExpansionListener#treeCollapsed(javax.swing.event.TreeExpansionEvent)
		 */
		public void treeCollapsed(TreeExpansionEvent event) {
			TreePath path = event.getPath();
			LocalTreeNodeElement node = (LocalTreeNodeElement) path.getLastPathComponent();
			TreeBoxElement.this.fireXamjEvent(AbstractXamjEvent.createTreeNodeEvent(TreeBoxElement.this, "on-collapsed", node));
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.event.TreeExpansionListener#treeExpanded(javax.swing.event.TreeExpansionEvent)
		 */
		public void treeExpanded(TreeExpansionEvent event) {
			TreePath path = event.getPath();
			LocalTreeNodeElement node = (LocalTreeNodeElement) path.getLastPathComponent();
			TreeBoxElement.this.fireXamjEvent(AbstractXamjEvent.createTreeNodeEvent(TreeBoxElement.this, "on-expanded", node));
		}
	}
}
