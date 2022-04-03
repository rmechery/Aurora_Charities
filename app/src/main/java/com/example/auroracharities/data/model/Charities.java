package com.example.auroracharities.data.model;

public class Charities {

    private String title;
    private int image;
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

}
