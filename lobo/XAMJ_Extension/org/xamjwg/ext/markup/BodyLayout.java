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

import org.lobobrowser.util.*;
import org.xamjwg.dom.*;
import org.xamjwg.ext.domimpl.*;
import org.xamjwg.ext.gui.*;

import java.util.logging.*;

/**
 * @author J. H. S.
 */
public class BodyLayout {
	private static final Logger logger = Logger.getLogger(BodyLayout.class.getName());
	
	// Renderables should be "lines" in positional order
	// to allow binary searches by position.
	private final ArrayList renderables = new ArrayList();
	private RLine currentLine;
	private int maxX;
	private XamjRenderState currentRenderState;
	private BaseMarkupElement currentMarkupElement;
	private boolean skipParagraphBreakBefore;
	private boolean skipLineBreakBefore;
	
	/**
	 * 
	 */
	public BodyLayout() {
		super();
	}

	public Dimension layout(Container container, BodyElement bodyElement) {
		Diagnostics.Assert(EventQueue.isDispatchThread(), "Expected call to be in event thread");
		Insets insets = container.getInsets();
		//if(logger.isLoggable(Level.INFO))logger.info("layout(): insets=" + insets);

		// This invalidates, but that's fine
		container.removeAll();
		
		this.renderables.clear();
		Rectangle bounds = container.getBounds();
		this.maxX = bounds.width - insets.right;

		//if(logger.isLoggable(Level.INFO))logger.info("layout(): starting maxX=" + this.maxX);

		this.currentRenderState = new XamjRenderState();
		this.currentMarkupElement = null;
		this.currentLine = this.addLine(bounds, insets, null);
		this.currentLine.addRenderState(this.currentRenderState);
		
		this.skipLineBreakBefore = true;
		this.skipParagraphBreakBefore = true;
		
		this.layoutElement(container, insets, bodyElement);

		Rectangle lastBounds = this.currentLine.getBounds();
		
		//if(logger.isLoggable(Level.INFO))logger.info("layout(): bounds=" + bounds + ",lastBounds=" + lastBounds);
		
		if(insets.left + lastBounds.width > this.maxX) {
			this.maxX = insets.left + lastBounds.width;
		}

		//if(logger.isLoggable(Level.INFO))logger.info("layout(): ending maxX=" + this.maxX);

		int maxY = lastBounds.y + lastBounds.height;
		return new Dimension(insets.right + this.maxX, insets.bottom + maxY);
	}

	private RLine addLine(Rectangle containerBounds, Insets insets, RLine prevLine) {
		RLine rline;
		if(prevLine == null) {
			rline = new RLine(this.currentMarkupElement, this.currentRenderState, insets.left, insets.top, containerBounds.width - insets.left - insets.right, 0);
		}
		else {
			Rectangle prevBounds = prevLine.getBounds();
			if(insets.left + prevBounds.width > this.maxX) {
				this.maxX = insets.left + prevBounds.width;
			}
			rline = new RLine(this.currentMarkupElement, this.currentRenderState, insets.left, prevBounds.y + prevBounds.height, containerBounds.width - insets.left - insets.right, 0);
		}
		
		// TODO Not terribly efficient to add this to every line:
		rline.addRenderState(this.currentRenderState);
		this.renderables.add(rline);
		this.currentLine = rline;
		return rline;
	}
	
	private void layoutElement(Container container, Insets insets, BaseElement element) {
		Iterator children = element.getChildNodes();
		while(children.hasNext()) {
			XNode node = (XNode) children.next();
			if(node instanceof BaseMarkupElement) {
				this.layoutMarkup(container, insets, (BaseMarkupElement) node);
			}
			else if(node instanceof BaseWidgetElement) {
				this.layoutWidget(container, insets, (BaseWidgetElement) node);
			}
			else if(node instanceof XText) {
				this.layoutText(container, insets, ((XText) node).getValue());
			}
			else if(node instanceof XComment || node instanceof XScriptletElement){
				// ignore
			}
			else {
				logger.warning("layoutElement(): Ignoring " + node);
			}
		}
	}

