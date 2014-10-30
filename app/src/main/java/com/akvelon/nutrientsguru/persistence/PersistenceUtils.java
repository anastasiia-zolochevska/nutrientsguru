package com.akvelon.nutrientsguru.persistence;

import java.util.Collection;

/**
 * @author Anastasiia Zolochevska
 */
public class PersistenceUtils {

    /*
    * Transforms given list to the string in format "(value1,value2,value3)"
    */
    public static String transformListToCommaSeparatedString(Collection<Long> values) {
        String result = "(";
        for (Long value : values) {
            result += value;
            result += ",";
        }
        result = result.substring(0, result.length() - 1);
        result += ")";
        return result;

    }
}
