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
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * the add appointment controller - enables users to add appointments
 */
public class addAppointmentController implements Initializable {
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
    public ComboBox endTime;
    public ComboBox startYear;
    public ComboBox startMonth;
    public ComboBox startDay;
    public ComboBox endYear;
    public ComboBox endMonth;
    public ComboBox endDay;
    public Button clearButton;
    public TextField customerID;
    public TextField contactID;


    ObservableList<AppointmentModel> allAppointments = appointmentDbAccess.getAllAppointments();
    ObservableList <ZonedDateTime> allPossibleStartTimes = FXCollections.observableArrayList();
    ObservableList <ZonedDateTime> allPossibleEndTimes = FXCollections.observableArrayList();
    ObservableList <LocalTime> displayStartTimes = FXCollections.observableArrayList();
    ObservableList <LocalTime> displayEndTimes = FXCollections.observableArrayList();


    ObservableList<String> customers = FXCollections.observableArrayList();
    ObservableList<String> contacts = FXCollections.observableArrayList();



    /**
     * program users cannot be expected to know ID #s for all applicable customers, have set combo boxes to reflect customer and contact names
     * system will correct to ID #s for each applicable item for database usage
     * @param url
     * @param resourceBundle
     */
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
        ObservableList <String> yearsList = FXCollections.observableArrayList();
        for (int j =0; j < 5; j++){
            yearsList.add(String.valueOf(LocalDate.now().plusYears(j).format(DateTimeFormatter.ofPattern("YYYY"))));
        }
        startYear.setItems(yearsList);


