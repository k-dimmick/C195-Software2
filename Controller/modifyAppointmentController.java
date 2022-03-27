package Controller;

import Model.AppointmentModel;
import Model.UsersModel;
import helper.appointmentDbAccess;
import helper.contactDbAccess;
import helper.customerDbAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * the modify appointments controller - enables users to modify an appointment
 */
public class modifyAppointmentController implements Initializable{
    public ComboBox contactCombo;
    public ComboBox customerIDCombo;

    public TextField appointmentIDInput;
    public TextField titleInput;
    public TextField descriptionInput;
    public TextField locationInput;
    public TextField typeInput;
    public Button saveButton;
    public Button cancelButton;
    public Button appointmentsButton;
    public Button customersButton;
    public ComboBox startTime;
    public Button clearButton;
    public ComboBox endDay;
    public ComboBox endMonth;
    public ComboBox endYear;
    public ComboBox startDay;
    public ComboBox startMonth;
    public ComboBox startYear;
    public ComboBox endTime;
    public TextField customerID;
    public TextField contactID;


    ObservableList <String> contacts = FXCollections.observableArrayList();
    ObservableList <String> customers = FXCollections.observableArrayList();
    ObservableList <String> yearsList = FXCollections.observableArrayList();
    ObservableList <String> monthsList = FXCollections.observableArrayList();
    ObservableList <String> daysList = FXCollections.observableArrayList();
    ObservableList <AppointmentModel> allExistingAppointments = appointmentDbAccess.getAllAppointments();
    ObservableList <ZonedDateTime> takenStartDates = FXCollections.observableArrayList();
    ObservableList <ZonedDateTime> takenEndDates = FXCollections.observableArrayList();
    ObservableList <ZonedDateTime> allPossibleStartTimes = FXCollections.observableArrayList();
    ObservableList <ZonedDateTime> allPossibleEndTimes = FXCollections.observableArrayList();
    ObservableList <ZonedDateTime> differentZoneStartTimes = FXCollections.observableArrayList();
    ObservableList <ZonedDateTime> differentZoneEndTimes = FXCollections.observableArrayList();
    ObservableList <LocalTime> displayStartTimes = FXCollections.observableArrayList();
    ObservableList <LocalTime> displayEndTimes = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        /**
     * Get all contacts for combo box
     */
        for (int i = 0; i < contactDbAccess.getAllContacts().size(); i++){
            contacts.add(contactDbAccess.getAllContacts().get(i).getContactName());
        }
        contactCombo.setItems(contacts);


        /**
         * Get all Customers for combo box
         */
        for (int i = 0; i < customerDbAccess.getAllCustomers().size(); i++){
            customers.add(customerDbAccess.getAllCustomers().get(i).getCustomerName());
        }
        customerIDCombo.setItems(customers);


        /**
         * Set combo boxes for start & end years
         */

        for (int j =0; j < 5; j++){
            yearsList.add(String.valueOf(LocalDate.now().plusYears(j).format(DateTimeFormatter.ofPattern("YYYY"))));
        }
        startYear.setItems(yearsList);
        endYear.setItems(yearsList);


        /**
         * Set combo boxes for start & end month
         */

