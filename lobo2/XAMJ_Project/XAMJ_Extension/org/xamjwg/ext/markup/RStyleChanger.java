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

//import java.util.logging.*;

/**
 * @author J. H. S.
 */
public class RStyleChanger implements Renderable {
	//private final static Logger logger = Logger.getLogger(RStyleChanger.class);
	private final XamjRenderState renderState;
	
	/**
	 * 
	 */
	public RStyleChanger(XamjRenderState renderState) {
		this.renderState = renderState;
	}

	public XamjRenderState getRenderState() {
		return this.renderState;
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.markup.Renderable#getBounds()
	 */
	public Rectangle getBounds() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.markup.Renderable#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		XamjRenderState rs = this.renderState;
		g.setColor(rs.getColor());
		g.setFont(rs.getFont());
		if(g instanceof Graphics2D) {
			Graphics2D g2 = (Graphics2D) g;
			boolean textAntiAliasing = rs.hasTextAntiAliasing();
			//if(logger.isLoggable(Level.INFO))logger.info("paint(): textAntiAliasing=" + textAntiAliasing);
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, textAntiAliasing ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		}
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.markup.Renderable#setBounds(int, int, int, int)
	 */
	public void setBounds(int x, int y, int with, int height) {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.markup.Renderable#setHeight(int)
	 */
	public void setHeight(int height) {
		throw new UnsupportedOperationException();
	}
	
	public void onMouseClick(java.awt.event.MouseEvent event, int x, int y) {
		throw new UnsupportedOperationException("unexpected");
	}

	public void onMousePressed(java.awt.event.MouseEvent event, int x, int y) {
		throw new UnsupportedOperationException("unexpected");
	}

	public void onMouseReleased(java.awt.event.MouseEvent event, int x, int y) {
		throw new UnsupportedOperationException("unexpected");
	}
}
