package scheduler.mysql;

import scheduler.dao.CountryDAO;
import scheduler.model.Country;
import scheduler.util.Exceptions;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides access to Country records in the database.
 * @author Steven Kazmierkiewicz
 * */
public class DBCountry implements CountryDAO {
    private Connection conn = DBConnection.getConnection();

    @Override
    public List<Country> getAllCountries() {
        String sql = "SELECT * FROM countries";
        List<Country> countryList = new ArrayList<>();

        try (var ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("Country_ID");
                String name = rs.getString("Country");

                countryList.add(new Country(id, name));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return countryList;
    }

    @Override
    public Country getCountry(int id) {
        String sql = "SELECT * FROM countries WHERE Country_ID = ?";

        try (var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (var rs = ps.executeQuery();) {
                if(rs.next()) {
                    int countryId = rs.getInt("Country_ID");
                    String name = rs.getString("Country");
                    return new Country(countryId, name);
                } else {
                    throw new Exceptions.CountryException("No country with id " + id);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }
}
