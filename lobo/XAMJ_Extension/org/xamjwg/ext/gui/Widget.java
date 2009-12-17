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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.*;

import javax.swing.JComponent;


import java.util.logging.*;

import org.lobobrowser.util.*;
import org.lobobrowser.util.gui.ColorFactory;
import org.lobobrowser.util.gui.FontFactory;
import org.xamjwg.dom.XWidgetStyleElement;

/**
 * @author J. H. S.
 */
public class Widget extends JComponent {
    private static final java.util.logging.Logger logger = Logger.getLogger(Widget.class.getName());
    protected static final Color TRANSPARENT = new Color(0, 0, 0, 0); 
    protected final WidgetStyleElement style;
    protected BaseWidgetElement element;
    
    public Widget() {
        super();
        this.style = new WidgetStyleElement(this.getName() + "$style");
        this.style.addPropertyChangeListener(new StylePropertyChangeListener());
        this.setOpaque(false);
    }    
    
    public void refresh() {
    	this.revalidate();
    	this.repaint();
    }
    
    public void setWidgetElement(BaseWidgetElement element) {
    	this.element = element;
    }

    public BaseWidgetElement getWidgetElement() {
    	return this.element;
    }
    
    private boolean allEnabled = true;
    
    public void setAllEnabled(boolean value) {
    	boolean oldValue = this.allEnabled;
    	if(oldValue != value) {
    		this.updateActualEnabling(value);
    		// Assign after call above
    		this.allEnabled = value;	
    	}
    }
    
    public boolean isAllEnabled() {
    	return this.allEnabled;
    }

    private void updateActualEnabling(boolean allEnabling) {
    	Component parent = this.getParent();
    	boolean parentEnabled = parent == null ? true : parent.isEnabled();
    	boolean newEnabled = allEnabling && parentEnabled;
    	if(this.isEnabled() != newEnabled) {
    		this.setEnabled(newEnabled);
    		Component[] components = this.getComponents();
    		for(int i = 0; i < components.length; i++) {
    			Component c = components[i];
    			if(c instanceof Widget) {
    				Widget w = (Widget) c;
    				w.updateActualEnabling(w.allEnabled);
    			}
    		}
    	}
    }

//    private void updateActualEnablingLocal(boolean allEnabling) {
//    	if(allEnabling) {
//    		Component parent = this.getParent();
//    		this.setEnabled(parent == null ? true : parent.isEnabled());
//    	}
//    	else {
//    		this.setEnabled(false);
//    	}
//    }

    private Dimension minimumSize = null;
    private Dimension preferredSize = null;
    private Dimension maximumSize = null;
    private Integer preferredWidth = null;
    private Integer preferredHeight = null;
    
    /* (non-Javadoc)
     * @see java.awt.Component#getMaximumSize()
     */
    public Dimension getMaximumSize() {
        if(this.maximumSize == null) {
            this.maximumSize = super.getMaximumSize();
        }
        return this.maximumSize;
    }
    
    /* (non-Javadoc)
     * @see java.awt.Component#getMinimumSize()
     */
    public Dimension getMinimumSize() {
        if(this.minimumSize == null) {
            this.minimumSize = super.getMinimumSize();
        }
        return this.minimumSize;
    }

    /* (non-Javadoc)
     * @see java.awt.Component#getPreferredSize()
     */
    public Dimension getPreferredSize() {
        if(this.preferredSize == null) {
            this.preferredSize = super.getPreferredSize();
        }
        return this.preferredSize;
    }
    
    public XWidgetStyleElement getStyle() {
        return this.style;
    }
    
    public int getPreferredWidth() {
    	if(this.preferredWidth != null) {
    		return this.preferredWidth.intValue();
    	}
    	Diagnostics.Assert(EventQueue.isDispatchThread(), "Not in event dispatch thread");
    	int pw = this.getPreferredWidthImpl();
    	this.preferredWidth = new Integer(pw);
    	return pw;
    }
    
    public int getPreferredWidthImpl() {
        if(this.style.hasHorizontalFill()) {
            return -1;
        }
        Integer pw = this.style.getWidth();
        if(pw != null) {
            return pw.intValue();
        }
        return this.getDefaultWidth();
    }
    
    protected int getDefaultWidth() {
        Dimension ps = this.getPreferredSize();
        return ps == null ? -1 : ps.width;
    }
    
    public int getPreferredHeight() {
    	if(this.preferredHeight != null) {
    		return this.preferredHeight.intValue();
    	}
    	Diagnostics.Assert(EventQueue.isDispatchThread(), "Not in event dispatch thread");
    	int ph = this.getPreferredHeightImpl();
    	this.preferredHeight = new Integer(ph);
    	return ph;	
    }
    
    public int getPreferredHeightImpl() {
        if(this.style.hasVerticalFill()) {
            return -1;
        }
        Integer pw = this.style.getHeight();
        if(pw != null) {
            return pw.intValue();
        }
        return this.getDefaultHeight();
    }
    
    protected int getDefaultHeight() {
        Dimension ps = this.getPreferredSize();
        return ps == null ? -1 : ps.height;
    }
    
    public int getMinimumWidth() {
        Dimension minimumSize = this.getMinimumSize();
        return minimumSize.width;        
    }

    public int getMinimumHeight() {
        Dimension minimumSize = this.getMinimumSize();
        return minimumSize.height;        
    }

