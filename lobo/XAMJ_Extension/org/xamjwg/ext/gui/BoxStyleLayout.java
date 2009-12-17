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

/**
 * @author J. H. S.
 */
public abstract class BoxStyleLayout implements LayoutManager {
    public static final BoxStyleLayout HORIZONTAL = new Horizontal();
    public static final BoxStyleLayout VERTICAL = new Vertical();
        
    /* (non-Javadoc)
     * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
     */
    public void removeLayoutComponent(Component arg0) {
        //nop
    }

    /* (non-Javadoc)
     * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String, java.awt.Component)
     */
    public void addLayoutComponent(String arg0, Component arg1) {
        //nop
    }

    
    /* (non-Javadoc)
     * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
     */
    public abstract void layoutContainer(Container arg0);

    /* (non-Javadoc)
     * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
     */
    public abstract Dimension minimumLayoutSize(Container arg0);
    
    /* (non-Javadoc)
     * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
     */
    public abstract Dimension preferredLayoutSize(Container arg0);

    private static class Horizontal extends BoxStyleLayout {

        /* (non-Javadoc)
         * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
         */
        public Dimension minimumLayoutSize(Container arg0) {
            Component[] components = arg0.getComponents();
            int count = components.length;

            int minWidth = 0;
            int minHeight = 0;
            for(int i = 0; i < count; i++) {
            	try {
            		Widget widget = (Widget) components[i];
            		int minw = widget.getMinimumWidth();
            		int minh = widget.getMinimumHeight();
            		minWidth += minw;
            		if(minh > minHeight) {
            			minHeight = minh;
            		}
            	} catch(ClassCastException cce) {
            		throw new IllegalStateException("Container=" + arg0 + ",widget=" + components[i]);
            	}
            }
            
            Insets insets = arg0.getInsets();
            minWidth += insets.left + insets.right;
            minHeight += insets.top + insets.bottom;
            return new Dimension(minWidth, minHeight);
        }
        
        /* (non-Javadoc)
         * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
         */
        public Dimension preferredLayoutSize(Container arg0) {
            Component[] components = arg0.getComponents();
            int count = components.length;

            int prefWidth = 0;
            int prefHeight = 0;
            for(int i = 0; i < count; i++) {
                Widget widget = (Widget) components[i];
                Dimension ps = widget.getPreferredSize();
                int pw = ps.width;
                int ph = ps.height;
                prefWidth += pw;
                if(ph > prefHeight) {
                    prefHeight = ph;
                }
            }
            
            Insets insets = arg0.getInsets();
            prefWidth += insets.left + insets.right;
            prefHeight += insets.top + insets.bottom;
            return new Dimension(prefWidth, prefHeight);
        }
        
        /* (non-Javadoc)
         * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
         */
        public void layoutContainer(Container arg0) {
            Dimension size = arg0.getSize();
            Insets insets = arg0.getInsets();
            Component[] components = arg0.getComponents();
            int count = components.length;
            int availWidth = size.width - insets.left - insets.right;
            int availHeight = size.height - insets.top - insets.bottom;
            
            //if(logger.isLoggable(Level.INFO))logger.info("layoutContainer(): availWidth=" + availWidth + ",availHeight=" + availHeight + ",widget=" + arg0);
            
            // Gather width counts
            int prefWidth = 0;
            int fillSum = 0;
            int numFillers = 0;
            int numNonFillers = 0;
            for(int i = 0; i < count; i++) {
                Widget widget = (Widget) components[i];
                int pw = widget.getPreferredWidth();
                int minw = widget.getMinimumWidth();
                if(pw != -1) {
                    int maxw = widget.getMaximumWidth();
                    if(pw > maxw) {
                        pw = maxw;
                    }
                    if(pw < minw) {
                        pw = minw;
                    }
                    numNonFillers++;
                    prefWidth += pw;
                }
                else {
                    numFillers++;
                    prefWidth += minw;
                    Integer width = widget.getStyle().getWidth();
                    fillSum += width == null ? 0 : width.intValue();  
                }
            }

            //if(logger.isLoggable(Level.INFO))logger.info("layoutContainer(): numFillers=" + numFillers + ",fillSum=" + fillSum + ",prefWidth=" + prefWidth);
            
            // Set preferred sizes (or minimun sizes) and
            // expand fillers
            int fillerSpace = availWidth - prefWidth;        
            int x = insets.left;
            for(int i = 0; i < count; i++) {
                Widget widget = (Widget) components[i];
                int pw = widget.getPreferredWidth();
                int width;
                int minw = widget.getMinimumWidth();
                if(pw != -1) {
                    int maxw = widget.getMaximumWidth();
                    if(pw > maxw) {
                        pw = maxw;
                    }
                    if(pw < minw) {
                        pw = minw;
                    }
                    width = pw;
                    if(fillerSpace < 0) {
                        // Reduce width so widgets fit
                        width -= (-fillerSpace / numNonFillers);
                        if(width < minw) {
                            int diff = pw - minw;
                            width = minw;
                            // More fillerSpace for remaining non-fillers
                            fillerSpace += diff;
                            numNonFillers--;
                        }
                    }
                }
                else if (fillerSpace > 0) {
                    if(fillSum == 0) {
                        if(numFillers == 0) {
                            throw new IllegalStateException("No fillers but preferredWidth is -1");
                        }
                        // fillerSpace is outside of minWidth's
                        width = minw + (fillerSpace / numFillers);
                    }
                    else {
                        Integer relWidth = widget.getStyle().getWidth();
                        int relWidthInt = relWidth == null ? 0 : relWidth.intValue();
                        width = minw + (fillerSpace * relWidthInt / fillSum);
                    }
                }
                else {
                    width = minw;
                }

                //if(logger.isLoggable(Level.INFO))logger.info("layoutContainer(): width=" + width + ",widget=" + widget);
                
                int ph = widget.getPreferredHeight();
                int minh = widget.getMinimumHeight();
                if(ph != -1) {
                    if(ph > availHeight) {
                        ph = availHeight;
                    }
                    int maxh = widget.getMaximumHeight();
                    if(ph > maxh) {
                        ph = maxh;
                    }
                    if(ph < minh) {
                        ph = minh;
                    }
                }
                else {
                	// Verticall fill
                    ph = availHeight;
                }
                float alignY = widget.getAlignmentY();
                int y = insets.top + (int) ((availHeight - ph) * alignY);
                widget.setBounds(x, y, width, ph);
                x += width;
            }
        }
    }
    
