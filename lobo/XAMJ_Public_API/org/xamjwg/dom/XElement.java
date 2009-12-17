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

import java.util.*;

/**
 * All XAMJ element classes implement this interface.
 */
public interface XElement extends XNode
{
	/**
	 * Gets the element name.
	 */
	public String getNodeName();
	
	/**
	 * Appends a child node to this element.
	 * @param node An instance of XNode. A User Agent
	 * 			   may have additional requirements for
	 *             the implementation of XNode, and in
	 *             general nodes should be created by
	 *             invoking create methods in XDocument.
	 */
	public void appendChild(XNode node);

	/**
	 * Removes a child node from this element.
	 * @param node An instance of XNode.
	 */
	public void removeChildNode(XNode node);

	
	/**
	 * Gets an iterator of the child nodes of this element. 
	 */
	public Iterator getChildNodes();
	
	/**
	 * Sets the id of the element. 
	 * @param value A valid Java identifier.
	 */
	public void setId(String value);

	/**
	 * Gets the id of the element.
	 */
	public String getId();
	
	/**
	 * Gets an element-dependent value. This is only defined
	 * for text-box, text-field, password-field, combo-box
	 * (a String), plus radio-button, check-box and toggle-button (a Boolean.)
	 * For other elements, the value could be anything.
	 */
	public Object getElementValue();
	
	/**
	 * Removes all child nodes of this element.
	 */
	public void removeChildNodes();

	/**
	 * Removes child nodes accepted by the given filter.
	 */
	public void removeChildNodes(XamjNodeFilter filter);

//	/**
//	 * Removes all child nodes and replaces them with
//	 * clones of the provided element's children.
//	 * @param source The element whose child nodes are deep-cloned. 
//	 */
//	public void replaceChildNodes(XElement source);
//
//	/**
//	 * Removes filtered child nodes and adds
//	 * clones of all the provided element's children.
//	 * @param source The element whose child nodes are deep-cloned. 
//	 * @param filter A NodeFilter that determines which nodes should be removed. 
//	 */
//	public void replaceChildNodes(XElement source, XamjNodeFilter filter);
//	
////	/**
//	 * Removes all child nodes and replaces them with
//	 * the provided document's root element's children.
//	 * The operation is performed asynchronously.
//	 * @param url Absolute or relative URL of a partial XAMJ document.
//	 *            The document mime-type may be text/xml. Extension
//	 *            .xami is appropriate for partial XAMJ documents.
//	 */
//	public void replaceChildNodes(String url) throws java.net.MalformedURLException;

//	/**
//	 * Removes filtered child nodes and replaces them with
//	 * the provided document's root element's children.
//	 * The operation is performed asynchronously.
//	 * @param url Absolute or relative URL of a partial XAMJ document.
//	 *            The document mime-type may be text/xml. Extension
//	 *            .xami is appropriate for partial XAMJ documents.
//	 * @param filter A NodeFilter that determines which nodes should be removed. 
//	 */
//	public void replaceChildNodes(String url, XamjNodeFilter filter) throws java.net.MalformedURLException;

	//public void startBatch();
	//public void endBatch();
	
//	/**
//	 * Sets a macro element from which all child nodes
//	 * of this element are obtained.
//	 */
//	public void setMacro(XMacroElement value);
//	
//	/**
//	 * Gets the macro element of this element.
//	 * @return An instance of XMacroElement or null.
//	 */
//	public XMacroElement getMacro();
}