    public int getMaximumWidth() {
        Dimension maximumSize = this.getMaximumSize();
        return maximumSize.width;        
    }

    public int getMaximumHeight() {
        Dimension maximumSize = this.getMaximumSize();
        return maximumSize.height;        
    }

    private Float alignmentY = null;
    public float getAlignmentY() {
        if(this.alignmentY == null) {
            String valign = this.getStyle().getValign();
            float alignY;
            if("top".equals(valign)) {
                alignY = 0.0f; 
            }
            else if("middle".equals(valign)) {
                alignY = 0.5f;
            }
            else if("bottom".equals(valign)) {
                alignY = 1.0f;
            }
            else if(valign == null) {
                alignY = 0.5f;
            }
            else {
                try {
                	alignY = Float.parseFloat(valign);
                } catch(NumberFormatException nfe) {
                	alignY = 0.5f;
                    logger.log(Level.SEVERE, "getAlignmentY(): Invalid valign: " + valign, nfe);                	
                }
            }
            this.alignmentY = new Float(alignY);
        }
        return this.alignmentY.floatValue();
    }
    
    private Float alignmentX = null;
    public float getAlignmentX() {
        if(this.alignmentX == null) {
            String halign = this.getStyle().getHalign();
            float alignX;
            if("left".equals(halign)) {
                alignX = 0.0f; 
            }
            else if("center".equals(halign)) {
                alignX = 0.5f;
            }
            else if("right".equals(halign)) {
                alignX = 1.0f;
            }
            else if(halign == null) {
                alignX = 0.5f;
            }
            else {
                try {
                	alignX = Float.parseFloat(halign);
                } catch(NumberFormatException nfe) {
                	alignX = 0.5f;
                    logger.log(Level.SEVERE, "getAlignmentY(): Invalid halign: " + halign, nfe);                	
                }
            }
            this.alignmentX = new Float(alignX);
        }
        return this.alignmentX.floatValue();
    }

    protected void onStyleChange(String propertyName) {
    	//Note: orientation handled by each specific widget
    	
    	if("fill".equals(propertyName) || 
			"width".equals(propertyName) || 
			"height".equals(propertyName) || 
			"alignmentX".equals(propertyName) || 
			"alignmentY".equals(propertyName)) {
    		
    		if(EventQueue.isDispatchThread()) {
    			this.refresh();
    		}    			
    	}
    	else if("color".equals(propertyName)) {
    		String color = this.style.getColor();
    		if(color != null) {
    			this.setForeground(ColorFactory.getInstance().getColor(color));
    		}
    	}
    	else if("backgroundColor".equals(propertyName)) {
    		String color = this.style.getBackgroundColor();
    		if(color != null) {
    			this.setBackground(ColorFactory.getInstance().getColor(color));
    		}
    	}
    	else if(propertyName.startsWith("font")) {
    		//TODO A bit inefficient if several font properties are changed.
    		Double fontSize = this.style.getFontSize();
    		Float fontSizeF = fontSize == null ? null : Float.valueOf(fontSize.floatValue());
    		this.setFont(FontFactory.getInstance().getFont(this.style.getFontFamily(), this.style.getFontStyle(), this.style.getFontVariant(), this.style.getFontWeight(), fontSizeF));
    	}
    }
    
    public void invalidate() {
        this.alignmentX = null;
        this.alignmentY = null;
        this.minimumSize = null;
        this.maximumSize = null;
        this.preferredSize = null;
        this.preferredWidth = null;
        this.preferredHeight = null;
        super.invalidate();
    }
        
    public void setVisible(boolean flag) {
    	boolean prevVisible = this.isVisible();
    	super.setVisible(flag);	
    	boolean newVisible = this.isVisible();
    	if(newVisible != prevVisible) {
    		this.firePropertyChange("visible", prevVisible, newVisible);
    	}
    }

    private boolean neverPainted = true;
    
    public void paint(Graphics g) {
    	super.paint(g);
    	BaseWidgetElement e = this.element;
    	if(this.neverPainted) {
    		this.neverPainted = false;
    		if(e != null) {
    			e.onFirstShown();
    		}
    	}
    }
    
    public void paintComponent(Graphics g) {
    	Color color = this.getBackground();
    	if(color.getTransparency() != Color.TRANSLUCENT) {
    		//if(logger.isLoggable(Level.INFO))logger.info("paintComponent(): this=" + this + ",background=" + this.getBackground());
    		Rectangle clipBounds = g.getClipBounds();
    		g.setColor(color);
    		g.fillRect(clipBounds.x, clipBounds.y, clipBounds.width, clipBounds.height);
    	}
    	else {
    		//if(logger.isLoggable(Level.INFO))logger.info("paintComponent(): this=" + this + " is translucent");    		
    	}
    	super.paintComponent(g);
    	BaseWidgetElement e = this.element;
    	if(e != null) {
    		e.onPaint(g, this, this.getInsets());
    	}
    }
    
    private class StylePropertyChangeListener implements PropertyChangeListener {
        /* (non-Javadoc)
         * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
         */
        public void propertyChange(PropertyChangeEvent arg0) {
        	onStyleChange(arg0.getPropertyName());
        }
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#addNotify()
	 */
	@Override
	public void addNotify() {
		super.addNotify();
		this.updateActualEnabling(this.allEnabled);
	}
}
