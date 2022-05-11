package scheduler.model;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Represents an appointment on the schedule.
 * @author Steven Kazmierkiewicz
 * */
public class Appointment {
    private final int id;
    private String title, description, location;
    private Type type;
    private LocalDateTime start, end;
    private Customer customer;
    private User user;
    private Contact contact;

    /**
     * Creates an Appointment instance by providing individual values for all fields.
     * @param id the id to set
     * @param title the title to set
     * @param description the description to set
     * @param location the location to set
     * @param type the type to set
     * @param start the start date and time to set
     * @param end the end date and time to set
     * @param customer the customer to set
     * @param contact the contact to set
     * @param user the user to set
     * */
    public Appointment(int id, String title, String description, String location, Type type,
                       LocalDateTime start, LocalDateTime end, Customer customer, User user, Contact contact) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customer = new Customer(customer);
        this.user = new User(user);
        this.contact = new Contact(contact);
    }

    /**
     * Creates a new Appointment instance with the same values as the provided Appointment object.
     * @param appointment the appointment to copy
     * */
    public Appointment(Appointment appointment) {
        this.id = appointment.id;
        this.title = appointment.title;
        this.description = appointment.description;
        this.location = appointment.location;
        this.type = appointment.type;
        this.start = appointment.start;
        this.end = appointment.end;
        this.customer = new Customer(appointment.customer);
        this.user = new User(appointment.user);
        this.contact = new Contact(appointment.contact);
    }

    /**
     * Getter for the id.
     * @return the id
     * */
    public int getId() {
        return id;
    }

    /**
     * Getter for the title.
     * @return the title
     * */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for the title.
     * @param title the title to set
     * */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for the description.
     * @return the description
     * */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for the description.
     * @param description the description to set
     * */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for the location.
     * @return the location
     * */
    public String getLocation() {
        return location;
    }

    /**
     * Setter for the location.
     * @param location the location to set
     * */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Getter for the type.
     * @return the type
     * */
    public Type getType() {
        return type;
    }

    /**
     * Setter for the type.
     * @param type the type to set
     * */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Getter for the start date and time
     * @return the start date and time
     * */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Setter for the start date and time.
     * @param start the start date and time to set
     * */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    /**
     * Getter for the end date and time.
     * @return the end date and time
     * */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Setter for the end date and time.
     * @param end the end date and time to set
     * */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    /**
     * Getter for the customer.
     * @return the customer
     * */
    public Customer getCustomer() {
        return new Customer(customer);
    }

    /**
     * Provides direct access to the customers ID.
     * @return the customer's id
     * */
    public int getCustomerId() { return customer.getId(); }

    /**
     * Provides direct access to the customers name.
     * @return the customer's name
     * */
    public String getCustomerName() { return customer.getName(); }

    /**
     * Setter for the customer.
     * @param customer the customer to set
     * */
    public void setCustomer(Customer customer) {
        this.customer = new Customer(customer);
    }

    /**
     * Getter for the user.
     * @return the user
     * */
    public User getUser() {
        return new User(user);
    }

    /**
     * Setter for the user.
     * @param user the user to set
     * */
    public void setUser(User user) {
        this.user = new User(user);
    }

    /**
     * Getter for the contact.
     * @return the contact
     * */
    public Contact getContact() {
        return new Contact(contact);
    }

    /**
     * Setter for the contact.
     * @param contact the contact to set
     * */
    public void setContact(Contact contact) {
        this.contact = new Contact(contact);
    }

    /**
     * Gives the total minutes from now until the appointments start datetime based on the default timezone.
     * It returns a negative number if the appointment's datetime has already passed.
     * @return the minutes until the appointment
     * */
    public long minutesUntil() {
        return LocalDateTime.now().until(start, ChronoUnit.MINUTES);
    }

    /**
     * Checks if the object has equal values.
     * @return true if the objects are equal
     * */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return id == that.id &&
                Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(location, that.location) &&
                type == that.type &&
                Objects.equals(start, that.start) &&
                Objects.equals(end, that.end) &&
                Objects.equals(customer, that.customer) &&
                Objects.equals(user, that.user) &&
                Objects.equals(contact, that.contact);
    }

    /**
     * Provides a hash of the object's fields.
     * @return the hash value
     * */
    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, location, type, start, end, customer, user, contact);
    }

    /**
     * Return true if an appointment with given start and end date times overlaps the this appointment.
     * @param start the start date time
     * @param end the end date time
     * @return true if this appointment would overlap
     * */
    public boolean overlaps(LocalDateTime start, LocalDateTime end) {
        if (!start.isBefore(end))
            throw new DateTimeException("Start must be before End.");
        if (end.equals(this.start) || end.isBefore(this.start))
            return false;
        return !(start.equals(this.end) || start.isAfter(this.end));
    }

    /**
     * Getter for start as a formatted ISO string.
     * @return start as ISO string
     * */
    public String getStartFormatted() {
        return start.format(DateTimeFormatter.ISO_DATE) + " " + start.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    /**
     * Getter for end as a formatted ISO string.
     * @return end as ISO string
     * */
    public String getEndFormatted() {
        return end.format(DateTimeFormatter.ISO_DATE) + " " + end.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    /**
     * Contains the various Appointment types.
     * */
    public enum Type {
        INTRO("Introductory"),
        CLOSING("Closing"),
        SPECIAL_EVENT("Special Event"),
        PROGRESS_REPORT("Progress Report"),
        PLANNING("Planning Session"),
        DEBRIEF("De-Briefing");

        private final String value;

        Type(String value) {
            this.value = value;
        }

        /**
         * Getter for the string value.
         * @return the string value
         * */
        public String getValue() {
            return value;
        }

        /**
         * Converts the enum into a string representation.
         * @return the enum as a string
         * */
        @Override
        public String toString() {
            return this.getValue();
        }

        /**
         * Gets the correct Type enum based on the string value.
         * @param text the string value of the Type
         * @return the appointment type
         * @throws IllegalArgumentException if there is no corresponding type to the provided string value
         * */
        public static Type of(String text) throws IllegalArgumentException {
            switch (text) {
                case "Introductory":
                    return Type.INTRO;
                case "Closing":
                    return Type.CLOSING;
                case "Special Event":
                    return Type.SPECIAL_EVENT;
                case "Planning Session":
                    return Type.PLANNING;
                case "De-Briefing":
                    return Type.DEBRIEF;
                default:
                    throw new IllegalArgumentException("No such value for Appointment.Type");
            }
        }
    }
}
