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
 * Created on Apr 17, 2005
 */
package org.xamjwg.ext.markup;


import org.xamjwg.ext.gui.*;

/**
 * @author J. H. S.
 */
public class RWidget extends BaseRenderable {
	public final Widget widget;
	
	/**
	 * 
	 */
	public RWidget(BaseMarkupElement me, Widget widget) {
		super(me);
		this.widget = widget;
	}
	
	public void onMouseClick(java.awt.event.MouseEvent event, int x, int y) {
		BaseMarkupElement me = this.markupElement;
		if(me != null) {
			me.onMouseClick(event, x, y);
		}
	}

	public void onMousePressed(java.awt.event.MouseEvent event, int x, int y) {
		BaseMarkupElement me = this.markupElement;
		if(me != null) {
			me.onMousePressed(event, x, y);
		}
	}

	public void onMouseReleased(java.awt.event.MouseEvent event, int x, int y) {
		BaseMarkupElement me = this.markupElement;
		if(me != null) {
			me.onMouseReleased(event, x, y);
		}
	}

}
