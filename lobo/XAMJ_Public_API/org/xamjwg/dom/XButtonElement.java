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
package org.xamjwg.dom;

/**
 * A button element.
 * @author J. H. S.
 */
public interface XButtonElement extends XWidgetElement {
	/**
	 * Sets a URL to which the XAMJ browser navigates
	 * when the button is pressed. 
	 */
	public void setHref(XamjEvaluatable<String> value);
	public XamjEvaluatable<String> getHref();
	
	/**
	 * Sets the button caption.
	 */
    public void setText(String text);
    public String getText();
    
    /**
     * Sets the button icon.
     */
    public void setIcon(String value);
    public String getDisabledIcon();
    
    /**
     * Sets the icon shown when the button is disabled.
     */
    public void setDisabledIcon(String value);
    public String getIcon();
    
    /**
     * Programmatically clicks the button.
     */
    public void doClick();

    /**
     * Programmatically clicks the button, which
     * appears pressed for the given amount of time.
     */
    public void doClick(int pressTime);
    public XButtonGroupElement getGroup();
    
// STYLE:
//    /**
//     * Sets the gap between the icon on the button
//     * caption.
//     */
//    public void setIconTextGap(int gap);
//    
//    public int getIconTextGap();
    
    /**
     * Sets the button group. Typically several radio
     * buttons will be set up with a common button group. 
     */
    public void setGroup(XButtonGroupElement value);

}