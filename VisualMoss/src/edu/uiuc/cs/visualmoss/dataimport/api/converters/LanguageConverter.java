package edu.uiuc.cs.visualmoss.dataimport.api.converters;

import edu.uiuc.cs.visualmoss.dataimport.api.objects.Language;
import org.apache.commons.beanutils.Converter;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Dec 4, 2010
 *
 * <p> <p> Generic converter for <code>Language</code> enumsso we can populate <code>Language</code> using reflection
 */
public class LanguageConverter implements Converter {

    /**
     * Converts a generic object into a <code>Language</code> enum
     *
     * @param destinationClass The class to which to convert
     * @param abstractObject The object to convert
     * @return  A <code>Language</code> enum
     */
    public Object convert(Class destinationClass, Object abstractObject) {

        try{

            //Try to parse this object into a Language enumerated type
            return Language.valueOf((String) abstractObject);

        } catch(ClassCastException e){
            return null;
        } catch ( EnumConstantNotPresentException e){
            return null;
        }
    }
}
