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
package org.lobobrowser.jweb.ext;

import org.lobobrowser.ua.*;
import org.lobobrowser.clientlet.*;

public class NavigatorExtensionImpl implements NavigatorExtension {
	public void destroy() {
	}

	public void init(NavigatorExtensionContext pcontext) {
		ClientletSelector selector = new JWebClientletSelector();
		pcontext.addClientletSelector(selector);
	}

	public void windowClosing(NavigatorWindow wcontext) {
	}

	public void windowOpening(NavigatorWindow wcontext) {
	}
}
