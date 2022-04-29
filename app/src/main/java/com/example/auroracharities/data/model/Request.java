package com.example.auroracharities.data.model;

import java.util.ArrayList;
import java.util.Map;

public class Request {
    public Request(){

    }

    public Request(String name, String description, ArrayList<String> ageTag, ArrayList<String> sizeTag) {
        this.name = name;
        this.description = description;
        this.ageTag = ageTag;
        this.sizeTag = sizeTag;
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

    private String name;
    private String description;
    private ArrayList<String> ageTag;
    private ArrayList<String> sizeTag;
}
