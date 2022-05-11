package scheduler.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provides the connection to the database.
 * @author Steven Kazmierkiewicz
 * */
public final class DBConnection {
    // jdbc url parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com:3306/";
    private static final String dbName = "WJ08aAS";
    private static final String timezoneFix = "?connectionTimeZone=SERVER";

    // full jdbc url
    private static final String jdbcUrl = protocol + vendorName + ipAddress + dbName + timezoneFix;

    private static final String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    private static Connection conn = null;

    private static final String username = "U08aAS";
    private static final String password = "53689234094";

    /**
     * Opens the connection to the database.
     * @return the connection
     * */
    public static Connection startConnection() {
        try {
            Class.forName(MYSQLJDBCDriver);
            conn = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return conn;
    }

    /**
     * Getter for the database connection.
     * @return the connections
     * */
    public static Connection getConnection() {
        return conn;
    }

    /**
     * Closes the database connection. In order to avoid race conditions, the method does nothing if there is an exception.
     * */
    public static void closeConnection() {
        try {
            conn.close();
        } catch (Exception e) {
            // Do Nothing
        }
    }

}
