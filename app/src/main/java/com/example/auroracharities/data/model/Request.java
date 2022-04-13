package com.example.auroracharities.data.model;

import java.util.Map;

public class Request {
    private String name;
    private String category;
    private Map<String, Object> tag;

    public Request() {

    }

    public Request(String name, String category, Map<String, Object> tag) {
        this.name = name;
        this.category = category;
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Map<String, Object> getTag() {
        return tag;
    }

    public void setTag(Map<String, Object> tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Request{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", tag=" + tag +
                '}';
    }
}
