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

import java.awt.*;



import java.util.logging.*;

import org.lobobrowser.util.*;
import org.xamjwg.dom.XStyleElement;
import org.xamjwg.dom.XWidgetStyleElement;
import org.xamjwg.ext.domimpl.*;

/**
 * @author J. H. S.
 */
public class WidgetStyleElement extends BaseStyleElement implements XWidgetStyleElement {
    private static final java.util.logging.Logger logger = Logger.getLogger(WidgetStyleElement.class.getName()); 

    private Integer x = null;
    private Integer y = null;
    private Integer width = null;
    private Integer height = null;
    private String orientation = null;
    private String halign = null;
    private String valign = null;
    private String fill = null; 

    private volatile LayoutManager layoutManager;
    
    public WidgetStyleElement(String name) {
        super(name);
    }

    public void setBasedOn(XStyleElement style) {
        if(!(style instanceof WidgetStyleElement)) {
            throw new IllegalArgumentException("basedOn value is not a valid widget style element");
        }
        super.setBasedOn(style);
    }

    protected void fireOnBasedOn(XStyleElement styleElement) {
    	super.fireOnBasedOn(styleElement);
    	WidgetStyleElement bse = (WidgetStyleElement) styleElement;
    	if(this.x == null && bse.getX() != null) {
    		this.firePropertyChange("x", null, bse.getX());
    	}
    	if(this.y == null && bse.getY() != null) {
    		this.firePropertyChange("y", null, bse.getY());
    	}
    	if(this.width == null && bse.getWidth() != null) {
    		this.firePropertyChange("width", null, bse.getWidth());
    	}
    	if(this.height == null && bse.getHeight() != null) {
    		this.firePropertyChange("height", null, bse.getHeight());
    	}
    	if(this.orientation == null && bse.getOrientation() != null) {
    		// layoutManager invalidated elsewhere
    		this.firePropertyChange("orientation", null, bse.getOrientation());
    	}
    	if(this.halign == null && bse.getHalign() != null) {
    		this.firePropertyChange("halign", null, bse.getHalign());
    	}
    	if(this.valign == null && bse.getValign() != null) {
    		this.firePropertyChange("valign", null, bse.getValign());
    	}
    	if(this.fill == null && bse.getFill() != null) {
    		this.firePropertyChange("fill", null, bse.getFill());
    	}
    }
    
    private WidgetStyleElement properStyle(Object localValue) {
        if(localValue != null) {
            // TODO Perhaps enforce setter parameters to be non null.
            return this;
        }
        else {
            WidgetStyleElement basedOn = (WidgetStyleElement) this.basedOn;
            if(basedOn != null) {
                return basedOn;
            }
            return this;
        }
    }
    
    
    boolean hasHorizontalFill() {
        String fill = this.getFill();
        return "horizontal".equals(fill) || "both".equals(fill);
    }

    boolean hasVerticalFill() {
        String fill = this.getFill();
        return "vertical".equals(fill) || "both".equals(fill);
    }
    
    LayoutManager getLayoutManager() {
        if(this.layoutManager == null) {
            String layout = this.getOrientation();
            if("horizontal".equals(layout)) {
                this.layoutManager = BoxStyleLayout.HORIZONTAL;
            }
            else if("vertical".equals(layout)) {
                this.layoutManager = BoxStyleLayout.VERTICAL;
            }
            else if (layout == null) {
                //if(logger.isLoggable(Level.INFO))logger.info("getStyleLayout(): No layout defined");
                this.layoutManager = BoxStyleLayout.HORIZONTAL;
            }    
            else {
                logger.log(Level.SEVERE, "getStyleLayout(): Layout unknown: " + layout);
                this.layoutManager = BoxStyleLayout.HORIZONTAL;                
            }
        }
        return this.layoutManager;
    }
    
