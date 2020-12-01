package com.example.myapplication;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.util.concurrent.TimeUnit;



public class PeriodicSetWallpaper extends Worker {

    //public int[] images = new int[] {R.drawable.img_pure, R.drawable.kitty, R.drawable.cute, R.drawable.mops};

    public PeriodicSetWallpaper(
            @NonNull Context appContext,
            @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    private static final String TAG = PeriodicSetWallpaper.class.getSimpleName();

    @NonNull
    @Override
    public Result doWork() {
        String valueA = getInputData().getString("keyA"); // путь к картинке
        Context applicationContext = getApplicationContext();
        ImageLoadSave ils = new ImageLoadSave();
        //int a = (int) ( Math.random() * images.length );
        Bitmap bitmap = null;
        try {
            bitmap = ils.getImageFromName(valueA);
        } catch (IOException e) {
            e.printStackTrace();
        }

        WallpaperManager manager = WallpaperManager.getInstance(applicationContext);

        try {
            manager.setBitmap(bitmap);
            final Result success = Result.success();
            return success;
        } catch (Throwable throwable) {
            Log.e(TAG, "Error set periodic wallpaper", throwable);
            return Result.failure();
        }
    }

}