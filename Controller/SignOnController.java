package Controller;

import Model.AppointmentModel;
import Model.UsersModel;
import helper.appointmentDbAccess;
import helper.userDbAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * the sign on controller - enables users to log in to the program
 */
public class SignOnController implements Initializable {
    public Button logInButton;
    public TextField userNameInput;
    public TextField passwordInput;
    public Label userNameLabel;
    public Label passwordLabel;
    public Label userLocation;

    ObservableList <AppointmentModel> allAppointments = appointmentDbAccess.getAllAppointments();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userLocation.setText(String.valueOf(ZoneId.systemDefault()));

        ResourceBundle bundle = ResourceBundle.getBundle("rb", Locale.getDefault());

        if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")){
            userNameLabel.setText(bundle.getString("User Name"));
            passwordLabel.setText(bundle.getString("Password"));
            logInButton.setText(bundle.getString("Log In"));
        }
    }


    public void logInAttempted(ActionEvent actionEvent) throws IOException {
        String uNInput = userNameInput.getText();
        String pwInput = passwordInput.getText();

        ObservableList<UsersModel> userList = userDbAccess.attemptedLogIn(uNInput);

        ResourceBundle bundle = ResourceBundle.getBundle("rb", Locale.getDefault());

        if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {

            if (userList.isEmpty()) {
                writeToFile(false);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Georgia';");
                alert.setHeaderText(bundle.getString("ERROR"));
                alert.setContentText(bundle.getString("Incorrect User Name or Password"));
                alert.showAndWait();
                System.out.println("Wrong Password");
            }
            else if (userList.get(0).getPassword().equals(pwInput)) {
                int userID = userList.get(0).getUserID();
                String userName1 = userList.get(0).getUserName();
                String password1 = userList.get(0).getPassword();
                LocalDateTime createDate = userList.get(0).getCreateDate();
                String createdBy = userList.get(0).getCreatedBy();
                Timestamp lastUpdate = userList.get(0).getLastUpdate();
                String lastUpdatedBy = userList.get(0).getLastUpdatedBy();

                UsersModel.currentUser = new UsersModel(userID, userName1, password1, createDate, createdBy, lastUpdate, lastUpdatedBy);
                System.out.println("Correct Password");
                writeToFile(true);

                upcomingAppointment();


                Parent root = FXMLLoader.load(getClass().getResource("/View/allAppointmentScreen.fxml"));
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                root.setStyle("-fx-font-family: 'Georgia';");
                stage.setTitle("All Appointments");
                stage.setScene(new Scene(root, 800, 800));
                stage.show();
            } else {
                writeToFile(false);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Georgia';");
                alert.setHeaderText(bundle.getString("ERROR"));
                alert.setContentText(bundle.getString("Incorrect User Name or Password"));
                alert.showAndWait();
                System.out.println("Wrong Password");
            }
        }
    }



    /**
     * Writes to login_activity.txt with the attempted username, the date and time of the attempted login, and
     * whether or not the attempt was successful
     * @param successful
     */
    public void writeToFile(Boolean successful){
        try {
            String userName = userNameInput.getText();
            Timestamp timestamp = Timestamp.from(Instant.now());
            String time = timestamp.toString();
            String success = null;



            if (successful){
                success = "Attempt successful";
            }
            else {
                success = "Attempt failed";
            }
            String entry = userName  + " " + time + " " + success + "\n";

            FileWriter myWriter = new FileWriter("login_activity.txt", true);
            myWriter.write(entry);
            myWriter.close();
            System.out.println("Successfully Written");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    /**
     *Checks through list of appointments to see if there is an appointment within the next 15 minutes from user's
     * sign on
     */

    public void upcomingAppointment(){
        ObservableList <AppointmentModel> upcomingAppointment = FXCollections.observableArrayList();
        upcomingAppointment.clear();


        LocalDateTime signOn = LocalDateTime.now();
        LocalDateTime upcomingCheck = signOn.plusMinutes(15);

        for (int i = 0; i < allAppointments.size(); i++) {
            if (allAppointments.get(i).getStart().toLocalDateTime().isAfter(signOn) && allAppointments.get(i).getStart().toLocalDateTime().isBefore(upcomingCheck)) {
                upcomingAppointment.add(allAppointments.get(i));
            }
        }
   if (upcomingAppointment.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Georgia';");
                alert.setHeaderText("No Upcoming Appointments!");
                alert.setContentText("There are no appointments upcoming within the next 15 minutes.");
                alert.showAndWait();
            }
            else {
       for (int i = 0; i < upcomingAppointment.size(); i++) {
           int appointmentID = upcomingAppointment.get(i).getAppointmentID();
           LocalDate appointmentDate = upcomingAppointment.get(i).getStart().toLocalDate();
           LocalTime appointmentStartTime = upcomingAppointment.get(i).getStart().toLocalTime();

           Alert alert = new Alert(Alert.AlertType.INFORMATION);
           alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Georgia';");
           alert.setHeaderText("Upcoming Appointment!");
           alert.setContentText("Appointment ID " + appointmentID + "begins within the next 15 minutes. Appointment start date and time " + appointmentDate + " " + appointmentStartTime + ".");
           alert.showAndWait();
       }
   }
    }







}
