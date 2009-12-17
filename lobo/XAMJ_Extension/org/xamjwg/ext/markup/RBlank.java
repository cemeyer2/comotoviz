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
 * Created on May 21, 2005
 */
package org.xamjwg.ext.markup;

import java.awt.*;

/**
 * @author J. H. S.
 */
public class RBlank extends BaseRenderable {
	public final int width;
	public final int height;
	public final int ascentPlusLeading;
	private final int textDecoration;
	private final FontMetrics fontMetrics;
	
	/**
	 * 
	 */
	public RBlank(BaseMarkupElement me, FontMetrics fm, int width, int textDecoration) {
		super(me);
		this.width = width;
		this.textDecoration = textDecoration;
		this.fontMetrics = fm;
		this.height = fm.getHeight();
		this.ascentPlusLeading = fm.getAscent() + fm.getLeading();
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
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.markup.Renderable#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		if(this.textDecoration == XamjRenderState.TEXTDECORATION_UNDERLINE) {
			int lineOffset = this.fontMetrics.getAscent() + this.fontMetrics.getLeading() + 2;
			g.drawLine(0, lineOffset, this.width, lineOffset);
		}
	}
}
