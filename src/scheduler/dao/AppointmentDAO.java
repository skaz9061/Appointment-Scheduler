package scheduler.dao;

import scheduler.model.Appointment;
import scheduler.model.Contact;
import scheduler.model.Customer;
import scheduler.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Interface for retrieving Appointment data from the database.
 * */
public interface AppointmentDAO {
    /**
     * Retrieves all appointments from the database.
     * @return the list of all appointments
     * */
    List<Appointment> getAllAppointments();

    /**
     * Retrieves all appointments for a specific customer.
     * @param customer the customer to search for
     * @return the list of appointments for the customer
     * */
    List<Appointment> getAppointmentsFor(Customer customer);

    /**
     * Retrieves all appointments for a specific contact.
     * @param contact the contact to search for
     * @return the list of appointments for the contact
     * */
    List<Appointment> getAppointmentsFor(Contact contact);

    /**
     * Retrieves an appointment from the database with a specific id.
     * @param id the id of the appointment
     * @return the appointment with the id
     * */
    Appointment getAppointment(int id);

    /**
     * Creates an appointment in the database.
     * @param title the title
     * @param description the description
     * @param location the location
     * @param type the type
     * @param customer the customer
     * @param contact the contact
     * @param user the user
     * @param start the start date and time
     * @param end the end date and time
     * @return true if the creation of the appointment is successful
     */
    boolean createAppointment(String title, String description, String location, Appointment.Type type,
                              Customer customer, Contact contact, User user, LocalDateTime start, LocalDateTime end);

    /**
     * Updates an appointment in the database.
     * @param appointment the updated appointment
     * @return true if the update is successful
     * */
    boolean updateAppointment(Appointment appointment);

    /**
     * Delete an appointment from the database with a specific id.
     * @param id the id of the appointment to delete
     * @return true if the delete is successful
     * */
    boolean deleteAppointment(int id);

    /**
     * Delete an appointment from the database.
     * @param appointment the appointment to delete
     * @return true if the delete is successful
     * */
    default boolean deleteAppointment(Appointment appointment) {
        return deleteAppointment(appointment.getId());
    }

    /**
     * Retrieves the number of appointments associated with a customer.
     * @param customer the customer
     * @return the number of appointments for the customer
     * */
    int numOfAppointments(Customer customer);

    /**
     * Retrieves the number of appointments for each appointment type.
     * @return the map associating appointment type with its count of appointments
     * */
    Map<Appointment.Type, Long> appointmentsByType();
}
