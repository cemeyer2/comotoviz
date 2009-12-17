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
package org.xamjwg.dom;

/**
 * Base interface for all widget elements.
 * @author J. H. S.
 */
public interface XWidgetElement extends XElement {
	/**
	 * Gets a special local instance of
	 * XWidgetStyleElement.
  	 * @attribute This is a read-only attribute that may only
	 * be used via fully qualified names of a sub-attributes,
	 * e.g. <code>style.fill</code> or <code>style.backgroundColor</code>. 
	 */
    public XWidgetStyleElement getStyle();
    public boolean isEnabled();
    public void setEnabled(boolean value);
    public boolean isVisible();
    public void setVisible(boolean value);
    public String getToolTip();
    public void setToolTip(String value);
//    public void replaceWidgetElements(String url) throws java.net.MalformedURLException;
}