        for (int k = 0; k < 12; k++){
            monthsList.add(String.valueOf(LocalDate.now().plusMonths(k).format(DateTimeFormatter.ofPattern("MM"))));
        }
        startMonth.setItems(monthsList);
        endMonth.setItems(monthsList);

    }

    /**
     * set fields with the selected appointment information
     * @param selectedAppointment
     */

    public void initData(AppointmentModel selectedAppointment) {
        // pre-set text data with selected items
        appointmentIDInput.setText(Integer.toString(selectedAppointment.getAppointmentID()));
        titleInput.setText(selectedAppointment.getTitle());
        descriptionInput.setText(selectedAppointment.getDescription());
        locationInput.setText(selectedAppointment.getLocation());
        typeInput.setText(selectedAppointment.getAppointmentType());


        // get the contact name from the ID number
        for (int i = 0; i < contactDbAccess.getAllContacts().size(); i++){
            int cID = selectedAppointment.getContactID();
            contactID.setText(String.valueOf(cID));
            if (cID == contactDbAccess.getAllContacts().get(i).getContactID()) {
                String contactName = contactDbAccess.getAllContacts().get(i).getContactName();
                for (int j = 0; j < contacts.size(); j++) {
                    if (contactName.equals(contacts.get(j))){
                        contactCombo.setValue(contacts.get(j));
                    }
                }
            }
        }


        // get the customer name from the ID number
        for (int j = 0; j < customerDbAccess.getAllCustomers().size(); j++){
            int cID = selectedAppointment.getCustomerID();
            customerID.setText(String.valueOf(cID));
            if (cID == customerDbAccess.getAllCustomers().get(j).getCustomerID()){
                String customerName = customerDbAccess.getAllCustomers().get(j).getCustomerName();
                for (int k = 0; k < customers.size(); k++){
                    if (customerName.equals(customers.get(k))){
                        customerIDCombo.setValue(customers.get(k));
                    }
                }
            }
        }

        // set combo boxes for date and time
        LocalDate selectedDate = selectedAppointment.getStart().toLocalDate();
        ZonedDateTime selectedStartTime = selectedAppointment.getStart().withZoneSameInstant(ZoneId.systemDefault());
        ZonedDateTime selectedEndTime = selectedAppointment.getEnd().withZoneSameInstant(ZoneId.systemDefault());

        int selectedYear = Integer.valueOf(selectedDate.getYear());
        int selectedMonth = Integer.valueOf(selectedDate.getMonthValue());
        int selectedDay = Integer.valueOf(selectedDate.getDayOfMonth());

        LocalDate dayInMonth = LocalDate.of(selectedYear, selectedMonth, 1);

        /**
         * creates list of all the possible start and end times within the office hours, sets both start and end boxes with same
         * List is limited based off EST office hours
         */

        LocalTime firstPossibleStartTime = LocalTime.of(8, 00);
        ZonedDateTime firstTime = ZonedDateTime.of(selectedDate, firstPossibleStartTime, ZoneId.of("EST5EDT"));
        allPossibleStartTimes.add(firstTime);
        for (int i = 1; i < 28; i++){
            allPossibleStartTimes.add(firstTime.plusMinutes(30 * i));
        }

        LocalTime firstPossibleEndTime = LocalTime.of(8, 15);
        ZonedDateTime firstEndTime = ZonedDateTime.of(selectedDate, firstPossibleEndTime, ZoneId.of("EST5EDT"));
        allPossibleEndTimes.add(firstEndTime);
        for (int i = 1; i < 56; i++){
            allPossibleEndTimes.add(firstEndTime.plusMinutes(15 * i));
        }

        for (int i = 0; i < allPossibleStartTimes.size(); i++){
            displayStartTimes.add(allPossibleStartTimes.get(i).withZoneSameInstant(ZoneId.systemDefault()).toLocalTime());
        }

        for (int i = 0; i < allPossibleEndTimes.size(); i++){
            displayEndTimes.add(allPossibleEndTimes.get(i).withZoneSameInstant(ZoneId.systemDefault()).toLocalTime());
        }

        startTime.setItems(displayStartTimes);
        endTime.setItems(displayEndTimes);


        for (int i =1; i <= dayInMonth.lengthOfMonth(); i++){
            daysList.add(String.valueOf(i));
        }
        startDay.setItems(daysList);
        endDay.setItems(daysList);

        for (int k = 0; k < yearsList.size(); k++){
            if (String.valueOf(selectedYear).equals(yearsList.get(k))){
                startYear.setValue(yearsList.get(k));
                endYear.setValue(yearsList.get(k));
            }
        }

        for (int l = 0; l < monthsList.size(); l++){
            if (String.valueOf(selectedMonth).equals(monthsList.get(l))){
                startMonth.setValue(monthsList.get(l));
                endMonth.setValue(monthsList.get(l));
            }
        }

        for (int m = 0; m < daysList.size(); m++){
            if (String.valueOf(selectedDay).equals(daysList.get(m))){
                startDay.setValue(daysList.get(m));
                endDay.setValue(daysList.get(m));
            }
        }


        //sets up selected combo box with the appropriate start and end time for the selected appointment.


        for (int n = 0; n < allPossibleStartTimes.size(); n++){
            if (selectedStartTime.toLocalTime().equals(allPossibleStartTimes.get(n).toLocalTime())){
                startTime.setValue(allPossibleStartTimes.get(n).toLocalTime());
            }
        }

        for (int o = 0; o < allPossibleEndTimes.size(); o++){
            if (selectedEndTime.toLocalTime().equals(allPossibleEndTimes.get(o).toLocalTime())){
                endTime.setValue(allPossibleEndTimes.get(o).toLocalTime());
            }
        }

    }


    /**
     * selected date is needed numerous times throughout the use of the page, created method to reduce bulk in code
     * @return
     */
    public LocalDate getSelectedDate() {
        int year = Integer.parseInt((String) startYear.getSelectionModel().getSelectedItem());
        int month = Integer.parseInt((String) startMonth.getSelectionModel().getSelectedItem());
        int day = Integer.parseInt((String) startDay.getSelectionModel().getSelectedItem());

        LocalDate selectedDate = LocalDate.of(year, month, day);
        return selectedDate;
    }



    public void contactComboClick(ActionEvent actionEvent) {
        String selectedContact = contactCombo.getSelectionModel().getSelectedItem().toString();
        contactID.clear();
        for (int i = 0; i < contactDbAccess.getAllContacts().size(); i++){
            if (selectedContact.equals(contactDbAccess.getAllContacts().get(i).getContactName())){
                contactID.setText(String.valueOf(contactDbAccess.getAllContacts().get(i).getContactID()));
            }
        }
    }

    public void customerIDComboClick(ActionEvent actionEvent) {
        String selectedCustomer = customerIDCombo.getSelectionModel().getSelectedItem().toString();
        customerID.clear();
        for (int i = 0; i < customerDbAccess.getAllCustomers().size(); i++){
            if (selectedCustomer.equals(customerDbAccess.getAllCustomers().get(i).getCustomerName())){
                customerID.setText(String.valueOf(customerDbAccess.getAllCustomers().get(i).getCustomerID()));
            }
        }
    }

    public void saveClick(ActionEvent actionEvent) throws IOException {

        int Appointment_ID = Integer.parseInt(appointmentIDInput.getText());
        String Title = titleInput.getText();
        String Description = descriptionInput.getText();
        String Location = locationInput.getText();
        String Type = typeInput.getText();


        String st = startTime.getSelectionModel().getSelectedItem().toString();
        LocalTime start = LocalTime.parse(st);

        String en = endTime.getSelectionModel().getSelectedItem().toString();
        LocalTime end = LocalTime.parse(en);

        ZonedDateTime s = ZonedDateTime.of(getSelectedDate(), start, ZoneId.systemDefault());
        ZonedDateTime e = ZonedDateTime.of(getSelectedDate(), end, ZoneId.systemDefault());

        ZonedDateTime Start = s.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime End = e.withZoneSameInstant(ZoneId.of("UTC"));

        LocalDateTime Create_Date = null;
        String Created_By = null;


        for (int i = 0; i < allExistingAppointments.size(); i++){
            if (Appointment_ID == allExistingAppointments.get(i).getAppointmentID()){
                Create_Date = allExistingAppointments.get(i).getCreateDate();
                Created_By = allExistingAppointments.get(i).getCreatedBy();
            }
        }

        Timestamp Last_Update = Timestamp.valueOf(LocalDateTime.now());
        String Last_Updated_By = UsersModel.currentUser.getUserName();
        int Customer_ID = Integer.parseInt(customerID.getText());
        int User_ID = UsersModel.getCurrentUser().getUserID();
        int Contact_ID = Integer.parseInt(contactID.getText());




       appointmentDbAccess.updateAppointment (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By,
        Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID);


        Parent root = FXMLLoader.load(getClass().getResource("/View/allAppointmentScreen.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        root.setStyle("-fx-font-family: 'Georgia';");
        stage.setTitle("All Appointments");
        stage.setScene(new Scene(root, 800, 800));
        stage.show();

    }


    /**
     * confirms if user intends to cancel and clear screen, then goes back to the appointments screen
     * @param actionEvent
     * @throws IOException
     */

    public void cancelClick(ActionEvent actionEvent) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("rb", Locale.getDefault());

        if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Georgia';");
            alert.setHeaderText(bundle.getString("Are you sure?"));
            alert.setContentText(bundle.getString("Are you sure you wish to cancel? All data will be lost."));

            Optional<ButtonType> results = alert.showAndWait();
            if (results.isPresent() && results.get() == ButtonType.OK) {
                Parent root = FXMLLoader.load(getClass().getResource("/View/allAppointmentScreen.fxml"));
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                root.setStyle("-fx-font-family: 'Georgia';");
                stage.setTitle("All Appointments");
                stage.setScene(new Scene(root, 800, 800));
                stage.show();
            } else {

            }
        }
    }


    /**
     * does not save changes, just changes scene to appointments scene
     * @param actionEvent
     * @throws IOException
     */

    public void appointmentsClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/allAppointmentScreen.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        root.setStyle("-fx-font-family: 'Georgia';");
        stage.setTitle("All Appointments");
        stage.setScene(new Scene(root, 800, 800));
        stage.show();
    }


    /**
     * does not save any changes, just changes scene to the customer's page
     * @param actionEvent
     * @throws IOException
     */
    public void customersClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/allCustomerScreen.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        root.setStyle("-fx-font-family: 'Georgia';");
        stage.setTitle("Customers");
        stage.setScene(new Scene(root, 800, 800));
        stage.show();
    }


    /**
     * set end time list based off the selected start time AND the next appointment's start time
     * this keeps overlapping appointments from being possible.
     * @param actionEvent
     */
    public void startTimeClick(ActionEvent actionEvent) {
        startOccurrences();
    }


    /**
     * Pulls the selected start time (in local time based off timezone)
     * pulls all appointments (except for the appointment being modified) to a new list "appointmentsOnSameDate"
     * then gets the duration of all appointments on the selected date (including start and end times)
     * "IF" statement checks if selected start time is in the durations list; if not - prints to system "no conflict"
     * if present - pushes error in user's OS default language advising that start time is taken
     */
    public void startOccurrences (){
        LocalDate startDate = getSelectedDate();

        String s = startTime.getSelectionModel().getSelectedItem().toString();
        LocalTime start = LocalTime.parse(s);

        ResourceBundle bundle = ResourceBundle.getBundle("rb", Locale.getDefault());

        ObservableList <AppointmentModel> appointmentsOnSameDate = FXCollections.observableArrayList();
        ObservableList <LocalTime> appointmentDurations = FXCollections.observableArrayList();

        appointmentsOnSameDate.clear();
        appointmentDurations.clear();

        int appointmentID = Integer.parseInt(appointmentIDInput.getText());

        for (int i = 0; i < allExistingAppointments.size(); i++){
            if (appointmentID != allExistingAppointments.get(i).getAppointmentID() && startDate.equals(allExistingAppointments.get(i).getStart().toLocalDate())){
                appointmentsOnSameDate.add(allExistingAppointments.get(i));
            }
        }

        for (int i = 0; i < allPossibleStartTimes.size(); i++){
            for (int j = 0; j < appointmentsOnSameDate.size(); j++){
                if (allPossibleStartTimes.get(i).toLocalTime().isAfter(appointmentsOnSameDate.get(j).getStart().toLocalTime()) && allPossibleStartTimes.get(i).toLocalTime().isBefore(appointmentsOnSameDate.get(j).getEnd().toLocalTime())) {
                    appointmentDurations.add(allPossibleStartTimes.get(i).withZoneSameInstant(ZoneId.systemDefault()).toLocalTime());
                }
            }
        }


        for (int i = 0; i < appointmentsOnSameDate.size(); i++){
            appointmentDurations.add(appointmentsOnSameDate.get(i).getStart().withZoneSameInstant(ZoneId.systemDefault()).toLocalTime());
            appointmentDurations.add(appointmentsOnSameDate.get(i).getEnd().withZoneSameInstant(ZoneId.systemDefault()).toLocalTime());
        }

        if (appointmentDurations.isEmpty()){
            System.out.println("No conflicts");
        } else {
            if (appointmentDurations.contains(start)) {
                if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Georgia';");
                    alert.setHeaderText(bundle.getString("ERROR"));
                    alert.setContentText(bundle.getString("Appointment time is already taken, please make new selection."));
                    alert.showAndWait();
                    return;
                }
            }
        }

    }


    public void endTimeSelect(ActionEvent actionEvent) {
        endOccurrences();
    }


    /**
     * Confirms that start time is before the selected end time; if it is - proceeds through the rest of the logic
     * Pulls the selected start time (in local time based off timezone)
     * pulls all appointments (except for the appointment being modified) to a new list "appointmentsOnSameDate"
     * Creates instance of LocalTime "nextAppointment" - this nextAppointment is a placeholder to help locate if there
     * is another appointment that starts after the selected start time.
     * If there is (i.e. nextAppointment != null) then confirms if the selected end time is before that next appointment (i.e. no overlapping appointments)
     * if nextAppointment != null, and the appointment end time is AFTER the next appointment's start time, error is displayed in GUI
     * advising that appointment end time needs to be before the next appointment's start time.
     */

    public void endOccurrences (){
        LocalDate startDate = getSelectedDate();

        String s = startTime.getSelectionModel().getSelectedItem().toString();
        LocalTime start = LocalTime.parse(s);

        String e = endTime.getSelectionModel().getSelectedItem().toString();
        LocalTime end = LocalTime.parse(e);

        ResourceBundle bundle = ResourceBundle.getBundle("rb", Locale.getDefault());




        ObservableList <AppointmentModel> appointmentsOnSameDate = FXCollections.observableArrayList();
        ObservableList <LocalTime> appointmentDurations = FXCollections.observableArrayList();

        appointmentsOnSameDate.clear();
        appointmentDurations.clear();




        int appointmentID = Integer.parseInt(appointmentIDInput.getText());

        for (int i = 0; i < allExistingAppointments.size(); i++){
            if (appointmentID != allExistingAppointments.get(i).getAppointmentID() && startDate.equals(allExistingAppointments.get(i).getStart().toLocalDate())){
                appointmentsOnSameDate.add(allExistingAppointments.get(i));
            }
        }


        if (end.isBefore(start) || end.equals(start)){
            if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Georgia';");
                alert.setHeaderText(bundle.getString("ERROR"));
                alert.setContentText(bundle.getString("Appointment end time must be after start time."));
                alert.showAndWait();
                return;
            }
        }
        else {
            LocalTime nextAppointment = null;
            if (!appointmentsOnSameDate.isEmpty()) {
                for (int j = 0; j < appointmentsOnSameDate.size(); j++) {
                    if (start.isBefore(appointmentsOnSameDate.get(j).getStart().toLocalTime())) {
                        nextAppointment = appointmentsOnSameDate.get(0).getStart().toLocalTime();
                        if (nextAppointment.isBefore(appointmentsOnSameDate.get(j).getStart().toLocalTime()) && start.isBefore(appointmentsOnSameDate.get(j).getStart().toLocalTime())) {
                            nextAppointment = appointmentsOnSameDate.get(j).getStart().toLocalTime();
                        }
                    }
                }
            }

            if (nextAppointment == null) {
                System.out.println("No conflicts");
            } else if (end.isAfter(nextAppointment)) {
                if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Georgia';");
                    alert.setHeaderText(bundle.getString("ERROR"));
                    alert.setContentText(bundle.getString("Appointment time is in conflict with another appointment. This appointments end time must be before next appointment start time."));
                    alert.showAndWait();
                    return;
                }
            }
        }
    }

    public void startYearClick(ActionEvent actionEvent) {
        endYear.setValue(startYear.getSelectionModel().getSelectedItem());
    }

    public void startMonthClick(ActionEvent actionEvent) {
        endMonth.setValue(startMonth.getSelectionModel().getSelectedItem());

        int selectedYear = Integer.parseInt((String) startYear.getSelectionModel().getSelectedItem());
        int selectedMonth = Integer.parseInt((String) startMonth.getSelectionModel().getSelectedItem());

        LocalDate dayInMonth = LocalDate.of(selectedYear, selectedMonth, 1);
        daysList.clear();


        ObservableList<String> daysList = FXCollections.observableArrayList();
        for (int i =1; i <= dayInMonth.lengthOfMonth(); i++){
            daysList.add(String.valueOf(i));
        }
        startDay.setItems(daysList);

    }

    public void startDayClick(ActionEvent actionEvent) {
        endDay.setValue(startDay.getSelectionModel().getSelectedItem());

    }

    public void clearClick(ActionEvent actionEvent) {
        titleInput.clear();
        descriptionInput.clear();
        locationInput.clear();
        typeInput.clear();
        customerIDCombo.getSelectionModel().clearSelection();
        contactCombo.getSelectionModel().clearSelection();
        startDay.getSelectionModel().clearSelection();
        startMonth.getSelectionModel().clearSelection();
        startYear.getSelectionModel().clearSelection();
        startTime.getSelectionModel().clearSelection();
        endTime.getSelectionModel().clearSelection();
        customerID.clear();
        contactID.clear();
    }
}
