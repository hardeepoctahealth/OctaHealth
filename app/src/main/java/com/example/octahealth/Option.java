package com.example.octahealth;

public class Option {

    String title,cost;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public Option() {
    }

    public Option(String title, String cost) {
        this.title = title;
        this.cost = cost;
    }
}
