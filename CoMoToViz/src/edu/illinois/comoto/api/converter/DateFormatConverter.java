/*
 * University of Illinois/NCSA
 * Open Source License
 *
 * Copyright (c) 2011 University of Illinois at Urbana-Champaign.
 * All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal with the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimers.
 *
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimers in the documentation and/or other materials provided
 *       with the distribution.
 *
 *     * Neither the names of the CoMoTo Project team, the University of
 *       Illinois at Urbana-Champaign, nor the names of its contributors
 *       may be used to endorse or promote products derived from this
 *       Software without specific prior written permission.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS WITH THE SOFTWARE.
 */

package edu.illinois.comoto.api.converter;

import org.apache.commons.beanutils.Converter;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Dec 4, 2010
 * <p/>
 * <p> <p> Generic converter for <code>DateFormat</code> objects so we can populate <code>DateFormat</code> using reflection
 */
public class DateFormatConverter implements Converter {

    /**
     * Converts a generic object into a <code>DateFormat</code> object
     *
     * @param destinationClass The class to which to convert (a list type)
     * @param abstractObject   The object to convert
     * @return A typed list containing the non-generic data from <code>abstractObject</code>
     */
    public Object convert(Class destinationClass, Object abstractObject) {

        try {

            //Try to parse this object into a DateFormat object as a string
            Date timestampDate = (Date) abstractObject;
            DateFormat timestamp = new SimpleDateFormat(timestampDate.toString(), new DateFormatSymbols());
            String s = "E M d H:m:s z y";
            return timestamp;
        } catch (ClassCastException e) {

            return null;
        }
    }
}
