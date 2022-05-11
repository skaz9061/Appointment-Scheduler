package scheduler.mysql;

import scheduler.dao.CountryDAO;
import scheduler.dao.DivisionDAO;
import scheduler.model.Contact;
import scheduler.model.Country;
import scheduler.model.Division;
import scheduler.util.Exceptions;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides access to First-Level Division records in the database.
 * @author Steven Kazmierkiewicz
 * */
public class DBDivision implements DivisionDAO {
    private Connection conn = DBConnection.getConnection();
    private DBCountry dbCountry= new DBCountry();

    @Override
    public List<Division> getAllDivisions() {
        String sql = "SELECT * FROM first_level_divisions";
        List<Division> divisionList = new ArrayList<>();

        try (var ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("Division_ID");
                String name = rs.getString("Division");
                Country country = dbCountry.getCountry(rs.getInt("COUNTRY_ID"));

                divisionList.add(new Division(id, name, country));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return divisionList;
    }

    @Override
    public Division getDivision(int id) {
        String sql = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";

        try (var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (var rs = ps.executeQuery();) {
                if(rs.next()) {
                    int divisionId = rs.getInt("Division_ID");
                    String name = rs.getString("Division");
                    Country country = dbCountry.getCountry(rs.getInt("COUNTRY_ID"));
                    return new Division(divisionId, name, country);
                } else {
                    throw new Exceptions.DivisionException("No division with id " + id);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Division> getDivisionsByCountry(Country country) {
        String sql = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = ?";
        List<Division> divisionList = new ArrayList<>();

        try (var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, country.getId());

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("Division_ID");
                    String name = rs.getString("Division");

                    divisionList.add(new Division(id, name, country));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return divisionList;
    }
}