	private void layoutMarkup(Container container, Insets insets, BaseMarkupElement markupElement) {
		XamjRenderState oldState = this.currentRenderState;
		BaseMarkupElement oldMarkupElement = this.currentMarkupElement;
		this.currentMarkupElement = markupElement;
		try {
			MarkupStyleElement mse = (MarkupStyleElement) markupElement.getStyle();
			if(!this.skipParagraphBreakBefore && mse.getParagraphBreakBeforeValue() == MarkupStyleElement.BREAK_ALWAYS) {
				this.addParagraphBreak(container, insets);
			}
			else if(!this.skipLineBreakBefore && mse.getLineBreakBeforeValue() == MarkupStyleElement.BREAK_ALWAYS) {
				this.addLineBreak(container, insets);
			}
			XamjRenderState state = markupElement.createRenderState(oldState);
			this.currentRenderState = state;
			this.currentLine.addRenderState(state);
			this.layoutElement(container, insets, markupElement);
			if(mse.getParagraphBreakAfterValue() == MarkupStyleElement.BREAK_ALWAYS) {
				this.addParagraphBreak(container, insets);
				this.skipParagraphBreakBefore = true;
			}
			else if(mse.getLineBreakAfterValue() == MarkupStyleElement.BREAK_ALWAYS) {
				this.addLineBreak(container, insets);
				this.skipLineBreakBefore = true;
			}
		} finally {
			this.currentMarkupElement = oldMarkupElement;
			this.currentRenderState = oldState;
			this.currentLine.addRenderState(oldState);
		}
	}

	private void addParagraphBreak(Container container, Insets insets) {
		this.addLineBreak(container, insets);
		this.addLineBreak(container, insets);
	}
	
	private void addLineBreak(Container container, Insets insets) {
		RLine line = this.currentLine;
		if(line.getBounds().height == 0) {
			XamjRenderState rs = this.currentRenderState;
			line.setHeight(rs.getFontMetrics().getHeight());
		}
		this.currentLine = this.addLine(container.getBounds(), insets, this.currentLine);
	}
	
	private void addRenderable(Container container, Insets insets, Renderable renderable) {
		this.skipLineBreakBefore = false;
		this.skipParagraphBreakBefore = false;
		RLine line = this.currentLine;
		try {
			line.add(renderable);
		} catch(OverflowException oe) {
			this.addLine(container.getBounds(), insets, line);
			Collection renderables = oe.getRenderables();
			Iterator i = renderables.iterator();
			while(i.hasNext()) {
				Renderable r = (Renderable) i.next();
				this.addRenderable(container, insets, r);
			}
		}
		
	}
	
	private void layoutWidget(Container container, Insets insets, BaseWidgetElement widgetElement) {
		Widget widget = widgetElement.getWidget();
		container.add(widget);
		this.addRenderable(container, insets, new RWidget(this.currentMarkupElement, widget));
	}
		
	
	private void layoutText(Container container, Insets insets, String text) {
		StringTokenizer tok = new StringTokenizer(text, " \r\n\t", true);
		boolean lastBlank = false;
		while(tok.hasMoreTokens()) {
			String token = tok.nextToken().trim();
			if("".equals(token)) {
				if(!lastBlank) {
					// Do not add as renderable or
					// fix skip*BreakBefore.
					this.currentLine.addBlank(this.currentMarkupElement);
				}
				lastBlank = true;
			}
			else {
				this.layoutWord(container, insets, token);
				lastBlank = false;
			}
		}
	}

	private void layoutWord(Container container, Insets insets, String word) {
		RLine line = this.currentLine;
		RWord rword = line.getRWord(this.currentMarkupElement, word);
		this.addRenderable(container, insets, rword);
	}
	
	public Iterator getRenderables(Rectangle clipBounds) {
		Diagnostics.Assert(EventQueue.isDispatchThread(), "Expected call to be in event thread");
		Renderable[] array = (Renderable[]) this.renderables.toArray(Renderable.EMPTY_ARRAY);
		Range range = MarkupUtilities.findRenderables(array, clipBounds, true);
		return org.lobobrowser.util.ArrayUtilities.iterator(array, range.offset, range.length);
	}
	
	public Renderable getRenderable(java.awt.Point point) {
		Diagnostics.Assert(EventQueue.isDispatchThread(), "Expected call to be in event thread");
		Renderable[] array = (Renderable[]) this.renderables.toArray(Renderable.EMPTY_ARRAY);
		return MarkupUtilities.findRenderable(array, point, true);
	}
}
