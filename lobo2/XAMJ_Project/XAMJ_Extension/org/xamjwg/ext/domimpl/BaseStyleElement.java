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
package org.xamjwg.ext.domimpl;



import java.util.logging.*;

import org.lobobrowser.util.*;
import org.xamjwg.dom.*;

import java.beans.*;
import java.lang.reflect.*;

/**
 * @author J. H. S.
 */
public abstract class BaseStyleElement extends BaseElement implements XStyleElement {
    private static final java.util.logging.Logger logger = Logger.getLogger(BaseStyleElement.class.getName());
    private static final Bean bean = new Bean(BaseStyleElement.class);
    protected XStyleElement basedOn;
	protected String fontFamily;
	protected String fontStyle;
	protected Double fontSize;
	protected String color;
	protected String backgroundColor;
	protected String fontVariant;
	protected String fontWeight;

    private final PropertyChangeListener pclistener = new LocalPropertyChangeListener();
        
    public BaseStyleElement(String name) {
        super(name);
    }

    /* (non-Javadoc)
     * @see org.xamjwg.dom.XStyleElement#getBasedOn()
     */
    public XStyleElement getBasedOn() {
        return this.basedOn;
    }

    /**
     * @param basedOn 
     */
    public void setBasedOn(XStyleElement basedOn) {
    	if(!(basedOn instanceof BaseStyleElement)) {
    		throw new IllegalArgumentException("Invalid XStyleElement implementation");
    	}
        Object oldBasedOn = this.basedOn;
        if(!basedOn.equals(oldBasedOn)) {
            if(oldBasedOn instanceof BaseStyleElement) {
                ((BaseStyleElement) oldBasedOn).removePropertyChangeListener(pclistener);
            }
            if(basedOn instanceof BaseStyleElement) {
                ((BaseStyleElement) basedOn).addPropertyChangeListener(pclistener);
            }
            this.basedOn = basedOn;
            this.firePropertyChange("basedOn", oldBasedOn, basedOn);
            this.fireOnBasedOn(basedOn);
        }
    }

    protected void fireOnBasedOn(XStyleElement styleElement) {
    	BaseStyleElement bse = (BaseStyleElement) styleElement;
    	if(this.fontFamily == null && bse.getFontFamily() != null) {
    		this.firePropertyChange("fontFamily", null, bse.getFontFamily());
    	}
    	if(this.fontStyle == null && bse.getFontStyle() != null) {
    		this.firePropertyChange("fontStyle", null, bse.getFontStyle());
    	}
    	if(this.fontSize == null && bse.getFontSize() != null) {
    		this.firePropertyChange("fontSize", null, bse.getFontSize());
    	}
    	if(this.color == null && bse.getColor() != null) {
    		this.firePropertyChange("color", null, bse.getColor());
    	}
    	if(this.backgroundColor == null && bse.getBackgroundColor() != null) {
    		this.firePropertyChange("backgroundColor", null, bse.getBackgroundColor());
    	}
    	if(this.fontVariant == null && bse.getFontVariant() != null) {
    		this.firePropertyChange("fontVariant", null, bse.getFontVariant());
    	}
    	if(this.fontWeight == null && bse.getFontWeight() != null) {
    		this.firePropertyChange("fontWeight", null, bse.getFontWeight());
    	}    	
    }
    
    private BaseStyleElement properStyle(Object localValue) {
        if(localValue != null) {
            return this;
        }
        else {
            BaseStyleElement basedOn = (BaseStyleElement) this.basedOn;
            if(basedOn != null) {
            	//TODO: It should go to parent of basedOn until
            	//non-null value found.
                return basedOn;
            }
            return this;
        }
    }
    