    private static class Vertical extends BoxStyleLayout {
        /* (non-Javadoc)
         * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
         */
        public void layoutContainer(Container arg0) {
            Dimension size = arg0.getSize();
            Insets insets = arg0.getInsets();
            Component[] components = arg0.getComponents();
            int count = components.length;
            int availWidth = size.width - insets.left - insets.right;
            int availHeight = size.height - insets.top - insets.bottom;
            
            // Gather height counts
            int prefHeight = 0;
            int fillSum = 0;
            int numFillers = 0;
            int numNonFillers = 0;
            for(int i = 0; i < count; i++) {
                Widget widget = (Widget) components[i];
                int ph = widget.getPreferredHeight();
                int minh = widget.getMinimumHeight();
                if(ph != -1) {
                    int maxh = widget.getMaximumHeight();
                    if(ph > maxh) {
                        ph = maxh;
                    }
                    if(ph < minh) {
                        ph = minh;
                    }
                    numNonFillers++;
                    prefHeight += ph;
                }
                else {
                    numFillers++;
                    prefHeight += minh;
                    Integer height = widget.getStyle().getHeight();
                    fillSum += height == null ? 0 : height.intValue();  
                }
            }
            
            // Set preferred sizes (or minimun sizes) and
            // expand fillers
            int fillerSpace = availHeight - prefHeight;        
            int y = insets.top;
            for(int i = 0; i < count; i++) {
                Widget widget = (Widget) components[i];
                int ph = widget.getPreferredHeight();
                int height;
                int minh = widget.getMinimumHeight();
                if(ph != -1) {
                    int maxh = widget.getMaximumHeight();
                    if(ph > maxh) {
                        ph = maxh;
                    }
                    if(ph < minh) {
                        ph = minh;
                    }
                    height = ph;
                    if(fillerSpace < 0) {
                        // Reduce width so widgets fit
                        height -= (-fillerSpace / numNonFillers);
                        if(height < minh) {
                            int diff = ph - minh;
                            height = minh;
                            // More fillerSpace for remaining non-fillers
                            fillerSpace += diff;
                            numNonFillers--;
                        }
                    }
                }
                else if (fillerSpace > 0) {
                    if(fillSum == 0) {
                        if(numFillers == 0) {
                            throw new IllegalStateException("No fillers but preferredWidth is -1");
                        }
                        height = minh + (fillerSpace / numFillers);
                    }
                    else {
                        Integer relHeight = widget.getStyle().getHeight();
                        int relHeightInt = relHeight == null ? 0 : relHeight.intValue();
                        height = minh + (fillerSpace * relHeightInt / fillSum);
                    }
                }
                else {
                    height = minh;
                }
                
                int pw = widget.getPreferredWidth();
                int minw = widget.getMinimumWidth();
                if(pw != -1) {
                    if(pw > availWidth) {
                        pw = availWidth;
                    }
                    int maxw = widget.getMaximumWidth();
                    if(pw > maxw) {
                        pw = maxw;
                    }
                    if(pw < minw) {
                        pw = minw;
                    }
                }
                else {
                	// Horizontal fill
                    pw = availWidth;
                }
                float alignX = widget.getAlignmentX();
                int x = insets.left + (int) ((availWidth - pw) * alignX);
                //if(logger.isLoggable(Level.INFO))logger.info("layoutContainer(): widget=" + widget + ",pw=" + pw);
                widget.setBounds(x, y, pw, height);
                y += height;
            }
        }
        
        /* (non-Javadoc)
         * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
         */
        public Dimension minimumLayoutSize(Container arg0) {
            Component[] components = arg0.getComponents();
            int count = components.length;

            int minWidth = 0;
            int minHeight = 0;
            for(int i = 0; i < count; i++) {
                Widget widget = (Widget) components[i];
                int minw = widget.getMinimumWidth();
                int minh = widget.getMinimumHeight();
                minHeight += minh;
                if(minw > minWidth) {
                    minWidth = minw;
                }
            }
            
            Insets insets = arg0.getInsets();
            minWidth += insets.left + insets.right;
            minHeight += insets.top + insets.bottom;
            return new Dimension(minWidth, minHeight);
        }

        /* (non-Javadoc)
         * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
         */
        public Dimension preferredLayoutSize(Container arg0) {
            Component[] components = arg0.getComponents();
            int count = components.length;

            int prefWidth = 0;
            int prefHeight = 0;
            for(int i = 0; i < count; i++) {
                Widget widget = (Widget) components[i];
                Dimension ps = widget.getPreferredSize();
                int pw = ps.width;
                int ph = ps.height;
                prefHeight += ph;
                if(pw > prefWidth) {
                    prefWidth = pw;
                }
            }
            
            Insets insets = arg0.getInsets();
            prefWidth += insets.left + insets.right;
            prefHeight += insets.top + insets.bottom;
            return new Dimension(prefWidth, prefHeight);
        }
    }
}
