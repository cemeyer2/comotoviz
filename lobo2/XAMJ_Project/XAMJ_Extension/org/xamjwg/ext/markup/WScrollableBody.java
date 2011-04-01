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

import java.util.*;

import javax.swing.border.*;
import java.util.logging.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.xamjwg.ext.gui.BodyElement;

/**
 * @author J. H. S.
 */
public class WScrollableBody extends JComponent implements Scrollable {
	private static final Logger logger = Logger.getLogger(WScrollableBody.class.getName());
	private volatile BodyElement bodyElement;
	private final BodyLayout bodyLayout = new BodyLayout();
	private Dimension viewSize = null;
	
	public WScrollableBody(BodyElement bodyElement) {
		//if(logger.isLoggable(Level.INFO))logger.info("WScrollableBody(): bodyElement=" + bodyElement);
		this.bodyElement = bodyElement;
		this.setBackground(Color.white);
		//TODO: Actual widget padding.
		this.setBorder(new EmptyBorder(10, 10, 10, 10));
		this.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				onMouseClick(e);
			}
			
			public void mouseEntered(MouseEvent e) { 
			}
			
			public void mouseExited(MouseEvent e) {	
			}
			
			public void mousePressed(MouseEvent e) {
				onMousePressed(e);
			}
			
			public void mouseReleased(MouseEvent e) {
				onMouseReleased(e);
			}
		});
	}
	
	public WScrollableBody() {
		this(null);
	}
	
	private void onMouseClick(MouseEvent event) {
		Point point = event.getPoint();
		Renderable r = this.bodyLayout.getRenderable(point);
		if(r != null) {
			Rectangle bounds = r.getBounds();
			r.onMouseClick(event, point.x - bounds.x, point.y - bounds.y);
		}
	}
	
	private void onMousePressed(MouseEvent event) {
		Point point = event.getPoint();
		Renderable r = this.bodyLayout.getRenderable(point);
		if(r != null) {
			Rectangle bounds = r.getBounds();
			r.onMousePressed(event, point.x - bounds.x, point.y - bounds.y);
		}
	}

	private void onMouseReleased(MouseEvent event) {
		Point point = event.getPoint();
		Renderable r = this.bodyLayout.getRenderable(point);
		if(r != null) {
			Rectangle bounds = r.getBounds();
			r.onMouseReleased(event, point.x - bounds.x, point.y - bounds.y);
		}
	}

	public void setBodyElement(BodyElement bodyElement) {
		this.bodyElement = bodyElement;
	}
	
	public Dimension getPreferredSize() {
		if(this.viewSize == null) {
			Container parent = this.getParent();
			if(parent != null) {
				return parent.getSize();
			}
			else {
				return new Dimension(0, 0);
			}
		}
		return this.viewSize;
	}

	public void setBounds(int x, int y, int width, int height) {
		if(logger.isLoggable(Level.INFO))logger.info("setBounds(): x=" + x + ",y=" + y + ",width=" + width + ",height=" + height);
		super.setBounds(x, y, width, height);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.Scrollable#getPreferredScrollableViewportSize()
	 */
	public Dimension getPreferredScrollableViewportSize() {
		return this.getPreferredSize();
	}

	/* (non-Javadoc)
	 * @see javax.swing.Scrollable#getScrollableUnitIncrement(java.awt.Rectangle, int, int)
	 */
	public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2) {
		// TODO Line height
		return 40;
	}

	/* (non-Javadoc)
	 * @see javax.swing.Scrollable#getScrollableBlockIncrement(java.awt.Rectangle, int, int)
	 */
	public int getScrollableBlockIncrement(Rectangle arg0, int arg1, int arg2) {
		// TODO Page height
		return 600;
	}

	/* (non-Javadoc)
	 * @see javax.swing.Scrollable#getScrollableTracksViewportWidth()
	 */
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.swing.Scrollable#getScrollableTracksViewportHeight()
	 */
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics arg0) {
		long time1 = System.currentTimeMillis();
		try {
			Object parent = this.getParent();
			if(parent instanceof JViewport) {
				JViewport vp = (JViewport) parent;
				Dimension viewSize = vp.getViewSize();
				Dimension extentSize = vp.getExtentSize();
				logger.info("paintComponent(): this.bounds=" + this.getBounds() + ",vp.viewSize=" + viewSize + ",vp.extentSize=" + extentSize);
				if(extentSize.width > viewSize.width || extentSize.height > viewSize.height) {
					Dimension newViewSize = new Dimension(viewSize);
					if(extentSize.width > viewSize.width) {
						newViewSize.width = extentSize.width;
					}
					if(extentSize.height > viewSize.height) {
						newViewSize.height = extentSize.height;
					}
					vp.setViewSize(newViewSize);
					vp.revalidate();
					logger.info("paintComponent(): Adjusting view to fit extent");
					return;
				}
			}
			super.paintComponent(arg0);
			Rectangle clipBounds = arg0.getClipBounds();
			arg0.setColor(this.getBackground());
			arg0.fillRect(clipBounds.x, clipBounds.y, clipBounds.width, clipBounds.height);
			Iterator i = this.bodyLayout.getRenderables(clipBounds);
			while(i.hasNext()) {
				Renderable renderable = (Renderable) i.next();
				Rectangle bounds = renderable.getBounds();
				//if(logger.isLoggable(Level.INFO))logger.info("paintComponent(): renderable=" + renderable + ",bounds=" + bounds);
				Graphics rg = arg0.create(bounds.x, bounds.y, bounds.width, bounds.height);
				renderable.paint(rg);
			}
		} finally {
			long time2 = System.currentTimeMillis();
			if(logger.isLoggable(Level.INFO))logger.info("paintComponent(): Took " + (time2 - time1) + " ms.");
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#doLayout()
	 */
	public void doLayout() {
		long time1 = System.currentTimeMillis();
		try {
			if(logger.isLoggable(Level.INFO))logger.info("doLayout(): this.bounds=" + this.getBounds());
			Dimension size = this.bodyLayout.layout(this, this.bodyElement);
			Object parent = this.getParent();
			if(parent instanceof JViewport) {
				JViewport vp = (JViewport) parent;
				Dimension oldViewSize = vp.getViewSize();
				Dimension newViewSize = new Dimension();
				Dimension extentSize = vp.getExtentSize();
				newViewSize.width = Math.max(extentSize.width, size.width);
				newViewSize.height = Math.max(extentSize.height, size.height);
				if(oldViewSize.width != newViewSize.width || oldViewSize.height != newViewSize.height) {
					this.viewSize = newViewSize;
					if(logger.isLoggable(Level.INFO))logger.info("doLayout(): Adjusting size: size=" + size + ",oldViewSize=" + oldViewSize + ",newViewSize=" + newViewSize + ",vp.extentSize=" + extentSize);
					vp.setViewSize(newViewSize);
				}
			}
			else {
				logger.warning("doLayout(): Parent not a JViewport");
			}
		} finally {
			long time2 = System.currentTimeMillis();
			if(logger.isLoggable(Level.INFO))logger.info("doLayout(): Took " + (time2 - time1) + " ms.");
		}
	}
}
