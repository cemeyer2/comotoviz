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

/**
 * @author J. H. S.
 */
public class WLabel extends WWrapper {
    public WLabel() {
        super();
        //setBorder(new BevelBorder(BevelBorder.RAISED));
    }
    
    public WLabel(String text) {
        this();
        ((JLabel) this.component).setText(text);
    }
    
    /* (non-Javadoc)
     * @see net.sourceforge.xamj.domimpl.gui.WWrapper#createComponent()
     */
    protected java.awt.Component createComponent() {
        JLabel label = new JLabel();
        return label;
    }
    
    public void setText(String text) {
        ((JLabel) this.component).setText(text);
    }
    
    public String getText() {
        return ((JLabel) this.component).getText();
    }
    
    public void setIcon(Icon icon) {
    	((JLabel) this.component).setIcon(icon);
    }

    public void setDisabledIcon(Icon icon) {
    	((JLabel) this.component).setDisabledIcon(icon);
    }
    
    public String toString() {
    	return "WLabel[text=" + this.getText() + ",preferredSize=" + this.getPreferredSize() + "]";
    }
}
