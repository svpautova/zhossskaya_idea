package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class Buttons extends MainScreenFragment {



    private static final int PERMISSION_REQUEST_CODE = 0;

    static void Like_button()  { // зеленая кнопка

        String name = "kkjj.jpg";
        Bitmap picture = ((BitmapDrawable) imageView.getDrawable()).getBitmap();


        ExecutorService executorservice = Executors.newSingleThreadExecutor();
        Runnable runnable =() -> {
            try {
                imageView.setImageBitmap(ThemederApp.getInstance().getRepo().getImageFromName(ThemederApp.getInstance().getRepo().saveBitmap(picture, Bitmap.CompressFormat.JPEG, "image/jpeg")));
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


        List<String> files = ThemederApp.getInstance().getRepo().getNamesImages();
        int a = (int) ( Math.random() * files.size());
        String picName = files.get(a);
        Data myData = new Data.Builder()
                .putString("keyA", picName)
                .build();
        OneTimeWorkRequest myWorkRequest = new OneTimeWorkRequest.Builder(PeriodicSetWallpaper.class)
                .setInputData(myData)
                .build();
        workManager.enqueue(myWorkRequest);

    }

    static void Switch_on() {
        WorkManager workManager = WorkManager.getInstance();
        List<String> files = ThemederApp.getInstance().getRepo().getNamesImages();
        int a = (int) ( Math.random() * files.size());
        String picName = files.get(a);
        Data myData = new Data.Builder()
                .putString("keyA", picName)
                .build();
        PeriodicWorkRequest myWorkRequest = new PeriodicWorkRequest.Builder(PeriodicSetWallpaper.class, 15, TimeUnit.MINUTES, 13, TimeUnit.MINUTES)
                .addTag("pwr")
                .setInputData(myData)
                .build();
        workManager.enqueue(myWorkRequest);
    }


}
