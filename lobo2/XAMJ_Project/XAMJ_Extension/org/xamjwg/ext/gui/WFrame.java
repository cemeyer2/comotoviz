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
 * Created on Mar 29, 2005
 */
package org.xamjwg.ext.gui;

import java.awt.Dimension;
import javax.swing.border.BevelBorder;

import org.lobobrowser.clientlet.*;
import org.lobobrowser.gui.*;
import org.lobobrowser.ua.NavigatorFrame;
import org.lobobrowser.util.gui.WrapperLayout;
import org.xamjwg.dom.*;

/**
 * @author J. H. S.
 */
public class WFrame extends Widget {
	/**
     * 
     */
    public WFrame() {
        super();
        this.setBorder(new BevelBorder(BevelBorder.RAISED));
        this.setLayout(WrapperLayout.getInstance());
		FramePanel framePanel = new FramePanel();
		this.framePanel = framePanel;    	
		this.add(framePanel);
    }
    
    public WFrame(FramePanel framePanel) {
        super();
        this.setBorder(new BevelBorder(BevelBorder.RAISED));
        this.setLayout(WrapperLayout.getInstance());
        this.framePanel = framePanel;
        this.add(framePanel); 
    }

    private volatile FramePanel framePanel;
    
    public FramePanel getFramePanel() {
    	return this.framePanel;
    }
    
	public java.awt.Dimension getPreferredSize() {
		// needed for split-box
		return new Dimension(400, 400);
	}

	public int getDefaultWidth() {
		return -1;
	}

	public int getDefaultHeight() {
		return -1;
	}	

	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
	}
	
	public String toString() {
		return "WFrame[parent=" + this.getParent() + ",bounds=" + this.getBounds() + ",preferredSize=" + this.getPreferredSize() + "]"; 
	}
}
