package scheduler.util;

import java.util.Locale;

/**
 * Provides utilities for modifying strings.
 * */
public class WordUtils {

    /**
     * Capitalize the first character of a string.
     * @param str the string to capitalize
     * @return the capitalized string
     * */
    public static String capitalize(String str) {
        if (str.length() == 0)
            return str;

        return String.valueOf(str.charAt(0)).toUpperCase(Locale.ROOT) + str.substring(1);
    }

    /**
     * Capitalize the first character of each word (separated by a space) in a string.
     * @param str the string to capitalize
     * @return the capitalized string
     * */
    public static String capitalizeAll(String str) {
        var words = str.split(" ");
        StringBuilder result = new StringBuilder();

        for (var word : words) {
            result.append(WordUtils.capitalize(word) + " ");
        }

        return result.toString().strip();
    }
}
