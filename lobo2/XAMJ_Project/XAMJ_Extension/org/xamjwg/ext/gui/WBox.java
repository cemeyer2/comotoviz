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

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Insets;

//import java.util.logging.*;

/**
 * @author J. H. S.
 */
public class WBox extends Widget {
    //private static final java.util.logging.Logger logger = Logger.getLogger(WBox.class); 
        
    public WBox() {
        super();
        this.setLayout(this.style.getLayoutManager());
    }
    
    protected void onStyleChange(String propertyName) {
    	super.onStyleChange(propertyName);
    	if("orientation".equals(propertyName)) {
    		this.setLayout(this.style.getLayoutManager());
    		if(EventQueue.isDispatchThread()) {
    			this.refresh();
    		}   		
    	}
    }
    
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics arg0) {
		super.paintComponent(arg0);
	}
	
	public int getDefaultWidth() {
		String orientation = this.style.getOrientation();
		boolean isHorizontal = "horizontal".equals(orientation);
        Component[] components = this.getComponents();
        int count = components.length;
        int prefWidth = 0;
        for(int i = 0; i < count; i++) {
            Widget widget = (Widget) components[i];
            int pw = widget.getPreferredWidth();
        	if(pw == -1) {
        		return -1;
        	}
            if(isHorizontal) {
            	prefWidth += pw;
            }
            else {
            	if(pw > prefWidth) {
            		prefWidth = pw;
            	}
            }
        }        
        Insets insets = this.getInsets();
        prefWidth += insets.left + insets.right;
        return prefWidth;
	}

	public int getDefaultHeight() {
		String orientation = this.style.getOrientation();
		boolean isHorizontal = "horizontal".equals(orientation);
        Component[] components = this.getComponents();
        int count = components.length;
        int prefHeight = 0;
        for(int i = 0; i < count; i++) {
            Widget widget = (Widget) components[i];
            int ph = widget.getPreferredHeight();
            if(ph == -1) {
            	return -1;
            }
            if(isHorizontal) {
            	if(ph > prefHeight) {
            		prefHeight = ph;
            	}
            }
            else {
            	prefHeight += ph;
            }
        }        
        Insets insets = this.getInsets();
        prefHeight += insets.top + insets.bottom;
        return prefHeight;
	}	
	
}
