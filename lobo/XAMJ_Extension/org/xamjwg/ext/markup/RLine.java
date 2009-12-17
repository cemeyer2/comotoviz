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
 * Created on Apr 16, 2005
 */
package org.xamjwg.ext.markup;

import java.awt.*;
import java.util.*;

import org.xamjwg.ext.gui.*;

/**
 * @author J. H. S.
 */
public class RLine extends BaseRenderable {
	private final ArrayList renderables = new ArrayList();
	private final XamjRenderState startRenderState;
	private XamjRenderState currentRenderState;
	private int baseLineOffset;
	private int offset = 0;
	
	/**
	 * 
	 */
	public RLine(BaseMarkupElement me, XamjRenderState lastRenderState, int x, int y, int width, int height) {
		super(me);
		Rectangle b = this.bounds;
		b.x = x;
		b.y = y;
		b.width = width;
		b.height = height;
		this.startRenderState = lastRenderState;
		this.currentRenderState = lastRenderState;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.markup.Renderable#getBounds()
	 */
	public Rectangle getBounds() {
		return this.bounds;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.markup.Renderable#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		Iterator i = this.renderables.iterator();
		while(i.hasNext()) {
			Renderable r = (Renderable) i.next();
			if(r instanceof RStyleChanger) {
				r.paint(g);
			}
			else {
				Rectangle bounds = r.getBounds();
				int offsetX = bounds.x;
				int offsetY = bounds.y;
				g.translate(offsetX, offsetY);
				try {
					r.paint(g);
				} finally {
					g.translate(-offsetX, -offsetY);
				}
			}
		}
	}
	
	public void addRenderState(XamjRenderState rs) {
		this.renderables.add(new RStyleChanger(rs));
		this.currentRenderState = rs;
	}
	
//	public boolean couldAdd(int width) {
//		int offset = this.offset;
//		if(offset == 0) {
//			return true;
//		}
//		return offset + width <= this.bounds.width;
//	}
//	
	public RWord getRWord(BaseMarkupElement me, String word) {
		XamjRenderState rs = this.currentRenderState;
		return new RWord(me, word, rs);
	}
	
	public RWidget getRWidget(BaseMarkupElement me, Widget widget) {
		return new RWidget(me, widget);
	}
	
	public void add(Renderable renderable) throws OverflowException {
		if(renderable instanceof RWord) {
			this.addWord((RWord) renderable);
		}
		else if(renderable instanceof RWidget) {
			this.addWidget((RWidget) renderable);
		}
		else {
			throw new IllegalArgumentException("Can't add " + renderable);
		}
	}
	
	public void addWord(RWord wordInfo) throws OverflowException {
		// Check if it fits horzizontally
		int offset = this.offset;
		int wiwidth = wordInfo.width;
		if(offset != 0 && offset + wiwidth > this.bounds.width) {
			throw new OverflowException(Collections.singleton(wordInfo));
		}
		
		// Add it
		this.renderables.add(wordInfo);
		int extraHeight = 0;
		int maxDescent = this.bounds.height - this.baseLineOffset;
		if(wordInfo.descent > maxDescent) {
			extraHeight += (wordInfo.descent - maxDescent);
		}
		int maxAscentPlusLeading = this.baseLineOffset;
		if(wordInfo.ascentPlusLeading > maxAscentPlusLeading) {
			extraHeight += (wordInfo.ascentPlusLeading - maxAscentPlusLeading);
		}
		if(extraHeight > 0) {
			this.adjustHeight(this.bounds.height + extraHeight, 0.0f);
		}
		else {
			int x = offset;
			offset += wiwidth;
			if(offset > this.bounds.width) {
				this.bounds.width = offset;
			}
			this.offset = offset;
			wordInfo.setBounds(x, this.baseLineOffset - wordInfo.ascentPlusLeading, wiwidth, wordInfo.height);
		}
	}
	
	public void addBlank(BaseMarkupElement me) {
		int x = this.offset;
		if(x > 0) {
			XamjRenderState rs = this.currentRenderState;
			FontMetrics fm = rs.getFontMetrics();
			int width = fm.charWidth(' ');
			RBlank rblank = new RBlank(me, fm, width, rs.getTextDecoration());
			rblank.setBounds(x, this.baseLineOffset - rblank.ascentPlusLeading, width, rblank.height);
			this.renderables.add(rblank);
			this.offset = x + width;
		}
	}

	private void layoutWidget(RWidget rwidget, int x) {
		Widget widget = rwidget.widget;
		int pw = widget.getPreferredWidth();
		if(pw == -1) {
			pw = this.bounds.width - x;
		}
		int ph = widget.getPreferredHeight();
		if(ph == -1) {
			Dimension ps = widget.getPreferredSize();
			ph = ps.height;
			int charHeight = this.currentRenderState.getFontMetrics().getHeight();
			if(ph < charHeight) {
				ph = charHeight;
			}
		}
		this.layoutWidget(rwidget, x, pw, ph);		
	}

	private void layoutWidget(RWidget rwidget, int x, int width, int height) {
		Widget widget = rwidget.widget;
		int yoffset;
		if(height >= this.bounds.height) {
			yoffset = 0;
		}
		else {
			yoffset = (int) ((this.bounds.height - height) * widget.getAlignmentY());
		}
		rwidget.setBounds(x, yoffset, width, height);
		widget.setBounds(this.bounds.x + x, this.bounds.y + yoffset, width, height);
	}
	
	public void addWidget(RWidget rwidget) throws OverflowException {
		// Check if it fits horizontally
		Widget widget = rwidget.widget;
		int pw = widget.getPreferredWidth();
		int offset = this.offset;
		if(pw == -1) {
			pw = this.bounds.width - offset;
		}
		if(offset != 0 && offset + pw > this.bounds.width) {
			throw new OverflowException(Collections.singleton(rwidget));
		}

		//Note: Renderable for widget doesn't paint the widget, but
		//it's needed for height readjustment.
		this.renderables.add(rwidget);
		int ph = widget.getPreferredHeight();
		if(ph == -1) {
			Dimension ps = widget.getPreferredSize();
			ph = ps.height;
			int charHeight = this.currentRenderState.getFontMetrics().getHeight();
			if(ph < charHeight) {
				ph = charHeight;
			}
		}
		if(ph > this.bounds.height) {
			this.adjustHeight(ph, widget.getAlignmentY());
		}
		else {
			this.layoutWidget(rwidget, this.offset, pw, ph);
			this.offset += rwidget.getBounds().width;
			if(this.offset > this.bounds.width) {
				this.bounds.width = this.offset;
			}
		}
	}
	
	private void adjustHeight(int newHeight, float alignmentY) {
		// Set new line height
		this.bounds.height = newHeight;
		Renderable[] rarray = (Renderable[]) this.renderables.toArray(Renderable.EMPTY_ARRAY);
		int rlength = rarray.length;
		
		// Find max baseline 
		int maxDescent = 0;
		for(int i = 0; i < rlength; i++) {
			Renderable r = rarray[i];
			if(r instanceof RWord) {
				RWord rword = (RWord) r;
				int descent = rword.descent;
				if(descent > maxDescent) {
					maxDescent = descent;
				}
			}
		}
		
		// Find max ascent 
		int maxAscentPlusLeading = 0;
		for(int i = 0; i < rlength; i++) {
			Renderable r = rarray[i];
			if(r instanceof RWord) {
				RWord rword = (RWord) r;
				int ascentPlusLeading = rword.ascentPlusLeading;
				if(ascentPlusLeading > maxAscentPlusLeading) {
					maxAscentPlusLeading = ascentPlusLeading;
				}
			}
		}
		
		int maxBaseline = newHeight - maxDescent;
		int minBaseline = maxAscentPlusLeading;
		
		//TODO What if the descent is huge and the ascent tiny?
		int baseline = (int) (minBaseline + alignmentY * (maxBaseline - minBaseline));
		this.baseLineOffset = baseline;
		
		// Change bounds of renderables accordingly
		int x = 0;
		this.currentRenderState = this.startRenderState;
		for(int i = 0; i < rlength; i++) {
			Renderable r = rarray[i];
			if(r instanceof RWord) {
				RWord rword = (RWord) r;
				int w = rword.width;
				rword.setBounds(x, baseline - rword.ascentPlusLeading, w, rword.height);
				x += w;
			}
			else if(r instanceof RBlank) {
				RBlank rblank = (RBlank) r;
				int w = rblank.width;
				rblank.setBounds(x, baseline - rblank.ascentPlusLeading, w, rblank.height);
				x += w;
			}
			else if(r instanceof RWidget) {
				RWidget rwidget = (RWidget) r;
				this.layoutWidget(rwidget, x);
				x += rwidget.getBounds().width;
			}
			else if(r instanceof RStyleChanger) {
				this.currentRenderState = ((RStyleChanger) r).getRenderState();
			}
		}
		this.offset = x;
		
		//TODO: Could throw OverflowException when we add floating widgets
	}
	
	public void onMouseClick(java.awt.event.MouseEvent event, int x, int y) {
		Renderable[] rarray = (Renderable[]) this.renderables.toArray(Renderable.EMPTY_ARRAY);
		Renderable r = MarkupUtilities.findRenderable(rarray, x, y, false);
		if(r != null) {
			Rectangle rbounds = r.getBounds();
			r.onMouseClick(event, x - rbounds.x, y - rbounds.y);
		}
	}

	public void onMousePressed(java.awt.event.MouseEvent event, int x, int y) {
		Renderable[] rarray = (Renderable[]) this.renderables.toArray(Renderable.EMPTY_ARRAY);
		Renderable r = MarkupUtilities.findRenderable(rarray, x, y, false);
		if(r != null) {
			Rectangle rbounds = r.getBounds();
			r.onMousePressed(event, x - rbounds.x, y - rbounds.y);
		}
	}

	public void onMouseReleased(java.awt.event.MouseEvent event, int x, int y) {
		Renderable[] rarray = (Renderable[]) this.renderables.toArray(Renderable.EMPTY_ARRAY);
		Renderable r = MarkupUtilities.findRenderable(rarray, x, y, false);
		if(r != null) {
			Rectangle rbounds = r.getBounds();
			r.onMouseReleased(event, x - rbounds.x, y - rbounds.y);
		}
	}

}
