package edu.uiuc.cs.visualmoss.dataimport.api;

import edu.uiuc.cs.visualmoss.dataimport.api.converters.*;
import edu.uiuc.cs.visualmoss.dataimport.api.objects.Language;
import edu.uiuc.cs.visualmoss.dataimport.api.objects.Season;
import edu.uiuc.cs.visualmoss.dataimport.api.objects.Type;
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
 *
 * <p> <p> This class lets us translate between Maps and CoMoToAPIObjects and back again
 */
public class CoMoToAPIReflector<T> {

    public void populate(T apiObject, Map map){

        //Build a new, empty hash map for the renamed data
        Map<String, Object> camelCaseMap = new HashMap<String, Object>();

        //Pull the key-value pairs from the original map and rename the keys
        Set<String> keys = map.keySet();
        for(String key : keys){
            String newKey = changeStyleFromUnderscoresToCamelCase(key);
            camelCaseMap.put(newKey, map.get(key));
        }

        //Register the type converters for populating non-primitive types on objects
        ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
        convertUtilsBean.deregister(List.class);
        convertUtilsBean.deregister(Type.class);
        convertUtilsBean.deregister(Season.class);
        convertUtilsBean.deregister(Language.class);
        convertUtilsBean.deregister(DateFormat.class);
        convertUtilsBean.register(new ListConverter(), List.class);
        convertUtilsBean.register(new DateFormatConverter(), DateFormat.class);
        convertUtilsBean.register(new TypeConverter(), Type.class);
        convertUtilsBean.register(new SeasonConverter(), Season.class);
        convertUtilsBean.register(new LanguageConverter(), Language.class);

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

    public String changeStyleFromUnderscoresToCamelCase(String original){

        //Get handles on our data
        String modified = original;
        int nextUnderscore = modified.indexOf('_');

        //While there are still underscores in the original string...
        while(nextUnderscore > 0){

            //Grab the segment before the underscore and capitalize the first letter after it
            String firstPart = modified.substring(0, nextUnderscore);
            String firstLetter = modified.substring(nextUnderscore+1, nextUnderscore+2);

            //Piece it together
            modified = firstPart + firstLetter.toUpperCase() + modified.substring(nextUnderscore+2);

            //Update our pointer to the underscore in the string
            nextUnderscore = modified.indexOf('_');
        }

        return modified;
    }
}