    /* (non-Javadoc)
     * @see net.sourceforge.xamj.domimpl.AbstractElement#firePropertyChange(java.lang.String, java.lang.Object, java.lang.Object)
     */
    protected void firePropertyChange(String name, Object oldValue,
            Object newValue) {
        if("orientation".equals(name)) {
            this.layoutManager = null;
        }
        super.firePropertyChange(name, oldValue, newValue);
    }

    /**
     * @return Returns
     */
    public String getFill() {
        return this.properStyle(this.fill).fill;
    }
    /**
     * @param fill 
     */
    public void setFill(String fill) {
        if(fill == null) {
            throw new IllegalArgumentException("value is null");
        }
        Object oldFill = this.fill;
        if(!fill.equals(oldFill)) {	
            this.fill = fill;
            this.firePropertyChange("fill", oldFill, fill);
        }            
    }
    /**
     * @return Returns
     */
    public Integer getHeight() {
        return this.properStyle(this.height).height;
    }
    /**
     * @param height 
     */
    public void setHeight(Integer value) {
        if(value == null) {
            throw new IllegalArgumentException("value is null");
        }
        Object oldValue = this.height;
        if(!Objects.equals(value, oldValue)) {	
            this.height = value;
            this.firePropertyChange("height", oldValue, value);
        }            
    }
    /**
     * @return Returns
     */
    public String getOrientation() {
        return this.properStyle(this.orientation).orientation;
    }
    /**
     * @param layout 
     */
    public void setOrientation(String value) {
        if(value == null) {
            throw new IllegalArgumentException("value is null");
        }
        Object oldValue = this.orientation;
        if(!Objects.equals(value, oldValue)) {	
            this.orientation = value;
            this.firePropertyChange("orientation", oldValue, value);
        }            
    }
    /**
     * @return Returns
     */
    public Integer getWidth() {
        return this.properStyle(this.width).width;
    }
    /**
     * @param width 
     */
    public void setWidth(Integer value) {
        if(value == null) {
            throw new IllegalArgumentException("value is null");
        }
        Object oldValue = this.width;
        if(!Objects.equals(value, oldValue)) {	
            this.width = value;
            this.firePropertyChange("width", oldValue, value);
        }            
    }
    /**
     * @return Returns
     */
    public Integer getX() {
        return this.properStyle(this.x).x;
    }
    /**
     * @param x 
     */
    public void setX(Integer value) {
        if(value == null) {
            throw new IllegalArgumentException("value is null");
        }
        Object oldValue = this.x;
        if(!Objects.equals(value, oldValue)) {	
            this.x = value;
            this.firePropertyChange("x", oldValue, value);
        }            
    }
    /**
     * @return Returns
     */
    public Integer getY() {
        return this.properStyle(this.y).y;
    }
    /**
     * @param y 
     */
    public void setY(Integer value) {
        if(value == null) {
            throw new IllegalArgumentException("value is null");
        }
        Object oldValue = this.y;
        if(!Objects.equals(value, oldValue)) {	
            this.y = value;
            this.firePropertyChange("y", oldValue, value);
        }            
    }
    
    /**
     * @return Returns
     */
    public String getHalign() {
        return this.properStyle(this.halign).halign;
    }
    /**
     * @param halign 
     */
    public void setHalign(String value) {
        if(value == null) {
            throw new IllegalArgumentException("value is null");
        }
        Object oldValue = this.halign;
        if(!Objects.equals(value, oldValue)) {	
            this.halign = value;
            this.firePropertyChange("halign", oldValue, value);
        }            
    }

    /**
     * @return Returns
     */
    public String getValign() {
        return this.properStyle(this.valign).valign;
    }
    /**
     * @param valing 
     */
    public void setValign(String value) {
        if(value == null) {
            throw new IllegalArgumentException("value is null");
        }
        Object oldValue = this.valign;
        if(!Objects.equals(value, oldValue)) {	
            this.valign = value;
            this.firePropertyChange("valign", oldValue, value);
        }            
    }

    // To add a property:
    // -- Getters and setters should be like existing ones.
    // -- Change basedOn setter accordingly.
}
