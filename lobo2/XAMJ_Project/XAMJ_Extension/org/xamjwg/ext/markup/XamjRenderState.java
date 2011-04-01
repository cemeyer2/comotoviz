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

import java.awt.*;
import java.awt.font.*;
import java.util.logging.*;

import org.lobobrowser.util.*;
import org.lobobrowser.util.gui.ColorFactory;
import org.lobobrowser.util.gui.FontFactory;

/**
 * @author J. H. S.
 */
public class XamjRenderState {
	private static final Logger logger = Logger.getLogger(XamjRenderState.class.getName());
	
	private String fontFamily;
	private String fontStyle;
	private String fontVariant;
	private String fontWeight;
	private Double fontSize;
	private String color;
	private String backgroundColor;
	private String textDecoration;
	private String textAntiAliasing;

//	private String lineBreakAfter;
//	private String paragraphBreakAfter;
	
	private Font iFont;
	
	private LineMetrics iLineMetrics;
	private FontMetrics iFontMetrics;
	private Color iColor;
	private Color iBackgroundColor;
	private int iTextDecoration = -1;
	private int iBlankWidth = -1;
	private Boolean iTextAntiAliasing;
	private boolean iHighlight;
//	private int iLineBreakAfter = -1;
//	private int iParagraphBreakAfter = -1;

	public static final int TEXTDECORATION_NONE = 0;
	public static final int TEXTDECORATION_UNDERLINE = 1;
//		
//	public static final int BREAK_AUTO = 0;
//	public static final int BREAK_ALWAYS = 1;
	
	/**
	 * @param fontFamily
	 * @param fontStyle
	 * @param fontVariant
	 * @param fontWeight
	 * @param fontSize
	 * @param color
	 * @param backgroundColor
	 * @param textDecoration
	 * @param font
	 * @param color2
	 * @param backgroundColor2
	 * @param textDecoration2
	 */
	private XamjRenderState(XamjRenderState other) {
		super();
		this.fontFamily = other.fontFamily;
		this.fontStyle = other.fontStyle;
		this.fontVariant = other.fontVariant;
		this.fontWeight = other.fontWeight;
		this.fontSize = other.fontSize;
		this.color = other.color;
		this.backgroundColor = other.backgroundColor;
		this.textDecoration = other.textDecoration;
		this.textAntiAliasing = other.textAntiAliasing;
		
//		this.lineBreakAfter = other.lineBreakAfter;
//		this.paragraphBreakAfter = other.paragraphBreakAfter;
		
		iFont = other.iFont;
		iLineMetrics = other.iLineMetrics;
		iFontMetrics = other.iFontMetrics;
		iColor = other.iColor;
		iBackgroundColor = other.iBackgroundColor;
		iTextDecoration = other.iTextDecoration;
		iTextAntiAliasing = other.iTextAntiAliasing;
		
//		iLineBreakAfter = other.iLineBreakAfter;
//		iParagraphBreakAfter = other.iParagraphBreakAfter;
	}

	public XamjRenderState() {
		super();
		this.fontFamily = "Verdana";
		this.fontStyle = "normal";
		this.fontVariant = "normal";
		this.fontWeight = "normal";
		this.fontSize = 12.0;
		this.color = "black";
		this.backgroundColor = "transparent";
		this.textDecoration = "none";
		this.textAntiAliasing = "true";
//		this.lineBreakAfter = "auto";
//		this.paragraphBreakAfter = "auto";
	}
	
	public XamjRenderState cloneRenderState() {
		return new XamjRenderState(this);
	}
	
