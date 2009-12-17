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
 * Created on Sep 17, 2005
 */
package org.xamjwg.dom;

import org.lobobrowser.clientlet.*;

public interface XDocument extends XNode {
	/**
	 * Gets the ClientletContext that was used to
	 * create this XDocument instance.
	 */
	public ClientletContext getClientletContext();
	
	/**
	 * Creates an element.
	 * @param name The element name.
	 * @return An XElement instance.
	 * @throws ClientletException The element name is invalid or unimplemented
	 *                            by the XAMJ engine.
	 */
	public XElement createElement(String name) throws XamjException;

	/**
	 * Creates a text node.
	 * @param text The content of the node.
	 * @return An XText instance.
	 */
	public XText createTextNode(String text);
	
	/**
	 * Creates an XML CDATA section node.
	 * @param text The CDATA value.
	 */
	public XCData createCDataSection(String text);
	
	/**
	 * Creates an XML comment node.
	 * @param text The comment value.
	 */
	public XComment createComment(String text);

	/**
	 * Creates a special action element used by XAMJ parsers.
	 * @param delegate A XamjListener used to execute the action.
	 */
	public XActionElement createActionElement(org.xamjwg.event.XamjListener delegate);
	
	/**
	 * Gets the root element of the document.
	 */
	public XElement getDocumentElement();

	public void setDocumentElement(XElement element) throws XamjException;
	
	/**
	 * Gets the top-level widget elements of the XAMJ document,
	 * usually an array of length 1.
	 */
	public XWidgetElement[] getWidgetElements();
	
	/**
	 * Creates a Component instance in order to 
	 * render the XAMJ document.
	 * @param context The ClientletContext for the request.
	 */
	public java.awt.Component createClientletContent(ClientletContext context);

	public java.util.Properties getWindowProperties();
}
