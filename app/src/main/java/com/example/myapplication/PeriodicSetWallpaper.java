package com.example.myapplication;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.util.List;
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
        //int a = (int) ( Math.random() * images.length );

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    LoadSavePhoto ls = new LoadSavePhoto(applicationContext);
                    Bitmap bitmap =ls.getImageFromName(ls.getNamesImages().get(1));
System.out.println(ls.getNamesImages().get(1));
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            WallpaperManager manager = WallpaperManager.getInstance(applicationContext);

                            try {
                                manager.setBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                    });
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        };
thread.start();
                    try {

                        final Result success = Result.success();
                        return success;
                    } catch (Throwable throwable) {
                        Log.e(TAG, "Error set periodic wallpaper", throwable);
                        return Result.failure();
                    }


    }

}