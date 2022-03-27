package helper;

import Model.UsersModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

public class userDbAccess {

    /**
     * Obtain all user data from SQL database
     * Program is not to be able to manipulate SQL data for Users, did not add functionality in program
     *
     * @return
     */
    public static ObservableList<UsersModel> getAllUsers() {
        ObservableList<UsersModel> UsersList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM client_schedule.users";
        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                UsersModel U = new UsersModel(userId, userName, password, createDate, createdBy, lastUpdate, lastUpdatedBy);
                UsersList.add(U);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return UsersList;
    }


    public static ObservableList<UsersModel> attemptedLogIn(String user_Name) {
        ObservableList<UsersModel> UsersList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM client_schedule.users WHERE User_Name = ?";
        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setString(1, user_Name);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                UsersModel U = new UsersModel(userId, userName, password, createDate, createdBy, lastUpdate, lastUpdatedBy);
                UsersList.add(U);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return UsersList;
    }

}
