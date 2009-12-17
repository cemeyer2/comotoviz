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

import java.util.EventObject;
import org.xamjwg.dom.XTreeNodeElement;

/**
 * Base class for all XAMJ events.
 * @author J. H. S.
 */
public abstract class AbstractXamjEvent extends EventObject {
	private final String name;
	
	public AbstractXamjEvent(Object source, String name) {
		super(source);
		this.name = name;
	}

	/**
	 * Gets the name of the event.
	 */
	public String getName() {
		return this.name;
	}
	
    /**
     * Creates a generic XAMJ event.
     * @param source The event source.
     * @param name The event name.
     * @return A AbstractXamjEvent instance.
     */
    public static AbstractXamjEvent create(Object source, String name) {
        return new SimpleXamjEvent(source, name);
    }

    /**
     * Creates an on-property-change event. 
     * @param source The event source.
     * @param propertyName The property name.
     * @param oldValue The old property value.
     * @param newValue The new property value.
     * @return A AbstractXamjEvent instance.
     */
    public static AbstractXamjEvent createPropertyChanged(Object source, String propertyName, Object oldValue, Object newValue) {
        return new XamjPropertyChangeEvent(source, propertyName, oldValue, newValue);
    }

    /**
     * Creates an on-progress event.
     * @param source The event source.
     * @param message A progress message.
     * @param value The progress value.
     * @param max The maximum progress value.
     * @return A AbstractXamjEvent instance.
     */
    public static AbstractXamjEvent createProgress(Object source, String message, int value, int max) {
        return new XamjProgressEvent(source, message, value, max);
    }
    
    public static AbstractXamjEvent createOnPaint(Object source, java.awt.Graphics g, java.awt.Rectangle paintBounds) {
    	return new XamjPaintEvent(source, g, paintBounds);
    }
    
    public static AbstractXamjEvent createKeyEvent(Object source, String eventName, char keyChar, int keyCode) {
    	return new XamjKeyEvent(source, eventName, keyChar, keyCode);
    }
    
    public static AbstractXamjEvent createTreeNodeEvent(Object source, String eventName, XTreeNodeElement node) {
    	return new XamjTreeNodeEvent(source, eventName, node);
    }

    public static AbstractXamjEvent createTreeNodeEvent(Object source, String eventName, XTreeNodeElement[] nodes) {
    	return new XamjTreeNodeEvent(source, eventName, nodes);
    }
}
