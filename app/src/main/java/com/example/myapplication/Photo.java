package com.example.myapplication;

public class Photo {
    private int id;

    private final String photosrc;

    public Photo(int id, String photosrc) {
        this.id = id;
        this.photosrc = photosrc;
    }

    public int getId() {
        return id;
    }

    public void setName(int id) {
        this.id = id;
    }

    public String getPhotosrc() { return photosrc; }

}
