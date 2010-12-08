package edu.uiuc.cs.visualmoss.dataimport.api.converters;

import edu.uiuc.cs.visualmoss.dataimport.api.objects.Season;
import org.apache.commons.beanutils.Converter;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Dec 4, 2010
 *
 * <p> <p> Generic converter for <code>Season</code> objects so we can populate <code>Season</code> using reflection
 */
public class SeasonConverter implements Converter {


    /**
     * Converts a generic object into a <code>Season</code> enum
     *
     * @param destinationClass The class to which to convert
     * @param abstractObject The object to convert
     * @return  A <code>Season</code> enum
     */
    public Object convert(Class destinationClass, Object abstractObject) {

        try{

            //Try to parse this object into a Season enumerated type
            return Season.valueOf((String) abstractObject);

        } catch(ClassCastException e){
            return null;

        } catch ( EnumConstantNotPresentException e){
            return null;
        }
    }
}
