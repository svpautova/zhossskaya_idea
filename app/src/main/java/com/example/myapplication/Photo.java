package com.example.myapplication;

public class Photo {
    private int id;
    private String url;
    private String large;
    private String large2x;
    private String medium;

    public Photo(int id, String url, String large, String large2x, String medium) {
        this.id = id;
        this.url = url;
        this.large = large;
        this.large2x = large2x;
        this.medium = medium;
    }

    public int getId() {
        return id;
    }

    public void setName(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public String getLarge() {
        return large;
    }

    public String getLarge2x() {
        return large2x;
    }

    public String getMedium() {
        return medium;
    }

}
