package Controller;

import Model.CountriesModel;
import Model.DivisionModel;
import Model.UsersModel;
import helper.countriesDbAccess;
import helper.divisionDbAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import helper.customerDbAccess;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * the add customer controller - enables users to add customers
 */
public class addCustomerController implements Initializable {
    public Button cancelButton;
    public Button saveButton;
    public Label customerName;
    public Label address;
    public Label phoneNumber;
    public Label postalCode;
    public TextField customerNameInput;
    public TextField addressInput;
    public TextField phoneNumberInput;
    public TextField postalCodeInput;
    public Button appointmentsButton;
    public TextField customerIDInput;
    public Label customerID;
    public ComboBox stateProvinceBox;
    public ComboBox countryComboBox;
    public Label stateProvinceLabel;
    public Label countryLabel;
    public Button customerButton;

    ObservableList<CountriesModel> countries = countriesDbAccess.getAllCountries();
    ObservableList<DivisionModel> divisions = divisionDbAccess.getAllDivisions();
    ObservableList<String> initialCountries = FXCollections.observableArrayList();
    ObservableList<String> initialDivisions = FXCollections.observableArrayList();


    /**
     * preloads comboboxes with country and states/provinces
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        for(int i = 0; i < countries.size(); i++){
            initialCountries.add(countries.get(i).getCountry());
            countryComboBox.setItems(initialCountries);
        }


        for (int i = 0; i < divisions.size(); i++) {
            initialDivisions.add(divisions.get(i).getDivision());
            stateProvinceBox.setItems(initialDivisions);
        }
    }

    public void cancelClick(ActionEvent actionEvent) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("rb", Locale.getDefault());

        if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Georgia';");
            alert.setHeaderText(bundle.getString("Are you sure?"));
            alert.setContentText(bundle.getString("Are you sure you wish to cancel? All data will be lost."));

            Optional<ButtonType> results = alert.showAndWait();
            if (results.isPresent() && results.get() == ButtonType.OK) {
                Parent root = FXMLLoader.load(getClass().getResource("/View/allCustomerScreen.fxml"));
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                root.setStyle("-fx-font-family: 'Georgia';");
                stage.setTitle("All Customers");
                stage.setScene(new Scene(root, 800, 800));
                stage.show();
            } else {
            }
        }
    }


    /**
     * confirms that all text fields have been filled out, if not, displays error to user
     */
    public void contentCheck() {

        ResourceBundle bundle = ResourceBundle.getBundle("rb", Locale.getDefault());

        if (customerNameInput.getText().isEmpty() || addressInput.getText().isEmpty() || postalCodeInput.getText().isEmpty() || phoneNumberInput.getText().isEmpty()) {
            if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
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
     * confirms that all combo boxes have been selected, if not, displays error to user
     */
    public void comboCheck (){
        ResourceBundle bundle = ResourceBundle.getBundle("rb", Locale.getDefault());
        if (stateProvinceBox.getSelectionModel().isEmpty() || countryComboBox.getSelectionModel().isEmpty()){
            if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Georgia';");
                alert.setHeaderText(bundle.getString("ERROR"));
                alert.setContentText(bundle.getString("Must make State and Country selection."));
                alert.showAndWait();
                return;
            }

        }
    }



    public void saveClick(ActionEvent actionEvent) throws IOException {
        contentCheck();
        comboCheck();


        String Customer_Name = customerNameInput.getText();
        String Address = addressInput.getText();
        String Postal_Code = postalCodeInput.getText();
        String Phone = phoneNumberInput.getText();
        LocalDateTime Create_Date = LocalDateTime.now();
        String Created_By = UsersModel.currentUser.getUserName();
        Timestamp Last_Update = Timestamp.valueOf(LocalDateTime.now());
        String Last_Updated_By = UsersModel.getCurrentUser().getUserName();

        String divName = stateProvinceBox.getSelectionModel().getSelectedItem().toString();
        ObservableList<DivisionModel> divisions = divisionDbAccess.getAllDivisions();


        for (int i = 0; i < divisions.size(); i++){
            if (divisions.get(i).getDivision().equals(divName)){
                int divisionID = divisions.get(i).getDivisionID();
                String division = divName;
                LocalDateTime divCreateDate = divisions.get(i).getCreateDate();
                String divCreatedBy = divisions.get(i).getCreatedBy();
                Timestamp divLastUpdate = divisions.get(i).getLastUpdate();
                String divLastUpdatedBy = divisions.get(i).getLastUpdatedBy();
                int countryID = divisions.get(i).getCountryID();

                DivisionModel.currentDivision = new DivisionModel(divisionID, division, divCreateDate, divCreatedBy, divLastUpdate, divLastUpdatedBy, countryID);
                break;
            }
        }

        int Division_ID = DivisionModel.getCurrentDivision().getDivisionID();

        customerDbAccess.addCustomer(Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID);

        customerDbAccess.logInfo(Customer_Name);

        customerNameInput.clear();
        phoneNumberInput.clear();
        postalCodeInput.clear();
        addressInput.clear();

    }




    public void appointmentsClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/allAppointmentScreen.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        root.setStyle("-fx-font-family: 'Georgia';");
        stage.setTitle("Appointments");
        stage.setScene(new Scene(root, 800, 800));
        stage.show();


    }


    /**
     * Attempted language to auto select country based off state/province selected
     * @param actionEvent
     */
    public void stateBoxClick(ActionEvent actionEvent) {
    }


    /**
     * limits applicable states and provinces based off country selected
     * @param actionEvent
     */
    public void countryBoxClick(ActionEvent actionEvent) {
        String country = countryComboBox.getSelectionModel().getSelectedItem().toString();


        ObservableList<String> selectedDivisions = FXCollections.observableArrayList();
        selectedDivisions.clear();
        initialDivisions.clear();

        int sc = 0;

        for (int i = 0; i < countries.size(); i++){
            if (countries.get(i).getCountry().equals(country)){
                sc = countries.get(i).getCountryID();
            }
        }

        for (int i = 0; i < divisions.size(); i++){
            if (divisions.get(i).getCountryID() == sc){
                selectedDivisions.add(divisions.get(i).getDivision());
            }
        }


        stateProvinceBox.setItems(selectedDivisions);

    }

    public void customerClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/allCustomerScreen.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        root.setStyle("-fx-font-family: 'Georgia';");
        stage.setTitle("Customers");
        stage.setScene(new Scene(root, 800, 800));
        stage.show();
    }
}
