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

/**
 * An on-progress event for the <code>frame</code> element.
 * @author J. H. S.
 */
public class XamjProgressEvent extends AbstractXamjEvent {
	public final String message;
	public final int value;
	public final int max;
	
	/**
	 * Constructs an on-progress event. 
	 * @param source The frame firing the event.
	 * @param max The maximum progress value, or <code>-1</code> to indicate that
	 * the upper bound is unknown.
	 * @param message A progress message.
	 * @param value The current progress value.
	 */
	public XamjProgressEvent(Object source, String message, int value, int max) {
		super(source, "on-progress");
		this.max = max;
		this.message = message;
		this.value = value;
	}
	
	/**
	 * Gets the maximum value, which may be <code>-1</code> to indicate that
	 * the upper bound is unknown.
	 */
	public int getProgressMax() {
		return max;
	}
	
	/**
	 * Gets a progress message.
	 */
	public String getProgressMessage() {
		return message;
	}

	/**
	 * Gets the current progress value, which should be less than or equal
	 * to <code>getProgressMax()</code> unless <code>getProgressMax()</code> is <code>-1</code>.
	 */
	public int getProgressValue() {
		return value;
	}	
}
