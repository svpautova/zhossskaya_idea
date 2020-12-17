package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class Buttons extends MainScreenFragment{
    static Context context; // возможно это не нужно


    public Buttons(Context context){
        this.context = context;
        throw new AssertionError("Instantiating utility class.");

    }

    static void Like_button() throws IOException { // зеленая кнопка

        String name = "kkjj.jpg";
        Bitmap picture = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ExecutorService executorservice = Executors.newSingleThreadExecutor();
        Runnable runnable =() -> {
            try {
                LoadSavePhoto ls = new LoadSavePhoto(context);
            ls.saveBitmap(picture, Bitmap.CompressFormat.JPEG, "image/jpeg", name);
                imageView.setImageBitmap(ls.getImageFromName(name));
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        executorservice.submit(runnable);
        GetPhotos.ImageGlide(); // показать следующее фото
    }

    static void Dislike_button(){
        GetPhotos.ImageGlide();
    }

    static void Change_Wallpaper() {
        WorkManager workManager = WorkManager.getInstance();
        LoadSavePhoto ls = new LoadSavePhoto(context);

        List<String> files = ls.getNamesImages();
        int a = (int) ( Math.random() * files.size());
        String picture_name = files.get(a);
        Data myData = new Data.Builder()
                .putString("keyA", picture_name)
                .build();
        OneTimeWorkRequest myWorkRequest = new OneTimeWorkRequest.Builder(PeriodicSetWallpaper.class)
                .setInputData(myData)
                .build();
        workManager.enqueue(myWorkRequest);

    }

    static void Switch_on() {
        WorkManager workManager = WorkManager.getInstance();
LoadSavePhoto ls = new LoadSavePhoto(context);
        List<String> files = ls.getNamesImages();
        int a = (int) ( Math.random() * files.size());
        String picture_name = files.get(a);
        Data myData = new Data.Builder()
                .putString("keyA", picture_name)
                .build();
        PeriodicWorkRequest myWorkRequest = new PeriodicWorkRequest.Builder(PeriodicSetWallpaper.class, 15, TimeUnit.MINUTES, 13, TimeUnit.MINUTES)
                .addTag("pwr")
                .setInputData(myData)
                .build();
        workManager.enqueue(myWorkRequest);

    }
}