        /**
         * Set combo boxes for start & end month
         */
        ObservableList <String> monthsList = FXCollections.observableArrayList();
        for (int k = 0; k < 12; k++){
            monthsList.add(String.valueOf(LocalDate.now().plusMonths(k).format(DateTimeFormatter.ofPattern("MM"))));
        }
        startMonth.setItems(monthsList);
        endMonth.setItems(monthsList);



    }


    private LocalDate selectedDate(){
        int selectedYear = Integer.parseInt((String) startYear.getSelectionModel().getSelectedItem());
        int selectedMonth = Integer.parseInt((String) startMonth.getSelectionModel().getSelectedItem());
        int selectedDay = Integer.parseInt((String) startDay.getSelectionModel().getSelectedItem());

        LocalDate selectDate = LocalDate.of(selectedYear, selectedMonth, selectedDay);

        return selectDate;
    }



    /**
     * obtain and set contact ID based off contact name selection
     * @param actionEvent
     */
    public void contactComboClick(ActionEvent actionEvent) {
        String selectedContact = contactCombo.getSelectionModel().getSelectedItem().toString();
        int cID = 0;
        for (int i = 0; i < contactDbAccess.getAllContacts().size(); i++){
            if (selectedContact.equals(contactDbAccess.getAllContacts().get(i).getContactName())){
                cID = contactDbAccess.getAllContacts().get(i).getContactID();
                contactID.setText(String.valueOf(cID));
            }
        }
    }


    /**
     * obtain and set customer ID based off customer name selection
     * @param actionEvent
     */
    public void customerIDComboClick(ActionEvent actionEvent) {
        String selectedCustomer = customerIDCombo.getSelectionModel().getSelectedItem().toString();
        int cID = 0;
        for (int i = 0; i < customerDbAccess.getAllCustomers().size(); i++){
            if (selectedCustomer.equals(customerDbAccess.getAllCustomers().get(i).getCustomerName())){
                cID = customerDbAccess.getAllCustomers().get(i).getCustomerID();
                customerID.setText(String.valueOf(cID));
            }
        }
    }

    public void startYearClick(ActionEvent actionEvent) {
        endYear.setValue(startYear.getSelectionModel().getSelectedItem());
    }

    /**
     * pulls the correct number of days in the month and sets them for the next combo box
     * @param actionEvent
     */

    public void startMonthClick(ActionEvent actionEvent) {
        endMonth.setValue(startMonth.getSelectionModel().getSelectedItem());

        int selectedYear = Integer.parseInt((String) startYear.getSelectionModel().getSelectedItem());
        int selectedMonth = Integer.parseInt((String) startMonth.getSelectionModel().getSelectedItem());

        LocalDate dayInMonth = LocalDate.of(selectedYear, selectedMonth, 1);


        ObservableList<String> daysList = FXCollections.observableArrayList();
        for (int i =1; i <= dayInMonth.lengthOfMonth(); i++){
            daysList.add(String.valueOf(i));
        }
        startDay.setItems(daysList);
    }


    /**
     * Selection of start date automatically selects the "end date" as well, as no appointments can span outside office
     * hours
     * @param actionEvent
     */


    public void startDayClick(ActionEvent actionEvent) {
        endDay.setValue(startDay.getSelectionModel().getSelectedItem());


        LocalDate selectedDate = selectedDate();

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


    }



    public void startTimeClick(ActionEvent actionEvent) {
        startOccurrences();
    }


    /**
     * Pulls the selected start time (in local time based off timezone)
     * pulls all appointments to a new list "appointmentsOnSameDate"
     * then gets the duration of all appointments on the selected date (including start and end times)
     * "IF" statement checks if selected start time is in the durations list; if not - prints to system "no conflict"
     * if present - pushes error in user's OS default language advising that start time is taken
     */

    public void startOccurrences(){
        dateCheck();
        LocalDate selectedDate = selectedDate();

        String s = startTime.getSelectionModel().getSelectedItem().toString();
        LocalTime start = LocalTime.parse(s);



        ResourceBundle bundle = ResourceBundle.getBundle("rb", Locale.getDefault());

        ObservableList <AppointmentModel> appointmentsOnSameDate = FXCollections.observableArrayList();
        ObservableList <LocalTime> appointmentDurations = FXCollections.observableArrayList();
        appointmentsOnSameDate.clear();
        appointmentDurations.clear();

        for (int i = 0; i < allAppointments.size(); i++){
            if (selectedDate.equals(allAppointments.get(i).getStart().toLocalDate())){
                appointmentsOnSameDate.add(allAppointments.get(i));
            }
        }

        for (int j = 0; j < allPossibleStartTimes.size(); j++) {
            for (int i = 0; i < appointmentsOnSameDate.size(); i++) {
                if (allPossibleStartTimes.get(j).toLocalTime().isAfter(appointmentsOnSameDate.get(i).getStart().toLocalTime()) && allPossibleStartTimes.get(j).toLocalTime().isAfter(appointmentsOnSameDate.get(i).getEnd().toLocalTime())){
                    appointmentDurations.add(allPossibleStartTimes.get(j).toLocalTime());
                }
            }
        }

        for (int i = 0; i < appointmentsOnSameDate.size(); i++){
            appointmentDurations.add(appointmentsOnSameDate.get(i).getStart().toLocalTime());
            appointmentDurations.add(appointmentsOnSameDate.get(i).getEnd().toLocalTime());
        }

        if (appointmentDurations.isEmpty()) {
            System.out.println("No conflicts");
        }
        else {
              if (appointmentDurations.contains(start)){
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
     * pulls all appointments to a new list "appointmentsOnSameDate"
     * Creates instance of LocalTime "nextAppointment" - this nextAppointment is a placeholder to help locate if there
     * is another appointment that starts after the selected start time.
     * If there is (i.e. nextAppointment != null) then confirms if the selected end time is before that next appointment (i.e. no overlapping appointments)
     * if nextAppointment != null, and the appointment end time is AFTER the next appointment's start time, error is displayed in GUI
     * advising that appointment end time needs to be before the next appointment's start time.
     */
    public void endOccurrences () {
        timeCheck();
        LocalDate selectedDate = selectedDate();

        String s = startTime.getSelectionModel().getSelectedItem().toString();
        LocalTime start = LocalTime.parse(s);

        String e = endTime.getSelectionModel().getSelectedItem().toString();
        LocalTime end = LocalTime.parse(e);

        ResourceBundle bundle = ResourceBundle.getBundle("rb", Locale.getDefault());

        ObservableList<AppointmentModel> appointmentsOnSameDate = FXCollections.observableArrayList();

        appointmentsOnSameDate.clear();


        for (int i = 0; i < allAppointments.size(); i++) {
            if (selectedDate.equals(allAppointments.get(i).getStart().toLocalDate())) {
                appointmentsOnSameDate.add(allAppointments.get(i));
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
                        nextAppointment = appointmentsOnSameDate.get(0).getStart().withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();
                        if (nextAppointment.isBefore(appointmentsOnSameDate.get(j).getStart().toLocalTime()) && start.isBefore(appointmentsOnSameDate.get(j).getStart().toLocalTime())) {
                            nextAppointment = appointmentsOnSameDate.get(j).getStart().withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();
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



    /**
     * date logic error: is date selected, appointment date cannot be set before users current date
     */

    public void dateCheck (){
        ResourceBundle bundle = ResourceBundle.getBundle("rb", Locale.getDefault());

        if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
            if (startYear.getSelectionModel().isEmpty() || startMonth.getSelectionModel().isEmpty() || startDay.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Georgia';");
                alert.setHeaderText(bundle.getString("ERROR"));
                alert.setContentText(bundle.getString("Must select appointment date."));
                alert.showAndWait();
                return;
            } else if (selectedDate().isBefore(LocalDate.now())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Georgia';");
                alert.setHeaderText(bundle.getString("ERROR"));
                alert.setContentText(bundle.getString("Appointment date cannot be before today's date."));
                alert.showAndWait();
                return;
            }
        }
    }

    /**
     * Logic errors for time combo box; start time cannot be empty, start time cannot be after end time, start time cannot be before users current time
     */

    public void timeCheck(){
        ResourceBundle bundle = ResourceBundle.getBundle("rb", Locale.getDefault());

        if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
            if (startTime.getSelectionModel().isEmpty() || endTime.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Georgia';");
                alert.setHeaderText(bundle.getString("ERROR"));
                alert.setContentText(bundle.getString("Must select start and end time for appointment."));
                alert.showAndWait();
                return;
            } else {
                String start = startTime.getSelectionModel().getSelectedItem().toString();
                LocalTime selectedStartTime = LocalTime.parse(start);
                if (selectedDate().isEqual(LocalDate.now())) {
                    if (selectedStartTime.isBefore(LocalTime.now())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Georgia';");
                        alert.setHeaderText(bundle.getString("ERROR"));
                        alert.setContentText(bundle.getString("Appointment time cannot be before current time."));
                        alert.showAndWait();
                        return;
                    }
                }
            }
        }
    }


    /**
     * are the text fields filled?
     */

    public void contentCheck(){
        ResourceBundle bundle = ResourceBundle.getBundle("rb", Locale.getDefault());

        if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
            if (titleInput.getText().isEmpty() || descriptionInput.getText().isEmpty() || locationInput.getText().isEmpty() || typeInput.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Georgia';");
                alert.setHeaderText(bundle.getString("ERROR"));
                alert.setContentText(bundle.getString("All fields must be filled out."));
                alert.showAndWait();
                return;
            }
        }
    }


    /**
     * are the contact and customer selection boxes filled out?
     */
    public void comboBoxCheck(){
        ResourceBundle bundle = ResourceBundle.getBundle("rb", Locale.getDefault());

        if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
            if (contactCombo.getSelectionModel().isEmpty() || customerIDCombo.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Georgia';");
                alert.setHeaderText(bundle.getString("ERROR"));
                alert.setContentText(bundle.getString("Must make Customer and Contact selection."));
                alert.showAndWait();
                return;
            }
        }
    }


    /**
     * runs through above content checks (are selections made in each combo box? are the text fields filled with text?)
     * grabs all selections and saves them to temporary variables
     * then inputs data into MYSQL Database
     * @param actionEvent
     */
    public void saveClick(ActionEvent actionEvent) throws IOException {
        contentCheck();
        comboBoxCheck();

        String Title = titleInput.getText();
        String Description = descriptionInput.getText();
        String Location = locationInput.getText();
        String Type = typeInput.getText();

        String st = startTime.getSelectionModel().getSelectedItem().toString();
        String en = endTime.getSelectionModel().getSelectedItem().toString();

        LocalTime endT = LocalTime.parse(en);
        LocalTime startT = LocalTime.parse(st);


        ZonedDateTime s = ZonedDateTime.of(selectedDate(), startT, ZoneId.systemDefault());
        ZonedDateTime e = ZonedDateTime.of(selectedDate(), endT, ZoneId.systemDefault());

        ZonedDateTime Start = s.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime End = e.withZoneSameInstant(ZoneId.of("UTC"));

        LocalDateTime Create_Date = LocalDateTime.now();
        String Created_By = UsersModel.currentUser.getUserName();
        Timestamp Last_Update = Timestamp.valueOf(LocalDateTime.now());
        String Last_Updated_By = UsersModel.currentUser.getUserName();

        int Customer_ID = Integer.parseInt(customerID.getText());
        int User_ID = UsersModel.currentUser.getUserID();
        int Contact_ID = Integer.parseInt(contactID.getText());


        appointmentDbAccess.addAppointment(Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID);


        Parent root = FXMLLoader.load(getClass().getResource("/View/allAppointmentScreen.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        root.setStyle("-fx-font-family: 'Georgia';");
        stage.setTitle("All Appointments");
        stage.setScene(new Scene(root, 800, 800));
        stage.show();
    }


    /**
     * clears field and goes back to appointment screen
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
     * goes back to appointments screen
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
     * Switches to the all customers screen
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
     * clears all input fields
     * @param actionEvent
     */
    public void clearClick(ActionEvent actionEvent) {
        titleInput.clear();
        descriptionInput.clear();
        locationInput.clear();
        typeInput.clear();
        startDay.getSelectionModel().clearSelection();
        startMonth.getSelectionModel().clearSelection();
        startYear.getSelectionModel().clearSelection();
        startTime.getSelectionModel().clearSelection();
        endTime.getSelectionModel().clearSelection();
        customerID.clear();
        customerIDCombo.getSelectionModel().clearSelection();
        contactID.clear();
        contactCombo.getSelectionModel().clearSelection();
    }
}
