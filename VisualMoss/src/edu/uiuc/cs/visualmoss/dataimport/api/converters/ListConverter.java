package edu.uiuc.cs.visualmoss.dataimport.api.converters;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Dec 4, 2010
 *
 * <p> <p> Generic converter for list objects. This will individually add build objects into a list from the generic
 * type returned by the CoMoTo API
 */
public class ListConverter implements Converter {

    /**
     * Converts a generic object into a <code>List</code> of typed objects
     *
     * @param destinationClass The class to which to convert (a list type)
     * @param abstractObject The object to convert
     * @return  A typed list containing the non-generic data from <code>abstractObject</code>
     */
    public Object convert(Class destinationClass, Object abstractObject) {

        //Get the class of the abstract object
        Class sourceClass = abstractObject.getClass();

        //Let's put a check into this to ensure that the <code>abstractObject</code> is an array
        if(sourceClass.isArray()){

            //Try to build a list of integers from this array, and just try something else if this fails
            try{
                List<Integer> integerList = new ArrayList<Integer>();
                Object[] integerArray = (Object[]) abstractObject;

                for(Object integer: integerArray){
                    integerList.add((Integer) integer);
                }

                return integerList;

            } catch(ClassCastException e){}

            //Try to build a list of strings from this array, and just try something else if this fails
            try{
                List<String> stringList = new ArrayList<String>();
                Object[] stringArray = (Object[]) abstractObject;

                for(Object string: stringArray){
                    stringList.add((String) string);
                }

                return stringList;

            } catch(ClassCastException e){}

        } else {
            throw new ConversionException("Cannot convert non-array to list!");
        }
        return null;
    }
}
