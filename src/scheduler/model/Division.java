package scheduler.model;

import java.util.Objects;

/**
 * An immutable first-level division in the database.
 * @author Steven Kazmierkiewicz
 * */
public final class Division {
    private final int id;
    private final String division;
    private final Country country;

    /**
     * Creates a Division instance by providing individual values for all fields.
     * @param id the id to set
     * @param division the first-level division name to set
     * @param country the country to set
     * */
    public Division(int id, String division, Country country) {
        this.id = id;
        this.division = division;
        this.country = country;
    }

    /**
     * Creates a new Division instance with the same values as the provided Division object.
     * @param division the division to copy
     * */
    public Division(Division division) {
        this.id = division.id;
        this.division = division.division;
        this.country = division.country;
    }

    /**
     * Getter for id.
     * @return the id
     * */
    public int getId() {
        return id;
    }

    /**
     * Gets the division name as a string.
     * @return the division name
     * */
    public String get() {
        return division;
    }

    /**
     * Getter for the country.
     * @return the country
     * */
    public Country getCountry() {
        return country;
    }

    /**
     * Checks if the object has equal values.
     * @param o the object to check
     * @return true if the objects are equal
     * */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Division division1 = (Division) o;
        return id == division1.id &&
                Objects.equals(division, division1.division) &&
                Objects.equals(country, division1.country);
    }

    /**
     * Provides a hash of the object's fields.
     * @return the hash value
     * */
    @Override
    public int hashCode() {
        return Objects.hash(id, division, country);
    }

    /**
     * Converts the object into a string representation.
     * @return the object as a string
     * */
    @Override
    public String toString() {
        return division;
    }
}
