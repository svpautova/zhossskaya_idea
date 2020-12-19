package com.example.myapplication;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


public class PeriodicSetWallpaper extends Worker {

    public PeriodicSetWallpaper(
            @NonNull Context appContext,
            @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    private static final String TAG = PeriodicSetWallpaper.class.getSimpleName();

    @NonNull
    @Override
    public Result doWork() {
        try {
            String valueA = getInputData().getString("keyA"); // путь к картинке
            Context applicationContext = getApplicationContext();
            LoadSavePhoto ls = LoadSavePhoto.getInstance(getApplicationContext());
            //List<String> names = ls.getNamesImages();
            Uri imageUri = Uri.parse(valueA);
            Bitmap bitmap = ls.getImageFromName(imageUri);
            WallpaperManager manager = WallpaperManager.getInstance(applicationContext);
            manager.setBitmap(bitmap);
            return Result.success();
        } catch (Throwable throwable) {
            Log.e(TAG, "Error set periodic wallpaper", throwable);
            return Result.failure();
        }
    }
}