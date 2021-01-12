package com.example.myapplication;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;


public class PeriodicSetWallpaper extends Worker {


    //public int[] images = new int[] {R.drawable.img_pure, R.drawable.kitty, R.drawable.cute, R.drawable.mops};

    public PeriodicSetWallpaper(
            @NonNull Context appContext,
            @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    private static final String TAG = PeriodicSetWallpaper.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Result doWork() {

        try {
            int flag = WallpaperChangerConstants.wallpaperRegime;
            String valueA = getInputData().getString("keyA"); // путь к картинке
            Context applicationContext = getApplicationContext();
            List<String> files = ThemederApp.getInstance().getRepo().getNamesImages();
            int a = (int) (Math.random() * files.size());
            if (files.size() != 0) {
                String picName = files.get(a);
                if (WallpaperChangerConstants.random == 0) {
                    Uri imageUri = Uri.parse(valueA);
                    Bitmap bitmap = ThemederApp.getInstance().getRepo().getImageFromName(imageUri);

                    WallpaperManager manager = WallpaperManager.getInstance(applicationContext);
                    if (WallpaperChangerConstants.wallpaperRegime == 0) {
                        manager.setBitmap(bitmap);
                    } else if (WallpaperChangerConstants.wallpaperRegime == 1) {
                        manager.setBitmap(bitmap, null, false, WallpaperManager.FLAG_SYSTEM);
                    } else if (WallpaperChangerConstants.wallpaperRegime == 2) {
                        manager.setBitmap(bitmap, null, false, WallpaperManager.FLAG_LOCK);
                    }
                    return Result.success();
                } else if (WallpaperChangerConstants.random == 1) {
                    Uri imageUri = Uri.parse(picName);
                    Bitmap bitmap = ThemederApp.getInstance().getRepo().getImageFromName(imageUri);

                    WallpaperManager manager = WallpaperManager.getInstance(applicationContext);
                    if (WallpaperChangerConstants.wallpaperRegime == 0) {
                        manager.setBitmap(bitmap);
                    } else if (WallpaperChangerConstants.wallpaperRegime == 1) {
                        manager.setBitmap(bitmap, null, false, WallpaperManager.FLAG_SYSTEM);
                    } else if (WallpaperChangerConstants.wallpaperRegime == 2) {
                        manager.setBitmap(bitmap, null, false, WallpaperManager.FLAG_LOCK);
                    }
                    return Result.success();
                }
                else {
                    return Result.failure();
                }

            }
            else {
                return Result.failure();
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "Error set periodic wallpaper", throwable);
            return Result.failure();
        }
    }
}