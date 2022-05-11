package scheduler.model;

import java.util.Objects;

/**
 * The customer associated with an appointment.
 * @author Steven Kazmierkiewicz
 * */
public class Customer extends Account {
    private String address;
    private String postalCode;
    private String phone;
    private Division division;

    /**
     * Creates a Customer instance by providing individual values for all fields.
     * @param id the id to set
     * @param name the name to set
     * @param address the address to set
     * @param postalCode the postal code to set
     * @param phone the phone number to set
     * @param division the division to set
     * */
    public Customer(int id, String name, String address, String postalCode, String phone, Division division) {
        super(id, name);
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.division = division;
    }

    /**
     * Creates a new Customer instance with the same values as the provided Customer object.
     * @param acc the Customer to copy
     * */
    public Customer(Customer acc) {
        super(acc);
        this.address = acc.address;
        this.postalCode = acc.postalCode;
        this.phone = acc.phone;
        this.division = acc.division;
    }

    /**
     * Getter for the address.
     * @return the address
     * */
    public String getAddress() {
        return address;
    }

    /**
     * Setter for the address.
     * @param address the address to set
     * */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Getter for the postal code.
     * @return the postal code
     * */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Setter for the postal code.
     * @param postalCode the postal code to set
     * */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Getter for the phone number.
     * @return the phone number
     * */
    public String getPhone() {
        return phone;
    }

    /**
     * Setter for the phone number.
     * @param phone the phone number to set
     * */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Getter for the division.
     * @return the division
     * */
    public Division getDivision() {
        return division;
    }

    /**
     * Setter for the division.
     * @param division the division to set
     * */
    public void setDivision(Division division) {
        this.division = division;
    }

    /**
     * Getter for division name. Useful for direct access to the division name.
     * @return the division name
     * */
    public String getDivisionName() { return division.get(); }

    /**
     * Getter for country name. Useful for direct access to the country name.
     * @return the country name
     * */
    public String getCountryName() { return division.getCountry().get(); }

    /**
     * Checks if the object has equal values.
     * @return true if the objects are equal
     * */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(address, customer.address) &&
                Objects.equals(postalCode, customer.postalCode) &&
                Objects.equals(phone, customer.phone) &&
                Objects.equals(division, customer.division);
    }

    /**
     * Provides a hash of the object's fields.
     * @return the hash value
     * */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), address, postalCode, phone, division);
    }
}
