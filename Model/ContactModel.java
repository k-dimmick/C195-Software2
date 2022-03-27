package Model;

public class ContactModel {
    private int contactID;
    private String contactName;
    private String email;

    /**
     * Contact model based off contact table in SQL
     * @param contactID
     * @param contactName
     * @param email
     */
    public ContactModel ( int contactID, String contactName, String email){
        this.contactID=contactID;
        this.contactName=contactName;
        this.email=email;
    }

    /**
     * all getters and setters
     * @return
     */
    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
