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
 * A keyboard event.
 * @author J. H. S.
 */
public class XamjKeyEvent extends AbstractXamjEvent {
	private final int keyCode;
	private final char keyChar;
	
	/**
	 * Constructs a keyboard event.
	 * @param source The object firing the event.
	 * @param eventName The name of the event, which may be on-key-pressed, on-key-released, and on-key-typed.
	 * @param keyChar The key character.
	 * @param keyCode The key code.
	 */
	public XamjKeyEvent(Object source, String eventName, char keyChar, int keyCode) {
		super(source, eventName);
		this.keyChar = keyChar;
		this.keyCode = keyCode;
	}

	/**
	 * @return Returns the keyChar.
	 */
	public char getKeyChar() {
		return keyChar;
	}

	/**
	 * @return Returns the keyCode.
	 */
	public int getKeyCode() {
		return keyCode;
	}
}
