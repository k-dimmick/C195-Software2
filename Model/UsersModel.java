package Model;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Timestamp;
import java.time.LocalDateTime;


public class UsersModel {
    private int userID;
    private String userName;
    private String password;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;

    public static UsersModel currentUser = null;

    /**
     * Users Model based off users table in SQL Database
     * @param userID
     * @param userName
     * @param password
     * @param createDate
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdatedBy
     */
    public UsersModel(int userID, String userName, String password, LocalDateTime createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy){
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * All getters and setters
     * @return
     */
    public static UsersModel getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(UsersModel currentUser) {
        UsersModel.currentUser = currentUser;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userId) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
