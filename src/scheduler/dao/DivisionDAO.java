package scheduler.dao;

import scheduler.model.Country;
import scheduler.model.Division;

import java.util.List;

/**
 * Interface for retrieving First-Level Division data from the database.
 * */
public interface DivisionDAO {
    /**
     * Retrieves all First-Level Divisions from the database.
     * @return the list of all divisions
     * */
    List<Division> getAllDivisions();

    /**
     * Retrieves a First-Level Division with a specific id from the database.
     * @param id the id of the division
     * @return the division with the id
     * */
    Division getDivision(int id);

    /**
     * Retrieves all First-Level Divisions for a specific country.
     * @param country the country to search for
     * @return the list of divisions associated with the country
     * */
    List<Division> getDivisionsByCountry(Country country);
}
