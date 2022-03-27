package helper;

import Model.AppointmentModel;
import Model.CustomerModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

public class customerDbAccess extends customerLogger {

   public static void logInfo(String string){
        System.out.println("New Customer added: " + string);
    }




    /**
     * Get all customers and make them accessible throughout the program
     * @return
     */

    public static ObservableList<CustomerModel> getAllCustomers() {
        ObservableList<CustomerModel> customerList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM client_schedule.customers";
        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int customerID = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phoneNumber = rs.getString("Phone");
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int divisionID = rs.getInt("Division_ID");
                CustomerModel C = new CustomerModel(customerID, customerName, address, postalCode, phoneNumber, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionID);
                customerList.add(C);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customerList;
    }


    /**
     * add new customer to SQL Database
     * @param Customer_Name
     * @param Address
     * @param Postal_Code
     * @param Phone
     * @param Create_Date
     * @param Created_By
     * @param Last_Update
     * @param Last_Updated_By
     * @param Division_ID
     */

    public static void addCustomer(String Customer_Name, String Address, String Postal_Code, String Phone, LocalDateTime Create_Date, String Created_By, Timestamp Last_Update, String Last_Updated_By, int Division_ID) {

        try {
            String sqli = "INSERT INTO client_schedule.customers VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


            PreparedStatement psti = JDBC.getConnection().prepareStatement(sqli);

            psti.setString(1, Customer_Name);
            psti.setString(2, Address);
            psti.setString(3, Postal_Code);
            psti.setString(4, Phone);
            psti.setTimestamp(5, Timestamp.valueOf(Create_Date));
            psti.setString(6, Created_By);
            psti.setTimestamp(7, Last_Update);
            psti.setString(8, Last_Updated_By);
            psti.setInt(9, Division_ID);

            psti.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


    /**
     * update customer based off unique identifier "Customer ID"
     * @param Customer_ID
     * @param Customer_Name
     * @param Address
     * @param Postal_Code
     * @param Phone
     * @param Create_Date
     * @param Created_By
     * @param Last_Update
     * @param Last_Updated_By
     * @param Division_ID
     */

    public static void updateCustomer(int Customer_ID, String Customer_Name, String Address, String Postal_Code, String Phone, LocalDateTime Create_Date, String Created_By, Timestamp Last_Update, String Last_Updated_By, int Division_ID) {
        try {
            String sqli = "UPDATE client_schedule.customers SET Customer_Name = ?, Address =?, Postal_Code =?, Phone =?, Last_Update = ?, Last_Updated_By=?, Division_ID=? WHERE Customer_ID = ? ";


            PreparedStatement psti = JDBC.getConnection().prepareStatement(sqli);

            psti.setString(1, Customer_Name);
            psti.setString(2, Address);
            psti.setString(3, Postal_Code);
            psti.setString(4, Phone);
            psti.setTimestamp(5, Last_Update);
            psti.setString(6, Last_Updated_By);
            psti.setInt(7, Division_ID);
            psti.setInt(8, Customer_ID);

            psti.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    /**
     * delete customer based off unique identifier Customer ID
     * @param Customer_ID
     */
    public static void deleteCustomer (int Customer_ID){

        try {
            String sqlDC = "DELETE from client_schedule.customers WHERE Customer_ID = ?";

            PreparedStatement psDC = JDBC.getConnection().prepareStatement(sqlDC);

            psDC.setInt(1, Customer_ID);

            psDC.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}