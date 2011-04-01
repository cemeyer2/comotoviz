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

import org.lobobrowser.util.*;
import org.xamjwg.dom.XProgressBarElement;

/**
 * @author J. H. S.
 */
public class ProgressBarElement extends BaseWidgetElement implements
		XProgressBarElement {

	/**
	 * @param name
	 */
	public ProgressBarElement(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.gui.BaseWidgetElement#createWidget()
	 */
	protected Widget createWidget() {
		return new WProgressBar();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#getMaximum()
	 */
	public int getMaximum() {
		return ((WProgressBar) this.widget).getMaximum();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#getMinimum()
	 */
	public int getMinimum() {
		return ((WProgressBar) this.widget).getMinimum();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#getPercentComplete()
	 */
	public double getPercentComplete() {
		return ((WProgressBar) this.widget).getPercentComplete();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#isTextPainted()
	 */
	public boolean isTextPainted() {
		return ((WProgressBar) this.widget).isStringPainted();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#isIndeterminate()
	 */
	public boolean isIndeterminate() {
		return ((WProgressBar) this.widget).isIndeterminate();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#getValue()
	 */
	public int getValue() {
		return ((WProgressBar) this.widget).getValue();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#getText()
	 */
	public String getText() {
		return ((WProgressBar) this.widget).getString();
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#setMaximum(int)
	 */
	public void setMaximum(int value) {
		int oldValue = this.getMaximum();
		if(oldValue != value) {
			((WProgressBar) this.widget).setMaximum(value);
			this.firePropertyChange("maximum", new Integer(oldValue), new Integer(value));
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#setMinimum(int)
	 */
	public void setMinimum(int value) {
		int oldValue = this.getMinimum();
		if(oldValue != value) {
			((WProgressBar) this.widget).setMinimum(value);
			this.firePropertyChange("minimum", new Integer(oldValue), new Integer(value));
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#setText(java.lang.String)
	 */
	public void setText(String value) {
		String oldValue = this.getText();
		if(!Objects.equals(oldValue, value)) {
			((WProgressBar) this.widget).setString(value);
			this.firePropertyChange("text", oldValue, value);
		}	
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#setTextPainted(boolean)
	 */
	public void setTextPainted(boolean value) {
		boolean oldValue = this.isTextPainted();
		if(oldValue != value) {
			((WProgressBar) this.widget).setStringPainted(value);
			this.firePropertyChange("textPainted", Boolean.valueOf(oldValue), Boolean.valueOf(value));
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#setIndeterminate(boolean)
	 */
	public void setIndeterminate(boolean value) {
		boolean oldValue = this.isIndeterminate();
		if(oldValue != value) {
			((WProgressBar) this.widget).setIndeterminate(value);
			this.firePropertyChange("indeterminate", Boolean.valueOf(oldValue), Boolean.valueOf(value));
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XProgressBarElement#setValue(int)
	 */
	public void setValue(int value) {
		int oldValue = this.getValue();
		if(oldValue != value) {
			((WProgressBar) this.widget).setValue(value);
			this.firePropertyChange("value", new Integer(oldValue), new Integer(value));
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#getElementValue()
	 */
	public Object getElementValue() {
		return Integer.valueOf(this.getValue());
	}
}
