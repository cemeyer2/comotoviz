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

package edu.uiuc.cs.visualmoss.dataimport.api;

import edu.uiuc.cs.visualmoss.dataimport.api.converters.DateFormatConverter;
import edu.uiuc.cs.visualmoss.dataimport.api.converters.EnumConverter;
import edu.uiuc.cs.visualmoss.dataimport.api.converters.ListConverter;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 24, 2010
 * <p/>
 * <p> <p> This class lets us translate between Maps and CoMoToAPIObjects and back again
 */
public class CoMoToAPIReflector<T> {

    public void populate(T apiObject, Map map) {

        //Build a new, empty hash map for the renamed data
        Map<String, Object> camelCaseMap = new HashMap<String, Object>();

        //Pull the key-value pairs from the original map and rename the keys
        Set<String> keys = map.keySet();
        for (String key : keys) {
            String newKey = changeStyleFromUnderscoresToCamelCase(key);
            camelCaseMap.put(newKey, map.get(key));
        }

        //Register the type converters for populating non-primitive types on objects
        ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
        convertUtilsBean.deregister(List.class);
        convertUtilsBean.deregister(String.class);
        convertUtilsBean.deregister(DateFormat.class);
        convertUtilsBean.register(new ListConverter(), List.class);
        convertUtilsBean.register(new DateFormatConverter(), DateFormat.class);
        convertUtilsBean.register(new EnumConverter(), String.class);

        //Build the bean utils object with the specified converters
        BeanUtilsBean beanUtilsBean = new BeanUtilsBean(convertUtilsBean);

        //Populate the object from this new map using reflection
        try {
            beanUtilsBean.populate(apiObject, camelCaseMap);
        } catch (IllegalAccessException e) {
            System.out.println("Error populating " + apiObject.getClass() + " object:\n" + e.getMessage());
        } catch (InvocationTargetException e) {
            System.out.println("Error populating " + apiObject.getClass() + " object:\n" + e.getMessage());
        }
    }

    public String changeStyleFromUnderscoresToCamelCase(String original) {

        //Get handles on our data
        String modified = original;
        int nextUnderscore = modified.indexOf('_');

        //While there are still underscores in the original string...
        while (nextUnderscore > 0) {

            //Grab the segment before the underscore and capitalize the first letter after it
            String firstPart = modified.substring(0, nextUnderscore);
            String firstLetter = modified.substring(nextUnderscore + 1, nextUnderscore + 2);

            //Piece it together
            modified = firstPart + firstLetter.toUpperCase() + modified.substring(nextUnderscore + 2);

            //Update our pointer to the underscore in the string
            nextUnderscore = modified.indexOf('_');
        }

        return modified;
    }
}
