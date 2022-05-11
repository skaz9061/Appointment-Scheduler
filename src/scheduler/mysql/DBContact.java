package scheduler.mysql;

import scheduler.dao.ContactDAO;
import scheduler.model.Contact;
import scheduler.util.Exceptions;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides access to Contact records in the database.
 * @author Steven Kazmierkiewicz
 * */
public class DBContact implements ContactDAO {
    private Connection conn = DBConnection.getConnection();

    @Override
    public List<Contact> getAllContacts() {
        String sql = "SELECT * FROM contacts";
        List<Contact> contactList = new ArrayList<>();

        try (var ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("Contact_ID");
                String name = rs.getString("Contact_Name");

                contactList.add(new Contact(id, name));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return contactList;
    }

    @Override
    public Contact getContact(int id) {
        String sql = "SELECT * FROM contacts WHERE Contact_ID = ?";

        try (var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (var rs = ps.executeQuery();) {
                if(rs.next()) {
                    int contactId = rs.getInt("Contact_ID");
                    String name = rs.getString("Contact_Name");
                    return new Contact(contactId, name);
                } else {
                    throw new Exceptions.ContactException("No contact with id " + id);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }
}
