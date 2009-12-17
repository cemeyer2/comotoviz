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
 * A property change event for XAMJ element properties/attributes.
 * @author J. H. S.
 */
public class XamjPropertyChangeEvent extends AbstractXamjEvent {
	private final String propertyName;
	private final Object oldValue;
	private final Object newValue;
	
	/**
	 * Constructs a XamjPropertyChangeEvent.
	 */
	public XamjPropertyChangeEvent(Object source, String propertyName, Object oldValue, Object newValue) {
		super(source, "on-property-change");
		// TODO Auto-generated constructor stub
		this.newValue = newValue;
		this.oldValue = oldValue;
		this.propertyName = propertyName;
	}

	/**
	 * @return Returns the newValue.
	 */
	public Object getNewValue() {
		return newValue;
	}

	/**
	 * @return Returns the oldValue.
	 */
	public Object getOldValue() {
		return oldValue;
	}

	/**
	 * @return Returns the propertyName.
	 */
	public String getPropertyName() {
		return propertyName;
	}

}
