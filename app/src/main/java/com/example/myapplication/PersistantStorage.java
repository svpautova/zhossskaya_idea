package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PersistantStorage {
    public static final String STORAGE_NAME = "MyStorage";

    private PersistantStorage(){
        throw new AssertionError("Instantiating utility class.");
    }

    private static SharedPreferences settings = null;
    private static SharedPreferences.Editor editor = null;
    @SuppressLint("StaticFieldLeak")
    private static Context context = null;

    public static void init(Context cntxt){
        context = cntxt;
    }

    @SuppressLint("CommitPrefEdits")
    private static void init(){
        settings = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
    }

    public static void setPropertyBoolean(String name, Boolean value){
        if(settings == null){
            init();
        }
        editor.putBoolean(name, value);
        editor.apply();
    }

    public static Boolean getPropertyBoolean(String name){
        if(settings == null){
            init();
        }
        return settings.getBoolean(name, false);
    }
}