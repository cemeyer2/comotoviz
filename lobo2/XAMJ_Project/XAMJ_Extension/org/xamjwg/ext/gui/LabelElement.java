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
 * Created on Mar 19, 2005
 */
package org.xamjwg.ext.gui;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.lobobrowser.async.AsyncResult;
import org.lobobrowser.async.AsyncResultEvent;
import org.lobobrowser.async.AsyncResultListener;
import org.lobobrowser.request.*;
import org.lobobrowser.util.*;
import org.xamjwg.dom.XLabelElement;
import java.util.logging.*;

/**
 * @author J. H. S.
 */
public class LabelElement extends BaseWidgetWithTextElement implements XLabelElement {
	private static final Logger logger = Logger.getLogger(LabelElement.class.getName());
	
    public LabelElement(String name) {
        super(name);
    }
    
    /* (non-Javadoc)
     * @see net.sourceforge.xamj.domimpl.gui.BaseWidgetElement#createComponent()
     */
    protected Widget createWidget() {
        return new WLabel();
    }

    public void setText(String value) {
        String oldValue = ((WLabel) this.widget).getText();
        if(!Objects.equals(value, oldValue)) {
            ((WLabel) this.widget).setText(value);
            this.firePropertyChange("text", oldValue, value);
        }   
    }
    
    public String getText() {
        return ((WLabel) this.getWidget()).getText();
    }
    
    private String icon;
    
    public void setIcon(final String value) {	
        String oldValue = this.icon;
        if(!Objects.equals(value, oldValue)) {
        	try {
        		AsyncResult<byte[]> ar = RequestEngine.getInstance().loadBytesAsync(value);
        		ar.addResultListener(new AsyncResultListener<byte[]>() {
					public void exceptionReceived(AsyncResultEvent<Throwable> event) {
        				logger.log(Level.WARNING, "setIcon(): Unable to retrieve image: " + value, (Throwable) event.getResult());
					}

					public void resultReceived(AsyncResultEvent<byte[]> event) {
        				byte[] contentBytes = event.getResult();
        				Icon icon = new ImageIcon(contentBytes);
        				((WLabel) LabelElement.this.widget).setIcon(icon);
        				didChange();
					}
        			
        		});
        		this.firePropertyChange("icon", oldValue, value);
        	} catch(java.net.MalformedURLException mfu) {
        		throw new IllegalArgumentException("Malformed URL: " + value);
        	}
        }   
    }

    public String getIcon() {
    	return this.icon;
    }
    
    private String disabledIcon;
    
    public void setDisabledIcon(final String value) {	
        String oldValue = this.disabledIcon;
        if(!Objects.equals(value, oldValue)) {
        	try {
        		AsyncResult<byte[]> ar = RequestEngine.getInstance().loadBytesAsync(value);
        		ar.addResultListener(new AsyncResultListener<byte[]>() {
					public void exceptionReceived(AsyncResultEvent<Throwable> event) {
        				logger.log(Level.WARNING, "setDisabledIcon(): Unable to retrieve image: " + value, (Throwable) event.getResult());
					}

					public void resultReceived(AsyncResultEvent<byte[]> event) {
        				byte[] contentBytes = event.getResult();
        				Icon icon = new ImageIcon(contentBytes);
        				((WLabel) LabelElement.this.widget).setDisabledIcon(icon);
        				didChange();
					}
        			
        		});
        		this.firePropertyChange("disabledIcon", oldValue, value);
        	} catch(java.net.MalformedURLException mfu) {
        		throw new IllegalArgumentException("Malformed URL: " + value);
        	}
        }   
    }

    public String getDisabledIcon() {
    	return this.disabledIcon;
    }
    
    protected void didChange() {
    }
}
