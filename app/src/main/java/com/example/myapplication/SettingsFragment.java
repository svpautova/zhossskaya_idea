package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import java.util.concurrent.atomic.AtomicReference;

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
        ThemederApp.getInstance().getRepo().setPropertyBoolean(getString(R.string.switch_check), false);
        return v;
    }

    @Override
    public void onClick(View v) {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                },
                PERMISSION_REQUEST_CODE);
        if ((ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)) {

            new Thread(() -> {
                WorkManager workManager = WorkManager.getInstance();
                List<String> files = ThemederApp.getInstance().getRepo().getNamesImages();
                //Log.d("!!!!!!", files.get(0));
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
            }).start();
        }
    }
    private static final int PERMISSION_REQUEST_CODE = 0;
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ThemederApp.getInstance().getRepo().setPropertyBoolean(getString(R.string.switch_check), isChecked);
            Log.d("!!!!!!", "switch "+ isChecked);
        WorkManager workManager = WorkManager.getInstance();

        if (isChecked){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    PERMISSION_REQUEST_CODE);
            if ((ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)) {
                ExecutorService executorservice = Executors.newSingleThreadExecutor();
                Runnable runnable = () -> {
                    List<String> files = ThemederApp.getInstance().getRepo().getNamesImages();
                    int a = (int) (Math.random() * files.size());


                    if (files.size() != 0) {
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
                };
                executorservice.submit(runnable);
            }
        }
        else {
            WorkManager.getInstance().cancelAllWorkByTag("pwr");
        }
    }
}