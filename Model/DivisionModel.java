package Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DivisionModel {
    private int divisionID;
    private String division;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    private int countryID;

    public static DivisionModel currentDivision;

    /**
     * Division Model based off firstLevelDivisions table in SQL
     * @param divisionID
     * @param division
     * @param createDate
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdatedBy
     * @param countryID
     */
    public DivisionModel (int divisionID, String division, LocalDateTime createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy, int countryID){
        this.divisionID=divisionID;
        this.division=division;
        this.createDate=createDate;
        this.createdBy=createdBy;
        this.lastUpdate=lastUpdate;
        this.lastUpdatedBy=lastUpdatedBy;
        this.countryID=countryID;
    }

    /**
     * All getters and setters
     * @return
     */
    public static DivisionModel getCurrentDivision() {
        return currentDivision;
    }

    public static void setCurrentDivision(DivisionModel currentDivision) {
        DivisionModel.currentDivision = currentDivision;
    }

    public int getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
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

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }
}
