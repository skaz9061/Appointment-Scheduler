package scheduler.model;

/**
 * The user who is associated with an appointment. Also keeps a static copy of the current user.
 * @author Steven Kazmierkiewicz
 * */
public class User extends Account {
    private static User currentUser = null;

    /**
     * Creates a User instance by providing individual values for all fields.
     * @param id the id to set
     * @param name the name to set
     * */
    public User(int id, String name) {
        super(id, name);
    }

    /**
     * Creates a new User instance with the same values as the provided User object.
     * @param acc the User to copy
     * */
    public User(User acc) {
        super(acc);
    }

    /**
     * Sets the current user of the application.
     * @param user the current user to set
     * */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    /**
     * Gets the current user of the application.
     * @return the current user
     * */
    public static User getCurrentUser() {
        return currentUser;
    }

}
