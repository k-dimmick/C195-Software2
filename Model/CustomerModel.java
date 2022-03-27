package Model;


import java.sql.*;
import java.time.LocalDateTime;

public class CustomerModel {
    private int customerID;
    private String customerName;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    private int divisionID;


    /**
     * Customer Model based off customer table in SQL database
     * @param customerID
     * @param customerName
     * @param address
     * @param postalCode
     * @param phoneNumber
     * @param createDate
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdatedBy
     * @param divisionID
     */
    public CustomerModel(int customerID, String customerName, String address, String postalCode, String phoneNumber, LocalDateTime createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy, int divisionID ){
        this.customerID=customerID;
        this.customerName=customerName;
        this.address=address;
        this.postalCode=postalCode;
        this.phoneNumber=phoneNumber;
        this.createDate=createDate;
        this.createdBy=createdBy;
        this.lastUpdate=lastUpdate;
        this.lastUpdatedBy=lastUpdatedBy;
        this.divisionID=divisionID;
    }

    /**
     * all getters and setters
     * @return
     */
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

     public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public int getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }
}
