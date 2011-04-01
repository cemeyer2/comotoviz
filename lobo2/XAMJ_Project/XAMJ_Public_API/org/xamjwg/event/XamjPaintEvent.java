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
 * Created on Jul 3, 2005
 */
package org.xamjwg.event;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * A XAMJ on-paint event.
 * @author J. H. S.
 */
public class XamjPaintEvent extends AbstractXamjEvent {
	private final Rectangle paintBounds;
	private final java.awt.Graphics graphics;

	/**
	 * Constructs an on-paint event.
	 * @param source The object firing the event.
	 * @param graphics A Graphics instance.
	 * @param bounds The bounds where painting is allowed.
	 */
	public XamjPaintEvent(Object source, Graphics graphics, Rectangle bounds) {
		super(source, "on-paint");
		this.graphics = graphics;
		paintBounds = bounds;
	}

	/**
	 * Gets a Graphics object.
	 */
	public java.awt.Graphics getGraphics() {
		return graphics;
	}

	/**
	 * Gets the bounds where painting is allowed.
	 */
	public Rectangle getPaintBounds() {
		return paintBounds;
	}
	
	/**
	 * Gets a Graphics2D instance or <code>null</code> if Java2D
	 * is not supported by the XAMJ implementation.
	 */
    public Graphics2D getGraphics2D() {
    	Object g = this.graphics;
    	if(g instanceof Graphics2D) {
    		return (java.awt.Graphics2D) g;
    	}
    	else {
    		return null;
    	}
    }

}
