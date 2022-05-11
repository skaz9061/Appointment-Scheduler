package scheduler.util;

import javafx.collections.ObservableList;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides methods for searching observable lists.
 * @author Steven Kazmierkiewicz*/
public class SearchUtils {
    /**
     * Using reflection, searches all fields of the objects in an observable list for the specified search string.
     * The objects must have getter methods in the form 'getFieldName' where 'FieldName' is the name of the fields to
     * search. The string value of the result of the getter method is what the method uses to search. The lambda expression
     * provides the criteria for filtering each object in the list.
     * @param list the list to search
     * @param searchStr the string to search for
     * @param <T> the type of object in the list
     * @return the filtered list
     * */
    public static <T> ObservableList<T> search(ObservableList<T> list, String searchStr) {
        return list.filtered((T obj) -> {
            List<Field> fields = ReflectUtils.getFields(new ArrayList<>(), obj.getClass());

            for (var field: fields)  {
                String fieldName = field.getName();
                try {
                    String test = ReflectUtils.getter(obj, fieldName).invoke(obj).toString();
                    if (test.toUpperCase().contains(searchStr.toUpperCase()))
                        return true;
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return false;
        });
    }

    /**
     * Using reflection, searches a specific field of the objects in an observable list for the specified search string.
     * The objects must have a getter method in the form 'getFieldName' where 'FieldName' is the name of the field to
     * search. The string value of the result of the getter method is what the method uses to search. The lambda expression
     * provides the criteria for filtering each object in the list.
     * @param list the list to search
     * @param searchStr the string to search for
     * @param fieldName the field to search through
     * @param <T> the type of object in the list
     * @return the filtered list
     * */
    public static <T> ObservableList<T> search(ObservableList<T> list, String searchStr, String fieldName) {
        return list.filtered((T obj) -> {
            try {
                String val = ReflectUtils.getter(obj, fieldName).invoke(obj).toString().toUpperCase();
                return val.contains(searchStr.toUpperCase());
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return false;
        });
    }
}
