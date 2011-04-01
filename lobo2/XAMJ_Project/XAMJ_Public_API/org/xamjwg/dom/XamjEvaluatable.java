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
 * Created on Mar 13, 2005
 */
package org.xamjwg.dom;

/**
 * Attributes evaluated "on the fly" as needed
 * by their corresponding elements are declared
 * as properties of this type. An example is
 * the <code>href</code> attribute of an anchor
 * tag. It is evaluated every time the anchor is
 * clicked. Note that this is a generic interface.
 * A property with a type such as <code>XamjEvaluatable&lt;Long&gt;</code>
 * is the dynamic equivalent of a property
 * of type <code>Long</code>. 
 * @author J. H. S.
 */
public interface XamjEvaluatable<TValue> {
	/**
	 * Invoked to perform an evaluation and return a value.
	 */
    public TValue evaluate() throws Exception;
}
