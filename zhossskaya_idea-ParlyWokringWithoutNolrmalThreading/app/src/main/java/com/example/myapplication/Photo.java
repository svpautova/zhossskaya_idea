package com.example.myapplication;

public class Photo {
    private int id;
    private final String medium;

    public Photo(int id, String medium) {
        this.id = id;
        this.medium = medium;
    }

    public int getId() {
        return id;
    }

    public void setName(int id) {
        this.id = id;
    }

    public String getMedium() {
        return medium;
    }

}
