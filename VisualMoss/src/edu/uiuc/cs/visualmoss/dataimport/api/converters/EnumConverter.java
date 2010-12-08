package edu.uiuc.cs.visualmoss.dataimport.api.converters;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.StringConverter;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Dec 8, 2010
 *
 * <p> <p> This class takes care of converting String objects into enums in the apache reflection library. Note that this
 *          class must be registered as the String converter, and will default to the standard string converter if the
 *          parameter is not an enum.
 */
public class EnumConverter implements Converter {

    /**
     * Converts a generic object into an enum
     *
     * @param destinationClass The class to which to convert
     * @param abstractObject The object to convert
     * @return  An enum, or String if the object is not an enum
     */
    public Object convert(Class destinationClass, Object abstractObject) {

        if(destinationClass.isEnum()){
            try{

                //Try to parse this object into a Language enumerated type
                return Enum.valueOf(destinationClass, (String) abstractObject);

            } catch(ClassCastException e){
                return null;
            } catch ( EnumConstantNotPresentException e){
                return null;
            }
        } else {

            StringConverter stringConverter = new StringConverter();
            return stringConverter.convert(destinationClass, abstractObject);
        }
    }
}
