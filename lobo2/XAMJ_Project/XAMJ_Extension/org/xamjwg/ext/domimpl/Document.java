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
package org.xamjwg.ext.domimpl;

import java.awt.Component;
import java.net.MalformedURLException;

import java.util.Properties;
import java.util.logging.*;

import org.lobobrowser.clientlet.*;
import org.lobobrowser.request.*;
import org.xamjwg.dom.*;
import org.xamjwg.event.XamjListener;
import org.xamjwg.ext.gui.*;


public class Document extends BaseNode implements XDocument {
	private static final Logger logger = Logger.getLogger(Document.class.getName());
	private final ClientletContext context;
	
	public Document(ClientletContext context) {
		super();
		this.context = context;
	}
	
	public ClientletContext getClientletContext() {
		return this.context;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#getElementValue()
	 */
	public Object getElementValue() {
		return this;
	}

    /* (non-Javadoc)
     * @see org.xamjwg.dom.XDocument#createCDataSection(java.lang.String)
     */
    public XCData createCDataSection(String text) {
        CData node = new CData(text);
        node.setOwnerDocument(this);
        return node;
    }
    
    /* (non-Javadoc)
     * @see org.xamjwg.dom.XDocument#createComment(java.lang.String)
     */
    public XComment createComment(String text) {
        Comment node = new Comment(text);
        node.setOwnerDocument(this);
        return node;
    }
    
    /* (non-Javadoc)
     * @see org.xamjwg.dom.XDocument#createTextNode(java.lang.String)
     */
    public XText createTextNode(String text) {
        Text node = new Text(text);
        node.setOwnerDocument(this);
        return node;
    }

    /* (non-Javadoc)
	 * @see org.xamjwg.dom.XDocument#createElement(java.lang.String)
	 */
	public XElement createElement(String name) throws XamjException {
	    BaseElement element = (BaseElement) ElementFactory.getInstance().createElement(name);
	    element.setOwnerDocument(this);
	    return element;
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XDocument#createActionElement(org.xamjwg.dom.ClientletListener)
	 */
	public XActionElement createActionElement(XamjListener delegate) {
		InternalActionElement element = new InternalActionElement(delegate);
		element.setOwnerDocument(this);
		return element;
	}

	private volatile XElement documentElement;
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XDocument#getDocumentElement()
	 */
	public XElement getDocumentElement() {
		return this.documentElement;
	}
	
	public void setDocumentElement(XElement element) throws XamjException {
		if(this.documentElement != null && element != this.documentElement) {
			throw new XamjException("Document element already set");
		}
		this.documentElement = element;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XNode#getParent()
	 */
	public XElement getParent() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XDocument#getWidgetElements()
	 */
	public XWidgetElement[] getWidgetElements() {
		XElement element = this.getDocumentElement();
		if(element instanceof RootElement) {
			RootElement re = (RootElement) element;
			XWidgetElement we = re.getWidgetElement();
			return we == null ? new XWidgetElement[0] : new XWidgetElement[] { we };
		}
		else {
			return new XWidgetElement[0];
		}
	}

	private String getWidth() {
		XElement element = this.getDocumentElement();
		if(element instanceof RootElement) {
			RootElement re = (RootElement) element;
			XHeadElement he = re == null ? null : re.getHeadElement();
			return he == null ? null : he.getDocumentWidth();
		}
		else {
			return null;
		}
	}

	private String getHeight() {
		XElement element = this.getDocumentElement();
		if(element instanceof RootElement) {
			RootElement re = (RootElement) element;
			XHeadElement he = re == null ? null : re.getHeadElement();
			return he == null ? null : he.getDocumentHeight();
		}
		else {
			return null;
		}
	}

	private String getTitle() {
		XElement element = this.getDocumentElement();
		if(element instanceof RootElement) {
			RootElement re = (RootElement) element;
			XHeadElement he = re == null ? null : re.getHeadElement();
			return he == null ? "" : he.getTitle();
		}
		else {
			return "";
		}
	}

	private String getIconURL() {
		XElement element = this.getDocumentElement();
		if(element instanceof RootElement) {
			RootElement re = (RootElement) element;
			XHeadElement he = re == null ? null : re.getHeadElement();
			return he == null ? "" : he.getDocumentIcon();
		}
		else {
			return "";
		}
	}
	
	public Component createClientletContent(ClientletContext context) {
		XWidgetElement[] widgets = this.getWidgetElements();
		if(widgets.length < 1) {
			throw new IllegalStateException("No widget elements in document");
		}
		XWidgetElement we = widgets[0];
		if(!(we instanceof BaseWidgetElement)) {
			throw new IllegalStateException("Widget element implementation unknown");
		}
		return ((BaseWidgetElement) we).getWidget();
	}

	public Properties getWindowProperties() {
		String relIconURL = this.getIconURL();
		java.net.URL iconURL;
		try {
			iconURL = relIconURL == null ? null : RequestEngine.createURL(context.getResponse().getResponseURL(), relIconURL);
		} catch(MalformedURLException mfu) {
			logger.log(Level.SEVERE, "createClientletContent()", mfu);
			iconURL = null;
		}
		Properties properties = new Properties();
		properties.setProperty("title", this.getTitle());
		properties.setProperty("width", this.getWidth());
		properties.setProperty("height", this.getHeight());
		if(iconURL != null) {
			properties.setProperty("icon", iconURL.toExternalForm());
		}
		return properties;
	}
}
