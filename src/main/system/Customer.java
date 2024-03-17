package main.system;

public class Customer extends BaseItem {
    private String name;
    private String contactNumber;
    private String contactEmail;

    public Customer(String name, String contactNumber, String contactEmail) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.contactEmail = contactEmail;
    }

    public Customer(String Id, String name, String contactNumber, String contactEmail) {
        this.setId(Id);
        this.name = name;
        this.contactNumber = contactNumber;
        this.contactEmail = contactEmail;
    }

    public String getName() {
        return name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String toString() {
        return getId() + "," + name + "," + contactNumber + "," + contactEmail;
    }
}
