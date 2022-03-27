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
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * the all customers controller - enables users to see all customers
 */
public class allCustomerController implements Initializable{
    public Button addCustomerButton;
    public Button modifyCustomerButton;
    public Button deleteCustomerButton;
    public TableColumn custDivisionIDCol;
    public TableColumn custLastUpdateByCol;
    public TableColumn custLastUpdateCol;
    public TableColumn custCreateByCol;
    public TableColumn custCreateDateCol;
    public TableColumn custPhoneCol;
    public TableColumn custPostCodeCol;
    public TableColumn custAddressCol;
    public TableColumn custNameCol;
    public TableColumn custIDCol;
    public TableView customerTable;
    public Button backToApptButton;
    public Button reportsButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /***
         * Set Table Data
         */

        customerTable.setItems(customerDbAccess.getAllCustomers());
        custIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        custAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        custPostCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        custPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        custCreateDateCol.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        custCreateByCol.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        custLastUpdateCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        custLastUpdateByCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        custDivisionIDCol.setCellValueFactory(new PropertyValueFactory<>("divisionID"));

    }


    /**
     * Redirects to a delete screen with selected customer's associated appointments
     * @param actionEvent
     * @throws IOException
     */
    public void deleteCustomerClick(ActionEvent actionEvent) {
        CustomerModel selectedCustomer = (CustomerModel) customerTable.getSelectionModel().getSelectedItem();
        String customerName = selectedCustomer.getCustomerName();
        int Customer_ID = selectedCustomer.getCustomerID();

        ObservableList<AppointmentModel> allAppointments = appointmentDbAccess.getAllAppointments();

        for (int i = 0; i < allAppointments.size(); i++) {
            if (Customer_ID == allAppointments.get(i).getCustomerID()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Georgia';");
                alert.setHeaderText("ERROR");
                alert.setContentText("Customer cannot be deleted until all appointments with Customer ID " + Customer_ID + " have been cancelled.");
                alert.showAndWait();
                return;
            }
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Georgia';");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Are you sure you want to delete customer " + customerName + " with ID " + Customer_ID + "?");
        Optional<ButtonType> results = alert.showAndWait();
        if (results.isPresent() && results.get() == ButtonType.OK) {
            customerDbAccess.deleteCustomer(Customer_ID);
            customerTable.setItems(customerDbAccess.getAllCustomers());
        }
    }



    /**
     * Sets scene for modify parts screen & allows controller to access selected data
     */
    public void modifyCustomerClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/modifyCustomer.fxml"));
        Parent modifyCustomer = loader.load();

        modifyCustomerController controller = loader.getController();
        controller.initData((CustomerModel) customerTable.getSelectionModel().getSelectedItem());

        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        modifyCustomer.setStyle("-fx-font-family: 'Georgia';");
        stage.setTitle("Modify Customer");
        stage.setScene(new Scene(modifyCustomer, 800, 800));
        stage.show();
    }


    public void addCustomerClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/addCustomer.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        root.setStyle("-fx-font-family: 'Georgia';");
        stage.setTitle("Add Customer");
        stage.setScene(new Scene(root, 800, 800));
        stage.show();
    }

    public void backToClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/allAppointmentScreen.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        root.setStyle("-fx-font-family: 'Georgia';");
        stage.setTitle("All Appointments");
        stage.setScene(new Scene(root, 800, 800));
        stage.show();
    }

    public void reportsClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/reports.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        root.setStyle("-fx-font-family: 'Georgia';");
        stage.setTitle("Reports");
        stage.setScene(new Scene(root, 800, 800));
        stage.show();
    }
}
