package helper;

import Model.ContactModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class contactDbAccess {

    /**
     * Get all contacts from SQL Database
     * Program is not intended to update or add contacts, so only added this functionality
     * @return
     */
    public static ObservableList<ContactModel> getAllContacts() {
        ObservableList<ContactModel> ContactList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM client_schedule.contacts";
        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                ContactModel C = new ContactModel(contactID, contactName, email);
                ContactList.add(C);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } return ContactList;
    }
}
