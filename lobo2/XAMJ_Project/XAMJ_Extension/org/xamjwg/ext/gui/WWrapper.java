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

import javax.swing.*;

import org.lobobrowser.util.*;
import org.lobobrowser.util.gui.WrapperLayout;

/**
 * @author J. H. S.
 */
public abstract class WWrapper extends Widget {
    protected java.awt.Component component;
    
    public WWrapper() {
    	this(false);
    }

    public WWrapper(boolean scrollable) {
        super();
        this.setOpaque(false);
        this.setLayout(WrapperLayout.getInstance());
        this.component = this.createComponent();
        if(scrollable) {
        	this.add(new JScrollPane(this.component));
        }
        else {
        	this.add(this.component);
        }
    }
    
    public boolean delegateHasFocus() {
    	return this.component.hasFocus();
    }
    
    public void setForeground(java.awt.Color c) {
    	super.setForeground(c);
    	this.component.setForeground(c);
    }

    public void setFont(java.awt.Font f) {
    	super.setFont(f);
    	this.component.setFont(f);
    }
    
    public void setEnabled(boolean value) {
    	super.setEnabled(value);
    	this.component.setEnabled(value);
    }
    
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#getToolTipText()
	 */
	public String getToolTipText() {
		if(this.component instanceof JComponent) {
			return ((JComponent) this.component).getToolTipText();
		}
		else {
			return this.getToolTipText();
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setToolTipText(java.lang.String)
	 */
	public void setToolTipText(String arg0) {
		if(this.component instanceof JComponent) {
			((JComponent) this.component).setToolTipText(arg0);
		}
		else {
			this.setToolTipText(arg0);
		}
	}

	protected abstract java.awt.Component createComponent();
}
