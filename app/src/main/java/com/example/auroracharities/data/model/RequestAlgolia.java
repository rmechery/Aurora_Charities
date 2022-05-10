package com.example.auroracharities.data.model;

import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;

public class RequestAlgolia {
    public RequestAlgolia(){

    }

    public RequestAlgolia(String charity, String name, String description, ArrayList<String> ageTag, ArrayList<String> sizeTag, ArrayList<String> categoriesTag, ArrayList<String> conditionTag) {
        //"charity", "name", "description","ageTag", "sizeTag", "categoriesTag", "conditionTag"
        this.charity = charity;
        this.name = name;
        this.description = description;
        this.ageTag = ageTag;
        this.sizeTag = sizeTag;
        this.categoriesTag = categoriesTag;
        this.conditionTag = conditionTag;
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


    public String getCharity() {
        return charity;
    }

    public void setCharity(String charity) {
        this.charity = charity;
    }

    public ArrayList<String> getConditionTag() {
        return conditionTag;
    }

    public void setConditionTag(ArrayList<String> conditionTag) {
        this.conditionTag = conditionTag;
    }

    public ArrayList<String> getCategoriesTag() {
        return categoriesTag;
    }

    public void setCategoriesTag(ArrayList<String> categoriesTag) {
        this.categoriesTag = categoriesTag;
    }

    private String name;
    private String description;
    private ArrayList<String> ageTag;
    private ArrayList<String> sizeTag;
    private ArrayList<String> conditionTag;
    private ArrayList<String> categoriesTag;
    private FieldValue dateCreated;
    private String charity;

}
