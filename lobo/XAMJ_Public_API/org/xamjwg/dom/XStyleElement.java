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
 * Base interface for style elements.
 * @author J. H. S.
 */
public interface XStyleElement extends XElement {
    public XStyleElement getBasedOn();
    public void setBasedOn(XStyleElement value);
    
	public String getFontFamily();
	public Double getFontSize();
	public String getFontStyle();
	public String getFontVariant();
	public String getFontWeight();
	public String getColor();
	public String getBackgroundColor();

	public void setFontFamily(String value);
	
	//Note: It must be Double to allow conversion from text.
	public void setFontSize(Double value);
	
	public void setFontStyle(String value);
	public void setColor(String value);
	public void setBackgroundColor(String value);
	public void setFontVariant(String value);
	public void setFontWeight(String value);
}
