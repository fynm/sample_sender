package sampleSender.complexTypes;

/*CT for Orgs */
public class OrganizationData {
    private int itemNbr = 0;
    private String primeIdNumber = "";
    private String employerId = "";
    private String stgLevel = "";
    private String streetAddress = "";
    private String city = "";
    private String state = "";
    private String zipCode = "";
    private String country = "";
    private String teleNumber = "";

    public int getItemNbr() {
        return itemNbr;
    }

    public void setItemNbr(int itemNbr) {
        this.itemNbr = itemNbr;
    }

    public String getPrimeIdNumber() {
        return primeIdNumber;
    }

    public void setPrimeIdNumber(String primeIdNumber) {
        this.primeIdNumber = primeIdNumber;
    }

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public String getStgLevel() {
        return stgLevel;
    }

    public void setStgLevel(String stgLevel) {
        this.stgLevel = stgLevel;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTeleNumber() {
        return teleNumber;
    }

    public void setTeleNumber(String teleNumber) {
        this.teleNumber = teleNumber;
    }

}