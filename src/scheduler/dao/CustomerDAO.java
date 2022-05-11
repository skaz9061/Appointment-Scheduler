package scheduler.dao;

import scheduler.model.Customer;
import scheduler.model.Division;

import java.util.List;

/**
 * Interface for retrieving Customer data from the database.
 * */
public interface CustomerDAO {
    /**
     * Retrieves all customers from the database.
     * @return the list of all customers
     * */
    List<Customer> getAllCustomers();

    /**
     * Retrieves a customer from the database with a specific id.
     * @param id the id of the customer
     * @return the customer with the id
     * */
    Customer getCustomer(int id);

    /**
     * Creates a customer in the database.
     * @param name the name
     * @param address the address
     * @param postalCode the postal code
     * @param phone the phone number
     * @param division the first-level division
     * @return true if the creation of the customer is successful
     */
    boolean createCustomer(String name, String address, String postalCode, String phone, Division division);

    /**
     * Updates the customer in the database.
     * @param customer the updated customer
     * @return true if the update was successful
     * */
    boolean updateCustomer(Customer customer);

    /**
     * Deletes the customer from the database with the specified id.
     * @param id the id of the customer to delete
     * @return true if the delete is successful
     * */
    boolean deleteCustomer(int id);

    /**
     * Deletes the customer from the database.
     * @param customer the customer to be deleted
     * @return true if the delete is successful
     * */
    default boolean deleteCustomer(Customer customer) {
        return deleteCustomer(customer.getId());
    }
}
