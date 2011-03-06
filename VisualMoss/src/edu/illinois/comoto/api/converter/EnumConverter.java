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
import org.apache.commons.beanutils.converters.StringConverter;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Dec 8, 2010
 * <p/>
 * <p> <p> This class takes care of converting String objects into enums in the apache reflection library. Note that this
 * class must be registered as the String converter, and will default to the standard string converter if the
 * parameter is not an enum.
 */
public class EnumConverter implements Converter {

    /**
     * Converts a generic object into an enum
     *
     * @param destinationClass The class to which to convert
     * @param abstractObject   The object to convert
     * @return An enum, or String if the object is not an enum
     */
    public Object convert(Class destinationClass, Object abstractObject) {

        if (destinationClass.isEnum()) {
            try {

                //Try to parse this object into a Language enumerated type
                return Enum.valueOf(destinationClass, (String) abstractObject);

            } catch (ClassCastException e) {
                return null;
            } catch (EnumConstantNotPresentException e) {
                return null;
            }
        } else {

            StringConverter stringConverter = new StringConverter();
            return stringConverter.convert(destinationClass, abstractObject);
        }
    }
}
