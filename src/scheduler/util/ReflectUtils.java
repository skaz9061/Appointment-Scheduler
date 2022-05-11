package scheduler.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Provides methods for helping with reflective operations.
 * */
public class ReflectUtils {
    /**
     * Recursive helper method for getting a list of all fields, including those from superclasses.
     * Got solution to this problem from stack overflow:
     * <a href="https://stackoverflow.com/questions/1042798/retrieving-the-inherited-attribute-names-values-using-java-reflection" target="_blank">HERE</a>.
     * @param list the current list of fields
     * @param cls the class to get the fields from
     * @return the list of all fields
     * */
    public static List<Field> getFields(List<Field> list, Class cls) {
        list.addAll(Arrays.asList(cls.getDeclaredFields()));

        if(cls.getSuperclass() != null)
            return getFields(list, cls.getSuperclass());

        return list;
    }

    /**
     * Retrieve the getter method for a field on an object. Format is 'getFieldName'.
     * @param obj the object
     * @param fieldName the field to retrieve the getter for
     * @return the getter method
     * @throws NoSuchMethodException if there is no valid getter method on the object
     * */
    public static Method getter(Object obj, String fieldName) throws NoSuchMethodException {
        return obj.getClass().getMethod("get" + WordUtils.capitalize(fieldName));
    }
}
