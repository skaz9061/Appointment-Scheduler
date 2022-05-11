package scheduler.dao;

import scheduler.model.Country;

import java.util.List;

/**
 * Interface for retrieving Country data from the database.
 * */
public interface CountryDAO {
    /**
     * Retrieves all countries from the database.
     * @return the list of all countries
     * */
    List<Country> getAllCountries();

    /**
     * Retrieves a country with a specific id from the database.
     * @param id the id of the country
     * @return the country with the id
     * */
    Country getCountry(int id);
}
