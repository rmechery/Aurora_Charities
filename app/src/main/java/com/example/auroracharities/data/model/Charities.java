package com.example.auroracharities.data.model;

public class Charities {
    private String addressGeopoint;
    private String addressString;
    private String email;
    private String hoursBegin;
    private String hoursEnd;
    private String motto;
    private String phone;
    private String title;
    private int image;

    public Charities(String addressGeopoint, String addressString, String email, String hoursBegin, String hoursEnd, String motto, String phone) {
        this.addressGeopoint = addressGeopoint;
        this.addressString = addressString;
        this.email = email;
        this.hoursBegin = hoursBegin;
        this.hoursEnd = hoursEnd;
        this.motto = motto;
        this.phone = phone;
        this.title = null;
        this.image = 0;
    }

    public Charities(String title, int image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getAddressGeopoint() {
        return addressGeopoint;
    }

    public void setAddressGeopoint(String AddressGeopoint) {
        this.addressGeopoint = addressGeopoint;
    }

    public String getAddressString() {
        return addressString;
    }

    public void setAddressString(String addressString) {
        this.addressString = addressString;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHoursBegin() {
        return hoursBegin;
    }

    public void setHoursBegin(String hoursBegin) {
        this.hoursBegin = hoursBegin;
    }

    public String getHoursEnd() {
        return hoursEnd;
    }

    public void setHoursEnd(String hoursEnd) {
        this.hoursEnd = hoursEnd;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }
}
