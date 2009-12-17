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

import javax.swing.*;
import java.util.*;

public class GridElement extends BaseWidgetElement implements XGridElement {
	private final ArrayList columns = new ArrayList();
	private final ArrayList rows = new ArrayList();
	private LocalTableModel model;
	
	public GridElement(String name) {
		super(name);
	}

	public GridElement(String name, Widget widget, boolean isWidgetElement) {
		super(name, widget, isWidgetElement);
	}

	public String getColumnName(int index) {
		try {
			synchronized(this.columns) {
				ColumnElement ce = (ColumnElement) this.columns.get(index);
				if(ce != null) {
					return ce.getText();
				}
				else {
					return "";
				}
			}
		} catch(IndexOutOfBoundsException iob) {
			return "";
		}
	}
	
	@Override
	protected Widget createWidget() {
		WGrid grid = new WGrid();
		JTable table = (JTable) grid.component;
		//table.addColumn(new TableColumn(0));
		LocalTableModel model = new LocalTableModel(this);
		this.model = model;
		table.setModel(model);
		//table.setColumnModel(new LocalTableColumnModel(this));
		return grid;
	}

	public Object getElementValue() {
		return this.getId();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XGridElement#clearSelection()
	 */
	public void clearSelection() {
		((JTable) ((WGrid) this.widget).component).clearSelection();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XGridElement#selectAll()
	 */
	public void selectAll() {
		((JTable) ((WGrid) this.widget).component).selectAll();
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.BaseElement#addedNode(org.xamjwg.dom.XNode)
	 */
	@Override
	protected void addedNode(XNode node) {
		super.addedNode(node);
		if(node instanceof ColumnElement) {
			synchronized(this.columns) {
				this.columns.add(node);
			}
			//TODO: Shouldn't fire this often, but it's 
			//needed for table to be shown right.
			this.model.fireTableStructureChanged();
		}
		if(node instanceof RowElement) {
			int rowIdx;
			synchronized(this.rows) {
				rowIdx = this.rows.size();
				this.rows.add(node);				
			}
			this.model.fireTableRowsInserted(rowIdx, rowIdx);
		}
	}
	
	
	@Override
	public void removingNode(XNode node) {
		super.removingNode(node);
		if(node instanceof ColumnElement) {
			synchronized(this.columns) {
				this.columns.remove(node);
			}
			if(java.awt.EventQueue.isDispatchThread()) {
				this.model.fireTableStructureChanged();
			}
		}
		if(node instanceof RowElement) {
			int rowIdx;
			synchronized(this.rows) {
				rowIdx = this.rows.indexOf(node);
				this.rows.remove(node);
			}
			if(rowIdx != -1) {
				this.model.fireTableRowsDeleted(rowIdx, rowIdx);
			}
		}
	}
	
	public int getRowCount() {
		if(this.rows == null) {
			return 0;
		}
		synchronized(this.rows) {
			return this.rows.size();
		}
	}

	public int getColumnCount() {
		if(this.columns == null) {
			return 0;
		}
		synchronized(this.columns) {
			return this.columns.size();
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		synchronized(this.rows) {
			try {
				RowElement row = (RowElement) this.rows.get(rowIndex);
				return row == null ? null : row.getValueAt(columnIndex);
			} catch(IndexOutOfBoundsException iob) {
				return null;
			}
		}
	}

	public void select(XRowElement row, boolean unique) {
		int idx;
		synchronized(this.rows) {
			idx = this.rows.indexOf(row);
		}
		if(idx != -1) {
			if(unique) {
				((JTable) ((WGrid) this.widget).component).getSelectionModel().setSelectionInterval(idx, idx);
			}
			else {
				((JTable) ((WGrid) this.widget).component).getSelectionModel().addSelectionInterval(idx, idx);
			}
		}
	}
	
	public boolean isSelected(XRowElement row) {
		int idx;
		synchronized(this.rows) {
			idx = this.rows.indexOf(row);
		}
		return idx == -1 ? null : ((JTable) ((WGrid) this.widget).component).getSelectionModel().isSelectedIndex(idx);
	}
}
