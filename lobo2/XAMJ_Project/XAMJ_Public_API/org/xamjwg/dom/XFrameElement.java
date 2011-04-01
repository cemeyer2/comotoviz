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
 * Created on Apr 2, 2005
 */
package org.xamjwg.dom;

import java.net.MalformedURLException;

/**
 * A frame element.
 * @author J. H. S.
 */
public interface XFrameElement extends XWidgetElement{
	/**
	 * Gets the current frame URL.
	 */
	public String getHref();

	/**
	 * Causes the frame to navigate to the given URL.
	 * This method is meant to be used as a XAMJ attribute setter.
	 * @param value An absolute or relative URL.
	 */
	public void setHref(String value) throws MalformedURLException;
	
	/**
	 * Causes the frame to navigate to the given URL.
	 * @param url An absolute URL. If the URL text is missing
	 *            a protocol, http is assumed.
	 * @return An AsyncResult that receives the ClientletContext of the response.
	 * @throws java.net.MalformedURLException
	 */
	public void navigate(String url) throws java.net.MalformedURLException;
	
	/**
	 * Get a collection of URLs in the history of the frame that match the 
	 * given partial URL as a prefix. 
	 * 
	 * @param partialUrl A partial URL, or URL prefix. It may
	 *                   be missing a protocol, in which case http
	 *                   could be assumed.
	 * @param maxMatches The maximum number of matches desired.
	 * @throws SecurityException If caller not privileged.
	 */
	public java.util.Collection getPotentialMatches(String partialUrl, int maxMatches);
	
	/**
	 * Gets a collection of URLs in the history of the frame,
	 * with the most recently accessed URLs appearing first.  
	 * @param maxLocations The maximum number of URLs desired.
	 * @throws SecurityException If caller not privileged.
	 */
	public java.util.Collection getRecentLocations(int maxLocations);
	
	/**
     * Moves back in the history of the frame, if possible.
	 */
	public void back();
	
	/**
     * Moves forward in the history of the frame, if possible.
	 */
	public void forward();
	
	/**
	 * Reloads the current page ignoring caching.
	 */
	public void refresh();

	/**
	 * Stops any content 
	 */
	public void stop();
	
	/**
	 * Gets source code for the current displayed document if available.
	 */
	public String getSource();
	}
