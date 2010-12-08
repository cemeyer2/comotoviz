package edu.uiuc.cs.visualmoss.dataimport.api.converters;

import edu.uiuc.cs.visualmoss.dataimport.api.objects.Type;
import org.apache.commons.beanutils.Converter;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Dec 4, 2010
 *
 * <p> <p> Generic converter for <code>Type</code> objects so we can populate <code>Type</code> using reflection
 */
public class TypeConverter implements Converter {


    /**
     * Converts a generic object into a <code>Type</code> enum
     *
     * @param destinationClass The class to which to convert
     * @param abstractObject The object to convert
     * @return  A <code>Type</code> enum
     */
    public Object convert(Class destinationClass, Object abstractObject) {

        try{

            //Try to parse this object into a Type enumerated type
            return Type.valueOf((String) abstractObject);

        } catch(ClassCastException e){
            return null;

        } catch ( EnumConstantNotPresentException e){
            return null;
        }
    }
}
