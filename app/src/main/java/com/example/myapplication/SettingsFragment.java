package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.yuyakaido.android.cardstackview.Direction;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SettingsFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{
    private static final int PERMISSION_REQUEST_CODE = 0;
    private static final int PERMISSION_REQUEST_CODE_SWITCH = 1;
    private Button changeWallpaper;
    private SwitchMaterial changeWallpaperSwitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        changeWallpaper = v.findViewById(R.id.change_wallpaperButton);
        changeWallpaper.setOnClickListener(this);
        changeWallpaperSwitch = v.findViewById(R.id.switch_periodic);
        if (changeWallpaperSwitch != null) {
            changeWallpaperSwitch.setOnCheckedChangeListener(this);
        }
        ThemederApp.getInstance().getRepo().setPropertyBoolean(getString(R.string.switch_check), false);
        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            System.out.println("fsdasD");
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    intentWorkManager();
                }
            }else{
                if ((grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED)){
                    intentWorkManager();
                }
            }
        }
        if (requestCode == PERMISSION_REQUEST_CODE_SWITCH) {
            System.out.println("fsdasD");
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    changeWallpaperSwitch.setChecked(true);
                    intentChangeWorkManager();
                }
            }else{
                if ((grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED)){
                    changeWallpaperSwitch.setChecked(true);
                    intentChangeWorkManager();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void intentWorkManager(){
        OneTimeWorkRequest myWorkRequest1 = new OneTimeWorkRequest.Builder(FileWallpaperWorkManager.class).build();
        WorkManager.getInstance().enqueue(myWorkRequest1);
    }

    public void intentChangeWorkManager(){
        OneTimeWorkRequest myWorkRequest2 = new OneTimeWorkRequest.Builder(ChangeFileWallpaperWorkManager.class).build();
        WorkManager.getInstance().enqueue(myWorkRequest2);
    }

    @Override
    public void onClick(View v) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if ((ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()).getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) ) {
                intentWorkManager();
            }else {
                requestPermissions(
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        },
                        PERMISSION_REQUEST_CODE);
            }
        }else{
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if ((ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()).getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()).getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED)) {
                    intentWorkManager();
                }else {
                    requestPermissions(
                            new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                            },
                            PERMISSION_REQUEST_CODE);
                }
            }else{
                intentWorkManager();
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ThemederApp.getInstance().getRepo().setPropertyBoolean(getString(R.string.switch_check), isChecked);
        Log.d("!!!!!!", "switch "+ isChecked);
        if (isChecked){
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if ((ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()).getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) ) {
                    intentChangeWorkManager();
                }else {
                    changeWallpaperSwitch.setChecked(false);
                    requestPermissions(
                            new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                            },
                            PERMISSION_REQUEST_CODE_SWITCH);
                }
            }else{
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if ((ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()).getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()).getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED)) {
                        intentChangeWorkManager();
                    }else {
                        changeWallpaperSwitch.setChecked(false);
                        requestPermissions(
                                new String[]{
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                },
                                PERMISSION_REQUEST_CODE_SWITCH);
                    }
                }else{
                    intentChangeWorkManager();
                }
            }
        }
        else {
            WorkManager.getInstance().cancelAllWorkByTag("pwr");
        }
    }
}