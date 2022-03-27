package Controller;

import Model.AppointmentModel;
import Model.CustomerModel;
import helper.appointmentDbAccess;
import helper.customerDbAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * the all appointments controller - enables users to see all appointments
 */

public class allAppointmentsController implements Initializable {
    public RadioButton appointmentByWeek;
    public RadioButton appointmentByMonth;
    public Button modifyAppointment;
    public Button addAppointment;
    public Label dateTime;
    public TableView appointmentTable;
    public TableColumn apptIDCol;
    public TableColumn titleCol;
    public TableColumn descCol;
    public TableColumn locationCol;
    public TableColumn contactIDCol;
    public TableColumn typeCol;
    public TableColumn startCol;
    public TableColumn endCol;
    public TableColumn custIDCol;
    public TableColumn userIDCol;
    public Button viewCustomer;
    public Button deleteButton;
    public Button reportsButton;
    public ToggleGroup appointmentSort;
    public TextField searchByLocation;


    ObservableList <AppointmentModel> allAppointments = appointmentDbAccess.getAllAppointments();
    ObservableList <AppointmentModel> allAppointmentsZoned = FXCollections.observableArrayList();
    ObservableList <AppointmentModel> selectedAppointments = FXCollections.observableArrayList();


    /**
     * Runs timezone method - which updates appointment model with start and end time based off the user's zone ID
     *
     *
     * LAMBDA expressions used to make start and end date/time cells readable to the average user --
     * pulls ZonedDateTime and updates it to format outlined, then further down, appointment model information is passed
     * through to populate table
     *
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

      timeZones();
        apptIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));


        startCol.setCellFactory(tableColumn -> {
            TableCell<AppointmentModel, ZonedDateTime> cell = new TableCell<>() {
                private DateTimeFormatter format = DateTimeFormatter.ofPattern("MM.dd.yyyy HH:mm");

                @Override
                protected void updateItem(ZonedDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        this.setText(format.format(item));
                    }
                }
            };
            return cell;
        });


        endCol.setCellFactory(tableColumn -> {
            TableCell<AppointmentModel, ZonedDateTime> cell = new TableCell<>() {
                private DateTimeFormatter format = DateTimeFormatter.ofPattern("MM.dd.yyyy HH:mm");

                @Override
                protected void updateItem(ZonedDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        this.setText(format.format(item));
                    }
                }
            };
            return cell;
        });



        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        custIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        contactIDCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        appointmentTable.setItems(allAppointmentsZoned);

    }



    public void timeZones (){

        ZonedDateTime startTime = null;
        ZonedDateTime endTime = null;

        for (int i = 0; i < allAppointments.size(); i++){

          allAppointmentsZoned.add(allAppointments.get(i));
          startTime = allAppointmentsZoned.get(i).getStart().withZoneSameInstant(ZoneId.systemDefault());
          endTime = allAppointmentsZoned.get(i).getEnd().withZoneSameInstant(ZoneId.systemDefault());
          allAppointmentsZoned.get(i).setStart(startTime);
          allAppointmentsZoned.get(i).setEnd(endTime);
        }
    }




    public void addAppointmentClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/addAppointment.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        root.setStyle("-fx-font-family: 'Georgia';");
        stage.setTitle("Add Appointments");
        stage.setScene(new Scene(root, 800, 800));
        stage.show();
    }



    /**
     * If radio button is selected, table will reflect all appointments within the next 7 days
     * @param actionEvent
     */
    public void apptByWeekSelected(ActionEvent actionEvent) {
        selectedAppointments.clear();

        LocalDate signOnDate = LocalDate.now();

        if (appointmentByWeek.isSelected()){
            for (int i = 0; i < allAppointmentsZoned.size(); i++){
                if (allAppointmentsZoned.get(i).getStart().toLocalDate().isAfter(signOnDate) && allAppointmentsZoned.get(i).getStart().toLocalDate().isBefore(signOnDate.plusDays(7))){
                    selectedAppointments.add(allAppointmentsZoned.get(i));
                }
                if (allAppointmentsZoned.get(i).getStart().toLocalDate().equals(signOnDate)){
                    selectedAppointments.add(allAppointmentsZoned.get(i));
                }
            }
            appointmentTable.setItems(selectedAppointments);
        }
    }

    /**
     * If radio button is selected table view will reflect all appointments within the calendar month.
     * @param actionEvent
     */
    public void apptByMonthSelected(ActionEvent actionEvent) {

        selectedAppointments.clear();
        int signOnYear = LocalDate.now().getYear();
        int signOnMonth = LocalDate.now().getMonthValue();


        if (appointmentByMonth.isSelected()){
            for (int i = 0; i < allAppointmentsZoned.size(); i++){
                if (allAppointmentsZoned.get(i).getStart().toLocalDate().getMonthValue() == signOnMonth && allAppointmentsZoned.get(i).getStart().toLocalDate().getYear() == signOnYear){
                    selectedAppointments.add(allAppointmentsZoned.get(i));
                }
            }
            appointmentTable.setItems(selectedAppointments);
        }
    }

    public void modifyAppointmentClick(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/modifyAppointment.fxml"));
        Parent modifyAppointment = loader.load();

        modifyAppointmentController controller = loader.getController();
        controller.initData((AppointmentModel) appointmentTable.getSelectionModel().getSelectedItem());

        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        modifyAppointment.setStyle("-fx-font-family: 'Georgia';");
        stage.setTitle("Modify Appointment");
        stage.setScene(new Scene(modifyAppointment, 800, 800));
        stage.show();


    }

    public void viewCustomerClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/allCustomerScreen.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        root.setStyle("-fx-font-family: 'Georgia';");
        stage.setTitle("Customers");
        stage.setScene(new Scene(root, 800, 800));
        stage.show();

    }

    public void deleteClick(ActionEvent actionEvent) {
        AppointmentModel selectedAppointment = (AppointmentModel) appointmentTable.getSelectionModel().getSelectedItem();
        int Appointment_ID = selectedAppointment.getAppointmentID();
        String appointmentType = selectedAppointment.getAppointmentType();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Georgia';");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Are you sure you wish to cancel appointment ID " + Appointment_ID + "? With appointment type " + appointmentType + "?");
        Optional<ButtonType> results = alert.showAndWait();
        if (results.isPresent() && results.get() == ButtonType.OK) {
            appointmentDbAccess.deleteAppointment(Appointment_ID);
            appointmentTable.setItems(appointmentDbAccess.getAllAppointments());
        }
    }



    public void reportsClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/reports.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        root.setStyle("-fx-font-family: 'Georgia';");
        stage.setTitle("Reports");
        stage.setScene(new Scene(root, 800, 800));
        stage.show();
    }

    public void searchField(ActionEvent actionEvent) {
        ObservableList<AppointmentModel> searchedList = FXCollections.observableArrayList();
        String searchInput = searchByLocation.getText();
        for (int i = 0; i < allAppointmentsZoned.size(); i++){
            if (allAppointmentsZoned.get(i).getLocation().contains(searchInput)){
                searchedList.add(allAppointmentsZoned.get(i));
            }
        }
        if (!searchedList.isEmpty()){
            appointmentTable.setItems(searchedList);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Georgia';");
            alert.setHeaderText("Failed Search");
            alert.setContentText("The location you searched for cannot be found.");
            alert.showAndWait();
        }
    }
}
