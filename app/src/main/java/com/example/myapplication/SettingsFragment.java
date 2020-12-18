package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SettingsFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    Button changeWallpaper;
    SwitchMaterial changeWallpaperSwitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        changeWallpaper = v.findViewById(R.id.change_wallpaper_button);
        changeWallpaper.setOnClickListener(this);
        changeWallpaperSwitch = v.findViewById(R.id.switch_periodic);
        if (changeWallpaperSwitch != null) {
            changeWallpaperSwitch.setOnCheckedChangeListener(this);
        }
        PersistantStorage.init(getContext());
        PersistantStorage.setPropertyBoolean(getString(R.string.switch_check), false);
        return v;
    }

    @Override
    public void onClick(View v) {
        //Buttons b = new Buttons(getActivity());
        //Buttons.Change_Wallpaper();
        WorkManager workManager = WorkManager.getInstance();
        new Thread(new Runnable() {
            @Override
            public void run() {
                LoadSavePhoto ls = LoadSavePhoto.getInstance(getContext().getApplicationContext());
                List<String> files = ls.getNamesImages();
                int a = (int) (Math.random() * files.size());
                String picture_name = files.get(a);
                Data myData = new Data.Builder()
                        .putString("keyA", picture_name)
                        .build();
                OneTimeWorkRequest myWorkRequest = new OneTimeWorkRequest.Builder(PeriodicSetWallpaper.class)
                        .setInputData(myData)
                        .build();
                workManager.enqueue(myWorkRequest);
                Log.d("!!!!!!", "click change");
            }
        }).start();


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            PersistantStorage.setPropertyBoolean(getString(R.string.switch_check), isChecked);
            Log.d("!!!!!!", "switch "+ isChecked);
        WorkManager workManager = WorkManager.getInstance();

        if (isChecked == true){

            ExecutorService executorservice = Executors.newSingleThreadExecutor();
            Runnable runnable =() -> {
                LoadSavePhoto ls = LoadSavePhoto.getInstance(getContext().getApplicationContext());
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
            };
            executorservice.submit(runnable);


            }
            else {
                WorkManager.getInstance().cancelAllWorkByTag("pwr");
            }
    }
}