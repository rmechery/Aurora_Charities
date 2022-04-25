package com.example.auroracharities.data.model;

import com.example.auroracharities.R;
import com.google.firebase.firestore.GeoPoint;

public class Charities {
    private GeoPoint addressGeopoint;
    private String addressString;
    private String email;
    private String hoursBegin;
    private String hoursEnd;
    private String motto;
    private String phone;
    private String title;
    private String logo;
    private int image;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }


    public Charities(){
//        this.addressGeopoint = addressGeopoint;
//        this.addressString = "22 Bridle Path";
//        this.email = "ryanmechery@gmail.com";
//        this.hoursBegin = "9:30";
//        this.hoursEnd = "10:30";
//        this.motto = "beans are very nutritious";
//        this.phone = "(774) 633-1387";
//        this.title = "LKJSFLSKDJFLKSDJF";
          this.image = R.drawable.a4g_logo_background;
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

    public GeoPoint getAddressGeopoint() {
        return addressGeopoint;
    }

    public void setAddressGeopoint(GeoPoint AddressGeopoint) {
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

    @Override
    public String toString(){
        return title+" " +motto;
    }
}
