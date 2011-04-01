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
 * Style element used by widget elements.
 * @author J. H. S.
 */
public interface XWidgetStyleElement extends XStyleElement {
    public String getFill();
    public void setFill(String fill);
    public Integer getHeight();
    public void setHeight(Integer height);
    public String getOrientation();
    public void setOrientation(String layout);
    public Integer getWidth();
    public void setWidth(Integer width);
    public Integer getX();
    public void setX(Integer x);
    public Integer getY();
    public void setY(Integer y);
    public String getHalign();
    public void setHalign(String value);
    public String getValign();
    public void setValign(String value);
    public XStyleElement getBasedOn();
    
    /**
     * Sets another style element this style element is based on.
     * Style properties are obtained from the <code>basedOn</code> style
     * if not set locally. Note that widget and markup elements
     * have an internal style element instance accessed via
     * their <code>style</code> property. So setting the
     * <code>style.basedOn</code> property on a widget or markup
     * element is how style-sheet styles are shared.
     */
    public void setBasedOn(XStyleElement value);    
}