	/**
	 * @param backgroundColor The backgroundColor to set.
	 */
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
		this.iBackgroundColor = null;
	}
	/**
	 * @param color The color to set.
	 */
	public void setColor(String color) {
		this.color = color;
		this.iColor = null;
	}
	
	/**
	 * @param fontFamily The fontFamily to set.
	 */
	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
		this.iFont = null;
		this.iFontMetrics = null;
		this.iBlankWidth = -1;
	}
	
	/**
	 * @param fontSize The fontSize to set.
	 */
	public void setFontSize(Double fontSize) {
		this.fontSize = fontSize;
		this.iFont = null;
		this.iFontMetrics = null;
		this.iBlankWidth = -1;
	}

	/**
	 * @param fontStyle The fontStyle to set.
	 */
	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
		this.iFont = null;
		this.iFontMetrics = null;
		this.iBlankWidth = -1;
	}

	/**
	 * @param fontVariant The fontVariant to set.
	 */
	public void setFontVariant(String fontVariant) {
		this.fontVariant = fontVariant;
		this.iFont = null;
		this.iFontMetrics = null;
		this.iBlankWidth = -1;
	}
	/**
	 * @param fontWeight The fontWeight to set.
	 */
	public void setFontWeight(String fontWeight) {
		this.fontWeight = fontWeight;
		this.iFont = null;
		this.iFontMetrics = null;
	}
	
	/**
	 * @param textDecoration The textDecoration to set.
	 */
	public void setTextDecoration(String textDecoration) {
		this.textDecoration = textDecoration;
		this.iTextDecoration = -1;
	}
	
	public void setTextAntiAliasing(String textAntiAliasing) {
		this.textAntiAliasing = textAntiAliasing;
		this.iTextAntiAliasing = null;
	}
	
	public Font getFont() {
		Font f = this.iFont;
		if(f != null) {
			return f;
		}
		Double fontSize = this.fontSize;
		Float fontSizeF = fontSize == null ? null : Float.valueOf(fontSize.floatValue());
		f = FontFactory.getInstance().getFont(this.fontFamily, this.fontStyle, this.fontVariant, this.fontWeight, fontSizeF);
		this.iFont = f;
		return f;
	}

	public Color getColor() {
		Color c = this.iColor;
		if(c != null) {
			return c;
		}
		c = ColorFactory.getInstance().getColor(this.color);
		this.iColor = c;
		return c;
	}

	public Color getBackgroundColor() {
		Color c = this.iBackgroundColor;
		if(c != null) {
			return c;
		}
		c = ColorFactory.getInstance().getColor(this.color);
		this.iBackgroundColor = c;
		return c;
	}
	
	public int getTextDecoration() {
		int td = this.iTextDecoration;
		if(td != -1) {
			return td;
		}
		String tdText = this.textDecoration;
		if("none".equals(tdText)) {
			td = XamjRenderState.TEXTDECORATION_NONE;
		}
		else if("underline".equals(tdText)) {
			td = XamjRenderState.TEXTDECORATION_UNDERLINE;
		}
		else {
			logger.warning("getTextDecoration(): Invalid value: " + tdText);
			td = XamjRenderState.TEXTDECORATION_NONE;
		}
		this.iTextDecoration = td;
		return td;
	}

	public boolean hasTextAntiAliasing() {
		Boolean a = this.iTextAntiAliasing;
		if(a != null) {
			return a.booleanValue();
		}
		String atext = this.textAntiAliasing;
		a = Boolean.valueOf("true".equals(atext));
		this.iTextAntiAliasing = a;
		return a.booleanValue();
	}
	
	public FontMetrics getFontMetrics() {
		FontMetrics fm = this.iFontMetrics;
		if(fm == null) {
			//TODO getFontMetrics deprecated. How to get text width?
			fm = Toolkit.getDefaultToolkit().getFontMetrics(this.getFont());
			this.iFontMetrics = fm;
		}
		return fm;
	}
	
	public int getBlankWidth() {
		int bw = this.iBlankWidth;
		if(bw == -1) {
			bw = this.getFontMetrics().charWidth(' ');
			this.iBlankWidth = bw;
		}
		return bw;
	}

	
	
	/**
	 * @return Returns the iHighlight.
	 */
	public boolean isHighlight() {
		return this.iHighlight;
	}
	/**
	 * @param highlight The iHighlight to set.
	 */
	public void setHighlight(boolean highlight) {
		this.iHighlight = highlight;
	}
	
//	public void setLineBreakAfter(String value) {
//		this.lineBreakAfter = value;
//		this.iLineBreakAfter = -1;
//	}
//	
//	public void setParagraphBreakAfter(String value) {
//		this.paragraphBreakAfter = value;
//		this.iParagraphBreakAfter = -1;
//	}
//
//	public int getLineBreakAfter() {
//		if(this.iLineBreakAfter == -1) {
//			String lba = this.lineBreakAfter;
//			if("auto".equals(lba)) {
//				this.iLineBreakAfter = BREAK_AUTO;
//			}
//			else if("always".equals(lba)) {
//				this.iLineBreakAfter = BREAK_ALWAYS;
//			}
//			else {
//				logger.warning("getLineBreakAfter(): Invalid value: " + lba);
//				this.iLineBreakAfter = BREAK_AUTO;
//			}
//		}
//		return this.iLineBreakAfter;
//	}
//	
//	public int getParagraphBreakAfter() {
//		if(this.iParagraphBreakAfter == -1) {
//			String lba = this.paragraphBreakAfter;
//			if("auto".equals(lba)) {
//				this.iParagraphBreakAfter = BREAK_AUTO;
//			}
//			else if("always".equals(lba)) {
//				this.iParagraphBreakAfter = BREAK_ALWAYS;
//			}
//			else {
//				logger.warning("getParagraphBreakAfter(): Invalid value: " + lba);
//				this.iParagraphBreakAfter = BREAK_AUTO;
//			}
//		}
//		return this.iParagraphBreakAfter;
//	}
	
	public boolean equals(Object other) {
		if(!(other instanceof XamjRenderState)) {
			return false;
		}
		XamjRenderState ors = (XamjRenderState) other;
		return this.fontStyle.equals(ors.fontStyle) &&
			   this.fontWeight.equals(ors.fontWeight) &&
			   this.fontFamily.equals(ors.fontFamily) &&
			   this.fontVariant.equals(ors.fontVariant) &&
			   this.fontSize == ors.fontSize &&
			   this.textDecoration == ors.textDecoration &&
//			   this.paragraphBreakAfter.equals(ors.paragraphBreakAfter) &&
//			   this.lineBreakAfter.equals(ors.lineBreakAfter) &&
			   this.color.equals(ors.color) &&
			   this.backgroundColor.equals(ors.color);
	}
	
	public int hashCode() {
		return this.fontFamily.hashCode() ^ this.fontSize.hashCode();
	}	
}
