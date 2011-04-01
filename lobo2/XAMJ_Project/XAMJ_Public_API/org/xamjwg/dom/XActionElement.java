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
 * Created on Mar 31, 2005
 */
package org.xamjwg.dom;

import org.xamjwg.event.*;

/**
 * An action element. Its <code>execute</code> method is
 * typically invoked when a GUI event is fired in order
 * to perform an action. Note that this is a generic interface.
 * The <code>TEvent</code> type parameter allows the <code>execute</code>
 * method to take an event object in a type-safe manner. For example,
 * <code>XActionElement&lt;XamjPropertyChangeEvent&gt;</code> is an
 * interface whose <code>execute</code> method takes an argument
 * of type <code>XamjPropertyChangeEvent</code>.
 * <p/>
 * Examples of XAMJ action elements are <code>do-navigate</code>,
 * <code>do-open</code>, and <code>do-set</code>.
 * @see org.xamjwg.dom.XEventElement
 * @see org.xamjwg.dom.XActionContainerElement
 * @author J. H. S.
 */
public interface XActionElement<TEvent extends AbstractXamjEvent> extends XElement {
	/**
	 * Executes the action.
	 * @param event A XAMJ event.
	 */
    public void execute(TEvent event) throws Exception;
}
