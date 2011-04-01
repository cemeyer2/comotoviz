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

import java.awt.*;

/**
 * @author J. H. S.
 */
public class RWord extends BaseRenderable {
	public final FontMetrics fontMetrics;
	public final String word;
	public final int descent;
	public final int ascentPlusLeading;
	public final int width;
	public final int height;
	private final int textDecoration;
	//private final RenderState renderState;
	
	/**
	 * 
	 */
	public RWord(BaseMarkupElement me, String word, XamjRenderState renderState) {
		super(me);
		FontMetrics fm = renderState.getFontMetrics();
		//this.renderState = renderState;
		this.fontMetrics = fm;
		this.word = word;
		this.descent = fm.getDescent();
		this.ascentPlusLeading = fm.getAscent() + fm.getLeading();
		this.width = fm.stringWidth(word);
		this.height = fm.getHeight();
		this.textDecoration = renderState.getTextDecoration();
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.markup.Renderable#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		g.drawString(this.word, 0, this.ascentPlusLeading);
		if(this.textDecoration == XamjRenderState.TEXTDECORATION_UNDERLINE) {
			int lineOffset = this.ascentPlusLeading + 2;
			g.drawLine(0, lineOffset, this.width, lineOffset);
		}
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
