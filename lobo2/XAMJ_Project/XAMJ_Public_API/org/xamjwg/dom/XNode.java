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
package org.xamjwg.dom;

/**
 * The root of the XAMJ DOM type hierarchy.
 */
public interface XNode
{
	public XElement getParent();
	public XDocument getOwnerDocument();
	public void setItem(String name, Object value);
	public Object getItem(String name);
//	/**
//	 * Deep-clones this node.
//	 * @return A copy of the node and all of its children.
//	 */
//	public XNode cloneNode();
}
