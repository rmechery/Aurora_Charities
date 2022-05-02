package com.example.auroracharities.data.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.Map;

public class Request {
    public Request(){

    }

    public Request(String charity, String name, String description, ArrayList<String> ageTag, ArrayList<String> sizeTag) {
        this.charity = charity;
        this.name = name;
        this.description = description;
        this.ageTag = ageTag;
        this.sizeTag = sizeTag;
        this.dateCreated = FieldValue.serverTimestamp();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getAgeTag() {
        return ageTag;
    }

    public void setAgeTag(ArrayList<String> ageTag) {
        this.ageTag = ageTag;
    }

    public ArrayList<String> getSizeTag() {
        return sizeTag;
    }

    public void setSizeTag(ArrayList<String> sizeTag) {
        this.sizeTag = sizeTag;
    }

    public FieldValue getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(FieldValue dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCharity() {
        return charity;
    }

    public void setCharity(String charity) {
        this.charity = charity;
    }

    private String name;
    private String description;
    private ArrayList<String> ageTag;
    private ArrayList<String> sizeTag;
    private FieldValue dateCreated;
    private String charity;
}
