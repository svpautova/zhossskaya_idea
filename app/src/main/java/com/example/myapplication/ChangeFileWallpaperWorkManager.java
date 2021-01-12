package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChangeFileWallpaperWorkManager extends Worker {

    public ChangeFileWallpaperWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        WorkManager workManager = WorkManager.getInstance();
        List<String> files = ThemederApp.getInstance().getRepo().getNamesImages();
        int a = (int) (Math.random() * files.size());


        if (files.size() != 0) {
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
            return Result.success();
    }
}
