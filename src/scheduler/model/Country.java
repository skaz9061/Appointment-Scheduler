package scheduler.model;

import java.util.Objects;

/**
 * An immutable country in the database.
 * @author Steven Kazmierkiewicz
 * */
public final class Country {
    private final int id;
    private final String country;

    /**
     * Creates a Country instance by providing individual values for all fields.
     * @param id the id to set
     * @param country the country to set
     * */
    public Country(int id, String country) {
        this.id = id;
        this.country = country;
    }

    /**
     * Creates a new Country instance with the same values as the provided Country object.
     * @param country the country to copy
     * */
    public Country(Country country) {
        this.id = country.id;
        this.country = country.country;
    }

    /**
     * Getter for id.
     * @return the id
     * */
    public int getId() {
        return id;
    }

    /**
     * Gets the country name as a string.
     * @return the country name
     * */
    public String get() {
        return country;
    }

    /**
     * Checks if the object has equal values.
     * @return true if the objects are equal
     * */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country1 = (Country) o;
        return id == country1.id && Objects.equals(country, country1.country);
    }

    /**
     * Provides a hash of the object's fields.
     * @return the hash value
     * */
    @Override
    public int hashCode() {
        return Objects.hash(id, country);
    }

    /**
     * Converts the object into a string representation.
     * @return the object as a string
     * */
    @Override
    public String toString() {
        return country;
    }
}
