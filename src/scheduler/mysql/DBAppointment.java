package scheduler.mysql;

import scheduler.dao.AppointmentDAO;
import scheduler.model.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides access to Appointment records in the database.
 * @author Steven Kazmierkiewicz
 * */
public class DBAppointment implements AppointmentDAO {
    private Connection conn = DBConnection.getConnection();
    private DBCustomer dbCustomer = new DBCustomer();
    private DBContact dbContact = new DBContact();
    private DBUser dbUser = new DBUser();

    @Override
    public List<Appointment> getAllAppointments() {
        String sql = "SELECT * FROM appointments";
        List<Appointment> appointmentList = new ArrayList<>();

        try (var ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Appointment.Type apptType = Appointment.Type.of(type);
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                Customer customer = dbCustomer.getCustomer(rs.getInt("Customer_ID"));
                Contact contact = dbContact.getContact(rs.getInt("Contact_ID"));
                User user = dbUser.getUser(rs.getInt("User_ID"));

                appointmentList.add(new Appointment(id, title, description, location, apptType, start, end, customer, user, contact));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    @Override
    public List<Appointment> getAppointmentsFor(Customer customer) {
        String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";
        List<Appointment> appointmentList = new ArrayList<>();

        try (var ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customer.getId());

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("Appointment_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String type = rs.getString("Type");
                    Appointment.Type apptType = Appointment.Type.of(type);
                    LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                    LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                    Customer apptCustomer = dbCustomer.getCustomer(rs.getInt("Customer_ID"));
                    Contact contact = dbContact.getContact(rs.getInt("Contact_ID"));
                    User user = dbUser.getUser(rs.getInt("User_ID"));

                    appointmentList.add(new Appointment(id, title, description, location, apptType, start, end, apptCustomer, user, contact));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    @Override
    public List<Appointment> getAppointmentsFor(Contact contact) {
        String sql = "SELECT * FROM appointments WHERE Contact_ID = ?";
        List<Appointment> appointmentList = new ArrayList<>();

        try (var ps = conn.prepareStatement(sql)) {

            ps.setInt(1, contact.getId());

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("Appointment_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String type = rs.getString("Type");
                    Appointment.Type apptType = Appointment.Type.of(type);
                    LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                    LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                    Customer customer = dbCustomer.getCustomer(rs.getInt("Customer_ID"));
                    Contact apptContact = dbContact.getContact(rs.getInt("Contact_ID"));
                    User user = dbUser.getUser(rs.getInt("User_ID"));

                    appointmentList.add(new Appointment(id, title, description, location, apptType, start, end, customer, user, apptContact));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return appointmentList;    }

    @Override
    public Appointment getAppointment(int id) {
        String sql = "SELECT * FROM appointments WHERE Appointment_ID = ?";

        try (var ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    int apptId = rs.getInt("Appointment_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String type = rs.getString("Type");
                    Appointment.Type apptType = Appointment.Type.of(type);
                    LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                    LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                    Customer customer = dbCustomer.getCustomer(rs.getInt("Customer_ID"));
                    Contact apptContact = dbContact.getContact(rs.getInt("Contact_ID"));
                    User user = dbUser.getUser(rs.getInt("User_ID"));

                    return new Appointment(apptId, title, description, location, apptType, start, end, customer, user, apptContact);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean createAppointment(String title, String description, String location, Appointment.Type type, Customer customer, Contact contact, User user, LocalDateTime start, LocalDateTime end) {
        String sql = "INSERT INTO " +
                "appointments(Title, Description, Location, Type, Start, End, Customer_ID, " +
                "User_ID, Contact_ID, Create_Date, Created_By, Last_Update, Last_Updated_By) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type.toString());
            ps.setTimestamp(5, Timestamp.valueOf(start));
            ps.setTimestamp(6, Timestamp.valueOf(end));
            ps.setInt(7, customer.getId());
            ps.setInt(8, user.getId());
            ps.setInt(9, contact.getId());

            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            ps.setTimestamp(10, now);
            ps.setString(11, User.getCurrentUser().getName());
            ps.setTimestamp(12, now);
            ps.setString(13, User.getCurrentUser().getName());

            return ps.executeUpdate() > 0;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateAppointment(Appointment appointment) {
        String sql = "UPDATE appointments SET " +
                "Title = ?," +
                "Description = ?," +
                "Location = ?," +
                "Type = ?," +
                "Start = ?," +
                "End = ?," +
                "Customer_ID = ?," +
                "User_ID = ?," +
                "Contact_ID = ?," +
                "Last_Update = ?," +
                "Last_Updated_By = ?" +
                "WHERE Appointment_ID = ?";

        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getType().toString());
            ps.setTimestamp(5, Timestamp.valueOf(appointment.getStart()));
            ps.setTimestamp(6, Timestamp.valueOf(appointment.getEnd()));
            ps.setInt(7, appointment.getCustomer().getId());
            ps.setInt(8, appointment.getUser().getId());
            ps.setInt(9, appointment.getContact().getId());
            ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(11, User.getCurrentUser().getName());
            ps.setInt(12, appointment.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteAppointment(int id) {
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";

        try (var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    @Override
    public int numOfAppointments(Customer customer) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE Customer_ID = ?";

        try (var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customer.getId());

            try (var rs = ps.executeQuery()){
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return 0;
    }

    @Override
    public Map<Appointment.Type, Long> appointmentsByType() {
        String sql = "SELECT Type, COUNT(*) FROM appointments GROUP BY Type";
        Map<Appointment.Type, Long> typeCounts = new HashMap<>();

        try (var ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                typeCounts.put(Appointment.Type.of(rs.getString(1)), rs.getLong(2));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return typeCounts;
    }
}
