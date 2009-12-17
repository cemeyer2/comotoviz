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
 * Created on Apr 16, 2005
 */
package org.xamjwg.ext.markup;


import org.lobobrowser.util.*;
import org.xamjwg.dom.XMarkupStyleElement;
import org.xamjwg.dom.XStyleElement;
import org.xamjwg.ext.domimpl.BaseStyleElement;

import java.util.logging.*;

/**
 * @author J. H. S.
 */
public class MarkupStyleElement extends BaseStyleElement implements
		XMarkupStyleElement {
	private static final Logger logger = Logger.getLogger(MarkupStyleElement.class.getName());
	
	private String textDecoration;
	private String lineBreakAfter;
	private String paragraphBreakAfter;
	private String lineBreakBefore;
	private String paragraphBreakBefore;
	private int iLineBreakAfter = -1;
	private int iParagraphBreakAfter = -1;
	private int iLineBreakBefore = -1;
	private int iParagraphBreakBefore = -1;
	private boolean highlight;
	
	public static final int BREAK_AUTO = 0;
	public static final int BREAK_ALWAYS = 1;
	
	/**
	 * @param name
	 */
	public MarkupStyleElement(String name) {
		super(name);
	}

    public void setBasedOn(XStyleElement style) {
        if(!(style instanceof MarkupStyleElement)) {
            throw new IllegalArgumentException("basedOn value is not a valid markup style element");
        }
        super.setBasedOn(style);
    }

    protected void fireOnBasedOn(XStyleElement styleElement) {
    	super.fireOnBasedOn(styleElement);
    	MarkupStyleElement bse = (MarkupStyleElement) styleElement;
    	if(this.textDecoration == null && bse.getTextDecoration() != null) {
    		this.firePropertyChange("textDecoration", null, bse.getTextDecoration());
    	}
    	if(this.lineBreakAfter == null && bse.getLineBreakAfter() != null) {
    		this.iLineBreakAfter = -1;
    		this.firePropertyChange("lineBreakAfter", null, bse.getLineBreakAfter());
    	}
    	if(this.paragraphBreakAfter == null && bse.getParagraphBreakAfter() != null) {
    		this.iParagraphBreakAfter = -1;
    		this.firePropertyChange("paragraphBreakAfter", null, bse.getParagraphBreakAfter());
    	}
    }
    
    private MarkupStyleElement properStyle(Object localValue) {
        if(localValue != null) {
            // TODO Perhaps enforce setter parameters to be non null.
            return this;
        }
        else {
            MarkupStyleElement basedOn = (MarkupStyleElement) this.basedOn;
            if(basedOn != null) {
                return basedOn;
            }
            return this;
        }
    }
    
	/**
	 * @return Returns the textDecoration.
	 */
	public String getTextDecoration() {
		return this.properStyle(this.textDecoration).textDecoration;
	}
	/**
	 * @param textDecoration The textDecoration to set.
	 */
	public void setTextDecoration(String value) {
		Object oldValue = this.textDecoration;
		if(!Objects.equals(oldValue, value)) {
			this.textDecoration = value;
			this.firePropertyChange("textDecoration", oldValue, value);
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMarkupStyleElement#getLineBreakAfter()
	 */
	public String getLineBreakAfter() {
		return this.properStyle(this.lineBreakAfter).lineBreakAfter;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMarkupStyleElement#getParagraphBreakAfter()
	 */
	public String getParagraphBreakAfter() {
		return this.properStyle(this.paragraphBreakAfter).paragraphBreakAfter;
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMarkupStyleElement#getLineBreakAfter()
	 */
	public String getLineBreakBefore() {
		return this.properStyle(this.lineBreakBefore).lineBreakBefore;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMarkupStyleElement#getParagraphBreakAfter()
	 */
	public String getParagraphBreakBefore() {
		return this.properStyle(this.paragraphBreakBefore).paragraphBreakBefore;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMarkupStyleElement#setLineBreakAfter(java.lang.String)
	 */
	public void setLineBreakAfter(String value) {
		Object oldValue = this.lineBreakAfter;
		if(!Objects.equals(oldValue, value)) {
			this.lineBreakAfter = value;
			this.iLineBreakAfter = -1;
			this.firePropertyChange("lineBreakAfter", oldValue, value);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMarkupStyleElement#setParagraphBreakAfter(java.lang.String)
	 */
	public void setParagraphBreakAfter(String value) {
		Object oldValue = this.paragraphBreakAfter;
		if(!Objects.equals(oldValue, value)) {
			this.paragraphBreakAfter = value;
			this.iParagraphBreakAfter = -1;
			this.firePropertyChange("paragraphBreakAfter", oldValue, value);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMarkupStyleElement#setLineBreakAfter(java.lang.String)
	 */
	public void setLineBreakBefore(String value) {
		Object oldValue = this.lineBreakBefore;
		if(!Objects.equals(oldValue, value)) {
			this.lineBreakBefore = value;
			this.iLineBreakBefore = -1;
			this.firePropertyChange("lineBreakBefore", oldValue, value);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XMarkupStyleElement#setParagraphBreakAfter(java.lang.String)
	 */
	public void setParagraphBreakBefore(String value) {
		Object oldValue = this.paragraphBreakBefore;
		if(!Objects.equals(oldValue, value)) {
			this.paragraphBreakBefore = value;
			this.iParagraphBreakBefore = -1;
			this.firePropertyChange("paragraphBreakBefore", oldValue, value);
		}
	}

	public int getLineBreakAfterValue() {
		if(this.iLineBreakAfter == -1) {
			String lba = this.lineBreakAfter;
			if(lba == null) {
				this.iLineBreakAfter = BREAK_AUTO;
			}
			else if("auto".equals(lba)) {
				this.iLineBreakAfter = BREAK_AUTO;
			}
			else if("always".equals(lba)) {
				this.iLineBreakAfter = BREAK_ALWAYS;
			}
			else {
				logger.warning("getLineBreakAfterValue(): Invalid lineBreakAfter value: " + lba);
				this.iLineBreakAfter = BREAK_AUTO;
			}
		}
		return this.iLineBreakAfter;
	}
	
	public int getParagraphBreakAfterValue() {
		if(this.iParagraphBreakAfter == -1) {
			String lba = this.paragraphBreakAfter;
			if(lba == null) {
				this.iParagraphBreakAfter = BREAK_AUTO;
			}
			else if("auto".equals(lba)) {
				this.iParagraphBreakAfter = BREAK_AUTO;
			}
			else if("always".equals(lba)) {
				this.iParagraphBreakAfter = BREAK_ALWAYS;
			}
			else {
				logger.warning("getParagraphBreakAfterValue(): Invalid paragraphBreakAfter value: " + lba);
				this.iParagraphBreakAfter = BREAK_AUTO;
			}
		}
		return this.iParagraphBreakAfter;
	}

	public int getLineBreakBeforeValue() {
		if(this.iLineBreakBefore == -1) {
			String lba = this.lineBreakBefore;
			if(lba == null) {
				this.iLineBreakBefore = BREAK_AUTO;
			}
			else if("auto".equals(lba)) {
				this.iLineBreakBefore = BREAK_AUTO;
			}
			else if("always".equals(lba)) {
				this.iLineBreakBefore = BREAK_ALWAYS;
			}
			else {
				logger.warning("getLineBreakBeforeValue(): Invalid lineBreakBefore value: " + lba);
				this.iLineBreakBefore = BREAK_AUTO;
			}
		}
		return this.iLineBreakBefore;
	}
	
	public int getParagraphBreakBeforeValue() {
		if(this.iParagraphBreakBefore == -1) {
			String lba = this.paragraphBreakBefore;
			if(lba == null) {
				this.iParagraphBreakBefore = BREAK_AUTO;
			}
			else if("auto".equals(lba)) {
				this.iParagraphBreakBefore = BREAK_AUTO;
			}
			else if("always".equals(lba)) {
				this.iParagraphBreakBefore = BREAK_ALWAYS;
			}
			else {
				logger.warning("getParagraphBreakBeforeValue(): Invalid paragraphBreakBefore value: " + lba);
				this.iParagraphBreakBefore = BREAK_AUTO;
			}
		}
		return this.iParagraphBreakBefore;
	}
	/**
	 * @return Returns the highlight.
	 */
	public boolean isHighlight() {
		return this.highlight;
	}
	
	/**
	 * @param highlight The highlight to set.
	 */
	public void setHighlight(boolean value) {
		boolean oldValue = this.highlight;
		if(oldValue != value) {
			this.highlight = value;
			this.firePropertyChange("highlight", Boolean.valueOf(oldValue), Boolean.valueOf(value));
		}
	}
	
	public XamjRenderState createRenderState(XamjRenderState prev) {
		XamjRenderState newRenderState = prev.cloneRenderState();
		String fontFamily = this.getFontFamily();
		if(fontFamily != null) {
			newRenderState.setFontFamily(fontFamily);
		}
		String fontStyle = this.getFontStyle();
		if(fontStyle != null) {
			newRenderState.setFontStyle(fontStyle);
		}
		String fontVariant = this.getFontVariant();
		if(fontVariant != null) {
			newRenderState.setFontVariant(fontVariant);
		}
		String fontWeight = this.getFontWeight();
		if(fontWeight != null) {
			newRenderState.setFontWeight(fontWeight);
		}
		Double fontSize = this.getFontSize();
		if(fontSize != null) {
			newRenderState.setFontSize(fontSize);
		}
		String color = this.getColor();
		if(color != null) {
			newRenderState.setColor(color);
		}
		String backgroundColor = this.getBackgroundColor();
		if(backgroundColor != null) {
			newRenderState.setBackgroundColor(backgroundColor);
		}
		String textDecoration = this.getTextDecoration();
		if(textDecoration != null) {
			newRenderState.setTextDecoration(textDecoration);
		}
		
		//TODO: A style property?
		newRenderState.setHighlight(this.highlight);
		
//		String lineBreakAfter = this.getLineBreakAfter();
//		if(lineBreakAfter != null) {
//			newRenderState.setLineBreakAfter(lineBreakAfter);
//		}
//		String paragraphBreakAfter = this.getParagraphBreakAfter();
//		if(paragraphBreakAfter != null) {
//			newRenderState.setParagraphBreakAfter(paragraphBreakAfter);
//		}
		
		return newRenderState;
	}
}
