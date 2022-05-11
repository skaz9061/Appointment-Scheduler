package scheduler.mysql;

import scheduler.dao.CustomerDAO;
import scheduler.model.Customer;
import scheduler.model.Division;
import scheduler.model.User;
import scheduler.util.Exceptions;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides access to Customer records in the database.
 * @author Steven Kazmierkiewicz
 * */
public class DBCustomer implements CustomerDAO {
    private Connection conn = DBConnection.getConnection();
    private DBDivision dbDivision = new DBDivision();

    @Override
    public List<Customer> getAllCustomers() {
        String sql = "SELECT * FROM customers";
        List<Customer> customerList = new ArrayList<>();

        try (var ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                int divisionId = rs.getInt("Division_ID");
                Division division = dbDivision.getDivision(divisionId);

                customerList.add(new Customer(id, name, address, postalCode, phone, division));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return customerList;
    }

    @Override
    public Customer getCustomer(int id) {
        String sql = "SELECT * FROM customers WHERE Customer_ID = ?";

        try (var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (var rs = ps.executeQuery();) {
                if(rs.next()) {
                    int customerId = rs.getInt("Customer_ID");
                    String name = rs.getString("Customer_Name");
                    String address = rs.getString("Address");
                    String postalCode = rs.getString("Postal_Code");
                    String phone = rs.getString("Phone");
                    int divisionId = rs.getInt("Division_ID");
                    Division division = dbDivision.getDivision(divisionId);

                    return new Customer(customerId, name, address, postalCode, phone, division);
                } else {
                    throw new Exceptions.CustomerException("No customer with id " + id);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean createCustomer(String name, String address, String postalCode, String phone, Division division) {
        String sql = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Division_ID, " +
                "Create_Date, Created_By, Last_Update, Last_Updated_By) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setInt(5, division.getId());

            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            ps.setTimestamp(6, now);
            ps.setString(7, User.getCurrentUser().getName());
            ps.setTimestamp(8, now);
            ps.setString(9, User.getCurrentUser().getName());

            return ps.executeUpdate() > 0;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE customers SET " +
                "Customer_Name = ?," +
                "Address = ?," +
                "Postal_Code = ?," +
                "Phone = ?," +
                "Division_ID = ?," +
                "Last_Update = ?," +
                "Last_Updated_By = ?" +
                "WHERE Customer_ID = ?";

        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPostalCode());
            ps.setString(4, customer.getPhone());
            ps.setInt(5, customer.getDivision().getId());
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(7, User.getCurrentUser().getName());
            ps.setInt(8, customer.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteCustomer(int id) {
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";

        try (var ps = conn.prepareStatement(sql)){
            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }
}
