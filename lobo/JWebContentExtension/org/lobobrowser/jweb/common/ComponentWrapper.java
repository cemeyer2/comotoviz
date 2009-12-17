/*
    GNU GENERAL PUBLIC LICENSE
    Copyright (C) 2006 The Lobo Project

    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public
    License as published by the Free Software Foundation; either
    version 2 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General Public License for more details.

    You should have received a copy of the GNU General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

    Contact info: lobochief@users.sourceforge.net
*/
package org.lobobrowser.jweb.common;

import javax.swing.*;
import java.awt.*;

import org.lobobrowser.gui.*;
import org.lobobrowser.util.gui.CenterLayout;
import org.lobobrowser.util.gui.WrapperLayout;
import org.lobobrowser.clientlet.*;

public class ComponentWrapper extends JPanel {
	private final Component component;
	private final ClientletContext context;

	public ComponentWrapper(ClientletContext context, Component component, boolean usePreferred) {
		super();
		this.component = component;
		this.context = context;
		this.setOpaque(false);
		this.setLayout(usePreferred ? CenterLayout.getInstance() : WrapperLayout.getInstance());
		this.add(component);
	}

	@Override
	protected void processEvent(AWTEvent event) {
		// This does not work.
		ClientletContext prevContext = ClientletAccess.getCurrentClientletContext();
		ClientletAccess.setCurrentClientletContext(this.context);
		try {
			super.processEvent(event);
		} finally {
			ClientletAccess.setCurrentClientletContext(prevContext);
		}
	}
}
