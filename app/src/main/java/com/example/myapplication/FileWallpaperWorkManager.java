package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;

public class FileWallpaperWorkManager extends Worker {

    public FileWallpaperWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        WorkManager workManager = WorkManager.getInstance();
        List<String> files = ThemederApp.getInstance().getRepo().getNamesImages();
        if (files.size() != 0) {
            int a = (int) (Math.random() * files.size());
            String pictureName = files.get(a);
            Data myData = new Data.Builder()
                    .putString("keyA", pictureName)
                    .build();
            OneTimeWorkRequest myWorkRequest = new OneTimeWorkRequest.Builder(PeriodicSetWallpaper.class)
                    .setInputData(myData)
                    .build();
            workManager.enqueue(myWorkRequest);
            Log.d("!!!!!!", "click change");
        }
        return Result.success();
    }
}