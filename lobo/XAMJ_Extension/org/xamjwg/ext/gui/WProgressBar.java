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
 * Created on Apr 15, 2005
 */
package org.xamjwg.ext.gui;

import java.awt.Component;
import javax.swing.*;

/**
 * @author J. H. S.
 */
public class WProgressBar extends WWrapper {
	/**
	 * 
	 */
	public WProgressBar() {
		super();
		((JProgressBar) this.component).setStringPainted(true);
	}

	/**
	 * @param scrollable
	 */
	public WProgressBar(boolean scrollable) {
		super(scrollable);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.WWrapper#createComponent()
	 */
	protected Component createComponent() {
		return new JProgressBar();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#getMaximum()
	 */
	public int getMaximum() {
		return ((JProgressBar) this.component).getMaximum();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#getMinimum()
	 */
	public int getMinimum() {
		return ((JProgressBar) this.component).getMinimum();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#getPercentComplete()
	 */
	public double getPercentComplete() {
		return ((JProgressBar) this.component).getPercentComplete();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#isTextPainted()
	 */
	public boolean isStringPainted() {
		return ((JProgressBar) this.component).isStringPainted();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#isIndeterminate()
	 */
	public boolean isIndeterminate() {
		return ((JProgressBar) this.component).isIndeterminate();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#getValue()
	 */
	public int getValue() {
		return ((JProgressBar) this.component).getValue();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#getText()
	 */
	public String getString() {
		return ((JProgressBar) this.component).getString();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#setMaximum(int)
	 */
	public void setMaximum(int value) {
		((JProgressBar) this.component).setMaximum(value);
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#setMinimum(int)
	 */
	public void setMinimum(int value) {
		((JProgressBar) this.component).setMinimum(value);
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#setText(java.lang.String)
	 */
	public void setString(String value) {
		((JProgressBar) this.component).setString(value);
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#setTextPainted(boolean)
	 */
	public void setStringPainted(boolean value) {
		((JProgressBar) this.component).setStringPainted(value);
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#setIndeterminate(boolean)
	 */
	public void setIndeterminate(boolean value) {
		((JProgressBar) this.component).setIndeterminate(value);
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#setValue(int)
	 */
	public void setValue(int value) {
		((JProgressBar) this.component).setValue(value);
	}
	
	public int getDefaultWidth() {
		return -1;
	}

	public int getDefaultHeight() {
		return super.getDefaultHeight();
	}	
}
