package scheduler.util;

/**
 * Contains custom exceptions.
 * @author Steven Kazmierkiewicz*/
public class Exceptions {
    /**
     * An exception associated with a customer.
     * */
    public static class CustomerException extends RuntimeException {
        public CustomerException() { super(); }
        public CustomerException(String m) { super(m); }
    }

    /**
     * An exception associated with a user.
     * */
    public static class UserException extends RuntimeException {
        public UserException() { super(); }
        public UserException(String m) { super(m); }
    }

    /**
     * An exception associated with a contact.
     * */
    public static class ContactException extends RuntimeException {
        public ContactException() { super(); }
        public ContactException(String m) { super(m); }
    }

    /**
     * An exception associated with a country.
     * */
    public static class CountryException extends RuntimeException {
        public CountryException() { super(); }
        public CountryException(String m) { super(m); }
    }

    /**
     * An exception associated with a division.
     * */
    public static class DivisionException extends RuntimeException {
        public DivisionException() { super(); }
        public DivisionException(String m) { super(m); }
    }
}
