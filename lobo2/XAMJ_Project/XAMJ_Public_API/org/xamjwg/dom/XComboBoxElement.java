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
 * A combo-box element.
 * @author J. H. S.
 */
public interface XComboBoxElement extends XWidgetElement {
	/**
	 * Sets the combo-box text.
	 * @param value Text to be set in the combo-box editor or selected.
	 */
    public void setText(String value);
    
	/**
	 * Sets the combo-box text.
	 * @param value Text to be set in the combo-box editor.
	 * @param forceSelect Force the item to be added to the combo-box and made the selected item.
	 */
    public void setText(String value, boolean forceSelect);

    /**
     * Sets the selected item of the combo box dropdown.
     */
    public void setSelectedItem(Object item);

    /**
     * Gets the combo-box text.
     */
    public String getText();
    
    /**
     * Makes the combo-box editable or not.
     * @param value
     */
    public void setEditable(boolean value);
    
    /**
     * Gets the editable property of the combo-box.
     */
    public boolean isEditable();
    
    /**
     * Gets visibility state of the combo-box popup.
     */
    public boolean isPopupVisible();

    /**
     * Shows the combo-box popup.
     */
    public void showPopup();

    /**
     * Hides the combo-box popup.
     */
    public void hidePopup();
    
    /**
     * Adds an item to the combo-box popup.
     */
    public void addItem(String item);

    /**
     * Removes all items from the combo-box popup.
     */
    public void removeAllItems();
    
    /**
     * Sets the maximum number of rows the combo-box displays.
     */
    public void setMaximumRowCount(int max);
    
    /**
     * Gets the maximum number of rows the combo-box displays.
     */
    public int getMaximumRowCount();
}