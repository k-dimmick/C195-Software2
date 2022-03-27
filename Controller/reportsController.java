package Controller;

import Model.*;
import helper.*;
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
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.time.*;

/**
 * the all reports controller - enables users to see all reports generated
 */

public class reportsController implements Initializable {


    public TableView customerAppointmentTable;
    public TableColumn appointmentMonth;
    public TableColumn numberOfAppointments;
    public TableView customerByLocation;
    public TableView logInTable;
    public TableColumn uNameColumn;
    public TableColumn dateAttempted;
    public TableColumn Timestamp;
    public TableColumn successOrNot;
    public TableView scheduleTable;
    public TableColumn appointmentID;
    public TableColumn title;
    public TableColumn type;
    public TableColumn description;
    public TableColumn startColumn;
    public TableColumn endCol;
    public TableColumn customerID;
    public RadioButton radio1;
    public RadioButton radio2;
    public RadioButton radio3;
    public Button appointmentsButton;
    public Button customersButton;
    public TableColumn division;
    public TableColumn divisionID;
    public TableColumn total;
    public TableColumn apptType;
    public RadioButton byType;
    public RadioButton byMonth;


    ObservableList <AppointmentModel> allAppointments = appointmentDbAccess.getAllAppointments();
    ObservableList <AppointmentModel> selectedAppointments = FXCollections.observableArrayList();
    ObservableList <ContactModel> allContacts = contactDbAccess.getAllContacts();
    ObservableList <CountriesModel> allCountries = countriesDbAccess.getAllCountries();
    ObservableList <CustomerModel> allCustomers = customerDbAccess.getAllCustomers();
    ObservableList <UsersModel> allUsers = userDbAccess.getAllUsers();
    ObservableList <DivisionModel> allDivisions = divisionDbAccess.getAllDivisions();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        locationTable();
        appointmentTime();
        scheduleByContact();


    }




    public void scheduleByContact(){
        radio1.setText(allContacts.get(0).getContactName());
        radio2.setText(allContacts.get(1).getContactName());
        radio3.setText(allContacts.get(2).getContactName());


        appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        type.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));

        startColumn.setCellFactory(tableColumn -> {
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




        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        scheduleTable.setItems(allAppointments);



    }



    /**
     * Due to how add/modify appointment start time selectors have been built, setting up for items to be
     * sorted/counted by year and month instead of just month. System has ability to set appointments for up to 5
     * years in the future.
     * Set up hashmap for month and year, incrementing per instance found while iterating through allAppointmentsList
     *
     * LAMBDA expression used to iterate through hashmap and get key/value pairs and add them to
     * reportAppointments list and applicable table.
     *
     */
    public void appointmentTime(){

        ObservableList<reportsAppointmentModel> reportAppointments = FXCollections.observableArrayList();
        reportAppointments.clear();


        appointmentMonth.setCellValueFactory(new PropertyValueFactory<>("monthAndYear"));
        numberOfAppointments.setCellValueFactory(new PropertyValueFactory<>("Total"));
        apptType.setCellValueFactory(new PropertyValueFactory<>("Type"));

        LocalDate s = null;

        HashMap<String, Integer> map = new HashMap<>();

        for (int i = 0; i < allAppointments.size(); i++) {
            s = allAppointments.get(i).getStart().toLocalDate();
            int y = s.getYear();
            int m = s.getMonthValue();
            String checkDates = String.valueOf(y) + "/" + String.valueOf(m);

            if (map.containsKey(checkDates)) {
                int c = map.get(checkDates);
                map.put(checkDates, c + 1);
            } else {
                map.put(checkDates, 1);
            }
        }


        map.forEach((key, value) -> {
            String date = key;
            int count = value;
            String type = "Multiple; types are freeform.";
                    reportsAppointmentModel a = new reportsAppointmentModel(date, count, type);
                    reportAppointments.add(a);
                });

                customerAppointmentTable.setItems(reportAppointments);

        }


    /**
     * generates table showing all applicable divisions (ID & Name) and the number of customers in each division
     * this allows business management to evaluate if there should be additional marketing to the under-represented
     * divisions the business is operating in; or if certain divisions should look at closing their offices
     */

    public void locationTable(){
        ObservableList<reportDivisionModel> reportDivision = FXCollections.observableArrayList();
        reportDivision.clear();

        divisionID.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
        division.setCellValueFactory(new PropertyValueFactory<>("division"));
        total.setCellValueFactory(new PropertyValueFactory<>("total"));





        for (int i = 0; i < allDivisions.size(); i++){
            int counter = 0;
            int dID = allDivisions.get(i).getDivisionID();
            String div = allDivisions.get(i).getDivision();



            for (int j = 0; j < allCustomers.size(); j++){
                if (allCustomers.get(j).getDivisionID() == allDivisions.get(i).getDivisionID()){
                    counter++;
                }
            }

            int c = counter;
            reportDivisionModel d = new reportDivisionModel(dID, div, c);

            reportDivision.add(d);

        }

        customerByLocation.setItems(reportDivision);

    }

    public void appointmentsClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/allAppointmentScreen.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        root.setStyle("-fx-font-family: 'Georgia';");
        stage.setTitle("Appointments");
        stage.setScene(new Scene(root, 800, 800));
        stage.show();
    }

    public void customersClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/allCustomerScreen.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        root.setStyle("-fx-font-family: 'Georgia';");
        stage.setTitle("Customers");
        stage.setScene(new Scene(root, 800, 800));
        stage.show();
    }

    public void radio1Selected(ActionEvent actionEvent) {
        selectedAppointments.clear();

        if (radio1.isSelected()){
            for (int i = 0; i < allAppointments.size(); i++){
                if (allAppointments.get(i).getContactID() == allContacts.get(0).getContactID()){
                    selectedAppointments.add(allAppointments.get(i));
                }
            }
            scheduleTable.setItems(selectedAppointments);
        }
    }

    public void radio2Selected(ActionEvent actionEvent) {
        selectedAppointments.clear();

        if (radio2.isSelected()){
            for (int i = 0; i < allAppointments.size(); i++){
                if (allAppointments.get(i).getContactID() == allContacts.get(1).getContactID()){
                    selectedAppointments.add(allAppointments.get(i));
                }
            }
           scheduleTable.setItems(selectedAppointments);
        }
    }

    public void radio3Selected(ActionEvent actionEvent) {
        selectedAppointments.clear();

        if (radio3.isSelected()){
            for (int i = 0; i < allAppointments.size(); i++){
                if (allAppointments.get(i).getContactID() == allContacts.get(2).getContactID()){
                    selectedAppointments.add(allAppointments.get(i));
                }
            }
            scheduleTable.setItems(selectedAppointments);
        }
    }


    /**
     * Set table to show the # of appointments by type
     * @param actionEvent
     */
    public void byTypeSelected(ActionEvent actionEvent) {
        ObservableList<reportsAppointmentModel> reportAppointments = FXCollections.observableArrayList();
        reportAppointments.clear();


        appointmentMonth.setCellValueFactory(new PropertyValueFactory<>("monthAndYear"));
        numberOfAppointments.setCellValueFactory(new PropertyValueFactory<>("Total"));
        apptType.setCellValueFactory(new PropertyValueFactory<>("Type"));




        String s = null;

        HashMap<String, Integer> map = new HashMap<>();

        for (int i = 0; i < allAppointments.size(); i++) {
            s = allAppointments.get(i).getAppointmentType();


            if (map.containsKey(s)) {
                int c = map.get(s);
                map.put(s, c + 1);
            } else {
                map.put(s, 1);
            }
        }


        map.forEach((key, value) -> {
            String type = key;
            int count = value;
            String date = "Multiple dates";

            reportsAppointmentModel a = new reportsAppointmentModel(date, count, type);
            reportAppointments.add(a);
        });

        customerAppointmentTable.setItems(reportAppointments);

    }




    public void byMonthSelected(ActionEvent actionEvent) {
        appointmentTime();

    }
}
