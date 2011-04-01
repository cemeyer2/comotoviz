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
 * Created on Apr 9, 2005
 */
package org.xamjwg.ext.gui;

import javax.swing.Icon;
import javax.swing.ImageIcon;


import org.lobobrowser.async.AsyncResult;
import org.lobobrowser.async.AsyncResultEvent;
import org.lobobrowser.async.AsyncResultListener;
import org.lobobrowser.clientlet.*;
import org.lobobrowser.request.RequestEngine;
import org.xamjwg.dom.XNode;
import org.xamjwg.dom.XTabbedBoxElement;
import java.util.logging.*;

/**
 * @author J. H. S.
 */
public class TabbedBoxElement extends BaseWidgetElement implements XTabbedBoxElement {
	private static final Logger logger = Logger.getLogger(TabbedBoxElement.class.getName());
	
	/**
	 * @param name
	 */
	public TabbedBoxElement(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.BaseWidgetElement#createWidget()
	 */
	protected Widget createWidget() {
		return new WTabbedBox();
	}
	
	private void requestIcon(final WTabbedBox tabbedBox, final Widget tab, final String iconURL, final boolean disabled) {
		try {
    		AsyncResult<byte[]> ar = RequestEngine.getInstance().loadBytesAsync(iconURL);
    		ar.addResultListener(new AsyncResultListener<byte[]>() {
				public void exceptionReceived(AsyncResultEvent<Throwable> event) {
    				logger.log(Level.WARNING, "requestIcon(): Unable to retrieve image: " + iconURL, (Throwable) event.getResult());
				}

				public void resultReceived(AsyncResultEvent<byte[]> event) {
    				byte[] contentBytes = event.getResult();
    				Icon icon = new ImageIcon(contentBytes);
					int index = tabbedBox.indexOfComponent(tab);
					if(disabled) {
						tabbedBox.setDisabledIconAt(index, icon);
					}
					else {
						tabbedBox.setIconAt(index, icon);
					}
				}
    		});
		} catch(java.net.MalformedURLException mfu) {
			throw new IllegalArgumentException("Malformed URL: " + iconURL);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#appendChild(org.xamjwg.dom.XNode)
	 */
	public void appendChild(XNode node) {
		if(node instanceof TabElement) {
			TabElement tabElement = (TabElement) node;
			Widget tab = tabElement.widget;
			WTabbedBox tabbedBox = (WTabbedBox) this.widget;
			tabbedBox.addTab(tabElement.getTitle(), tab);
			final String iconURL = tabElement.getIcon();
			final String disabledIconURL = tabElement.getDisabledIcon();
			if(iconURL != null) {
				this.requestIcon(tabbedBox, tab, iconURL, false);
			}
			if(disabledIconURL != null) {
				this.requestIcon(tabbedBox, tab, disabledIconURL, true);
			}
			
			//TODO Add propertyChange event listeners to tab
			//TODO Changes to enabled, visible
		}
		this.appendChildImpl(node);
	}
	
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#getElementValue()
	 */
	public Object getElementValue() {
		return this;
	}
}
