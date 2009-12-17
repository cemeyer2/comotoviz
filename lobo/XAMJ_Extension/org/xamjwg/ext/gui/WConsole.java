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
 * Created on Jun 18, 2005
 */
package org.xamjwg.ext.gui;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import org.lobobrowser.gui.ConsoleModel;

/**
 * @author J. H. S.
 */
public class WConsole extends WTextBox {
	/**
	 * 
	 */
	public WConsole() {
		super();
		final JScrollPane sp = (JScrollPane) this.getComponent(0);
		final JScrollBar vsb = sp.getVerticalScrollBar();
		JTextComponent tc = ((JTextComponent) this.component);
		Document doc = ConsoleModel.getStandard();
		tc.setDocument(doc);
		tc.setEditable(false);
		doc.addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				java.awt.EventQueue.invokeLater(new Runnable() {
					public void run() {
						vsb.setValue(vsb.getMaximum());
					}
				});
			}
	         
			public void changedUpdate(DocumentEvent e)  { 
			}
			
			public void removeUpdate(DocumentEvent e) {
			}
		});
	}
}
