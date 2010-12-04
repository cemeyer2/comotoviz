package edu.uiuc.cs.visualmoss.dataimport.api.converters;

import org.apache.commons.beanutils.Converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Dec 4, 2010
 *
 * <p> <p> Generic converter for <code>DateFormat</code> objects so we can populate <code>DateFormat</code> using reflection
 */
public class DateFormatConverter implements Converter {

    /**
     * Converts a generic object into a <code>DateFormat</code> object
     *
     * @param destinationClass The class to which to convert (a list type)
     * @param abstractObject The object to convert
     * @return  A typed list containing the non-generic data from <code>abstractObject</code>
     */
    public Object convert(Class destinationClass, Object abstractObject) {

        try{

            //Try to parse this object into a DateFormat object as a string
            String timestampString = (String) abstractObject;
            DateFormat timestamp = new SimpleDateFormat(timestampString);
            return timestamp;

        } catch(ClassCastException e){

            return null;
        }
    }
}
