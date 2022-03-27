package helper;

import Model.CountriesModel;
import Model.UsersModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

public class countriesDbAccess {

    /**
     * Get all countries from countries table in SQL
     * Program is not to write to countries table, so only created this method
     * @return
     */
    public static ObservableList<CountriesModel> getAllCountries() {
        ObservableList<CountriesModel> countriesList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM client_schedule.countries";
        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int countryID = rs.getInt("Country_ID");
                String country = rs.getString("Country");
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                CountriesModel c = new CountriesModel(countryID, country, createDate, createdBy, lastUpdate, lastUpdatedBy);
                countriesList.add(c);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return countriesList;
    }
}
