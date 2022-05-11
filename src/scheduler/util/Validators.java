package scheduler.util;

/**
 * Assists in validation of fields.
 * */
public class Validators {
    /**
     * Checks if a string is empty.
     * @param str the string to check
     * @return true if the string is not an empty string
     * */
    public static boolean notEmpty(String str) {
        return notNull(str) && !str.equals("");
    }

    /**
     * Checks if an object is null.
     * @param obj the object to check
     * @return true if the object is not null.
     * */
    public static boolean notNull(Object obj) {
        return obj != null;
    }

    /**
     * Checks if a string can be parsed to an integer.
     * @param num the value to check
     * @return true if the string is an integer
     * */
    public static boolean isInteger(String num) {
        try {
            Integer.parseInt(num);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Result object from doing a validation check.
     * */
    public static class ValidationResult {
        private boolean valid;
        private String error = "";

        public ValidationResult(boolean valid, String error) {
            this.valid = valid;
            this.error = error;
        }

        /**
         * Getter for valid.
         * @return true if the validation was successful
         * */
        public boolean isValid() {
            return valid;
        }

        /**
         * Gets the error string of the validation.
         * @return the error
         * */
        public String getError() {
            return error;
        }
    }
}
