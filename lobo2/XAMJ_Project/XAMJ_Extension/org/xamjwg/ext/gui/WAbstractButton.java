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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeListener;

/**
 * @author J. H. S.
 */
public abstract class WAbstractButton extends WWrapper {
    /**
     * 
     */
    public WAbstractButton() {
        super();
    }

    /* (non-Javadoc)
     * @see net.sourceforge.xamj.domimpl.gui.WWrapper#createComponent()
     */
    protected Component createComponent() {
        return this.createButton();
    }
    
    protected abstract AbstractButton createButton();
    
    public void setText(String text) {
        ((AbstractButton) this.component).setText(text);
    }
    
    public String getText() {
        return ((AbstractButton) this.component).getText();
    }
    
    public void setIcon(Icon icon) {
    	((AbstractButton) this.component).setIcon(icon);
    }

    public void setDisabledIcon(Icon icon) {
    	((AbstractButton) this.component).setDisabledIcon(icon);
    }
    
	public void setSelected(boolean value) {
		((AbstractButton) this.component).setSelected(value);
	}
	
	public boolean isSelected() {
		return ((AbstractButton) this.component).isSelected();		
	}
	
	public void addChangeListener(ChangeListener listener) {
		((AbstractButton) this.component).addChangeListener(listener);
	}

    public void doClick() {
        ((AbstractButton) this.component).doClick();
    }
    
    public void addActionListener(ActionListener listener) {
        ((AbstractButton) this.component).addActionListener(listener);
    }
}
