package Controller;

import Model.CountriesModel;
import Model.CustomerModel;
import Model.DivisionModel;
import Model.UsersModel;
import helper.countriesDbAccess;
import helper.customerDbAccess;
import helper.divisionDbAccess;
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
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * This is the modify customer controller; enabling this screen to function
 */
public class modifyCustomerController implements Initializable {


    public ComboBox stateProvinceBox;
    public ComboBox countryBox;
    public Button saveButton;
    public Button cancelButton;
    public Button appointments;
    public TextField customerID;
    public TextField customerName;
    public TextField address;
    public TextField postalCode;
    public TextField phoneNumber;


    ObservableList<CountriesModel> countries = countriesDbAccess.getAllCountries();
    ObservableList<DivisionModel> divisions = divisionDbAccess.getAllDivisions();
    ObservableList<String> initialCountries = FXCollections.observableArrayList();
    ObservableList<String> initialDivisions = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){


    }

    public void initData(CustomerModel selectedCustomer) {

        ObservableList <String> selectedStates = FXCollections.observableArrayList();
        selectedStates.clear();

        for (int i = 0; i < countries.size(); i++) {
            initialCountries.add(countries.get(i).getCountry());
            countryBox.setItems(initialCountries);
        }

        for (int i = 0; i < divisions.size(); i++){
            initialDivisions.add(divisions.get(i).getDivision());
            stateProvinceBox.setItems(initialDivisions);
        }



        customerID.setText(Integer.toString(selectedCustomer.getCustomerID()));
        customerName.setText(selectedCustomer.getCustomerName());
        address.setText(selectedCustomer.getAddress());
        postalCode.setText(selectedCustomer.getPostalCode());
        phoneNumber.setText(selectedCustomer.getPhoneNumber());
        int dID = selectedCustomer.getDivisionID();
        int cID = 0;

        String div = null;


        for (int i = 0; i < divisions.size(); i++) {
            if (dID == divisions.get(i).getDivisionID()) {
                cID = divisions.get(i).getCountryID();
                div = divisions.get(i).getDivision();
            }
        }

        for (int j = 0; j < countries.size(); j++) {
            if (cID == countries.get(j).getCountryID()) {
                countryBox.setValue(countries.get(j).getCountry());
            }
        }

        for (int i = 0; i < divisions.size(); i++){
            if (cID == divisions.get(i).getCountryID()){
                selectedStates.add(divisions.get(i).getDivision());
            }
        }

        stateProvinceBox.setItems(selectedStates);

        for (int i = 0; i < selectedStates.size(); i++){
            if (div.equals(selectedStates.get(i))){
                stateProvinceBox.setValue(selectedStates.get(i));
            }
        }

    }


    public void stateProvinceBoxClick(ActionEvent actionEvent) {
    }

    public void countryBoxClick(ActionEvent actionEvent) {
        String count = countryBox.getSelectionModel().getSelectedItem().toString();
        int sc = 0;
        ObservableList <String> selectedStates = FXCollections.observableArrayList();
        initialDivisions.clear();
        selectedStates.clear();

        for (int i = 0; i < countries.size(); i++){
            if (countries.get(i).getCountry().equals(count)){
                sc = countries.get(i).getCountryID();
            }
        }


        for (int i = 0; i < divisions.size(); i++){
            if (divisions.get(i).getCountryID() == sc){
                selectedStates.add(divisions.get(i).getDivision());
            }
        }

        stateProvinceBox.setItems(selectedStates);

    }

    public void saveButtonClick(ActionEvent actionEvent) throws IOException {
        int Customer_ID = Integer.parseInt(customerID.getText());
        String Customer_Name = customerName.getText();
        String Address = address.getText();
        String Postal_Code = postalCode.getText();
        String Phone = phoneNumber.getText();
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


        customerDbAccess.updateCustomer(Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID);


        Parent root = FXMLLoader.load(getClass().getResource("/View/allCustomerScreen.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        root.setStyle("-fx-font-family: 'Georgia';");
        stage.setTitle("Customers");
        stage.setScene(new Scene(root, 800, 800));
        stage.show();
    }

    public void cancelButtonClick(ActionEvent actionEvent) throws IOException {
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

    public void appointmentButtonClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/allAppointmentScreen.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        root.setStyle("-fx-font-family: 'Georgia';");
        stage.setTitle("Appointments");
        stage.setScene(new Scene(root, 800, 800));
        stage.show();
    }
}
