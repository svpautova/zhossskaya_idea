package com.example.myapplication;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
        try {
            String valueA = getInputData().getString("keyA"); // путь к картинке
            Context applicationContext = getApplicationContext();
            Uri imageUri = Uri.parse(ThemederApp.getInstance().getRepo().getNamesImages().get(1));
            Bitmap bitmap = ThemederApp.getInstance().getRepo().getImageFromName(imageUri);
            System.out.println(ThemederApp.getInstance().getRepo().getNamesImages().get(1));
            WallpaperManager manager = WallpaperManager.getInstance(applicationContext);
            manager.setBitmap(bitmap);
            return Result.success();
        } catch (Throwable throwable) {
            Log.e(TAG, "Error set periodic wallpaper", throwable);
            return Result.failure();
        }
    }
}