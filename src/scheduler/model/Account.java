package scheduler.model;

import java.util.Objects;

/**
 * Abstract class for account-like classes.
 * @author Steven Kazmierkiewicz
 * */
public abstract class Account {
    private final int id;
    private String name;

    /**
     * Creates an Account instance with an id and a name
     * @param id the id to set
     * @param name the name to set
     * */
    public Account(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Creates a new Account instance with the same values as the provided Account object.
     * @param acc the Account to copy
     * */
    public Account(Account acc) {
        this.id = acc.id;
        this.name = acc.name;
    }

    /**
     * Getter for id.
     * @return the id
     * */
    public int getId() {
        return id;
    }

    /**
     * Getter for name.
     * @return the name
     * */
    public String getName() {
        return name;
    }

    /**
     * Setter for name.
     * @param name the name to set
     * */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Checks if the object has equal values.
     * @return true if the objects are equal
     * */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id && Objects.equals(name, account.name);
    }

    /**
     * Provides a hash of the object's fields.
     * @return the hash value
     * */
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    /**
     * Converts the object into a string representation.
     * @return the object as a string
     * */
    @Override
    public String toString() {
        return "" + id + " : " + name;
    }
}
