package scheduler.dao;

import scheduler.model.Contact;

import java.util.List;

/**
 * Interface for retrieving Contact data from the database.
 * */
public interface ContactDAO {
    /**
     * Retrieves all contacts from the database.
     * @return the list of all contacts
     * */
    List<Contact> getAllContacts();

    /**
     * Retrieves a contact with a specific id from the database.
     * @param id the id of the contact
     * @return the contact with the id
     * */
    Contact getContact(int id);
}
