package helper;


import Model.AppointmentModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.*;

public class appointmentDbAccess {

    /**
     * Obtain all appointments from SQL database
     * @return
     */
    public static ObservableList<AppointmentModel> getAllAppointments() {
        ObservableList<AppointmentModel> appointmentList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM client_schedule.appointments";
        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String appointmentType = rs.getString("Type");
                ZonedDateTime start = rs.getTimestamp("Start").toInstant().atZone(ZoneId.systemDefault());
                ZonedDateTime end = rs.getTimestamp("End").toInstant().atZone(ZoneId.systemDefault());
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");
                AppointmentModel A = new AppointmentModel(appointmentID, title, description, location, appointmentType, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, customerID, userID, contactID);
                appointmentList.add(A);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentList;
    }

    /**
     * add new appointment to SQL Database based off user's input
     * @param Title
     * @param Description
     * @param Location
     * @param Type
     * @param Start
     * @param End
     * @param Create_Date
     * @param Created_By
     * @param Last_Update
     * @param Last_Updated_By
     * @param Customer_ID
     * @param User_ID
     * @param Contact_ID
     */

    public static void addAppointment (String Title, String Description, String Location, String Type, ZonedDateTime Start, ZonedDateTime End, LocalDateTime Create_Date, String Created_By, Timestamp Last_Update, String Last_Updated_By, int Customer_ID, int User_ID, int Contact_ID){

        try {
            String sqlai = "INSERT INTO client_schedule.appointments VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


            PreparedStatement psai = JDBC.getConnection().prepareStatement(sqlai);

            psai.setString(1, Title);
            psai.setString(2, Description);
            psai.setString(3, Location);
            psai.setString(4, Type);
            psai.setTimestamp(5, Timestamp.from(Instant.from(Start.withZoneSameInstant(ZoneOffset.UTC))));
            psai.setTimestamp(6, Timestamp.from(Instant.from(End.withZoneSameInstant(ZoneOffset.UTC))));
            psai.setTimestamp(7, Timestamp.valueOf(Create_Date));
            psai.setString(8, Created_By);
            psai.setTimestamp(9, Last_Update);
            psai.setString(10, Last_Updated_By);
            psai.setInt(11, Customer_ID);
            psai.setInt(12, User_ID);
            psai.setInt(13, Contact_ID);

            psai.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }


    /**
     * Update an appointment (at its given appointmentID) based off user's input
     * @param Appointment_ID
     * @param Title
     * @param Description
     * @param Location
     * @param Type
     * @param Start
     * @param End
     * @param Create_Date
     * @param Created_By
     * @param Last_Update
     * @param Last_Updated_By
     * @param Customer_ID
     * @param User_ID
     * @param Contact_ID
     */
    public static void updateAppointment (int Appointment_ID, String Title, String Description, String Location, String Type, ZonedDateTime Start, ZonedDateTime End, LocalDateTime Create_Date, String Created_By, Timestamp Last_Update, String Last_Updated_By, int Customer_ID, int User_ID, int Contact_ID) {

        try {
            String sqlai = "UPDATE client_schedule.appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";

            PreparedStatement psai = JDBC.getConnection().prepareStatement(sqlai);

            psai.setString(1, Title);
            psai.setString(2, Description);
            psai.setString(3, Location);
            psai.setString(4, Type);
            psai.setTimestamp(5, Timestamp.from(Instant.from(Start.withZoneSameInstant(ZoneOffset.UTC))));
            psai.setTimestamp(6, Timestamp.from(Instant.from(End.withZoneSameInstant(ZoneOffset.UTC))));
            psai.setTimestamp(7, Timestamp.valueOf(Create_Date));
            psai.setString(8, Created_By);
            psai.setTimestamp(9, Last_Update);
            psai.setString(10, Last_Updated_By);
            psai.setInt(11, Customer_ID);
            psai.setInt(12, User_ID);
            psai.setInt(13, Contact_ID);
            psai.setInt(14, Appointment_ID);

            psai.execute();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


    /**
     * Delete appointment based off its Appointment ID
     * @param Appointment_ID
     */
    public static void deleteAppointment(int Appointment_ID){

        try {
            String sqlDA = "DELETE from client_schedule.appointments WHERE Appointment_ID = ?";

            PreparedStatement psDA = JDBC.getConnection().prepareStatement(sqlDA);

            psDA.setInt(1, Appointment_ID);

            psDA.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }


}
