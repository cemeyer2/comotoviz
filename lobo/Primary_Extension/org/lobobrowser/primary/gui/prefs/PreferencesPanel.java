/*
    GNU GENERAL PUBLIC LICENSE
    Copyright (C) 2006 The Lobo Project

    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public
    License as published by the Free Software Foundation; either
    verion 2 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General Public License for more details.

    You should have received a copy of the GNU General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

    Contact info: lobochief@users.sourceforge.net
*/
package org.lobobrowser.primary.gui.prefs;

import javax.swing.*;
import javax.swing.border.*;

import org.lobobrowser.primary.gui.ValidationException;
import org.lobobrowser.util.gui.*;

public class PreferencesPanel extends JPanel {
	private AbstractSettingsUI ui;
	
	public PreferencesPanel() {
		this.setLayout(WrapperLayout.getInstance());
		this.setBorder(new CompoundBorder(new EtchedBorder(), new javax.swing.border.EmptyBorder(8, 8, 8, 8)));
	}
	
	public boolean save() {
		AbstractSettingsUI ui = this.ui;
		if(ui != null) {
			try {
				ui.save();
			} catch(ValidationException ve) {
				JOptionPane.showMessageDialog(this, ve.getMessage());
				return false;
			}
		}
		return true;
	}
	
	public void restoreDefaults() {
		AbstractSettingsUI ui = this.ui;
		if(ui != null) {
			ui.restoreDefaults();
		}
	}
		
	public void setSettingsUI(AbstractSettingsUI ui) {
		this.ui = ui;
		this.removeAll();
		if(ui != null) {
			this.add(ui);
		}
		this.revalidate();
	}
}