    /* (non-Javadoc)
	 * @see org.xamjwg.dom.XMarkupStyleElement#getFontFamily()
	 */
	public String getFontFamily() {
		return this.properStyle(this.fontFamily).fontFamily;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMarkupStyleElement#getFontSize()
	 */
	public Double getFontSize() {
		return this.properStyle(this.fontSize).fontSize;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMarkupStyleElement#getFontStyle()
	 */
	public String getFontStyle() {
		return this.properStyle(this.fontStyle).fontStyle;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMarkupStyleElement#getColor()
	 */
	public String getColor() {
		return this.properStyle(this.color).color;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMarkupStyleElement#setFontFamily(java.lang.String)
	 */
	public void setFontFamily(String value) {
		Object oldValue = this.fontFamily;
		if(!Objects.equals(oldValue, value)) {
			this.fontFamily = value;
			this.firePropertyChange("fontFamily", oldValue, value);
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMarkupStyleElement#setFontSize(java.lang.Float)
	 */
	public void setFontSize(Double value) {
		Object oldValue = this.fontSize;
		if(!Objects.equals(oldValue, value)) {
			this.fontSize = value;
			this.firePropertyChange("fontSize", oldValue, value);
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMarkupStyleElement#setFontStyle(java.lang.String)
	 */
	public void setFontStyle(String value) {
		Object oldValue = this.fontStyle;
		if(!Objects.equals(oldValue, value)) {
			this.fontStyle = value;
			this.firePropertyChange("fontStyle", oldValue, value);
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMarkupStyleElement#setColor(java.lang.String)
	 */
	public void setColor(String value) {
		Object oldValue = this.color;
		if(!Objects.equals(oldValue, value)) {
			this.color = value;
			this.firePropertyChange("color", oldValue, value);
		}
	}
	
	/**
	 * @return Returns the backgroundColor.
	 */
	public String getBackgroundColor() {
		return this.properStyle(this.backgroundColor).backgroundColor;
	}
	/**
	 * @param backgroundColor The backgroundColor to set.
	 */
	public void setBackgroundColor(String value) {
		Object oldValue = this.backgroundColor;
		if(!Objects.equals(oldValue, value)) {
			this.backgroundColor = value;
			this.firePropertyChange("backgroundColor", oldValue, value);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMarkupStyleElement#getFontVariant()
	 */
	public String getFontVariant() {
		return this.properStyle(this.fontVariant).fontVariant;
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMarkupStyleElement#getFontWeight()
	 */
	public String getFontWeight() {
		return this.properStyle(this.fontWeight).fontWeight;
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMarkupStyleElement#setFontVariant(java.lang.String)
	 */
	public void setFontVariant(String value) {
		Object oldValue = this.fontVariant;
		if(!Objects.equals(oldValue, value)) {
			this.fontVariant = value;
			this.firePropertyChange("fontVariant", oldValue, value);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMarkupStyleElement#setFontWeight(java.lang.String)
	 */
	public void setFontWeight(String value) {
		Object oldValue = this.fontWeight;
		if(!Objects.equals(oldValue, value)) {
			this.fontWeight = value;
			this.firePropertyChange("fontWeight", oldValue, value);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#appendChild(org.xamjwg.dom.XNode)
	 */
	public void appendChild(XNode node) {
		throw new IllegalArgumentException("Style elements do not accept children elements");
	}    
	
	protected void onParentPropertyChange(PropertyChangeEvent event) {
        try {
            String propertyName = event.getPropertyName();
            PropertyDescriptor pd = bean.getPropertyDescriptor(propertyName);
            if(pd != null) {
                Method getter = pd.getReadMethod();
                Object value = getter.invoke(this, new Object[0]);
                if(value == null) {
                    this.firePropertyChange(propertyName, event.getOldValue(), event.getNewValue());
                }
            }
        } catch(Exception err) {
            logger.log(Level.SEVERE, "onParentPropertyChange()", err);
        }
    }

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.BaseNode#setParent(org.xamjwg.dom.XElement)
	 */
	public void setParent(XElement parent) {
		if(parent != null && !(parent instanceof XStyleSheetElement)) {
			throw new IllegalArgumentException("Element named '" + this.getNodeName() + "' may not be a child of '" + parent.getNodeName() + "'");
		}
		super.setParent(parent);
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#getElementValue()
	 */
	public Object getElementValue() {
		return this;
	}	

    private class LocalPropertyChangeListener implements PropertyChangeListener {
        /* (non-Javadoc)
         * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
         */
        public void propertyChange(PropertyChangeEvent arg0) {
            onParentPropertyChange(arg0);
        }
    }
}
