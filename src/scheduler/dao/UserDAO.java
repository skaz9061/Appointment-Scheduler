package scheduler.dao;

import scheduler.model.User;

import java.util.List;

/**
 * Interface for retrieving User data from the database.
 * */
public interface UserDAO {
    /**
     * Retrieves all users from the database.
     * @return the list of all users
     * */
    List<User> getAllUsers();

    /**
     * Retrieves a user with a specific id from the database.
     * @param id the id of the user
     * @return the user with the id
     * */
    User getUser(int id);

    /**
     * Attempts to login with given username and password.
     * @param name the username
     * @param pass the password
     * @return the User object for the user
     * */
    User login(String name, String pass);
}
