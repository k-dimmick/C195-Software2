package helper;

import Model.ContactModel;
import Model.DivisionModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

public class divisionDbAccess {

    /**
     * get all division data from SQL database
     * Program does not have functionality to add divisions; did not include option for same within program
     * @return
     */
    public static ObservableList<DivisionModel> getAllDivisions() {
        ObservableList<DivisionModel> DivisionList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM client_schedule.first_level_divisions WHERE Division = \"Texas\" or Division = \"Oklahoma\" or Division = \"Illinois\" or Division = \"Ontario\" or Division = \"British Columbia\" or Division = \"Quebec\" or Division = \"Scotland\" or Division = \"England\" or Division = \"Northern Ireland\"";
        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int divisionID = rs.getInt("Division_ID");
                String division = rs.getString("Division");
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int countryID = rs.getInt("Country_ID");
                DivisionModel d = new DivisionModel(divisionID, division, createDate, createdBy, lastUpdate, lastUpdatedBy, countryID);
                DivisionList.add(d);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return DivisionList;
    }
}
