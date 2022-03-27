package Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class CountriesModel {
    private int countryID;
    private String country;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;

    /**
     * Countries model based off countries table in SQL Database
     * @param countryID
     * @param country
     * @param createDate
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdatedBy
     */
   public CountriesModel(int countryID, String country, LocalDateTime createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy){
        this.countryID=countryID;
        this.country=country;
        this.createDate=createDate;
        this.createdBy=createdBy;
        this.lastUpdate=lastUpdate;
        this.lastUpdatedBy=lastUpdatedBy;
    }

    /**
     * all getters and setters
     * @return
     */
    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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
