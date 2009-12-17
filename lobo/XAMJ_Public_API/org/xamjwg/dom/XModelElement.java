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
 * Created on May 20, 2005
 */
package org.xamjwg.dom;

/**
 * A model element. Elements of this type may be
 * children of widget elements. They create a sibbling
 * by obtaining a widget from an external XAMJ document,
 * but only when their parent becomes visible.
 * @author J. H. S.
 */
public interface XModelElement extends XElement {
	public XamjEvaluatable<String> getHref();
	
	/**
	 * Sets a XamjEvaluatable instance which is
	 * evaluated for a URL when the model element's parent
	 * first becomes visible, or right away if the parent
	 * is already visible.
	 * @attribute Takes a URL referring to a XAMJ document.
	 * @throws Exception
	 */
	public void setHref(XamjEvaluatable<String> value) throws Exception;
	
	/**
	 * Replaces sibblings previously created after requesting
	 * XAMJ content from the URL value of the local <code>href</code> property.
	 */
	public void refresh() throws Exception;
}
