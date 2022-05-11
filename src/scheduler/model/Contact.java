package scheduler.model;

/**
 * The contact person for an appointment.
 * @author Steven Kazmierkiewicz
 * */
public class Contact extends Account {
    /**
     * Creates a Contact instance by providing individual values for all fields.
     * @param id the id to set
     * @param name the name to set
     * */
    public Contact(int id, String name) {
        super(id, name);
    }

    /**
     * Creates a new Contact instance with the same values as the provided Contact object.
     * @param acc the Contact to copy
     * */
    public Contact(Contact acc) {
        super(acc);
    }
}
