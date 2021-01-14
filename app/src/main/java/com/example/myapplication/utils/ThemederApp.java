package com.example.myapplication.utils;

import android.app.Application;

public class ThemederApp extends Application {
    public static ThemederApp instance;

    private LoadSavePhoto repo;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        repo = new LoadSavePhoto(this);
    }

    public static ThemederApp getInstance() {
        return instance;
    }

    public LoadSavePhoto getRepo() {
        return repo;
    }
}