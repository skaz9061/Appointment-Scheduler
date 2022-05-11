package scheduler.mysql;

import scheduler.dao.UserDAO;
import scheduler.model.User;
import scheduler.util.Exceptions;
import scheduler.util.WordUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Provides access to User records in the database.
 * @author Steven Kazmierkiewicz
 * */
public class DBUser implements UserDAO {
    private Connection conn;

    {
        conn = DBConnection.getConnection();
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        List<User> userList = new ArrayList<>();

        try (var ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("User_ID");
                String name = rs.getString("User_Name");

                userList.add(new User(id, name));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return userList;
    }

    @Override
    public User getUser(int id) {
        String sql = "SELECT * FROM users WHERE User_ID = ?";

        try (var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try(var rs = ps.executeQuery();) {
                if(rs.next()) {
                    int userId = rs.getInt("User_ID");
                    String name = rs.getString("User_Name");
                    return new User(userId, name);
                } else {
                    throw new Exceptions.UserException("No user with id " + id);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    @Override
    public User login(String name, String pass) {
        String sql = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";

        try (var ps = conn.prepareStatement(sql)){
            ps.setString(1, name);
            ps.setString(2, pass);

            try (var rs = ps.executeQuery()) {
                if(rs.next()) {
                    int id = rs.getInt("User_ID");
                    String username = rs.getString("User_Name");
                    return new User(id, username);
                } else {
                    ResourceBundle rb = ResourceBundle.getBundle("scheduler/viewcontroller/login/login", Locale.getDefault());
                    throw new Exceptions.UserException(WordUtils.capitalize(rb.getString("incorrectUsernameOrPassword")));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }
}
