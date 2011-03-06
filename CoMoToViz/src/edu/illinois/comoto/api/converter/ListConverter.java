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

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Dec 4, 2010
 * <p/>
 * <p> <p> Generic converter for list objects. This will individually add build objects into a list from the generic
 * type returned by the CoMoTo API
 */
public class ListConverter implements Converter {

    /**
     * Converts a generic object into a <code>List</code> of typed objects
     *
     * @param destinationClass The class to which to convert (a list type)
     * @param abstractObject   The object to convert
     * @return A typed list containing the non-generic data from <code>abstractObject</code>
     */
    public Object convert(Class destinationClass, Object abstractObject) {

        //Get the class of the abstract object
        Class sourceClass = abstractObject.getClass();

        //Let's put a check into this to ensure that the <code>abstractObject</code> is an array
        if (sourceClass.isArray()) {

            //Try to build a list of integers from this array, and just try something else if this fails
            try {
                List<Integer> integerList = new ArrayList<Integer>();
                Object[] integerArray = (Object[]) abstractObject;

                for (Object integer : integerArray) {
                    integerList.add((Integer) integer);
                }

                return integerList;

            } catch (ClassCastException e) {
            }

            //Try to build a list of strings from this array, and just try something else if this fails
            try {
                List<String> stringList = new ArrayList<String>();
                Object[] stringArray = (Object[]) abstractObject;

                for (Object string : stringArray) {
                    stringList.add((String) string);
                }

                return stringList;

            } catch (ClassCastException e) {
            }

        } else {
            throw new ConversionException("Cannot convert non-array to list!");
        }
        return null;
    }
}
